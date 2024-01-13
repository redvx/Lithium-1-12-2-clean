/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.combat.instantexp;

import me.chachoox.lithium.impl.event.events.update.TickEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.combat.instantexp.InstantEXP;

public class ListenerTick
extends ModuleListener<InstantEXP, TickEvent> {
    public ListenerTick(InstantEXP module) {
        super(module, TickEvent.class);
    }

    @Override
    public void call(TickEvent event) {
        ((InstantEXP)this.module).doEXP();
    }
}

