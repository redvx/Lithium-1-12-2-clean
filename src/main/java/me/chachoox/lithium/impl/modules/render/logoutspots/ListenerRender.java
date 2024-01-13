/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.math.AxisAlignedBB
 *  org.lwjgl.opengl.GL11
 */
package me.chachoox.lithium.impl.modules.render.logoutspots;

import me.chachoox.lithium.api.util.math.MathUtil;
import me.chachoox.lithium.api.util.render.Interpolation;
import me.chachoox.lithium.api.util.render.RenderUtil;
import me.chachoox.lithium.impl.event.events.render.main.Render3DEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.render.logoutspots.LogoutSpots;
import me.chachoox.lithium.impl.modules.render.logoutspots.mode.RenderMode;
import me.chachoox.lithium.impl.modules.render.logoutspots.util.LogoutSpot;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

public class ListenerRender
extends ModuleListener<LogoutSpots, Render3DEvent> {
    public ListenerRender(LogoutSpots module) {
        super(module, Render3DEvent.class);
    }

    @Override
    public void call(Render3DEvent event) {
        if (!((LogoutSpots)this.module).isNull()) {
            for (LogoutSpot spot : ((LogoutSpots)this.module).spots.values()) {
                EntityPlayer player = spot.getEntity();
                AxisAlignedBB bb = Interpolation.interpolateAxis(spot.getBoundingBox());
                switch ((RenderMode)((Object)((LogoutSpots)this.module).render.getValue())) {
                    case GHOST: {
                        if (player == null) break;
                        player.hurtTime = 0;
                        double x = spot.getX() - ListenerRender.mc.getRenderManager().viewerPosX;
                        double y = spot.getY() - ListenerRender.mc.getRenderManager().viewerPosY;
                        double z = spot.getZ() - ListenerRender.mc.getRenderManager().viewerPosZ;
                        GlStateManager.pushMatrix();
                        GL11.glEnable((int)32823);
                        GL11.glPolygonOffset((float)1.0f, (float)-1100000.0f);
                        GlStateManager.color((float)((float)((LogoutSpots)this.module).getGhostColor().getRed() / 255.0f), (float)((float)((LogoutSpots)this.module).getGhostColor().getGreen() / 255.0f), (float)((float)((LogoutSpots)this.module).getGhostColor().getBlue() / 255.0f), (float)((float)((LogoutSpots)this.module).getGhostColor().getAlpha() / 255.0f));
                        mc.getRenderManager().renderEntity((Entity)player, x, y, z, player.rotationYaw, 0.0f, false);
                        GL11.glDisable((int)32823);
                        GL11.glPolygonOffset((float)1.0f, (float)1100000.0f);
                        GlStateManager.popMatrix();
                        player.setPosition(Interpolation.interpolateLastTickPos(player.lastTickPosX, player.posX), Interpolation.interpolateLastTickPos(player.lastTickPosY, player.posY), Interpolation.interpolateLastTickPos(player.lastTickPosZ, player.posZ));
                        break;
                    }
                    case OUTLINE: {
                        RenderUtil.startRender();
                        RenderUtil.drawOutline(bb, 1.5f, ((LogoutSpots)this.module).getBoxColor());
                        RenderUtil.endRender();
                    }
                }
                String text = spot.getName() + " logged out at " + MathUtil.round(spot.getX(), 2) + ", " + MathUtil.round(spot.getY(), 2) + ", " + MathUtil.round(spot.getZ(), 2);
                ((LogoutSpots)this.module).renderNameTag(text, bb);
            }
        }
    }
}

