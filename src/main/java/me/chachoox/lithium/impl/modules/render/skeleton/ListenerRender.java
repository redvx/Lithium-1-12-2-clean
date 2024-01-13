/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 *  net.minecraft.client.renderer.culling.Frustum
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  org.lwjgl.opengl.GL11
 */
package me.chachoox.lithium.impl.modules.render.skeleton;

import java.awt.Color;
import java.util.List;
import me.chachoox.lithium.api.util.entity.EntityUtil;
import me.chachoox.lithium.api.util.render.Interpolation;
import me.chachoox.lithium.api.util.render.RenderUtil;
import me.chachoox.lithium.impl.event.events.render.main.Render3DEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.other.colours.Colours;
import me.chachoox.lithium.impl.modules.render.skeleton.Skeleton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class ListenerRender
extends ModuleListener<Skeleton, Render3DEvent> {
    public ListenerRender(Skeleton module) {
        super(module, Render3DEvent.class);
    }

    @Override
    public void call(Render3DEvent event) {
        boolean lightning = GL11.glIsEnabled((int)2896);
        boolean blend = GL11.glIsEnabled((int)3042);
        boolean texture = GL11.glIsEnabled((int)3553);
        boolean depth = GL11.glIsEnabled((int)2929);
        boolean lineSmooth = GL11.glIsEnabled((int)2848);
        if (lightning) {
            GL11.glDisable((int)2896);
        }
        if (!blend) {
            GL11.glEnable((int)3042);
        }
        GL11.glLineWidth((float)((Skeleton)this.module).getLineWidth());
        if (texture) {
            GL11.glDisable((int)3553);
        }
        if (depth) {
            GL11.glDisable((int)2929);
        }
        if (!lineSmooth) {
            GL11.glEnable((int)2848);
        }
        GlStateManager.blendFunc((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GL11.glHint((int)3154, (int)4354);
        GlStateManager.depthMask((boolean)false);
        Entity renderEntity = RenderUtil.getEntity();
        Frustum frustum = Interpolation.createFrustum(renderEntity);
        List playerList = ListenerRender.mc.world.playerEntities;
        ((Skeleton)this.module).rotationList.keySet().removeIf(player -> player == null || player.equals((Object)renderEntity) || player.isPlayerSleeping() || player.isSpectator() || EntityUtil.isDead((Entity)player));
        playerList.forEach(player -> {
            AxisAlignedBB bb = player.getEntityBoundingBox();
            if (!frustum.isBoundingBoxInFrustum(bb)) {
                return;
            }
            if (((Skeleton)this.module).rotationList.get(player) != null) {
                this.renderSkeleton((EntityPlayer)player, ((Skeleton)this.module).rotationList.get(player));
            }
        });
        GlStateManager.depthMask((boolean)true);
        if (!lineSmooth) {
            GL11.glDisable((int)2848);
        }
        if (depth) {
            GL11.glEnable((int)2929);
        }
        if (texture) {
            GL11.glEnable((int)3553);
        }
        if (!blend) {
            GL11.glDisable((int)3042);
        }
        if (lightning) {
            GL11.glEnable((int)2896);
        }
    }

    protected void renderSkeleton(EntityPlayer player, float[][] rotations) {
        float sneak;
        GlStateManager.pushMatrix();
        if (Managers.FRIEND.isFriend(player)) {
            Color frdColor = Colours.get().getFriendColour();
            GlStateManager.color((float)((float)frdColor.getRed() / 255.0f), (float)((float)frdColor.getGreen() / 255.0f), (float)((float)frdColor.getBlue() / 255.0f), (float)((float)frdColor.getAlpha() / 255.0f));
        } else {
            Color color = ((Skeleton)this.module).getColor();
            GlStateManager.color((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
        }
        Vec3d interpolateEntity = Interpolation.interpolateEntity((Entity)player);
        GlStateManager.translate((double)interpolateEntity.x, (double)interpolateEntity.y, (double)interpolateEntity.z);
        GlStateManager.rotate((float)(-player.renderYawOffset), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.translate((double)0.0, (double)0.0, (double)(player.isSneaking() ? -0.235 : 0.0));
        float f = sneak = player.isSneaking() ? 0.6f : 0.75f;
        if (player.isElytraFlying()) {
            float f2 = (float)player.getTicksElytraFlying() + mc.getRenderPartialTicks();
            float f1 = MathHelper.clamp((float)(f2 * f2 / 100.0f), (float)0.0f, (float)1.0f);
            GlStateManager.rotate((float)(f1 * (90.0f - player.rotationPitch)), (float)1.0f, (float)0.0f, (float)0.0f);
            Vec3d vec3d = player.getLook(mc.getRenderPartialTicks());
            double d0 = player.motionX * player.motionX + player.motionZ * player.motionZ;
            double d1 = vec3d.x * vec3d.x + vec3d.z * vec3d.z;
            if (d0 > 0.0 && d1 > 0.0) {
                double d2 = (player.motionX * vec3d.x + player.motionZ * vec3d.z) / (Math.sqrt(d0) * Math.sqrt(d1));
                double d3 = player.motionX * vec3d.z - player.motionZ * vec3d.x;
                GlStateManager.rotate((float)((float)(Math.signum(d3) * Math.acos(d2)) * 180.0f / (float)Math.PI), (float)0.0f, (float)1.0f, (float)0.0f);
            }
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate((double)-0.125, (double)sneak, (double)0.0);
        if (rotations[3][0] != 0.0f) {
            GlStateManager.rotate((float)(rotations[3][0] * 57.295776f), (float)1.0f, (float)0.0f, (float)0.0f);
        }
        if (rotations[3][1] != 0.0f) {
            GlStateManager.rotate((float)(rotations[3][1] * 57.295776f), (float)0.0f, (float)1.0f, (float)0.0f);
        }
        if (rotations[3][2] != 0.0f) {
            GlStateManager.rotate((float)(rotations[3][2] * 57.295776f), (float)0.0f, (float)0.0f, (float)1.0f);
        }
        GlStateManager.glBegin((int)3);
        GL11.glVertex2d((double)0.0, (double)0.0);
        GL11.glVertex2d((double)0.0, (double)(-sneak));
        GlStateManager.glEnd();
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.translate((double)0.125, (double)sneak, (double)0.0);
        if (rotations[4][0] != 0.0f) {
            GlStateManager.rotate((float)(rotations[4][0] * 57.295776f), (float)1.0f, (float)0.0f, (float)0.0f);
        }
        if (rotations[4][1] != 0.0f) {
            GlStateManager.rotate((float)(rotations[4][1] * 57.295776f), (float)0.0f, (float)1.0f, (float)0.0f);
        }
        if (rotations[4][2] != 0.0f) {
            GlStateManager.rotate((float)(rotations[4][2] * 57.295776f), (float)0.0f, (float)0.0f, (float)1.0f);
        }
        GlStateManager.glBegin((int)3);
        GL11.glVertex2d((double)0.0, (double)0.0);
        GL11.glVertex2d((double)0.0, (double)(-sneak));
        GlStateManager.glEnd();
        GlStateManager.popMatrix();
        GlStateManager.translate((double)0.0, (double)0.0, (double)(player.isSneaking() ? 0.25 : 0.0));
        GlStateManager.pushMatrix();
        double sneakOffset = 0.0;
        if (player.isSneaking()) {
            sneakOffset = -0.05;
        }
        GlStateManager.translate((double)0.0, (double)sneakOffset, (double)(player.isSneaking() ? -0.01725 : 0.0));
        GlStateManager.pushMatrix();
        GlStateManager.translate((double)-0.375, (double)((double)sneak + 0.55), (double)0.0);
        if (rotations[1][0] != 0.0f) {
            GlStateManager.rotate((float)(rotations[1][0] * 57.295776f), (float)1.0f, (float)0.0f, (float)0.0f);
        }
        if (rotations[1][1] != 0.0f) {
            GlStateManager.rotate((float)(rotations[1][1] * 57.295776f), (float)0.0f, (float)1.0f, (float)0.0f);
        }
        if (rotations[1][2] != 0.0f) {
            GlStateManager.rotate((float)(-rotations[1][2] * 57.295776f), (float)0.0f, (float)0.0f, (float)1.0f);
        }
        GlStateManager.glBegin((int)3);
        GL11.glVertex2d((double)0.0, (double)0.0);
        GL11.glVertex2d((double)0.0, (double)-0.5);
        GlStateManager.glEnd();
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.translate((double)0.375, (double)((double)sneak + 0.55), (double)0.0);
        if (rotations[2][0] != 0.0f) {
            GlStateManager.rotate((float)(rotations[2][0] * 57.295776f), (float)1.0f, (float)0.0f, (float)0.0f);
        }
        if (rotations[2][1] != 0.0f) {
            GlStateManager.rotate((float)(rotations[2][1] * 57.295776f), (float)0.0f, (float)1.0f, (float)0.0f);
        }
        if (rotations[2][2] != 0.0f) {
            GlStateManager.rotate((float)(-rotations[2][2] * 57.295776f), (float)0.0f, (float)0.0f, (float)1.0f);
        }
        GlStateManager.glBegin((int)3);
        GL11.glVertex2d((double)0.0, (double)0.0);
        GL11.glVertex2d((double)0.0, (double)-0.5);
        GlStateManager.glEnd();
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.translate((double)0.0, (double)((double)sneak + 0.55), (double)0.0);
        if (rotations[0][0] != 0.0f) {
            GlStateManager.rotate((float)(rotations[0][0] * 57.295776f), (float)1.0f, (float)0.0f, (float)0.0f);
        }
        GlStateManager.glBegin((int)3);
        GL11.glVertex2d((double)0.0, (double)0.0);
        GL11.glVertex2d((double)0.0, (double)0.3);
        GlStateManager.glEnd();
        GlStateManager.popMatrix();
        GlStateManager.popMatrix();
        GlStateManager.rotate((float)(player.isSneaking() ? 25.0f : 0.0f), (float)1.0f, (float)0.0f, (float)0.0f);
        if (player.isSneaking()) {
            sneakOffset = -0.16175;
        }
        GlStateManager.translate((double)0.0, (double)sneakOffset, (double)(player.isSneaking() ? -0.48025 : 0.0));
        GlStateManager.pushMatrix();
        GlStateManager.translate((double)0.0, (double)sneak, (double)0.0);
        GlStateManager.glBegin((int)3);
        GL11.glVertex2d((double)-0.125, (double)0.0);
        GL11.glVertex2d((double)0.125, (double)0.0);
        GlStateManager.glEnd();
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.translate((double)0.0, (double)sneak, (double)0.0);
        GlStateManager.glBegin((int)3);
        GL11.glVertex2d((double)0.0, (double)0.0);
        GL11.glVertex2d((double)0.0, (double)0.55);
        GlStateManager.glEnd();
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.translate((double)0.0, (double)((double)sneak + 0.55), (double)0.0);
        GlStateManager.glBegin((int)3);
        GL11.glVertex2d((double)-0.375, (double)0.0);
        GL11.glVertex2d((double)0.375, (double)0.0);
        GlStateManager.glEnd();
        GlStateManager.popMatrix();
        GlStateManager.popMatrix();
    }
}

