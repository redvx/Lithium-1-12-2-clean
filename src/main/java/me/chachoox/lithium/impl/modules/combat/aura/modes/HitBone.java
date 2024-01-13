/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.combat.aura.modes;

public enum HitBone {
    FEET(0.0f),
    LEG(0.03f),
    DICK(0.6f),
    CHEST(0.7f),
    NECK(0.8f),
    HEAD(1.0f);

    private final float height;

    private HitBone(float height) {
        this.height = height;
    }

    public float getHeight() {
        return this.height;
    }
}

