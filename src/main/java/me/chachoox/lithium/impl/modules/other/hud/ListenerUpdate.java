/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.other.hud;

import me.chachoox.lithium.impl.event.events.update.UpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.other.hud.Hud;

public class ListenerUpdate
extends ModuleListener<Hud, UpdateEvent> {
    public ListenerUpdate(Hud module) {
        super(module, UpdateEvent.class);
    }

    @Override
    public void call(UpdateEvent event) {
        if (((Hud)this.module).timer.passed(1000L)) {
            ((Hud)this.module).outgoingPackets = 0;
            ((Hud)this.module).incomingPackets = 0;
            ((Hud)this.module).timer.reset();
        }
    }
}

