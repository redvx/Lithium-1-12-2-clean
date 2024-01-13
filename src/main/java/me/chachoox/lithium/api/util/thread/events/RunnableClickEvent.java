/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.text.event.ClickEvent
 *  net.minecraft.util.text.event.ClickEvent$Action
 */
package me.chachoox.lithium.api.util.thread.events;

import me.chachoox.lithium.api.util.thread.events.IClickEvent;
import net.minecraft.util.text.event.ClickEvent;

public class RunnableClickEvent
extends ClickEvent {
    public RunnableClickEvent(Runnable runnable) {
        super(ClickEvent.Action.RUN_COMMAND, "$runnable$");
        ((IClickEvent)((Object)this)).setRunnable(runnable);
    }
}

