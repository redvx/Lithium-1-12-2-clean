/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.player.antihitbox;

import me.chachoox.lithium.impl.event.events.update.UpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.player.antihitbox.AntiHitBox;

public class ListenerUpdate
extends ModuleListener<AntiHitBox, UpdateEvent> {
    public ListenerUpdate(AntiHitBox module) {
        super(module, UpdateEvent.class);
    }

    @Override
    public void call(UpdateEvent event) {
        if (((AntiHitBox)this.module).isValid(ListenerUpdate.mc.player.getHeldItemMainhand().getItem())) {
            ((AntiHitBox)this.module).noTrace = true;
            return;
        }
        ((AntiHitBox)this.module).noTrace = false;
    }
}

