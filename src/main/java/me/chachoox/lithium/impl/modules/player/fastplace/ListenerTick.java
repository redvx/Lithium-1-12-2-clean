/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.player.fastplace;

import me.chachoox.lithium.asm.ducks.IMinecraft;
import me.chachoox.lithium.impl.event.events.update.TickEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.player.fastplace.FastPlace;

public class ListenerTick
extends ModuleListener<FastPlace, TickEvent> {
    public ListenerTick(FastPlace module) {
        super(module, TickEvent.class);
    }

    @Override
    public void call(TickEvent event) {
        if (!event.isSafe()) {
            return;
        }
        if (ListenerTick.mc.gameSettings.keyBindUseItem.isKeyDown() && ((FastPlace)this.module).isValid(ListenerTick.mc.player.getHeldItemMainhand().getItem()) && (Integer)((FastPlace)this.module).delay.getValue() < ((IMinecraft)mc).getRightClickDelay()) {
            ((IMinecraft)mc).setRightClickDelay((Integer)((FastPlace)this.module).delay.getValue());
        }
    }
}

