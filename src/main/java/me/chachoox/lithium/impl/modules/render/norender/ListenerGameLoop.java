/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.render.norender;

import me.chachoox.lithium.impl.event.events.misc.GameLoopEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.render.norender.NoRender;

public class ListenerGameLoop
extends ModuleListener<NoRender, GameLoopEvent> {
    public ListenerGameLoop(NoRender module) {
        super(module, GameLoopEvent.class);
    }

    @Override
    public void call(GameLoopEvent event) {
        if (ListenerGameLoop.mc.world != null && ((NoRender)this.module).getTime() != 0) {
            ListenerGameLoop.mc.world.setWorldTime((long)(-((NoRender)this.module).getTime()));
        }
    }
}

