/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.text.ITextComponent
 */
package me.chachoox.lithium.api.interfaces;

import net.minecraft.util.text.ITextComponent;
import org.apache.logging.log4j.Level;

public interface Loggable {
    public void log(String var1);

    public void log(String var1, int var2);

    public void log(String var1, boolean var2);

    public void log(ITextComponent var1, boolean var2);

    public void log(Level var1, String var2);

    public void logNoMark(String var1);

    public void logNoMark(String var1, int var2);

    public void logNoMark(String var1, boolean var2);

    public void logNoMark(ITextComponent var1, boolean var2);
}

