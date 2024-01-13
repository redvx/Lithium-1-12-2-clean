/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.movement.noclip;

import me.chachoox.lithium.impl.event.events.update.UpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.movement.noclip.NoClip;

public class ListenerUpdate
extends ModuleListener<NoClip, UpdateEvent> {
    public ListenerUpdate(NoClip module) {
        super(module, UpdateEvent.class);
    }

    @Override
    public void call(UpdateEvent event) {
        ListenerUpdate.mc.player.setVelocity(0.0, 0.0, 0.0);
        double speed = ListenerUpdate.mc.player.movementInput.sneak && ((NoClip)this.module).shift.getValue() != false ? -0.062 : 0.0;
        double[] strafing = ((NoClip)this.module).getMotion();
        for (int i = 1; i < 2; ++i) {
            ListenerUpdate.mc.player.motionX = strafing[0] * (double)i * (double)((Float)((NoClip)this.module).phaseSpeed.getValue()).floatValue();
            ListenerUpdate.mc.player.motionY = speed * (double)i;
            ListenerUpdate.mc.player.motionZ = strafing[1] * (double)i * (double)((Float)((NoClip)this.module).phaseSpeed.getValue()).floatValue();
            ((NoClip)this.module).sendPackets(ListenerUpdate.mc.player.motionX, ListenerUpdate.mc.player.motionY, ListenerUpdate.mc.player.motionZ);
        }
    }
}

