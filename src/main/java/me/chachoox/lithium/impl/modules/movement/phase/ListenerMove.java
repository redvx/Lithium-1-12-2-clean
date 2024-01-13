/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.movement.phase;

import me.chachoox.lithium.impl.event.events.movement.actions.MoveEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.movement.phase.Phase;

public class ListenerMove
extends ModuleListener<Phase, MoveEvent> {
    public ListenerMove(Phase module) {
        super(module, MoveEvent.class);
    }

    @Override
    public void call(MoveEvent event) {
        ListenerMove.mc.player.noClip = true;
        if (((Phase)this.module).slow.getValue().booleanValue()) {
            event.setX(event.getX() * 0.3);
            event.setZ(event.getZ() * 0.3);
        }
    }
}

