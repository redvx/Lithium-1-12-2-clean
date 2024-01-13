/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.culling.Frustum
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 */
package me.chachoox.lithium.api.util.render;

import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.asm.ducks.IMinecraft;
import me.chachoox.lithium.asm.mixins.render.IRenderManager;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Interpolation
implements Minecraftable {
    public static Vec3d interpolatedEyePos() {
        return Interpolation.mc.player.getPositionEyes(mc.getRenderPartialTicks());
    }

    public static Vec3d interpolatedEyeVec() {
        return Interpolation.mc.player.getLook(mc.getRenderPartialTicks());
    }

    public static Vec3d interpolatedEyeVec(EntityPlayer player) {
        return player.getLook(mc.getRenderPartialTicks());
    }

    public static Vec3d interpolateEntity(Entity entity) {
        double x = Interpolation.interpolateLastTickPos(entity.posX, entity.lastTickPosX) - Interpolation.getRenderPosX();
        double y = Interpolation.interpolateLastTickPos(entity.posY, entity.lastTickPosY) - Interpolation.getRenderPosY();
        double z = Interpolation.interpolateLastTickPos(entity.posZ, entity.lastTickPosZ) - Interpolation.getRenderPosZ();
        return new Vec3d(x, y, z);
    }

    public static double interpolateLastTickPos(double pos, double lastPos) {
        return lastPos + (pos - lastPos) * (double)((IMinecraft)Interpolation.mc).getTimer().renderPartialTicks;
    }

    public static AxisAlignedBB interpolatePos(BlockPos pos, float height) {
        return new AxisAlignedBB((double)pos.getX() - Interpolation.mc.getRenderManager().viewerPosX, (double)pos.getY() - Interpolation.mc.getRenderManager().viewerPosY, (double)pos.getZ() - Interpolation.mc.getRenderManager().viewerPosZ, (double)pos.getX() - Interpolation.mc.getRenderManager().viewerPosX + 1.0, (double)pos.getY() - Interpolation.mc.getRenderManager().viewerPosY + (double)height, (double)pos.getZ() - Interpolation.mc.getRenderManager().viewerPosZ + 1.0);
    }

    public static AxisAlignedBB interpolateAxis(AxisAlignedBB bb) {
        return new AxisAlignedBB(bb.minX - Interpolation.mc.getRenderManager().viewerPosX, bb.minY - Interpolation.mc.getRenderManager().viewerPosY, bb.minZ - Interpolation.mc.getRenderManager().viewerPosZ, bb.maxX - Interpolation.mc.getRenderManager().viewerPosX, bb.maxY - Interpolation.mc.getRenderManager().viewerPosY, bb.maxZ - Interpolation.mc.getRenderManager().viewerPosZ);
    }

    public static AxisAlignedBB offsetRenderPos(AxisAlignedBB bb) {
        return bb.offset(-Interpolation.getRenderPosX(), -Interpolation.getRenderPosY(), -Interpolation.getRenderPosZ());
    }

    public static double getRenderPosX() {
        return ((IRenderManager)mc.getRenderManager()).getRenderPosX();
    }

    public static double getRenderPosY() {
        return ((IRenderManager)mc.getRenderManager()).getRenderPosY();
    }

    public static double getRenderPosZ() {
        return ((IRenderManager)mc.getRenderManager()).getRenderPosZ();
    }

    public static Frustum createFrustum(Entity entity) {
        Frustum frustum = new Frustum();
        Interpolation.setFrustum(frustum, entity);
        return frustum;
    }

    public static void setFrustum(Frustum frustum, Entity entity) {
        double x = Interpolation.interpolateLastTickPos(entity.posX, entity.lastTickPosX);
        double y = Interpolation.interpolateLastTickPos(entity.posY, entity.lastTickPosY);
        double z = Interpolation.interpolateLastTickPos(entity.posZ, entity.lastTickPosZ);
        frustum.setPosition(x, y, z);
    }
}

