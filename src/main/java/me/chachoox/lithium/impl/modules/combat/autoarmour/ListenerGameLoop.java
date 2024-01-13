/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.combat.autoarmour;

import me.chachoox.lithium.impl.event.events.misc.GameLoopEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.combat.autoarmour.AutoArmour;

public class ListenerGameLoop
extends ModuleListener<AutoArmour, GameLoopEvent> {
    public ListenerGameLoop(AutoArmour module) {
        super(module, GameLoopEvent.class);
    }

    @Override
    public void call(GameLoopEvent event) {
        ((AutoArmour)this.module).runClick();
    }
}

