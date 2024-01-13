/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.MathHelper
 */
package me.chachoox.lithium.impl.modules.movement.elytrafly;

import me.chachoox.lithium.api.event.events.Stage;
import me.chachoox.lithium.impl.event.events.movement.MotionUpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.movement.elytrafly.ElytraFly;
import net.minecraft.util.math.MathHelper;

public class ListenerMotion
extends ModuleListener<ElytraFly, MotionUpdateEvent> {
    public ListenerMotion(ElytraFly module) {
        super(module, MotionUpdateEvent.class);
    }

    @Override
    public void call(MotionUpdateEvent event) {
        if (((ElytraFly)this.module).wasp.getValue().booleanValue() && ((ElytraFly)this.module).boost.getValue().booleanValue()) {
            ((ElytraFly)this.module).boost.setValue(false);
            ((ElytraFly)this.module).wasp.setValue(false);
        }
        if (event.getStage() == Stage.PRE && ((ElytraFly)this.module).isElytra() && !((ElytraFly)this.module).boost.getValue().booleanValue()) {
            float moveStrafe = ListenerMotion.mc.player.movementInput.moveStrafe;
            float moveForward = ListenerMotion.mc.player.movementInput.moveForward;
            float strafe = moveStrafe * 90.0f * (moveForward != 0.0f ? 0.5f : 1.0f);
            event.setYaw(MathHelper.wrapDegrees((float)(ListenerMotion.mc.player.rotationYaw - strafe - (float)(moveForward < 0.0f ? 180 : 0))));
        }
    }
}

