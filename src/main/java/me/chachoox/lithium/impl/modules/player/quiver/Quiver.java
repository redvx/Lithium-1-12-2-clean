/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 *  net.minecraft.client.resources.I18n
 *  net.minecraft.init.PotionTypes
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemArrow
 *  net.minecraft.item.ItemSpectralArrow
 *  net.minecraft.item.ItemStack
 *  net.minecraft.potion.PotionEffect
 *  net.minecraft.potion.PotionType
 *  net.minecraft.potion.PotionUtils
 *  net.minecraft.util.EnumHand
 */
package me.chachoox.lithium.impl.modules.player.quiver;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.BindProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.util.Bind;
import me.chachoox.lithium.api.util.inventory.InventoryUtil;
import me.chachoox.lithium.api.util.inventory.ItemUtil;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.api.util.text.TextUtil;
import me.chachoox.lithium.impl.modules.player.quiver.ListenerKeyboard;
import me.chachoox.lithium.impl.modules.player.quiver.ListenerMotion;
import me.chachoox.lithium.impl.modules.player.quiver.ListenerUseItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemSpectralArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumHand;

public class Quiver
extends Module {
    protected static final PotionType SPECTRAL = new PotionType(new PotionEffect[0]);
    protected static final Set<PotionType> BAD_TYPES = Sets.newHashSet((Object[])new PotionType[]{PotionTypes.EMPTY, PotionTypes.WATER, PotionTypes.MUNDANE, PotionTypes.THICK, PotionTypes.AWKWARD, PotionTypes.HEALING, PotionTypes.STRONG_HEALING, PotionTypes.STRONG_HARMING, PotionTypes.HARMING});
    private final Set<String> arStrings = new HashSet<String>();
    protected final NumberProperty<Integer> releaseTicks = new NumberProperty<Integer>(3, 0, 20, new String[]{"ReleaseTicks", "releasetick", "rt"}, "How long it takes to release the bow.");
    protected final NumberProperty<Integer> maxTicks = new NumberProperty<Integer>(10, 0, 20, new String[]{"MaxTicks", "maxtick", "mt"}, "If shooting the bow fails and this many ticks passed we cancel shooting the bow.");
    protected final NumberProperty<Integer> cancelTime = new NumberProperty<Integer>(0, 0, 500, new String[]{"CancelTime", "waitTime", "pausetime", "pt", "wt"}, "How long we will cancel shooting the bow for if we are cycling with 2 arrows of different types.");
    protected final NumberProperty<Integer> cycleDelay = new NumberProperty<Integer>(250, 0, 500, new String[]{"CycleDelay", "Cd", "CycleDel"}, "The delay between cycling 2 arrows.");
    protected final NumberProperty<Integer> shootDelay = new NumberProperty<Integer>(500, 0, 500, new String[]{"ShootDelay", "shotdelay", "imgoingfuckinginsanae"}, "The delay after shooting an arrow.");
    protected final BindProperty cycleButton = new BindProperty(new Bind(0), new String[]{"CycleKey", "Cyclekeybind", "cycle"}, "Cycles arrows whenever we press this key instantly.");
    protected final Set<PotionType> cycled = new HashSet<PotionType>();
    protected final StopWatch cycleTimer = new StopWatch();
    protected final StopWatch timer = new StopWatch();
    protected boolean fast;

    public Quiver() {
        super("Quiver", new String[]{"Quiver", "SelfBow", "Arrows", "InstantSelfBow"}, "Automatically shoots valid arrows at yourself.", Category.PLAYER);
        this.offerProperties(this.releaseTicks, this.maxTicks, this.cancelTime, this.cycleDelay, this.shootDelay, this.cycleButton);
        this.offerListeners(new ListenerMotion(this), new ListenerKeyboard(this), new ListenerUseItem(this));
    }

    @Override
    public void onEnable() {
        this.fast = false;
    }

    protected boolean badStack(ItemStack stack) {
        return this.badStack(stack, true, Collections.emptySet());
    }

    protected boolean badStack(ItemStack stack, boolean checkType, Set<PotionType> cycled) {
        PotionType type = PotionUtils.getPotionFromItem((ItemStack)stack);
        if (stack.getItem() instanceof ItemSpectralArrow) {
            type = SPECTRAL;
        }
        if (cycled.contains(type)) {
            return true;
        }
        if (checkType) {
            if (BAD_TYPES.contains(type)) {
                return true;
            }
        } else if (type.getEffects().isEmpty() && this.isValid("none")) {
            return false;
        }
        if (stack.getItem() instanceof ItemSpectralArrow) {
            return !this.isValid("Spectral") || Quiver.mc.player.isGlowing();
        }
        boolean inValid = true;
        for (PotionEffect e : type.getEffects()) {
            if (!this.isValid(I18n.format((String)e.getPotion().getName(), (Object[])new Object[0]))) {
                return true;
            }
            PotionEffect eff = Quiver.mc.player.getActivePotionEffect(e.getPotion());
            if (eff != null && eff.getDuration() >= 200) continue;
            inValid = false;
        }
        if (!checkType) {
            return false;
        }
        return inValid;
    }

    protected void cycle(boolean recursive, boolean key) {
        if (!InventoryUtil.validScreen() || key && !this.cycleTimer.passed(((Integer)this.cycleDelay.getValue()).intValue())) {
            return;
        }
        int firstSlot = -1;
        int secondSlot = -1;
        ItemStack arrow = null;
        if (this.isArrow(Quiver.mc.player.getHeldItem(EnumHand.OFF_HAND))) {
            firstSlot = 45;
        }
        if (this.isArrow(Quiver.mc.player.getHeldItem(EnumHand.MAIN_HAND))) {
            if (firstSlot == -1) {
                firstSlot = ItemUtil.hotbarToInventory(Quiver.mc.player.inventory.currentItem);
            } else if (!this.badStack(Quiver.mc.player.getHeldItem(EnumHand.MAIN_HAND), key, this.cycled)) {
                secondSlot = ItemUtil.hotbarToInventory(Quiver.mc.player.inventory.currentItem);
                arrow = Quiver.mc.player.getHeldItem(EnumHand.MAIN_HAND);
            }
        }
        if (!this.badStack(Quiver.mc.player.inventory.getItemStack(), key, this.cycled)) {
            secondSlot = -2;
            arrow = Quiver.mc.player.inventory.getItemStack();
        }
        if (firstSlot == -1 || secondSlot == -1) {
            for (int i = 0; i < Quiver.mc.player.inventory.getSizeInventory(); ++i) {
                ItemStack stack = Quiver.mc.player.inventory.getStackInSlot(i);
                if (!this.isArrow(stack)) continue;
                if (firstSlot == -1) {
                    firstSlot = ItemUtil.hotbarToInventory(i);
                    continue;
                }
                if (this.badStack(stack, key, this.cycled)) continue;
                secondSlot = ItemUtil.hotbarToInventory(i);
                arrow = stack;
                break;
            }
        }
        if (firstSlot == -1) {
            return;
        }
        if (secondSlot == -1) {
            if (!recursive && !this.cycled.isEmpty()) {
                this.cycled.clear();
                this.cycle(true, key);
            }
            return;
        }
        PotionType type = PotionUtils.getPotionFromItem((ItemStack)arrow);
        if (arrow.getItem() instanceof ItemSpectralArrow) {
            type = SPECTRAL;
        }
        this.cycled.add(type);
        int finalFirstSlot = firstSlot;
        int finalSecondSlot = secondSlot;
        Item inFirst = InventoryUtil.get(finalFirstSlot).getItem();
        Item inSecond = InventoryUtil.get(finalSecondSlot).getItem();
        if (InventoryUtil.get(finalFirstSlot).getItem() == inFirst && InventoryUtil.get(finalSecondSlot).getItem() == inSecond) {
            if (finalSecondSlot == -2) {
                InventoryUtil.click(finalFirstSlot);
            } else {
                InventoryUtil.click(finalSecondSlot);
                InventoryUtil.click(finalFirstSlot);
                InventoryUtil.click(finalSecondSlot);
            }
        }
        this.cycleTimer.reset();
    }

    protected ItemStack findArrow() {
        if (this.isArrow(Quiver.mc.player.getHeldItem(EnumHand.OFF_HAND))) {
            return Quiver.mc.player.getHeldItem(EnumHand.OFF_HAND);
        }
        if (this.isArrow(Quiver.mc.player.getHeldItem(EnumHand.MAIN_HAND))) {
            return Quiver.mc.player.getHeldItem(EnumHand.MAIN_HAND);
        }
        for (int i = 0; i < Quiver.mc.player.inventory.getSizeInventory(); ++i) {
            ItemStack stack = Quiver.mc.player.inventory.getStackInSlot(i);
            if (!this.isArrow(stack)) continue;
            return stack;
        }
        return ItemStack.EMPTY;
    }

    private boolean isValid(String string) {
        if (string == null) {
            return false;
        }
        return !this.arStrings.contains(TextUtil.formatString(string));
    }

    public Collection<String> getList() {
        return this.arStrings;
    }

    protected boolean isArrow(ItemStack stack) {
        return stack.getItem() instanceof ItemArrow;
    }
}

