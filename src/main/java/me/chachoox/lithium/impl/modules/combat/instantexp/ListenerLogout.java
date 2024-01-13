/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.combat.instantexp;

import me.chachoox.lithium.impl.event.events.network.DisconnectEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.combat.instantexp.InstantEXP;

public class ListenerLogout
extends ModuleListener<InstantEXP, DisconnectEvent> {
    public ListenerLogout(InstantEXP module) {
        super(module, DisconnectEvent.class);
    }

    @Override
    public void call(DisconnectEvent event) {
        ((InstantEXP)this.module).disable();
    }
}

