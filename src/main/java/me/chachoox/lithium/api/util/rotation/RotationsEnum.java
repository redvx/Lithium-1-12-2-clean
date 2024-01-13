/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.api.util.rotation;

public enum RotationsEnum {
    PACKET,
    NORMAL,
    NONE;

    public static final String[] ALIASES;
    public static final String DESCRIPTION = "None - Does not rotate / Normal - Rotates normally by changing yaw & pitch / Packet - Uses packets to rotate, could get you kicked for too many packets.";

    static {
        ALIASES = new String[]{"Rotations", "rotate", "rots", "rotation"};
    }
}

