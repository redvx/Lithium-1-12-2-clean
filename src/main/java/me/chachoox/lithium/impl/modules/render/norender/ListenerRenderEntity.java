/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.item.EntityTNTPrimed
 *  net.minecraft.entity.passive.EntityParrot
 *  net.minecraft.entity.player.EntityPlayer
 */
package me.chachoox.lithium.impl.modules.render.norender;

import me.chachoox.lithium.impl.event.events.render.misc.RenderEntityEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.render.norender.NoRender;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.player.EntityPlayer;

public class ListenerRenderEntity
extends ModuleListener<NoRender, RenderEntityEvent> {
    public ListenerRenderEntity(NoRender module) {
        super(module, RenderEntityEvent.class);
    }

    @Override
    public void call(RenderEntityEvent event) {
        if (event.getEntity() instanceof EntityParrot && ((NoRender)this.module).getNoParrots()) {
            event.setCanceled(true);
        }
        if (event.getEntity() instanceof EntityPlayer && event.getEntity().isInvisible() && ((NoRender)this.module).getNoSpectators()) {
            event.setCanceled(true);
        }
        if (event.getEntity() instanceof EntityTNTPrimed && ((NoRender)this.module).getTnt()) {
            ListenerRenderEntity.mc.world.removeEntity(event.getEntity());
            event.setCanceled(true);
        }
    }
}

