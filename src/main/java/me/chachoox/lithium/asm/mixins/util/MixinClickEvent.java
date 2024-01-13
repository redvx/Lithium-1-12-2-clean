/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.text.event.ClickEvent
 */
package me.chachoox.lithium.asm.mixins.util;

import me.chachoox.lithium.api.util.thread.events.IClickEvent;
import net.minecraft.util.text.event.ClickEvent;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value={ClickEvent.class})
public abstract class MixinClickEvent
implements IClickEvent {
    private Runnable runnable;

    @Override
    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public Runnable getRunnable() {
        return this.runnable;
    }
}

