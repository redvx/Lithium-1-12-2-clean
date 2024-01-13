/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.movement.step;

import me.chachoox.lithium.impl.event.events.update.UpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.movement.step.Step;

public class ListenerUpdate
extends ModuleListener<Step, UpdateEvent> {
    public ListenerUpdate(Step module) {
        super(module, UpdateEvent.class);
    }

    @Override
    public void call(UpdateEvent event) {
        ListenerUpdate.mc.player.stepHeight = ((Step)this.module).timer.passed(200L) ? ((Float)((Step)this.module).height.getValue()).floatValue() : 0.6f;
    }
}

