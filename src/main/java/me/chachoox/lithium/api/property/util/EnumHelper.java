/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.api.property.util;

public class EnumHelper {
    public static Enum<?> fromString(Enum<?> initial, String name) {
        Object e = EnumHelper.fromString(initial.getDeclaringClass(), name);
        if (e != null) {
            return e;
        }
        return initial;
    }

    public static <T extends Enum<?>> T fromString(Class<T> type, String name) {
        for (Enum constant : (Enum[])type.getEnumConstants()) {
            if (!constant.name().equalsIgnoreCase(name)) continue;
            return (T)constant;
        }
        return null;
    }
}

