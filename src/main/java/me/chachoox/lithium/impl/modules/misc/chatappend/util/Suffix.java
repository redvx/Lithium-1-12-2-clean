/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.misc.chatappend.util;

public enum Suffix {
    SEXMASTER(" \u23d0 \uff33\uff45\ua1d3\uff2d\u039b\uff53\u01ac\u03b5\u0280\uff0e\uff23\uff23"),
    CHACHOOXWARE(" \u1d04\u029c\u1d00\u1d04\u029c\u1d0f\u1d0f\u0445\u1d21\u1d00\u0280\u1d07"),
    HOCKEYWARE(" \u23d0 \uff28\uff4f\uff43\uff4b\uff45\uff59\uff37\uff41\uff52\uff45"),
    KONAS(" \u23d0 \uff2b\uff4f\uff4e\uff41\uff53"),
    LEUXBACKDOOR(" \u00bb L\u03a3uxB\u03b1ckdoor"),
    XULU(" \u23d0 \u166d \u144c \u14aa \u144c"),
    PHOBOS(" \u23d0 \u1d18\u029c\u1d0f\u0299\u1d0f\ua731.\u1d07\u1d1c"),
    RAION("\u4e28\u24c7\u24b6\u24be\u24c4\u24c3"),
    TROLLHACK(" \u23d0 \uff34\uff32\uff2f\uff2c\uff2c \uff28\uff21\uff23\uff2b"),
    GAMESENSE(" \u300b\u0262\u1d00\u1d0d\u1d07\ua731\u1d07\u0274\ua731\u1d07"),
    OSIRIS(" \u300b\u1d0f\ua731\u026a\u0280\u026a\ua731"),
    ILCLIENT(" \u2551ILCLient\u4e97"),
    LOVER(" > Lover"),
    MOONGOD(" \u23d0 \uff2d\uff4f\uff4f\uff4e\uff27\uff4f\uff44"),
    YAKIGOD(" \u23d0 \u028f\u1d00\u1d0b\u026a\u0262\u1d0f\u1d05.\u1d04\u1d04"),
    BRANK(" \u23d0 \u0299\u0280\u1d00\u0274\u1d0b1"),
    TROLLGOD(" | Konas");

    private final String suffix;

    private Suffix(String suffix) {
        this.suffix = suffix;
    }

    public String getSuffix() {
        return this.suffix;
    }
}

