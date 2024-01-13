/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.combat.autotrap;

import me.chachoox.lithium.impl.event.events.movement.MotionUpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.combat.autotrap.AutoTrap;

public class ListenerMotion
extends ModuleListener<AutoTrap, MotionUpdateEvent> {
    public ListenerMotion(AutoTrap module) {
        super(module, MotionUpdateEvent.class);
    }

    @Override
    public void call(MotionUpdateEvent event) {
        if (((AutoTrap)this.module).isNull()) {
            ((AutoTrap)this.module).disable();
            return;
        }
        if (ListenerMotion.mc.player.posY != ((AutoTrap)this.module).enablePosY && ((AutoTrap)this.module).jumpDisable.getValue().booleanValue()) {
            ((AutoTrap)this.module).disable();
            return;
        }
        if (((AutoTrap)this.module).placeList != null) {
            ((AutoTrap)this.module).placeList.clear();
        }
        ((AutoTrap)this.module).target = null;
        ((AutoTrap)this.module).getTargets();
        ((AutoTrap)this.module).onPreEvent(((AutoTrap)this.module).placeList, event);
    }
}

