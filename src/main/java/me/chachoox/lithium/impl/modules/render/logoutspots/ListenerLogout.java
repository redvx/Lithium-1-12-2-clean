/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.render.logoutspots;

import me.chachoox.lithium.impl.event.events.network.DisconnectEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.render.logoutspots.LogoutSpots;

public class ListenerLogout
extends ModuleListener<LogoutSpots, DisconnectEvent> {
    public ListenerLogout(LogoutSpots module) {
        super(module, DisconnectEvent.class);
    }

    @Override
    public void call(DisconnectEvent event) {
        ((LogoutSpots)this.module).spots.clear();
    }
}

