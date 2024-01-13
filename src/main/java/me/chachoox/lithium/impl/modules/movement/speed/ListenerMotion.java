/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.movement.speed;

import me.chachoox.lithium.api.util.movement.MovementUtil;
import me.chachoox.lithium.impl.event.events.movement.MotionUpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.movement.speed.Speed;

public class ListenerMotion
extends ModuleListener<Speed, MotionUpdateEvent> {
    public ListenerMotion(Speed module) {
        super(module, MotionUpdateEvent.class);
    }

    @Override
    public void call(MotionUpdateEvent event) {
        if (!MovementUtil.anyMovementKeys()) {
            MovementUtil.setMotion(0.0, ListenerMotion.mc.player.motionY, 0.0);
        }
        ((Speed)this.module).distance = MovementUtil.getDistance2D();
    }
}

