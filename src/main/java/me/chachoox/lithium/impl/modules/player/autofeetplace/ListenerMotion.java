/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.player.autofeetplace;

import me.chachoox.lithium.impl.event.events.movement.MotionUpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.player.autofeetplace.AutoFeetPlace;

public class ListenerMotion
extends ModuleListener<AutoFeetPlace, MotionUpdateEvent> {
    public ListenerMotion(AutoFeetPlace module) {
        super(module, MotionUpdateEvent.class);
    }

    @Override
    public void call(MotionUpdateEvent event) {
        if ((ListenerMotion.mc.player.posY != ((AutoFeetPlace)this.module).enablePosY || !ListenerMotion.mc.player.onGround) && ((AutoFeetPlace)this.module).jumpDisable.getValue().booleanValue()) {
            ((AutoFeetPlace)this.module).disable();
            return;
        }
        ((AutoFeetPlace)this.module).onPreEvent(((AutoFeetPlace)this.module).getPlacements(), event);
    }
}

