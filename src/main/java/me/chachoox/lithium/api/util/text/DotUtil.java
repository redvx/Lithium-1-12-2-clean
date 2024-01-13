/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.api.util.text;

import me.chachoox.lithium.api.util.math.StopWatch;

public class DotUtil {
    private static final StopWatch dotTimer = new StopWatch();
    private static String dots = "";

    public static String getDots() {
        if (dotTimer.passed(500L)) {
            dots = dots + ".";
            dotTimer.reset();
        }
        if (dots.length() > 3) {
            dots = "";
        }
        return dots;
    }
}

