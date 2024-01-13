/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.Gui
 */
package me.chachoox.lithium.impl.modules.render.pollosesp;

import me.chachoox.lithium.impl.event.events.render.main.Render2DEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.render.pollosesp.PollosESP;
import net.minecraft.client.gui.Gui;

public class ListenerRender2D
extends ModuleListener<PollosESP, Render2DEvent> {
    public ListenerRender2D(PollosESP module) {
        super(module, Render2DEvent.class);
    }

    @Override
    public void call(Render2DEvent event) {
        if (((PollosESP)this.module).pollosScreen.getValue().booleanValue()) {
            mc.getTextureManager().bindTexture(PollosESP.POLLOS);
            Gui.drawModalRectWithCustomSizedTexture((int)0, (int)0, (float)0.0f, (float)0.0f, (int)event.getResolution().getScaledWidth(), (int)event.getResolution().getScaledHeight(), (float)event.getResolution().getScaledWidth(), (float)event.getResolution().getScaledHeight());
        }
    }
}

