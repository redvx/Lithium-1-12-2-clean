/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.api.event.bus.api;

import me.chachoox.lithium.api.event.bus.api.IListener;

public interface EventBus {
    public static final int DEFAULT_PRIORITY = 10;

    public void dispatch(Object var1);

    public void dispatch(Object var1, Class<?> var2);

    public void register(IListener<?> var1);

    public void unregister(IListener<?> var1);

    public void dispatchReversed(Object var1, Class<?> var2);

    public void subscribe(Object var1);

    public void unsubscribe(Object var1);

    public boolean isSubscribed(Object var1);

    public boolean hasSubscribers(Class<?> var1);

    public boolean hasSubscribers(Class<?> var1, Class<?> var2);
}

