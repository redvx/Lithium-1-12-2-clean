/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.misc.announcer;

import me.chachoox.lithium.impl.event.events.entity.DeathEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.misc.announcer.Announcer;

public class ListenerDeath
extends ModuleListener<Announcer, DeathEvent> {
    public ListenerDeath(Announcer module) {
        super(module, DeathEvent.class);
    }

    @Override
    public void call(DeathEvent event) {
        if (event.getEntity() == ListenerDeath.mc.player) {
            ((Announcer)this.module).reset();
        }
    }
}

