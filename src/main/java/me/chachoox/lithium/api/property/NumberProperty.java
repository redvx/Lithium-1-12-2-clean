/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.api.property;

import me.chachoox.lithium.api.property.Property;

public class NumberProperty<T extends Number>
extends Property<T> {
    private final T minimum;
    private final T maximum;
    private T steps;
    private final boolean clamp;

    public NumberProperty(T value, T minimum, T maximum, String[] aliases, String description) {
        super(value, aliases, description);
        this.clamp = true;
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public NumberProperty(T value, T minimum, T maximum, T steps, String[] aliases, String description) {
        super(value, aliases, description);
        this.clamp = true;
        this.minimum = minimum;
        this.maximum = maximum;
        this.steps = steps;
    }

    public NumberProperty(T value, String[] aliases, String desc) {
        super(value, aliases, desc);
        this.clamp = false;
        this.maximum = null;
        this.minimum = null;
        this.steps = null;
    }

    public T getMaximum() {
        return this.maximum;
    }

    public T getMinimum() {
        return this.minimum;
    }

    public T getSteps() {
        return this.steps;
    }

    @Override
    public void setValue(T value) {
        if (this.clamp & (this.maximum != null && this.minimum != null)) {
            if (value instanceof Integer) {
                if (((Number)value).intValue() > ((Number)this.maximum).intValue()) {
                    value = this.maximum;
                } else if (((Number)value).intValue() < ((Number)this.minimum).intValue()) {
                    value = this.minimum;
                }
            } else if (value instanceof Float) {
                if (((Number)value).floatValue() > ((Number)this.maximum).floatValue()) {
                    value = this.maximum;
                } else if (((Number)value).floatValue() < ((Number)this.minimum).floatValue()) {
                    value = this.minimum;
                }
            } else if (value instanceof Double) {
                if (((Number)value).doubleValue() > ((Number)this.maximum).doubleValue()) {
                    value = this.maximum;
                } else if (((Number)value).doubleValue() < ((Number)this.minimum).doubleValue()) {
                    value = this.minimum;
                }
            } else if (value instanceof Long) {
                if (((Number)value).longValue() > ((Number)this.maximum).longValue()) {
                    value = this.maximum;
                } else if (((Number)value).longValue() < ((Number)this.minimum).longValue()) {
                    value = this.minimum;
                }
            } else if (value instanceof Short) {
                if (((Number)value).shortValue() > ((Number)this.maximum).shortValue()) {
                    value = this.maximum;
                } else if (((Number)value).shortValue() < ((Number)this.minimum).shortValue()) {
                    value = this.minimum;
                }
            } else if (value instanceof Byte) {
                if (((Number)value).byteValue() > ((Number)this.maximum).byteValue()) {
                    value = this.maximum;
                } else if (((Number)value).byteValue() < ((Number)this.minimum).byteValue()) {
                    value = this.minimum;
                }
            }
        }
        super.setValue(value);
    }
}

