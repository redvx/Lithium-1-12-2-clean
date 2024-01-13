/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.api.util.thread;

public class MutableWrapper<T> {
    protected T value;

    public MutableWrapper() {
        this(null);
    }

    public MutableWrapper(T value) {
        this.value = value;
    }

    public T get() {
        return this.value;
    }

    public void set(T value) {
        this.value = value;
    }
}

