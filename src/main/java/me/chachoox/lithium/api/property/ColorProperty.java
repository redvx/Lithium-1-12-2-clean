/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.api.property;

import java.awt.Color;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.impl.modules.other.colours.Colours;

public class ColorProperty
extends Property<Color> {
    private boolean global;

    public ColorProperty(Color color, boolean global, String[] aliases) {
        super(color, aliases);
        this.value = color;
        this.global = global;
    }

    public Color getColor() {
        if (this.isGlobal()) {
            return Colours.get().getColourCustomAlpha(((Color)this.value).getAlpha());
        }
        return (Color)this.value;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }

    public boolean isGlobal() {
        return this.global;
    }
}

