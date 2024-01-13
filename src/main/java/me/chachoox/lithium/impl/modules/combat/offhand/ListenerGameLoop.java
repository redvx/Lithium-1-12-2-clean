/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.client.gui.inventory.GuiInventory
 *  net.minecraft.init.Items
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemSword
 */
package me.chachoox.lithium.impl.modules.combat.offhand;

import me.chachoox.lithium.impl.event.events.misc.GameLoopEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.combat.offhand.Offhand;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;

public class ListenerGameLoop
extends ModuleListener<Offhand, GameLoopEvent> {
    public ListenerGameLoop(Offhand module) {
        super(module, GameLoopEvent.class);
    }

    @Override
    public void call(GameLoopEvent event) {
        if (ListenerGameLoop.mc.player != null && !(ListenerGameLoop.mc.currentScreen instanceof GuiContainer) || ListenerGameLoop.mc.currentScreen instanceof GuiInventory) {
            int slot;
            if (((Offhand)this.module).swordGap.getValue().booleanValue()) {
                ((Offhand)this.module).gap = ListenerGameLoop.mc.gameSettings.keyBindUseItem.isKeyDown() && ListenerGameLoop.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword;
            }
            boolean safe = Managers.SAFE.isSafe();
            Item item = ((Offhand)this.module).getItem(safe, ((Offhand)this.module).gap);
            if (((Offhand)this.module).mainhand.getValue().booleanValue()) {
                ((Offhand)this.module).mainhandTotem(ListenerGameLoop.mc.player.inventory.currentItem);
                return;
            }
            if (ListenerGameLoop.mc.player.getHeldItemOffhand().getItem() != item && (slot = ((Offhand)this.module).getItemSlot(item)) != -1) {
                int n = slot = slot < 9 ? slot + 36 : slot;
                if (item != Items.TOTEM_OF_UNDYING && !((Offhand)this.module).timer.passed(350L)) {
                    return;
                }
                ((Offhand)this.module).windowClick(slot);
                ((Offhand)this.module).windowClick(45);
                ((Offhand)this.module).windowClick(slot);
                ((Offhand)this.module).timer.reset();
            }
        }
    }
}

