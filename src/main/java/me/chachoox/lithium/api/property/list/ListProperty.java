/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.api.property.list;

import java.util.ArrayList;
import java.util.List;
import me.chachoox.lithium.api.property.Property;

public abstract class ListProperty<T>
extends Property<List<T>> {
    public ListProperty(List<T> list, String[] aliases) {
        super(list, aliases);
    }

    public ListProperty(String[] aliases) {
        super(new ArrayList(), aliases);
    }
}

