/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.RayTraceResult$Type
 *  net.minecraft.util.math.Vec3d
 */
package me.chachoox.lithium.api.util.rotation.raytrace;

import java.util.Optional;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.api.util.movement.PositionUtil;
import me.chachoox.lithium.api.util.rotation.FacingUtil;
import me.chachoox.lithium.api.util.rotation.RotationUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class RaytraceUtil
implements Minecraftable {
    public static float[] hitVecToPlaceVec(BlockPos pos, Vec3d hitVec) {
        float x = (float)(hitVec.x - (double)pos.getX());
        float y = (float)(hitVec.y - (double)pos.getY());
        float z = (float)(hitVec.z - (double)pos.getZ());
        return new float[]{x, y, z};
    }

    public static RayTraceResult getRayTraceResult(float yaw, float pitch) {
        return RaytraceUtil.getRayTraceResult(yaw, pitch, RaytraceUtil.mc.playerController.getBlockReachDistance());
    }

    public static boolean canBlockBeSeen(Entity entity, BlockPos pos, boolean proper) {
        if (proper) {
            return RaytraceUtil.raytracePlaceCheck(entity, pos);
        }
        return RaytraceUtil.mc.world.rayTraceBlocks(PositionUtil.getEyesPos(entity), new Vec3d((double)pos.getX() + 0.5, (double)pos.getY() + 1.0, (double)pos.getZ() + 0.5), false, false, false) == null;
    }

    public static RayTraceResult getRayTraceResult(float yaw, float pitch, float distance) {
        Vec3d vec3d = PositionUtil.getEyesPos((Entity)RaytraceUtil.mc.player);
        Vec3d lookVec = RotationUtil.getVec3d(yaw, pitch);
        Vec3d rotations = vec3d.add(lookVec.x * (double)distance, lookVec.y * (double)distance, lookVec.z * (double)distance);
        return Optional.ofNullable(RaytraceUtil.mc.world.rayTraceBlocks(vec3d, rotations, false, false, false)).orElseGet(() -> new RayTraceResult(RayTraceResult.Type.MISS, new Vec3d(0.5, 1.0, 0.5), EnumFacing.UP, BlockPos.ORIGIN));
    }

    public static boolean canBeSeen(double x, double y, double z, Entity by) {
        return RaytraceUtil.canBeSeen(new Vec3d(x, y, z), by.posX, by.posY, by.posZ, by.getEyeHeight());
    }

    public static boolean canBeSeen(Vec3d toSee, Entity by) {
        return RaytraceUtil.canBeSeen(toSee, by.posX, by.posY, by.posZ, by.getEyeHeight());
    }

    public static boolean canBeSeen(Vec3d toSee, double x, double y, double z, float eyeHeight) {
        Vec3d start = new Vec3d(x, y + (double)eyeHeight, z);
        return RaytraceUtil.mc.world.rayTraceBlocks(start, toSee, false, true, false) == null;
    }

    public static boolean canBeSeen(Entity toSee, EntityLivingBase by) {
        return by.canEntityBeSeen(toSee);
    }

    public static boolean raytracePlaceCheck(Entity entity, BlockPos pos) {
        return RaytraceUtil.getFacing(entity, pos, false) != null;
    }

    public static EnumFacing getFacing(Entity entity, BlockPos pos, boolean verticals) {
        Vec3d eyePos = PositionUtil.getEyesPos(entity);
        for (EnumFacing facing : FacingUtil.VALUESNODOWN) {
            RayTraceResult result = RaytraceUtil.mc.world.rayTraceBlocks(eyePos, new Vec3d((double)pos.getX() + 0.5 + (double)facing.getDirectionVec().getX() * 1.0 / 2.0, (double)pos.getY() + 0.5 + (double)facing.getDirectionVec().getY() * 1.0 / 2.0, (double)pos.getZ() + 0.5 + (double)facing.getDirectionVec().getZ() * 1.0 / 2.0), false, true, false);
            if (result == null || result.typeOfHit != RayTraceResult.Type.BLOCK || !result.getBlockPos().equals((Object)pos)) continue;
            return facing;
        }
        if (verticals) {
            if ((double)pos.getY() > RaytraceUtil.mc.player.posY + (double)RaytraceUtil.mc.player.getEyeHeight()) {
                return EnumFacing.DOWN;
            }
            return EnumFacing.UP;
        }
        return null;
    }
}

