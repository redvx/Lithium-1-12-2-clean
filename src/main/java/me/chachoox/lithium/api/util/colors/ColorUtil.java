/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.api.util.colors;

import java.awt.Color;

public class ColorUtil {
    public static int toARGB(int r, int g, int b, int a) {
        return (r << 16) + (g << 8) + b + (a << 24);
    }

    public static int transparency(int color, double alpha) {
        Color c = new Color(color);
        float r = 0.003921569f * (float)c.getRed();
        float g = 0.003921569f * (float)c.getGreen();
        float b = 0.003921569f * (float)c.getBlue();
        return new Color(r, g, b, (float)alpha).getRGB();
    }

    public static int intFromHex(String hex) {
        try {
            return Integer.parseInt(hex, 16);
        }
        catch (NumberFormatException e) {
            return -1;
        }
    }

    public static String hexFromInt(int color) {
        return ColorUtil.hexFromInt(new Color(color));
    }

    public static String hexFromInt(Color color) {
        return Integer.toHexString(color.getRGB()).substring(2);
    }

    public static Color changeAlpha(Color origColor, int alpha) {
        return new Color(origColor.getRed(), origColor.getGreen(), origColor.getBlue(), alpha);
    }

    public static int staticRainbow(float offset, Color color) {
        double timer = (double)System.currentTimeMillis() % 4375.0 / 2125.0;
        float[] hsb = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        float brightness = (float)((double)hsb[2] * Math.abs(((double)offset + timer) % 1.0 - (double)0.55f) + (double)0.45f);
        return Color.HSBtoRGB(hsb[0], hsb[1], brightness);
    }

    public static float[] toArray(int color) {
        return new float[]{(float)(color >> 16 & 0xFF) / 255.0f, (float)(color >> 8 & 0xFF) / 255.0f, (float)(color & 0xFF) / 255.0f, (float)(color >> 24 & 0xFF) / 255.0f};
    }

    public static Color toColor(float red, float green, float blue, float alpha) {
        if (!(green < 0.0f) && !(green > 100.0f)) {
            if (!(blue < 0.0f) && !(blue > 100.0f)) {
                if (!(alpha < 0.0f) && !(alpha > 1.0f)) {
                    red = red % 360.0f / 360.0f;
                    float blueOff = (double)(blue /= 100.0f) < 0.0 ? blue * (1.0f + green) : blue + (green /= 100.0f) - green * blue;
                    green = 2.0f * blue - blueOff;
                    blue = Math.max(0.0f, ColorUtil.getFactor(green, blueOff, red + 0.33333334f));
                    float max = Math.max(0.0f, ColorUtil.getFactor(green, blueOff, red));
                    green = Math.max(0.0f, ColorUtil.getFactor(green, blueOff, red - 0.33333334f));
                    blue = Math.min(blue, 1.0f);
                    max = Math.min(max, 1.0f);
                    green = Math.min(green, 1.0f);
                    return new Color(blue, max, green, alpha);
                }
                throw new IllegalArgumentException("Color parameter outside of expected range - Alpha");
            }
            throw new IllegalArgumentException("Color parameter outside of expected range - Lightness");
        }
        throw new IllegalArgumentException("Color parameter outside of expected range - Saturation");
    }

    public static float getFactor(float red, float green, float blue) {
        if (blue < 0.0f) {
            blue += 1.0f;
        }
        if (blue > 1.0f) {
            blue -= 1.0f;
        }
        if (6.0f * blue < 1.0f) {
            return red + (green - red) * 6.0f * blue;
        }
        if (2.0f * blue < 1.0f) {
            return green;
        }
        return 3.0f * blue < 2.0f ? red + (green - red) * 6.0f * (0.6666667f - blue) : red;
    }
}

