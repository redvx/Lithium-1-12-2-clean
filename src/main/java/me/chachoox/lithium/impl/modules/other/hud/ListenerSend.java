/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.other.hud;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.other.hud.Hud;

public class ListenerSend
extends ModuleListener<Hud, PacketEvent.Send<?>> {
    public ListenerSend(Hud module) {
        super(module, PacketEvent.Send.class, Integer.MIN_VALUE);
    }

    @Override
    public void call(PacketEvent.Send<?> event) {
        if (!event.isCanceled()) {
            ++((Hud)this.module).outgoingPackets;
        }
    }
}

