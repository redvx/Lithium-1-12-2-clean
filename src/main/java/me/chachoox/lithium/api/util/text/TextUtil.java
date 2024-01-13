/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.api.util.text;

import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.chachoox.lithium.api.interfaces.Minecraftable;

public class TextUtil
implements Minecraftable {
    public static final Pattern PATTERN = Pattern.compile("(?i)\u00a7[0-9A-FK-OR]");

    public static String findMatch(String string) {
        Matcher matcher = PATTERN.matcher(string);
        String s = "\u00a7f";
        while (matcher.find()) {
            s = matcher.group();
        }
        return s;
    }

    public static String removeColor(String string) {
        if (string != null) {
            return PATTERN.matcher(string).replaceAll("");
        }
        return "";
    }

    public static String formatString(String string) {
        return string.toLowerCase();
    }

    public static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String getFixedName(String i) {
        return i.charAt(0) + i.toLowerCase().replaceFirst(Character.toString(i.charAt(0)).toLowerCase(), "");
    }

    public static String generateRandomString(int places) {
        return String.format(" | %s", Integer.toHexString((ThreadLocalRandom.current().nextInt() + 5) * ThreadLocalRandom.current().nextInt()).substring(0, places));
    }
}

