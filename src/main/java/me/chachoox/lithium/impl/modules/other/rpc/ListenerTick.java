/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.other.rpc;

import me.chachoox.lithium.impl.event.events.update.TickEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.other.rpc.RichPresence;

public class ListenerTick
extends ModuleListener<RichPresence, TickEvent> {
    public ListenerTick(RichPresence module) {
        super(module, TickEvent.class);
    }

    @Override
    public void call(TickEvent event) {
        if (((RichPresence)this.module).imageTimer.passed(30000L)) {
            String key;
            RichPresence.RPC.presence.largeImageKey = key = ((RichPresence)this.module).keys.get(0);
            ((RichPresence)this.module).keys.remove(key);
            ((RichPresence)this.module).keys.add(key);
            ((RichPresence)this.module).imageTimer.reset();
        }
    }
}

