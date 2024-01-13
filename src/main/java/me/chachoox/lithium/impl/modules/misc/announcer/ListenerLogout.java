/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.misc.announcer;

import me.chachoox.lithium.impl.event.events.network.DisconnectEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.misc.announcer.Announcer;

public class ListenerLogout
extends ModuleListener<Announcer, DisconnectEvent> {
    public ListenerLogout(Announcer module) {
        super(module, DisconnectEvent.class);
    }

    @Override
    public void call(DisconnectEvent event) {
        ((Announcer)this.module).reset();
    }
}

