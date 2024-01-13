/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.other.hud;

import me.chachoox.lithium.impl.event.events.render.main.Render2DEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.other.hud.Hud;

public class ListenerRender
extends ModuleListener<Hud, Render2DEvent> {
    public ListenerRender(Hud module) {
        super(module, Render2DEvent.class);
    }

    @Override
    public void call(Render2DEvent event) {
        if (ListenerRender.mc.gameSettings.showDebugInfo) {
            return;
        }
        ((Hud)this.module).onRender(event.getResolution());
    }
}

