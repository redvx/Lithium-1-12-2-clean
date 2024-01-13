/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.movement.speed.enums;

public enum JumpMode {
    VANILLA(0.42f),
    LOW(0.39999995f);

    private final float height;

    private JumpMode(float height) {
        this.height = height;
    }

    public float getHeight() {
        return this.height;
    }
}

