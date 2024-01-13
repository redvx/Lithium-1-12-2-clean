/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 */
package me.chachoox.lithium.impl.modules.combat.offhand;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.blocks.HoleUtil;
import me.chachoox.lithium.api.util.entity.EntityUtil;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.impl.modules.combat.offhand.ListenerGameLoop;
import me.chachoox.lithium.impl.modules.combat.offhand.ListenerInteract;
import me.chachoox.lithium.impl.modules.combat.offhand.OffhandMode;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Offhand
extends Module {
    protected final EnumProperty<OffhandMode> mode = new EnumProperty<OffhandMode>(OffhandMode.TOTEMS, new String[]{"Offhand", "item", "offhandmode"}, "Crystals: - Sets your offhand to crystals / Gapple: - Sets your offhand to golden apples / Totems: - Sets your offhand to totems.");
    protected final NumberProperty<Float> health = new NumberProperty<Float>(Float.valueOf(16.0f), Float.valueOf(1.0f), Float.valueOf(20.0f), Float.valueOf(0.5f), new String[]{"Health", "Hp"}, "Threshold that tells us if we should switch to a totem.");
    protected final NumberProperty<Float> holeHealth = new NumberProperty<Float>(Float.valueOf(12.0f), Float.valueOf(1.0f), Float.valueOf(20.0f), Float.valueOf(0.5f), new String[]{"HoleHealth", "HoleHp"}, "Threshold that tells us if we should switch to a totem while in holes.");
    protected final Property<Boolean> swordGap = new Property<Boolean>(true, new String[]{"SwordGapple", "Swordgap", "rightclickgap", "rightclickgapple"}, "Switches offhand to a gapple if we are holding a sword and pressing right click.");
    protected final Property<Boolean> gapOverride = new Property<Boolean>(true, new String[]{"GappleOverride", "gapoverride"}, "Switches offhand to gapple if right click is pressed and overrides the health checker.");
    protected final Property<Boolean> lethal = new Property<Boolean>(false, new String[]{"Lethal", "ExtraCalc", "Safety"}, "Switches to a totem if we are going to pop from falling.");
    protected final Property<Boolean> mainhand = new Property<Boolean>(false, new String[]{"Mainhand", "why is this here"}, "Switches mainhand to a totem.");
    protected final StopWatch timer = new StopWatch();
    protected boolean gap;

    public Offhand() {
        super("Offhand", new String[]{"offhand", "autototem", "offhandcrystal", "offhandgap"}, "Cycles offhand items.", Category.COMBAT);
        this.offerProperties(this.mode, this.health, this.holeHealth, this.swordGap, this.gapOverride, this.lethal, this.mainhand);
        this.offerListeners(new ListenerInteract(this), new ListenerGameLoop(this));
    }

    @Override
    public String getSuffix() {
        return this.mode.getFixedValue();
    }

    protected void windowClick(int slot) {
        Offhand.mc.playerController.windowClick(Offhand.mc.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, (EntityPlayer)Offhand.mc.player);
    }

    protected void mainhandTotem(int slot) {
        ItemStack stack = Offhand.mc.player.inventory.getStackInSlot(slot);
        if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
            return;
        }
        for (int i = 9; i < 36; ++i) {
            ItemStack itemStack = Offhand.mc.player.inventory.getStackInSlot(i);
            if (itemStack.isEmpty() || itemStack.getItem() != Items.TOTEM_OF_UNDYING) continue;
            Offhand.mc.playerController.windowClick(Offhand.mc.player.inventoryContainer.windowId, i, slot, ClickType.SWAP, (EntityPlayer)Offhand.mc.player);
        }
    }

    protected int getItemSlot(Item itemIn) {
        for (int i = 45; i > 0; --i) {
            Item item = Offhand.mc.player.inventory.getStackInSlot(i).getItem();
            if (item != itemIn) continue;
            if (i < 9) {
                i += 36;
            }
            return i;
        }
        return -1;
    }

    protected Item getItem(boolean safe, boolean gapple) {
        Item item = Items.TOTEM_OF_UNDYING;
        if (this.lethal.getValue().booleanValue() && !safe) {
            return item;
        }
        if (this.lethal.getValue().booleanValue() && Offhand.mc.player.fallDistance > 10.0f) {
            return item;
        }
        boolean inHole = HoleUtil.isHole(Offhand.mc.player.getPosition());
        if (EntityUtil.getHealth((EntityPlayer)Offhand.mc.player) >= this.getHealth(inHole, gapple, this.gapOverride.getValue())) {
            item = gapple ? Items.GOLDEN_APPLE : ((OffhandMode)((Object)this.mode.getValue())).item;
        }
        return item;
    }

    protected float getHealth(boolean safe, boolean gapple, boolean antigap) {
        return gapple ? (antigap ? 0.0f : (safe ? ((Float)this.holeHealth.getValue()).floatValue() : ((Float)this.health.getValue()).floatValue())) : (safe ? ((Float)this.holeHealth.getValue()).floatValue() : ((Float)this.health.getValue()).floatValue());
    }
}

