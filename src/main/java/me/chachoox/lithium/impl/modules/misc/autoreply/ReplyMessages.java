/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.misc.autoreply;

public enum ReplyMessages {
    LITHIUM("[Lithium] I'm afk."),
    SEXMASTER("[SexMaster.CC] I'm afk."),
    POLLOS("im gay."),
    MONEY("hello money gang"),
    COORDS("<Coords>");

    private final String reply;

    private ReplyMessages(String message) {
        this.reply = message;
    }

    public String getReply() {
        return this.reply;
    }
}

