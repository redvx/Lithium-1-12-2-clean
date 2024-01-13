/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.event.listener;

import me.chachoox.lithium.api.event.bus.Listener;
import me.chachoox.lithium.api.event.bus.api.Caller;

public class LambdaListener<E>
extends Listener<E> {
    private final Caller<E> caller;

    public LambdaListener(Class<? super E> target, Caller<E> invoker) {
        this(target, 10, invoker);
    }

    public LambdaListener(Class<? super E> target, Class<?> type, Caller<E> caller) {
        this(target, 10, type, caller);
    }

    public LambdaListener(Class<? super E> target, int priority, Caller<E> caller) {
        this(target, priority, null, caller);
    }

    public LambdaListener(Class<? super E> target, int priority, Class<?> type, Caller<E> caller) {
        super(target, priority, type);
        this.caller = caller;
    }

    @Override
    public void call(E event) {
        this.caller.call(event);
    }
}

