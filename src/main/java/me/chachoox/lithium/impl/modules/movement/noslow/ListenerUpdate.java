/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.movement.noslow;

import me.chachoox.lithium.impl.event.events.update.UpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.movement.noslow.NoSlow;
import me.chachoox.lithium.impl.modules.movement.noslow.util.AntiWebMode;

public class ListenerUpdate
extends ModuleListener<NoSlow, UpdateEvent> {
    public ListenerUpdate(NoSlow module) {
        super(module, UpdateEvent.class);
    }

    @Override
    public void call(UpdateEvent event) {
        if (!((NoSlow)this.module).doWeb() && ((NoSlow)this.module).antiWeb.getValue() == AntiWebMode.TIMER && ((NoSlow)this.module).timerCheck) {
            Managers.TIMER.reset();
            ((NoSlow)this.module).timerCheck = false;
        }
    }
}

