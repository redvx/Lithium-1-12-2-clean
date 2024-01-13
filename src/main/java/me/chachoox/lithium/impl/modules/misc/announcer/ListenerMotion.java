/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.misc.announcer;

import me.chachoox.lithium.api.event.events.Stage;
import me.chachoox.lithium.api.util.movement.MovementUtil;
import me.chachoox.lithium.impl.event.events.movement.MotionUpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.misc.announcer.Announcer;

public class ListenerMotion
extends ModuleListener<Announcer, MotionUpdateEvent> {
    public ListenerMotion(Announcer module) {
        super(module, MotionUpdateEvent.class);
    }

    @Override
    public void call(MotionUpdateEvent event) {
        if (event.getStage() == Stage.PRE && ((Announcer)this.module).move.getValue().booleanValue()) {
            ((Announcer)this.module).speed += MovementUtil.getDistance2D();
        }
    }
}

