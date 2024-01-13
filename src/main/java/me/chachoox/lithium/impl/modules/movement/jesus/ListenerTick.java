/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.movement.jesus;

import me.chachoox.lithium.api.util.movement.PositionUtil;
import me.chachoox.lithium.impl.event.events.update.TickEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.movement.jesus.Jesus;
import me.chachoox.lithium.impl.modules.movement.jesus.JesusMode;

public class ListenerTick
extends ModuleListener<Jesus, TickEvent> {
    public ListenerTick(Jesus module) {
        super(module, TickEvent.class);
    }

    @Override
    public void call(TickEvent event) {
        if (ListenerTick.mc.player != null && ((Jesus)this.module).timer.passed(600L) && ((Jesus)this.module).mode.getValue() == JesusMode.SOLID) {
            if (ListenerTick.mc.player.fallDistance > 3.0f) {
                return;
            }
            if ((ListenerTick.mc.player.isInLava() || ListenerTick.mc.player.isInWater()) && !ListenerTick.mc.player.isSneaking()) {
                ListenerTick.mc.player.motionY = 0.1;
                return;
            }
            if (PositionUtil.inLiquid() && !ListenerTick.mc.player.isSneaking()) {
                ListenerTick.mc.player.motionY = 0.1;
            }
        }
    }
}

