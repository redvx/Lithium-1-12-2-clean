/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.other.hud.mode;

public enum Welcomer {
    NONE(""),
    CLASSIC("Welcome <Player>"),
    SHALOM("Shalom <Player>"),
    HELLO("Hello <Player> :^)"),
    JAPANESE("Konichiwa <Player> :3"),
    PINWHEEL("Heil <Player> >:^)"),
    TIME("<Time> <Player>"),
    SEXY("<Player> is so sexy"),
    SMELLY("<Player> is smelly"),
    WAGWAN("Wagwan <Player>"),
    CHE("Che <Player>"),
    CUSTOM("");

    private final String welcomer;

    private Welcomer(String welcomer) {
        this.welcomer = welcomer;
    }

    public String getWelcomer() {
        return this.welcomer;
    }
}

