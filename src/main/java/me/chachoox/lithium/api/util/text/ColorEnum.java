/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.api.util.text;

public enum ColorEnum {
    NONE(""),
    BLACK("\u00a70"),
    WHITE("\u00a7f"),
    DARKBLUE("\u00a71"),
    DARKGREEN("\u00a72"),
    DARKAQUA("\u00a73"),
    DARKRED("\u00a74"),
    DARKPURPLE("\u00a75"),
    DARKGRAY("\u00a78"),
    GRAY("\u00a77"),
    GOLD("\u00a76"),
    BLUE("\u00a79"),
    GREEN("\u00a7a"),
    AQUA("\u00a7b"),
    RED("\u00a7c"),
    LIGHTPURPLE("\u00a7d"),
    YELLOW("\u00a7e"),
    OBFUSCATED("\u00a7k"),
    BOLD("\u00a7l"),
    STRIKE("\u00a7m"),
    UNDERLINE("\u00a7n"),
    ITALIC("\u00a7o"),
    RESET("\u00a7r"),
    RAINBOW("\u00a7+");

    private final String color;
    public static final String COLOR_DESC = "Current chat formatting color";

    private ColorEnum(String color) {
        this.color = color;
    }

    public String getColor() {
        return this.color;
    }
}

