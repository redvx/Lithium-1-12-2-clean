/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.misc.pvpinfo;

import me.chachoox.lithium.impl.event.events.render.main.Render2DEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.misc.pvpinfo.PvPInfo;

public class ListenerRender
extends ModuleListener<PvPInfo, Render2DEvent> {
    public ListenerRender(PvPInfo module) {
        super(module, Render2DEvent.class);
    }

    @Override
    public void call(Render2DEvent event) {
        if (ListenerRender.mc.gameSettings.showDebugInfo) {
            return;
        }
        ((PvPInfo)this.module).onRender(event.getResolution());
    }
}

