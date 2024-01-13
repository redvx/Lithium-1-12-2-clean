/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.api.event.bus.api;

import me.chachoox.lithium.api.event.bus.api.Caller;

public interface IListener<E>
extends Caller<E> {
    public int getPriority();

    public Class<? super E> getTarget();

    public Class<?> getType();
}

