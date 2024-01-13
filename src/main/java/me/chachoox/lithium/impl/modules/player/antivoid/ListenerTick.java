/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.player.antivoid;

import me.chachoox.lithium.impl.event.events.update.TickEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.player.antivoid.AntiVoid;
import me.chachoox.lithium.impl.modules.player.antivoid.AntiVoidMode;

public class ListenerTick
extends ModuleListener<AntiVoid, TickEvent> {
    public ListenerTick(AntiVoid module) {
        super(module, TickEvent.class);
    }

    @Override
    public void call(TickEvent event) {
        if (!event.isSafe()) {
            return;
        }
        switch ((AntiVoidMode)((Object)((AntiVoid)this.module).mode.getValue())) {
            case STOP: {
                if (!this.check() || !(ListenerTick.mc.player.posY < 0.0)) break;
                ListenerTick.mc.player.motionY = 0.0;
                break;
            }
            case ANTI: {
                if (!this.check()) break;
                if (ListenerTick.mc.player.posY < -10.0) {
                    return;
                }
                if (!(ListenerTick.mc.player.posY < -5.0)) break;
                ListenerTick.mc.player.motionY = 1.0;
            }
        }
    }

    private boolean check() {
        return !ListenerTick.mc.player.isSpectator() || ListenerTick.mc.player.noClip;
    }
}

