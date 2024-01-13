/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.api.event.bus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import me.chachoox.lithium.api.event.bus.Listener;
import me.chachoox.lithium.api.event.bus.api.Subscriber;

public class SubscriberImpl
implements Subscriber {
    protected final List<Listener<?>> listeners = new ArrayList();

    @Override
    public Collection<Listener<?>> getListeners() {
        return this.listeners;
    }
}

