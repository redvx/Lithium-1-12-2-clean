/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.api.util.render.animation;

import me.chachoox.lithium.api.util.render.animation.Animation;
import me.chachoox.lithium.api.util.render.animation.Direction;

public class DecelerateAnimation
extends Animation {
    public DecelerateAnimation(int ms, double endPoint) {
        super(ms, endPoint);
    }

    public DecelerateAnimation(int ms, double endPoint, Direction direction) {
        super(ms, endPoint, direction);
    }

    @Override
    protected double getEquation(double x) {
        double x1 = x / (double)this.duration;
        return 1.0 - (x1 - 1.0) * (x1 - 1.0);
    }
}

