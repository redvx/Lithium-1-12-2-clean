/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.render.pollosesp.util;

public class Plane {
    private final double x;
    private final double y;
    private final boolean visible;

    public Plane(double x, double y, boolean visible) {
        this.x = x;
        this.y = y;
        this.visible = visible;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public boolean isVisible() {
        return this.visible;
    }
}

