/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.text.event.ClickEvent
 */
package me.chachoox.lithium.asm.ducks;

import java.util.function.Supplier;
import net.minecraft.util.text.event.ClickEvent;

public interface IStyle {
    public void setRightClickEvent(ClickEvent var1);

    public void setMiddleClickEvent(ClickEvent var1);

    public ClickEvent getRightClickEvent();

    public ClickEvent getMiddleClickEvent();

    public void setSuppliedInsertion(Supplier<String> var1);

    public void setRightInsertion(Supplier<String> var1);

    public void setMiddleInsertion(Supplier<String> var1);

    public String getRightInsertion();

    public String getMiddleInsertion();
}

