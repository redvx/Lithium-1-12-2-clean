/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.misc.visualrange.mode;

public enum VisualRangeMessage {
    NORMAL(" has been spotted thanks to SexMaster.CC", " is no longer in sight thanks to SexMaster.CC!"),
    ALI(" nigger", " nigger"),
    GAY(" Hi OwO :)", " Bye UwU :("),
    ALTERNATIVE(" has entered your visual range", " has left your visual range");

    private final String join;
    private final String leave;

    private VisualRangeMessage(String join, String leave) {
        this.join = join;
        this.leave = leave;
    }

    public String getJoin() {
        return this.join;
    }

    public String getLeave() {
        return this.leave;
    }
}

