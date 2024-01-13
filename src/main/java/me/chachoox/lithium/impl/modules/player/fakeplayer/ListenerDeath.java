/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.player.fakeplayer;

import me.chachoox.lithium.impl.event.events.entity.DeathEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.player.fakeplayer.FakePlayer;

public class ListenerDeath
extends ModuleListener<FakePlayer, DeathEvent> {
    public ListenerDeath(FakePlayer module) {
        super(module, DeathEvent.class);
    }

    @Override
    public void call(DeathEvent event) {
        if (event.getEntity() == ListenerDeath.mc.player) {
            ((FakePlayer)this.module).disable();
        }
    }
}

