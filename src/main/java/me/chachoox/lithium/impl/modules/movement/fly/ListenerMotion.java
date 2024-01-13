/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.movement.fly;

import me.chachoox.lithium.impl.event.events.movement.MotionUpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.movement.fly.Fly;

public class ListenerMotion
extends ModuleListener<Fly, MotionUpdateEvent> {
    public ListenerMotion(Fly module) {
        super(module, MotionUpdateEvent.class);
    }

    @Override
    public void call(MotionUpdateEvent event) {
        ListenerMotion.mc.player.motionY = 0.0;
        if (ListenerMotion.mc.inGameHasFocus) {
            double speed = (Double)((Fly)this.module).speed.getValue() / 8.0;
            if (ListenerMotion.mc.gameSettings.keyBindJump.isKeyDown()) {
                ListenerMotion.mc.player.motionY = speed;
            }
            if (ListenerMotion.mc.gameSettings.keyBindSneak.isKeyDown()) {
                ListenerMotion.mc.player.motionY = -speed;
            }
        }
    }
}

