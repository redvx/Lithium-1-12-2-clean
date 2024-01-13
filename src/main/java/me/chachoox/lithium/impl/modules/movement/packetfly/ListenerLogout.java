/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.movement.packetfly;

import me.chachoox.lithium.impl.event.events.network.DisconnectEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.movement.packetfly.PacketFly;

public class ListenerLogout
extends ModuleListener<PacketFly, DisconnectEvent> {
    public ListenerLogout(PacketFly module) {
        super(module, DisconnectEvent.class);
    }

    @Override
    public void call(DisconnectEvent event) {
        ((PacketFly)this.module).disable();
    }
}

