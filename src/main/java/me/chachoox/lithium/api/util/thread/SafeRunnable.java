/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.api.util.thread;

@FunctionalInterface
public interface SafeRunnable
extends Runnable {
    public void runSafely() throws Throwable;

    @Override
    default public void run() {
        try {
            this.runSafely();
        }
        catch (Throwable t) {
            this.handle(t);
        }
    }

    default public void handle(Throwable t) {
        t.printStackTrace();
    }
}

