/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.player.autotool;

import me.chachoox.lithium.api.util.inventory.ItemUtil;
import me.chachoox.lithium.impl.event.events.update.UpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.player.autotool.AutoTool;

public class ListenerUpdate
extends ModuleListener<AutoTool, UpdateEvent> {
    public ListenerUpdate(AutoTool module) {
        super(module, UpdateEvent.class);
    }

    @Override
    public void call(UpdateEvent event) {
        if (((AutoTool)this.module).set && !ListenerUpdate.mc.gameSettings.keyBindAttack.isKeyDown() && ((AutoTool)this.module).swapBackTimer.passed((Integer)((AutoTool)this.module).swapBackTicks.getValue() * 50)) {
            ItemUtil.switchTo(((AutoTool)this.module).lastSlot);
            ((AutoTool)this.module).reset();
        }
    }
}

