/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 */
package me.chachoox.lithium.impl.modules.render.displaytweaks;

import me.chachoox.lithium.impl.event.events.render.main.Render2DEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.render.displaytweaks.DisplayTweaks;
import net.minecraft.client.gui.ScaledResolution;

public class ListenerRender
extends ModuleListener<DisplayTweaks, Render2DEvent> {
    public ListenerRender(DisplayTweaks module) {
        super(module, Render2DEvent.class);
    }

    @Override
    public void call(Render2DEvent event) {
        ScaledResolution res = event.getResolution();
        if (((DisplayTweaks)this.module).hotbarKeys.getValue().booleanValue() && !ListenerRender.mc.player.isSpectator()) {
            int x = res.getScaledWidth() / 2 - 87;
            int y = res.getScaledHeight() - 18;
            int length = ListenerRender.mc.gameSettings.keyBindsHotbar.length;
            for (int i = 0; i < length; ++i) {
                ListenerRender.mc.fontRenderer.drawStringWithShadow(ListenerRender.mc.gameSettings.keyBindsHotbar[i].getDisplayName(), (float)(x + i * 20), (float)y, ((DisplayTweaks)this.module).getColor().getRGB());
            }
        }
    }
}

