/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.other.hud;

import me.chachoox.lithium.impl.event.events.misc.GameLoopEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.other.hud.Hud;

public class ListenerGameLoop
extends ModuleListener<Hud, GameLoopEvent> {
    public ListenerGameLoop(Hud module) {
        super(module, GameLoopEvent.class);
    }

    @Override
    public void call(GameLoopEvent event) {
        long time = System.nanoTime();
        ((Hud)this.module).frames.add(time);
        while (true) {
            long f = ((Hud)this.module).frames.getFirst();
            long ONE_SECOND = 1000000000L;
            if (time - f <= 1000000000L) break;
            ((Hud)this.module).frames.remove();
        }
        ((Hud)this.module).fpsCount = ((Hud)this.module).frames.size();
    }
}

