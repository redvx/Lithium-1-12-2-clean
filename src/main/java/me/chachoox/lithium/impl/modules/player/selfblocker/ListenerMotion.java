/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.player.selfblocker;

import me.chachoox.lithium.impl.event.events.movement.MotionUpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.player.selfblocker.SelfBlocker;

public class ListenerMotion
extends ModuleListener<SelfBlocker, MotionUpdateEvent> {
    public ListenerMotion(SelfBlocker module) {
        super(module, MotionUpdateEvent.class);
    }

    @Override
    public void call(MotionUpdateEvent event) {
        if (ListenerMotion.mc.player.posY != ((SelfBlocker)this.module).enablePosY && ((SelfBlocker)this.module).jumpDisable.getValue().booleanValue()) {
            ((SelfBlocker)this.module).disable();
            return;
        }
        ((SelfBlocker)this.module).onPreEvent(((SelfBlocker)this.module).getPlacements(), event);
    }
}

