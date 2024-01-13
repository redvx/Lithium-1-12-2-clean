/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.api.util.render.animation;

public enum Direction {
    FORWARDS,
    BACKWARDS;


    public Direction opposite() {
        if (this == FORWARDS) {
            return BACKWARDS;
        }
        return FORWARDS;
    }
}

