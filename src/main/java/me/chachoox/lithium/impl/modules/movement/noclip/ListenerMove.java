/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.movement.noclip;

import me.chachoox.lithium.impl.event.events.movement.actions.MoveEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.movement.noclip.NoClip;

public class ListenerMove
extends ModuleListener<NoClip, MoveEvent> {
    public ListenerMove(NoClip module) {
        super(module, MoveEvent.class);
    }

    @Override
    public void call(MoveEvent event) {
        if (((NoClip)this.module).adjustMotion.getValue().booleanValue()) {
            event.setX(ListenerMove.mc.player.motionX);
            event.setY(ListenerMove.mc.player.motionY);
            event.setZ(ListenerMove.mc.player.motionZ);
            if (((NoClip)this.module).removeHitbox.getValue().booleanValue() && ((NoClip)this.module).checkHitBoxes()) {
                ListenerMove.mc.player.noClip = true;
            }
        }
    }
}

