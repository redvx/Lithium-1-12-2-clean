/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.combat.instantweb;

import me.chachoox.lithium.api.util.entity.CombatUtil;
import me.chachoox.lithium.impl.event.events.movement.MotionUpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.combat.instantweb.InstantWeb;

public class ListenerMotion
extends ModuleListener<InstantWeb, MotionUpdateEvent> {
    public ListenerMotion(InstantWeb module) {
        super(module, MotionUpdateEvent.class);
    }

    @Override
    public void call(MotionUpdateEvent event) {
        if (ListenerMotion.mc.player.posY != ((InstantWeb)this.module).enablePosY && ((InstantWeb)this.module).jumpDisable.getValue().booleanValue()) {
            ((InstantWeb)this.module).disable();
            return;
        }
        ((InstantWeb)this.module).target = CombatUtil.getTarget(((Float)((InstantWeb)this.module).targetRange.getValue()).floatValue());
        if (((InstantWeb)this.module).target == null) {
            return;
        }
        ((InstantWeb)this.module).onPreEvent(((InstantWeb)this.module).getPlacements(), event);
    }
}

