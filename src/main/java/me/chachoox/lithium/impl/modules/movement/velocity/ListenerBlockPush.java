/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.movement.velocity;

import me.chachoox.lithium.impl.event.events.blocks.BlockPushEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.movement.velocity.Velocity;

public class ListenerBlockPush
extends ModuleListener<Velocity, BlockPushEvent> {
    public ListenerBlockPush(Velocity module) {
        super(module, BlockPushEvent.class);
    }

    @Override
    public void call(BlockPushEvent event) {
        if (((Velocity)this.module).noPush.getValue().booleanValue()) {
            event.setCanceled(true);
        }
    }
}

