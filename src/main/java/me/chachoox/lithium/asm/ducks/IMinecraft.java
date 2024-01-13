/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Session
 *  net.minecraft.util.Timer
 */
package me.chachoox.lithium.asm.ducks;

import net.minecraft.util.Session;
import net.minecraft.util.Timer;

public interface IMinecraft {
    public int getRightClickDelay();

    public void setRightClickDelay(int var1);

    public void setLeftClickCounter(int var1);

    public Timer getTimer();

    public void click(Click var1);

    public void setSession(Session var1);

    public static enum Click {
        RIGHT,
        LEFT,
        MIDDLE;

    }
}

