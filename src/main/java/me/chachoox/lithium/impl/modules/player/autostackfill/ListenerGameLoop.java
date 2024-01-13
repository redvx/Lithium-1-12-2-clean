/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.inventory.GuiInventory
 */
package me.chachoox.lithium.impl.modules.player.autostackfill;

import me.chachoox.lithium.impl.event.events.misc.GameLoopEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.player.autostackfill.AutoStackFill;
import net.minecraft.client.gui.inventory.GuiInventory;

public class ListenerGameLoop
extends ModuleListener<AutoStackFill, GameLoopEvent> {
    public ListenerGameLoop(AutoStackFill module) {
        super(module, GameLoopEvent.class);
    }

    @Override
    public void call(GameLoopEvent event) {
        if (ListenerGameLoop.mc.player != null && ((AutoStackFill)this.module).timer.passed((long)((Integer)((AutoStackFill)this.module).delay.getValue()).intValue() * 50L) && !(ListenerGameLoop.mc.currentScreen instanceof GuiInventory)) {
            for (int i = 0; i < 9; ++i) {
                ((AutoStackFill)this.module).refillSlot(i);
                ((AutoStackFill)this.module).timer.reset();
            }
        }
    }
}

