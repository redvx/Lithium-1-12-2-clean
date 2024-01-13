/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.math.Vec3d
 */
package me.chachoox.lithium.impl.modules.render.pollosesp;

import me.chachoox.lithium.api.util.entity.EntityUtil;
import me.chachoox.lithium.api.util.render.Interpolation;
import me.chachoox.lithium.api.util.render.RenderUtil;
import me.chachoox.lithium.impl.event.events.render.main.Render3DEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.render.pollosesp.PollosESP;
import me.chachoox.lithium.impl.modules.render.pollosesp.util.VectorUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

public class ListenerRender
extends ModuleListener<PollosESP, Render3DEvent> {
    public ListenerRender(PollosESP module) {
        super(module, Render3DEvent.class);
    }

    @Override
    public void call(Render3DEvent event) {
        for (EntityPlayer player : ListenerRender.mc.world.playerEntities) {
            int height;
            if (player == RenderUtil.getEntity() || EntityUtil.isDead((Entity)player)) continue;
            Vec3d bottomVec = Interpolation.interpolateEntity((Entity)player);
            Vec3d topVec = bottomVec.add(new Vec3d(0.0, player.getRenderBoundingBox().maxY - player.posY, 0.0));
            VectorUtil.ScreenPos top = VectorUtil.toScreenie(topVec.x, topVec.y, topVec.z);
            VectorUtil.ScreenPos bot = VectorUtil.toScreenie(bottomVec.x, bottomVec.y, bottomVec.z);
            if (!top.isVisible && !bot.isVisible) continue;
            int width = height = bot.y - top.y;
            int x = (int)((double)top.x - (double)width / 1.8);
            int y = top.y;
            ListenerRender.mc.renderEngine.bindTexture(PollosESP.POLLOS);
            GlStateManager.color((float)255.0f, (float)255.0f, (float)255.0f);
            Gui.drawScaledCustomSizeModalRect((int)x, (int)y, (float)0.0f, (float)0.0f, (int)width, (int)height, (int)width, (int)height, (float)width, (float)height);
        }
    }
}

