/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.init.Blocks
 */
package me.chachoox.lithium.impl.modules.player.fastbreak;

import me.chachoox.lithium.api.util.blocks.MineUtil;
import me.chachoox.lithium.api.util.inventory.Swap;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.api.util.math.MathUtil;
import me.chachoox.lithium.impl.event.events.update.UpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.player.fastbreak.FastBreak;
import net.minecraft.init.Blocks;

public class ListenerUpdate
extends ModuleListener<FastBreak, UpdateEvent> {
    public ListenerUpdate(FastBreak module) {
        super(module, UpdateEvent.class, -10);
    }

    @Override
    public void call(UpdateEvent event) {
        if (ListenerUpdate.mc.player.capabilities.isCreativeMode) {
            return;
        }
        if (((FastBreak)this.module).pos != null) {
            if (ListenerUpdate.mc.player.getDistanceSq(((FastBreak)this.module).pos) > (double)MathUtil.square(((Float)((FastBreak)this.module).range.getValue()).floatValue())) {
                ((FastBreak)this.module).abortCurrentPos();
                return;
            }
            if (((FastBreak)this.module).getBlock() != Blocks.AIR) {
                ((FastBreak)this.module).state = ListenerUpdate.mc.world.getBlockState(((FastBreak)this.module).pos);
            }
            ((FastBreak)this.module).updateDamages();
            int pickSlot = MineUtil.findBestTool(((FastBreak)this.module).pos);
            if (((FastBreak)this.module).damages[ListenerUpdate.mc.player.inventory.currentItem] >= 1.0f || pickSlot >= 0 && ((FastBreak)this.module).damages[pickSlot] >= 1.0f) {
                ((FastBreak)this.module).canBreak = true;
                if (!((FastBreak)this.module).pingTimer.passed(((FastBreak)this.module).getPingDelay())) {
                    ((FastBreak)this.module).retryTimer.reset();
                    return;
                }
                if (!ListenerUpdate.mc.player.onGround && ((FastBreak)this.module).strict.getValue().booleanValue()) {
                    ((FastBreak)this.module).retryTimer.reset();
                    return;
                }
                if (!((FastBreak)this.module).auto.getValue().booleanValue() && !((FastBreak)this.module).executed) {
                    return;
                }
                if (((FastBreak)this.module).swap.getValue() == Swap.NONE && ListenerUpdate.mc.player.inventory.currentItem != pickSlot) {
                    ((FastBreak)this.module).retryTimer.reset();
                    return;
                }
                if (((FastBreak)this.module).retryTimer.passed(500L)) {
                    ((FastBreak)this.module).checkRetry();
                    return;
                }
                if (((FastBreak)this.module).auto.getValue().booleanValue()) {
                    if (((FastBreak)this.module).debug.getValue().booleanValue()) {
                        Logger.getLogger().log("\u00a7cTrying to break block", false);
                    }
                    ((FastBreak)this.module).tryBreak(pickSlot);
                    if (((FastBreak)this.module).debug.getValue().booleanValue()) {
                        Logger.getLogger().log("\u00a7cSent dig packets", false);
                    }
                }
            } else {
                ((FastBreak)this.module).canBreak = false;
                ((FastBreak)this.module).executed = false;
                ((FastBreak)this.module).retryTimer.reset();
                ((FastBreak)this.module).pingTimer.reset();
            }
        }
    }
}

