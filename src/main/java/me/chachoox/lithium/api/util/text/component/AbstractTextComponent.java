/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.text.TextComponentString
 */
package me.chachoox.lithium.api.util.text.component;

import net.minecraft.util.text.TextComponentString;

public abstract class AbstractTextComponent
extends TextComponentString {
    public static final AbstractTextComponent EMPTY = new AbstractTextComponent(""){

        @Override
        public String getText() {
            return "";
        }

        @Override
        public String getUnformattedComponentText() {
            return "";
        }

        @Override
        public TextComponentString createCopy() {
            return EMPTY;
        }
    };

    public AbstractTextComponent(String initial) {
        super(initial);
    }

    public abstract String getText();

    public abstract String getUnformattedComponentText();

    public abstract TextComponentString createCopy();

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractTextComponent)) {
            return false;
        }
        return this.getText().equals(((AbstractTextComponent)((Object)o)).getText());
    }

    public int hashCode() {
        return super.hashCode();
    }

    public String toString() {
        return "CustomComponent{text='" + this.getText() + '\'' + ", siblings=" + this.siblings + ", style=" + this.getStyle() + '}';
    }
}

