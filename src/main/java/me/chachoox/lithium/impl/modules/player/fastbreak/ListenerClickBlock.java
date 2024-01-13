/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.player.fastbreak;

import me.chachoox.lithium.api.util.blocks.MineUtil;
import me.chachoox.lithium.api.util.inventory.Swap;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.impl.event.events.blocks.ClickBlockEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.player.fastbreak.FastBreak;
import me.chachoox.lithium.impl.modules.player.fastbreak.mode.MineMode;

public class ListenerClickBlock
extends ModuleListener<FastBreak, ClickBlockEvent> {
    public ListenerClickBlock(FastBreak module) {
        super(module, ClickBlockEvent.class);
    }

    @Override
    public void call(ClickBlockEvent event) {
        if (((FastBreak)this.module).pos != null && ((FastBreak)this.module).pos.equals((Object)event.getPos()) && !((FastBreak)this.module).auto.getValue().booleanValue() && ((FastBreak)this.module).canBreak && ((FastBreak)this.module).isBlockValid(ListenerClickBlock.mc.world.getBlockState(((FastBreak)this.module).getPos()).getBlock())) {
            event.setCanceled(true);
            int pickSlot = MineUtil.findBestTool(((FastBreak)this.module).pos);
            if (((FastBreak)this.module).swap.getValue() == Swap.NONE && ListenerClickBlock.mc.player.inventory.currentItem != pickSlot) {
                return;
            }
            ((FastBreak)this.module).executed = true;
            if (((FastBreak)this.module).debug.getValue().booleanValue()) {
                Logger.getLogger().log("\u00a7cTrying to break block", false);
            }
            ((FastBreak)this.module).tryBreak(pickSlot);
            if (((FastBreak)this.module).debug.getValue().booleanValue()) {
                Logger.getLogger().log("\u00a7cSent dig packets", false);
            }
            ((FastBreak)this.module).resetSwap();
            ++((FastBreak)this.module).clicks;
            if (((FastBreak)this.module).clicks >= 3 && ((FastBreak)this.module).mode.getValue() == MineMode.PACKET || ((FastBreak)this.module).mode.getValue() == MineMode.INSTANT) {
                ((FastBreak)this.module).checkRetry();
            }
        }
    }
}

