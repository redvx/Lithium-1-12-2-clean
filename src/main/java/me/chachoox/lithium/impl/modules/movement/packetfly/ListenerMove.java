/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.movement.packetfly;

import me.chachoox.lithium.impl.event.events.movement.actions.MoveEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.movement.packetfly.PacketFly;
import me.chachoox.lithium.impl.modules.movement.packetfly.mode.PacketFlyMode;
import me.chachoox.lithium.impl.modules.movement.packetfly.mode.PhaseMode;

public class ListenerMove
extends ModuleListener<PacketFly, MoveEvent> {
    public ListenerMove(PacketFly module) {
        super(module, MoveEvent.class);
    }

    @Override
    public void call(MoveEvent event) {
        if (!event.isCanceled()) {
            if (((PacketFly)this.module).mode.getValue() != PacketFlyMode.SETBACK && ((PacketFly)this.module).lastTpID == 0) {
                return;
            }
            event.setCanceled(true);
            event.setX(ListenerMove.mc.player.motionX);
            event.setY(((PacketFly)this.module).conceal.getValue() != false ? ((PacketFly)this.module).concealOffset : ListenerMove.mc.player.motionY);
            event.setZ(ListenerMove.mc.player.motionZ);
            if (((PacketFly)this.module).phase.getValue() != PhaseMode.OFF && (((PacketFly)this.module).phase.getValue() == PhaseMode.SEMI || ((PacketFly)this.module).checkHitBox())) {
                ListenerMove.mc.player.noClip = true;
            }
        }
    }
}

