/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.item.ItemStack
 */
package me.chachoox.lithium.impl.modules.player.autostackfill;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.impl.modules.player.autostackfill.ListenerGameLoop;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;

public class AutoStackFill
extends Module {
    protected final NumberProperty<Integer> threshold = new NumberProperty<Integer>(32, 1, 64, new String[]{"Threshold", "thresh", "amount"}, "How low an item stack has to be for us to automatically refill it.");
    protected final NumberProperty<Integer> delay = new NumberProperty<Integer>(1, 0, 10, new String[]{"Delay", "del", "d"}, "Delay between refilling stacks.");
    protected final StopWatch timer = new StopWatch();

    public AutoStackFill() {
        super("AutoStackFill", new String[]{"Replenish", "AutoRefill", "Refill", "AutoStackFill", "HotbarRefill", "AutoFillStack"}, "Automatically fills item stacks.", Category.PLAYER);
        this.offerListeners(new ListenerGameLoop(this));
        this.offerProperties(this.threshold, this.delay);
    }

    protected void refillSlot(int slot) {
        ItemStack stack = AutoStackFill.mc.player.inventory.getStackInSlot(slot);
        if (stack.isEmpty() || stack.getCount() > (Integer)this.threshold.getValue() || stack.getItem() == Items.AIR || !stack.isStackable() || stack.getCount() >= stack.getMaxStackSize()) {
            return;
        }
        for (int i = 9; i < 36; ++i) {
            ItemStack itemStack = AutoStackFill.mc.player.inventory.getStackInSlot(i);
            if (itemStack.isEmpty() || !this.canMergeWith(stack, itemStack)) continue;
            AutoStackFill.mc.playerController.windowClick(AutoStackFill.mc.player.inventoryContainer.windowId, i, 0, ClickType.QUICK_MOVE, (EntityPlayer)AutoStackFill.mc.player);
        }
    }

    private boolean canMergeWith(ItemStack first, ItemStack second) {
        return first.getItem() == second.getItem() && first.getDisplayName().equals(second.getDisplayName());
    }
}

