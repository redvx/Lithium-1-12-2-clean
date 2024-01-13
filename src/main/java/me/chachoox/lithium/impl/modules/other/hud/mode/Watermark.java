/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.other.hud.mode;

public enum Watermark {
    NONE(""),
    LITHIUM("Lithium \u00a77v0.8.0"),
    SEXMASTER("SexMaster.CC \u00a77v1" + "v0.8.0".replace("v0", "")),
    CUSTOM("");

    private final String watermark;

    private Watermark(String watermark) {
        this.watermark = watermark;
    }

    public String getWatermark() {
        return this.watermark;
    }
}

