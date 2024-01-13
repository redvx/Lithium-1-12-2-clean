/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.api.util.math;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RoundingUtil {
    public static double roundDouble(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static double roundToStep(double value, double step) {
        if (step < 0.0) {
            throw new IllegalArgumentException();
        }
        return step * (double)Math.round(value * (1.0 / step));
    }

    public static float roundFloat(float value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.floatValue();
    }

    public static float roundToStep(float value, float step) {
        if (step < 0.0f) {
            throw new IllegalArgumentException();
        }
        return step * (float)Math.round(value * (1.0f / step));
    }
}

