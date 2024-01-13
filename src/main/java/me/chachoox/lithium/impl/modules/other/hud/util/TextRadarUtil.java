/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.other.hud.util;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class TextRadarUtil {
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        LinkedList<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());
        LinkedHashMap result = new LinkedHashMap();
        for (Map.Entry entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public static String getDistanceColor(float distance) {
        if (distance >= 25.0f) {
            return "\u00a7a";
        }
        if (distance > 15.0f) {
            return "\u00a7e";
        }
        if (distance >= 50.0f) {
            return "\u00a76";
        }
        return "\u00a7c";
    }

    public static String getHealthColor(int health) {
        if (health >= 20) {
            return "\u00a7a";
        }
        if (health >= 10) {
            return "\u00a7e";
        }
        if (health >= 5) {
            return "\u00a76";
        }
        return "\u00a7c";
    }
}

