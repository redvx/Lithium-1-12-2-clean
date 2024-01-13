/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.movement.noslow;

import me.chachoox.lithium.impl.event.events.movement.InputUpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.movement.noslow.NoSlow;

public class ListenerInput
extends ModuleListener<NoSlow, InputUpdateEvent> {
    public ListenerInput(NoSlow module) {
        super(module, InputUpdateEvent.class);
    }

    @Override
    public void call(InputUpdateEvent event) {
        if (((NoSlow)this.module).items.getValue().booleanValue() && ListenerInput.mc.player.isHandActive() && !ListenerInput.mc.player.isRiding()) {
            ListenerInput.mc.player.movementInput.moveForward /= 0.2f;
            ListenerInput.mc.player.movementInput.moveStrafe /= 0.2f;
        }
    }
}

