/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.api.module;

public enum Category {
    COMBAT("Combat"),
    MISC("Misc"),
    MOVEMENT("Movement"),
    PLAYER("Player"),
    RENDER("Render"),
    OTHER("Other");

    final String label;

    private Category(String label) {
        this.label = label;
    }

    public final String getLabel() {
        return this.label;
    }
}

