/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.player.autotool;

import me.chachoox.lithium.api.util.blocks.MineUtil;
import me.chachoox.lithium.api.util.inventory.ItemUtil;
import me.chachoox.lithium.impl.event.events.blocks.DamageBlockEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.player.autotool.AutoTool;
import me.chachoox.lithium.impl.modules.player.fastbreak.FastBreak;

public class ListenerDamageBlock
extends ModuleListener<AutoTool, DamageBlockEvent> {
    public ListenerDamageBlock(AutoTool module) {
        super(module, DamageBlockEvent.class);
    }

    @Override
    public void call(DamageBlockEvent event) {
        if (MineUtil.canBreak(event.getPos()) && !ListenerDamageBlock.mc.player.isCreative() && ListenerDamageBlock.mc.gameSettings.keyBindAttack.isKeyDown()) {
            int slot = MineUtil.findBestTool(event.getPos());
            if (slot != -1) {
                if (!((AutoTool)this.module).set) {
                    ((AutoTool)this.module).lastSlot = ListenerDamageBlock.mc.player.inventory.currentItem;
                    ((AutoTool)this.module).set = true;
                }
                if (((AutoTool)this.module).swapTimer.passed((Integer)((AutoTool)this.module).swapTicks.getValue() * 50)) {
                    FastBreak FAST_BREAK = Managers.MODULE.get(FastBreak.class);
                    if (FAST_BREAK.isEnabled() && !FAST_BREAK.isBlockValid(ListenerDamageBlock.mc.world.getBlockState(event.getPos()).getBlock())) {
                        ItemUtil.switchTo(slot);
                    } else if (!FAST_BREAK.isEnabled()) {
                        ItemUtil.switchTo(slot);
                    }
                }
            }
        } else if (((AutoTool)this.module).set && ((AutoTool)this.module).swapTimer.passed((Integer)((AutoTool)this.module).swapBackTicks.getValue() * 50)) {
            ItemUtil.switchTo(((AutoTool)this.module).lastSlot);
            ((AutoTool)this.module).reset();
        }
    }
}

