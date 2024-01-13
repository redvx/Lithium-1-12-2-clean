/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.movement.holepull;

import me.chachoox.lithium.impl.event.events.update.UpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.movement.holepull.HolePull;
import me.chachoox.lithium.impl.modules.movement.holepull.mode.PullMode;

public class ListenerUpdate
extends ModuleListener<HolePull, UpdateEvent> {
    public ListenerUpdate(HolePull module) {
        super(module, UpdateEvent.class);
    }

    @Override
    public void call(UpdateEvent event) {
        if (ListenerUpdate.mc.player.isSpectator()) {
            return;
        }
        if (((HolePull)this.module).mode.getValue() == PullMode.SNAP && ((HolePull)this.module).timer.getValue().booleanValue()) {
            if (((HolePull)this.module).boosted >= (Integer)((HolePull)this.module).timerLength.getValue()) {
                Managers.TIMER.reset();
                return;
            }
            Managers.TIMER.set(((Float)((HolePull)this.module).timerAmount.getValue()).floatValue());
            ++((HolePull)this.module).boosted;
        }
    }
}

