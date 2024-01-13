/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 */
package me.chachoox.lithium.api.util.math;

import java.math.BigDecimal;
import java.math.RoundingMode;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class MathUtil
implements Minecraftable {
    public static int clamp(int num, int min, int max) {
        return num < min ? min : Math.min(num, max);
    }

    public static float clamp(float num, float min, float max) {
        return num < min ? min : Math.min(num, max);
    }

    public static double round(double value, int places) {
        return places < 0 ? value : new BigDecimal(value).setScale(places, RoundingMode.HALF_UP).doubleValue();
    }

    public static Object round(String value, int places) {
        return places < 0 ? value : Float.valueOf(new BigDecimal(value).setScale(places, RoundingMode.HALF_UP).floatValue());
    }

    public static float round(float value, int places, float min, float max) {
        return MathHelper.clamp((float)(places < 0 ? value : new BigDecimal(value).setScale(places, RoundingMode.HALF_UP).floatValue()), (float)min, (float)max);
    }

    public static float rad(float angle) {
        return (float)((double)angle * Math.PI / 180.0);
    }

    public static float square(float in) {
        return in * in;
    }

    public static double angle(Vec3d vec3d, Vec3d other) {
        double lengthSq = vec3d.length() * other.length();
        if (lengthSq < 1.0E-4) {
            return 0.0;
        }
        double dot = vec3d.dotProduct(other);
        double arg = dot / lengthSq;
        if (arg > 1.0) {
            return 0.0;
        }
        if (arg < -1.0) {
            return 180.0;
        }
        return Math.acos(arg) * 180.0 / Math.PI;
    }

    public static Vec3d fromTo(Vec3d from, Vec3d to) {
        return MathUtil.fromTo(from.x, from.y, from.z, to);
    }

    public static Vec3d fromTo(Vec3d from, double x, double y, double z) {
        return MathUtil.fromTo(from.x, from.y, from.z, x, y, z);
    }

    public static Vec3d fromTo(double x, double y, double z, Vec3d to) {
        return MathUtil.fromTo(x, y, z, to.x, to.y, to.z);
    }

    public static Vec3d fromTo(double x, double y, double z, double x2, double y2, double z2) {
        return new Vec3d(x2 - x, y2 - y, z2 - z);
    }

    public static float fixedNametagScaling(float scale) {
        return scale * 0.01f;
    }

    public static double getYDifferenceSq(double firstY, double secondY) {
        double y = firstY - secondY;
        return y * y;
    }

    public static double distance2D(Vec3d from, Vec3d to) {
        double x = to.x - from.x;
        double z = to.z - from.z;
        return Math.sqrt(x * x + z * z);
    }
}

