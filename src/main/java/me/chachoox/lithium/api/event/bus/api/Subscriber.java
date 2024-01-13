/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.api.event.bus.api;

import java.util.Collection;
import me.chachoox.lithium.api.event.bus.Listener;

public interface Subscriber {
    public Collection<Listener<?>> getListeners();
}

