/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.api.property;

import java.awt.Color;
import java.util.List;
import me.chachoox.lithium.api.interfaces.Labeled;
import me.chachoox.lithium.api.observable.Observable;
import me.chachoox.lithium.api.property.list.BlockList;
import me.chachoox.lithium.api.property.list.ItemList;
import me.chachoox.lithium.api.property.util.Bind;
import me.chachoox.lithium.api.property.util.PropertyEvent;

public class Property<T>
extends Observable<PropertyEvent<T>>
implements Labeled {
    private final String[] aliases;
    private String description;
    protected T value;
    protected T initial;

    public Property(T initialValue, String[] aliases, String description) {
        this.value = initialValue;
        this.aliases = aliases;
        this.description = description;
    }

    public Property(T initialValue, String[] aliases) {
        this.value = initialValue;
        this.aliases = aliases;
    }

    @Override
    public String getLabel() {
        return this.aliases[0];
    }

    public String[] getAliases() {
        return this.aliases;
    }

    public T getValue() {
        return this.value;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public String getDescription() {
        return this.description;
    }

    public void setValue(T value) {
        PropertyEvent<T> event = this.onChange(new PropertyEvent<T>(this, value));
        this.value = !event.isCanceled() ? event.getValue() : value;
    }

    public String getValueAsString() {
        return this.value.toString();
    }

    public String getType() {
        if (this.isEnumProperty()) {
            return "Enum";
        }
        if (this instanceof ItemList) {
            return "ItemList";
        }
        if (this instanceof BlockList) {
            return "BlockList";
        }
        return this.getClassName(this.value);
    }

    public <T> String getClassName(T value) {
        return value.getClass().getSimpleName();
    }

    public boolean isNumberProperty() {
        return this.value instanceof Double || this.value instanceof Integer || this.value instanceof Short || this.value instanceof Long || this.value instanceof Float;
    }

    public boolean isEnumProperty() {
        return !this.isNumberProperty() && !(this.value instanceof Bind) && !(this.value instanceof String) && !(this.value instanceof Character) && !(this.value instanceof Boolean) && !(this.value instanceof Color) && !(this.value instanceof List);
    }

    public void reset() {
        this.value = this.initial;
    }

    public T getInitial() {
        return this.initial;
    }

    public boolean isStringProperty() {
        return this.value instanceof String;
    }
}

