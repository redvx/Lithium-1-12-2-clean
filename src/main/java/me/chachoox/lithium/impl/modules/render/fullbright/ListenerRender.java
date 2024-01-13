/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.render.fullbright;

import me.chachoox.lithium.api.util.render.Render2DUtil;
import me.chachoox.lithium.impl.event.events.render.main.Render2DEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.render.fullbright.Fullbright;

public class ListenerRender
extends ModuleListener<Fullbright, Render2DEvent> {
    public ListenerRender(Fullbright module) {
        super(module, Render2DEvent.class);
    }

    @Override
    public void call(Render2DEvent event) {
        if (((Fullbright)this.module).bozeMode.getValue().booleanValue()) {
            Render2DUtil.drawRect(0.0f, 0.0f, ListenerRender.mc.displayWidth, ListenerRender.mc.displayHeight, ((Fullbright)this.module).getColor().getRGB());
        }
    }
}

