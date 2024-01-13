/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.math.Vec3d
 *  org.lwjgl.opengl.GL11
 */
package me.chachoox.lithium.impl.modules.render.tracers;

import java.awt.Color;
import me.chachoox.lithium.api.util.colors.ColorUtil;
import me.chachoox.lithium.api.util.render.Interpolation;
import me.chachoox.lithium.api.util.render.RenderUtil;
import me.chachoox.lithium.asm.mixins.render.IEntityRenderer;
import me.chachoox.lithium.impl.event.events.render.main.Render3DEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.render.tracers.Tracers;
import me.chachoox.lithium.impl.modules.render.tracers.enums.Bone;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class ListenerRender
extends ModuleListener<Tracers, Render3DEvent> {
    public ListenerRender(Tracers module) {
        super(module, Render3DEvent.class);
    }

    @Override
    public void call(Render3DEvent event) {
        for (Entity entity : ListenerRender.mc.world.loadedEntityList) {
            if (!(entity instanceof EntityPlayer) || !(entity.posY < (double)((Integer)((Tracers)this.module).ydistance.getValue()).intValue()) || entity == ListenerRender.mc.player) continue;
            Vec3d interpolation = Interpolation.interpolateEntity(entity);
            double x = interpolation.x;
            double y = interpolation.y;
            double z = interpolation.z;
            RenderUtil.startRender();
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.pushMatrix();
            GlStateManager.loadIdentity();
            if (Managers.FRIEND.isFriend(entity.getName())) {
                GL11.glColor4f((float)0.33333334f, (float)0.78431374f, (float)0.78431374f, (float)0.55f);
            } else {
                float distance = RenderUtil.getEntity().getDistance(entity);
                float red = distance >= 60.0f ? 120.0f : distance + distance;
                Color color = ColorUtil.toColor(red, 100.0f, 50.0f, ((Float)((Tracers)this.module).opacity.getValue()).floatValue());
                GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
            }
            boolean viewBobbing = ListenerRender.mc.gameSettings.viewBobbing;
            ListenerRender.mc.gameSettings.viewBobbing = false;
            ((IEntityRenderer)ListenerRender.mc.entityRenderer).invokeOrientCamera(event.getPartialTicks());
            ListenerRender.mc.gameSettings.viewBobbing = viewBobbing;
            GL11.glLineWidth((float)1.0f);
            Vec3d rotateYaw = new Vec3d(0.0, 0.0, 1.0).rotatePitch(-((float)Math.toRadians(RenderUtil.getEntity().rotationPitch))).rotateYaw(-((float)Math.toRadians(RenderUtil.getEntity().rotationYaw)));
            GL11.glBegin((int)1);
            GL11.glVertex3d((double)rotateYaw.x, (double)((double)RenderUtil.getEntity().getEyeHeight() + rotateYaw.y), (double)rotateYaw.z);
            switch ((Bone)((Object)((Tracers)this.module).bone.getValue())) {
                case HEAD: {
                    GL11.glVertex3d((double)x, (double)(y + (double)entity.height - (double)0.18f), (double)z);
                    break;
                }
                case CHEST: {
                    GL11.glVertex3d((double)x, (double)(y + (double)(entity.height / 2.0f)), (double)z);
                    break;
                }
                case DICK: {
                    GL11.glVertex3d((double)x, (double)(y + (double)entity.height - (double)1.1f), (double)z);
                    break;
                }
                case FEET: {
                    GL11.glVertex3d((double)x, (double)y, (double)z);
                }
            }
            GL11.glEnd();
            GL11.glTranslated((double)x, (double)y, (double)z);
            GL11.glTranslated((double)(-x), (double)(-y), (double)(-z));
            GlStateManager.popMatrix();
            GlStateManager.disableAlpha();
            GlStateManager.disableBlend();
            RenderUtil.endRender();
        }
    }
}

