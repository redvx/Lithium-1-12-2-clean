/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.render.compass;

import me.chachoox.lithium.impl.event.events.render.main.Render2DEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.render.compass.Compass;

public class ListenerRender
extends ModuleListener<Compass, Render2DEvent> {
    public ListenerRender(Compass module) {
        super(module, Render2DEvent.class);
    }

    @Override
    public void call(Render2DEvent event) {
        if (ListenerRender.mc.gameSettings.showDebugInfo) {
            return;
        }
        ((Compass)this.module).onRender(event.getResolution());
    }
}

