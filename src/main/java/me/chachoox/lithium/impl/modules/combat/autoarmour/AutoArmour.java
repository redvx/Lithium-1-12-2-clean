/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.item.EntityEnderPearl
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.EntityEquipmentSlot
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemArmor
 *  net.minecraft.item.ItemElytra
 *  net.minecraft.item.ItemStack
 *  org.lwjgl.input.Mouse
 */
package me.chachoox.lithium.impl.modules.combat.autoarmour;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.entity.EntityUtil;
import me.chachoox.lithium.api.util.inventory.InventoryUtil;
import me.chachoox.lithium.api.util.math.MathUtil;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.impl.modules.combat.autoarmour.ListenerGameLoop;
import me.chachoox.lithium.impl.modules.combat.autoarmour.ListenerTick;
import me.chachoox.lithium.impl.modules.combat.autoarmour.util.MendingStage;
import me.chachoox.lithium.impl.modules.combat.autoarmour.util.SingleMendingSlot;
import me.chachoox.lithium.impl.modules.combat.autoarmour.util.WindowClick;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Mouse;

public class AutoArmour
extends Module {
    protected final NumberProperty<Integer> delay = new NumberProperty<Integer>(2, 1, 10, new String[]{"Delay", "delayjamin"}, "Delay between selecting armor pieces.");
    protected final Property<Boolean> autoMend = new Property<Boolean>(false, new String[]{"AutoMend", "crystalpvpnnmode"}, "Automatically takes off armor pieces when mending to not waste exp.");
    protected final Property<Boolean> singleMend = new Property<Boolean>(false, new String[]{"SingleMend", "crystalpvpnnmodepartTWO"}, "Only mends one armor piece at a time.");
    protected final NumberProperty<Integer> helmet = new NumberProperty<Integer>(99, 1, 100, new String[]{"Helmet%", "Hat%", "HelmetPercent", "HatPercent", "HatPercentage", "HelmetPercentage"}, "Threshold percentage for helmets.");
    protected final NumberProperty<Integer> chest = new NumberProperty<Integer>(95, 1, 100, new String[]{"Chest%", "Chestplate%", "ChestplatePercent", "ChestPercent", "ChestPercentage", "ChestplatePercentage"}, "Threshold percentage for chestplates.");
    protected final NumberProperty<Integer> pants = new NumberProperty<Integer>(97, 1, 100, new String[]{"Pants%", "Leggings%", "LeggingsPercent", "PantsPercent", "PantsPercentage", "LeggingsPercentage"}, "Threshold percentage for leggings.");
    protected final NumberProperty<Integer> boots = new NumberProperty<Integer>(97, 1, 100, new String[]{"Boots%", "Shoes%", "ShoesPercent", "BootsPercent", "BootsPercentage", "ShoePercentage"}, "Threshold percentage for boots.");
    protected final Property<Boolean> safe = new Property<Boolean>(true, new String[]{"Safe", "NoCrystals", "PlayerCheck", "CrystalCheck", "NoPlayers", "crystalppvnnbiggestenemy"}, "Wont automend if all safety checks havent passed.");
    protected final Property<Boolean> drag = new Property<Boolean>(false, new String[]{"Drag", "Pull", "XCarry"}, "Uses the drag slot more plus xcarry slots to store stuff if we can.");
    protected final Property<Boolean> putBack = new Property<Boolean>(true, new String[]{"Putback", "NoDrag", "AntiStuck"}, "Prevents items from getting stuck in the drag slot.");
    protected final Property<Boolean> doubleClick = new Property<Boolean>(false, new String[]{"DoubleClick", "Click", "ExtraClick"}, "Queues the polled windowclicks after the first click.");
    protected final Property<Boolean> strict = new Property<Boolean>(false, new String[]{"Strict", "strictjamin", "NoXCarry"}, "Avoids the xcarry slot when looking and iterating items.");
    protected final Property<Boolean> pauseInInv = new Property<Boolean>(false, new String[]{"PauseInInv", "PauseInInventory", "InvPause"}, "Stops putting on armor and automending if our inventory is open.");
    protected final StopWatch timer = new StopWatch();
    protected final Queue<WindowClick> windowClicks = new LinkedList<WindowClick>();
    protected Set<Integer> queuedSlots = new HashSet<Integer>();
    protected Property<?>[] damages;
    protected WindowClick putBackClick;
    protected boolean stackSet;
    protected MendingStage stage = MendingStage.MENDING;
    protected final StopWatch mendingTimer = new StopWatch();
    protected final SingleMendingSlot[] singleMendingSlots = new SingleMendingSlot[]{new SingleMendingSlot(EntityEquipmentSlot.HEAD), new SingleMendingSlot(EntityEquipmentSlot.CHEST), new SingleMendingSlot(EntityEquipmentSlot.LEGS), new SingleMendingSlot(EntityEquipmentSlot.FEET)};

    public AutoArmour() {
        super("AutoArmour", new String[]{"AutoArmour", "aa", "armor"}, "Puts armour on automatically.", Category.COMBAT);
        this.damages = new Property[]{this.helmet, this.chest, this.pants, this.boots};
        this.offerProperties(this.delay, this.autoMend, this.singleMend, this.helmet, this.chest, this.pants, this.boots, this.safe, this.drag, this.putBack, this.doubleClick, this.strict, this.pauseInInv);
        this.offerListeners(new ListenerTick(this), new ListenerGameLoop(this));
    }

    @Override
    public void onEnable() {
        this.stage = MendingStage.MENDING;
        this.windowClicks.clear();
        this.queuedSlots.clear();
        this.putBackClick = null;
        this.unblockMendingSlots();
    }

    @Override
    public void onDisable() {
        this.stage = MendingStage.MENDING;
        this.windowClicks.clear();
        this.queuedSlots.clear();
        this.putBackClick = null;
        this.unblockMendingSlots();
    }

    protected void unblockMendingSlots() {
        for (SingleMendingSlot mendingSlot : this.singleMendingSlots) {
            mendingSlot.setBlocked(false);
        }
    }

    protected WindowClick queueClick(int slot, ItemStack inSlot, ItemStack inDrag) {
        return this.queueClick(slot, inSlot, inDrag, slot);
    }

    protected WindowClick queueClick(int slot, ItemStack inSlot, ItemStack inDrag, int target) {
        WindowClick click = new WindowClick(slot, inSlot, inDrag, target);
        this.queueClick(click);
        click.setFast(this.strict.getValue());
        return click;
    }

    protected void queueClick(WindowClick click) {
        this.windowClicks.add(click);
    }

    protected void runClick() {
        if (InventoryUtil.validScreen() && AutoArmour.mc.playerController != null) {
            if (this.timer.passed((long)((Integer)this.delay.getValue()).intValue() * 10L)) {
                WindowClick windowClick = this.windowClicks.poll();
                while (windowClick != null) {
                    if (this.safe.getValue().booleanValue() && !windowClick.isValid()) {
                        this.windowClicks.clear();
                        this.queuedSlots.clear();
                        return;
                    }
                    windowClick.runClick(AutoArmour.mc.playerController);
                    this.timer.reset();
                    if (!windowClick.isDoubleClick()) {
                        return;
                    }
                    windowClick = this.windowClicks.poll();
                }
            }
        } else {
            this.windowClicks.clear();
            this.queuedSlots.clear();
        }
    }

    protected ItemStack setStack() {
        if (!this.stackSet) {
            ItemStack drag = AutoArmour.mc.player.inventory.getItemStack();
            if (!drag.isEmpty()) {
                int slot = AutoArmour.findItem(Items.AIR, this.strict.getValue() == false, this.queuedSlots);
                if (slot != -1) {
                    ItemStack inSlot = InventoryUtil.get(slot);
                    this.queueClick(slot, drag, inSlot);
                    this.queuedSlots.add(slot);
                    this.stackSet = true;
                    return inSlot;
                }
                return null;
            }
            this.stackSet = true;
            return drag;
        }
        return null;
    }

    protected boolean canAutoMend() {
        if (!(this.autoMend.getValue().booleanValue() && Mouse.isButtonDown((int)1) && InventoryUtil.isHolding(Items.EXPERIENCE_BOTTLE))) {
            return false;
        }
        EntityPlayer closestPlayer = EntityUtil.getClosestEnemy();
        if (closestPlayer != null && closestPlayer.getDistanceSq((Entity)AutoArmour.mc.player) < (double)MathUtil.square(9.0f)) {
            return false;
        }
        for (Entity entity : AutoArmour.mc.world.loadedEntityList) {
            if (entity instanceof EntityEnderCrystal && entity.getDistanceSq((Entity)AutoArmour.mc.player) < (double)MathUtil.square(13.0f) && !entity.isDead && AutoArmour.mc.player.getDistanceSq(entity) <= 144.0) {
                return false;
            }
            if (!(entity instanceof EntityEnderPearl) || !(entity.getDistanceSq((Entity)AutoArmour.mc.player) < (double)MathUtil.square(65.0f)) || entity.isDead || !(AutoArmour.mc.player.getDistanceSq(entity) <= 144.0)) continue;
            return false;
        }
        return true;
    }

    protected static EntityEquipmentSlot fromSlot(int slot) {
        switch (slot) {
            case 5: {
                return EntityEquipmentSlot.HEAD;
            }
            case 6: {
                return EntityEquipmentSlot.CHEST;
            }
            case 7: {
                return EntityEquipmentSlot.LEGS;
            }
            case 8: {
                return EntityEquipmentSlot.FEET;
            }
        }
        ItemStack stack = InventoryUtil.get(slot);
        return AutoArmour.getSlot(stack);
    }

    protected static int fromEquipment(EntityEquipmentSlot equipmentSlot) {
        switch (equipmentSlot) {
            case OFFHAND: {
                return 45;
            }
            case FEET: {
                return 8;
            }
            case LEGS: {
                return 7;
            }
            case CHEST: {
                return 6;
            }
            case HEAD: {
                return 5;
            }
        }
        return -1;
    }

    protected static EntityEquipmentSlot getSlot(ItemStack stack) {
        if (!stack.isEmpty()) {
            if (stack.getItem() instanceof ItemArmor) {
                ItemArmor armor = (ItemArmor)stack.getItem();
                return armor.getEquipmentSlot();
            }
            if (stack.getItem() instanceof ItemElytra) {
                return EntityEquipmentSlot.CHEST;
            }
        }
        return null;
    }

    public static int findItem(Item item, boolean xCarry, Set<Integer> blackList) {
        ItemStack stack;
        int i;
        ItemStack drag = AutoArmour.mc.player.inventory.getItemStack();
        if (!drag.isEmpty() && drag.getItem() == item && !blackList.contains(-2)) {
            return -2;
        }
        for (i = 9; i < 45; ++i) {
            stack = InventoryUtil.get(i);
            if (stack.getItem() != item || blackList.contains(i)) continue;
            return i;
        }
        if (xCarry) {
            for (i = 1; i < 5; ++i) {
                stack = InventoryUtil.get(i);
                if (stack.getItem() != item || blackList.contains(i)) continue;
                return i;
            }
        }
        return -1;
    }

    protected static int iterateItems(boolean xCarry, Set<Integer> blackList, Function<ItemStack, Boolean> accept) {
        ItemStack stack;
        int i;
        ItemStack drag = AutoArmour.mc.player.inventory.getItemStack();
        if (!drag.isEmpty() && !blackList.contains(-2) && accept.apply(drag).booleanValue()) {
            return -2;
        }
        for (i = 9; i < 45; ++i) {
            stack = InventoryUtil.get(i);
            if (blackList.contains(i) || !accept.apply(stack).booleanValue()) continue;
            return i;
        }
        if (xCarry) {
            for (i = 1; i < 5; ++i) {
                stack = InventoryUtil.get(i);
                if (blackList.contains(i) || !accept.apply(stack).booleanValue()) continue;
                return i;
            }
        }
        return -1;
    }
}

