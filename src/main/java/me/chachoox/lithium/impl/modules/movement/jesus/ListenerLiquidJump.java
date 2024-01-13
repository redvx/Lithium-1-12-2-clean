/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.movement.jesus;

import me.chachoox.lithium.impl.event.events.movement.liquid.LiquidJumpEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.movement.jesus.Jesus;

public class ListenerLiquidJump
extends ModuleListener<Jesus, LiquidJumpEvent> {
    public ListenerLiquidJump(Jesus module) {
        super(module, LiquidJumpEvent.class);
    }

    @Override
    public void call(LiquidJumpEvent event) {
        if (ListenerLiquidJump.mc.player != null && ListenerLiquidJump.mc.player.equals((Object)event.getEntity()) && (ListenerLiquidJump.mc.player.isInWater() || ListenerLiquidJump.mc.player.isInLava()) && (ListenerLiquidJump.mc.player.motionY == 0.1 || ListenerLiquidJump.mc.player.motionY == 0.5)) {
            event.setCanceled(true);
        }
    }
}

