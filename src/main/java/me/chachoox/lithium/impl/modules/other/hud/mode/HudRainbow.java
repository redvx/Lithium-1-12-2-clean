/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.other.hud.mode;

public enum HudRainbow {
    HORIZONTAL("\u00a7+"),
    VERTICAL("\u00a7-"),
    DIAGONAL("\u00a7=");

    private final String color;

    private HudRainbow(String color) {
        this.color = color;
    }

    public String getColor() {
        return this.color;
    }
}

