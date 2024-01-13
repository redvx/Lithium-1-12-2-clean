/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.api.util.math;

import me.chachoox.lithium.api.util.math.Passable;

public class StopWatch
implements Passable {
    private volatile long time;

    public boolean passed(double ms) {
        return (double)(System.currentTimeMillis() - this.time) >= ms;
    }

    @Override
    public boolean passed(long ms) {
        return System.currentTimeMillis() - this.time >= ms;
    }

    public StopWatch reset() {
        this.time = System.currentTimeMillis();
        return this;
    }

    public boolean sleep(long time) {
        if (this.getTime() >= time) {
            this.reset();
            return true;
        }
        return false;
    }

    public long getTime() {
        return System.currentTimeMillis() - this.time;
    }

    public void setTime(long ns) {
        this.time = ns;
    }
}

