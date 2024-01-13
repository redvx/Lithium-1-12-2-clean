/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.text.ITextComponent
 */
package me.chachoox.lithium.asm.ducks;

import java.util.function.Supplier;
import net.minecraft.util.text.ITextComponent;

public interface ITextComponentBase {
    public void setFormattingHook(Supplier<String> var1);

    public void setUnFormattedHook(Supplier<String> var1);

    public ITextComponent copyNoSiblings();
}

