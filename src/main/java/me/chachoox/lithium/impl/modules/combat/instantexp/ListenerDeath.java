/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.combat.instantexp;

import me.chachoox.lithium.impl.event.events.entity.DeathEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.combat.instantexp.InstantEXP;

public class ListenerDeath
extends ModuleListener<InstantEXP, DeathEvent> {
    public ListenerDeath(InstantEXP module) {
        super(module, DeathEvent.class);
    }

    @Override
    public void call(DeathEvent event) {
        if (event.getEntity() == ListenerDeath.mc.player) {
            ((InstantEXP)this.module).disable();
        }
    }
}

