/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 */
package me.chachoox.lithium.impl.modules.render.norender;

import me.chachoox.lithium.impl.event.events.render.main.Render3DEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.render.norender.NoRender;
import net.minecraft.entity.player.EntityPlayer;

public class ListenerRender
extends ModuleListener<NoRender, Render3DEvent> {
    public ListenerRender(NoRender module) {
        super(module, Render3DEvent.class);
    }

    @Override
    public void call(Render3DEvent event) {
        for (EntityPlayer entity : ListenerRender.mc.world.playerEntities) {
            if (entity.equals((Object)ListenerRender.mc.player)) {
                return;
            }
            if (!((NoRender)this.module).getLimbSwing()) continue;
            entity.limbSwing = 0.0f;
            entity.limbSwingAmount = 0.0f;
            entity.prevLimbSwingAmount = 0.0f;
        }
    }
}

