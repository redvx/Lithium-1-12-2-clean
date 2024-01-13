/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.api.property;

import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.property.util.EnumHelper;

public class EnumProperty<T extends Enum<?>>
extends Property<T> {
    public EnumProperty(T value, String[] aliases, String description) {
        super(value, aliases, description);
    }

    public String getFixedValue() {
        return ((Enum)this.value).name().charAt(0) + ((Enum)this.value).name().toLowerCase().replaceFirst(Character.toString(((Enum)this.value).name().charAt(0)).toLowerCase(), "");
    }

    public void setValueFromString(String string) {
        Enum<?> entry = EnumHelper.fromString((Enum)this.value, string);
        this.setValue(entry);
    }

    public void increment() {
        Enum[] array = (Enum[])((Enum)this.getValue()).getClass().getEnumConstants();
        int length = array.length;
        for (int i = 0; i < length; ++i) {
            if (!array[i].name().equalsIgnoreCase(this.getFixedValue())) continue;
            if (++i > array.length - 1) {
                i = 0;
            }
            this.setValue(array[i]);
        }
    }

    public void decrement() {
        Enum[] array = (Enum[])((Enum)this.getValue()).getClass().getEnumConstants();
        int length = array.length;
        for (int i = 0; i < length; ++i) {
            if (!array[i].name().equalsIgnoreCase(this.getFixedValue())) continue;
            if (--i < 0) {
                i = array.length - 1;
            }
            this.setValue(array[i]);
        }
    }
}

