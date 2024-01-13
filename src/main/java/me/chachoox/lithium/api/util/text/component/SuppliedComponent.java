/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.TextComponentBase
 *  net.minecraft.util.text.TextComponentString
 */
package me.chachoox.lithium.api.util.text.component;

import java.util.function.Supplier;
import me.chachoox.lithium.api.util.text.component.AbstractTextComponent;
import me.chachoox.lithium.api.util.text.component.SimpleTextFormatHook;
import me.chachoox.lithium.asm.ducks.ITextComponentBase;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentBase;
import net.minecraft.util.text.TextComponentString;

public class SuppliedComponent
extends AbstractTextComponent {
    protected final Supplier<String> supplier;

    public SuppliedComponent(Supplier<String> supplier) {
        super(supplier.get());
        this.supplier = supplier;
        ((ITextComponentBase)((Object)this)).setFormattingHook(new SimpleTextFormatHook((TextComponentBase)this));
        ((ITextComponentBase)((Object)this)).setUnFormattedHook(new SimpleTextFormatHook((TextComponentBase)this));
    }

    @Override
    public String getText() {
        return this.supplier.get();
    }

    @Override
    public String getUnformattedComponentText() {
        return this.supplier.get();
    }

    @Override
    public TextComponentString createCopy() {
        SuppliedComponent copy = new SuppliedComponent(this.supplier);
        copy.setStyle(this.getStyle().createShallowCopy());
        for (ITextComponent component : this.getSiblings()) {
            copy.appendSibling(component.createCopy());
        }
        return copy;
    }
}

