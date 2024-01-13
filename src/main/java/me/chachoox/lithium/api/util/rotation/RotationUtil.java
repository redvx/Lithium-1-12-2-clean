/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer$Rotation
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec2f
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.IBlockAccess
 */
package me.chachoox.lithium.api.util.rotation;

import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.api.util.math.MathUtil;
import me.chachoox.lithium.api.util.movement.PositionUtil;
import me.chachoox.lithium.api.util.render.Interpolation;
import me.chachoox.lithium.api.util.rotation.RotationsEnum;
import me.chachoox.lithium.impl.managers.Managers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;

public class RotationUtil
implements Minecraftable {
    public static float[] getRotations(BlockPos pos, EnumFacing facing) {
        return RotationUtil.getRotations(pos, facing, (Entity)RotationUtil.mc.player);
    }

    public static float[] getRotations(BlockPos pos, EnumFacing facing, Entity from) {
        return RotationUtil.getRotations(pos, facing, from, (IBlockAccess)RotationUtil.mc.world, RotationUtil.mc.world.getBlockState(pos));
    }

    public static float[] getRotations(BlockPos pos, EnumFacing facing, Entity from, IBlockAccess world, IBlockState state) {
        AxisAlignedBB bb = state.getBoundingBox(world, pos);
        double x = (double)pos.getX() + (bb.minX + bb.maxX) / 2.0;
        double y = (double)pos.getY() + (bb.minY + bb.maxY) / 2.0;
        double z = (double)pos.getZ() + (bb.minZ + bb.maxZ) / 2.0;
        if (facing != null) {
            x += (double)facing.getDirectionVec().getX() * ((bb.minX + bb.maxX) / 2.0);
            y += (double)facing.getDirectionVec().getY() * ((bb.minY + bb.maxY) / 2.0);
            z += (double)facing.getDirectionVec().getZ() * ((bb.minZ + bb.maxZ) / 2.0);
        }
        return RotationUtil.getRotations(x, y, z, from);
    }

    public static float[] getRotationsToTopMiddle(BlockPos pos) {
        return RotationUtil.getRotations((double)pos.getX() + 0.5, pos.getY(), (double)pos.getZ() + 0.5);
    }

    public static float[] getRotationsToTopMiddleUp(BlockPos pos) {
        return RotationUtil.getRotations((double)pos.getX() + 0.5, pos.getY() + 1, (double)pos.getZ() + 0.5);
    }

    public static float[] getRotations(double x, double y, double z, Entity f) {
        return RotationUtil.getRotations(x, y, z, f.posX, f.posY, f.posZ, f.getEyeHeight());
    }

    public static float[] getRotations(Entity from, Entity entity, double height, double maxAngle) {
        return RotationUtil.getRotations(entity, from.posX, from.posY, from.posZ, from.getEyeHeight(), height, maxAngle);
    }

    public static float[] getRotations(Entity entity, double fromX, double fromY, double fromZ, float eyeHeight, double height, double maxAngle) {
        float[] rotations = RotationUtil.getRotations(entity.posX, entity.posY + (double)entity.getEyeHeight() * height, entity.posZ, fromX, fromY, fromZ, eyeHeight);
        return RotationUtil.smoothen(rotations, maxAngle);
    }

    public static float[] smoothen(float[] rotations, double maxAngle) {
        float[] server = new float[]{Managers.ROTATION.getYaw(), Managers.ROTATION.getYaw()};
        return RotationUtil.smoothen(server, rotations, maxAngle);
    }

    public static float[] smoothen(float[] server, float[] rotations, double maxAngle) {
        if (maxAngle >= 180.0 || maxAngle <= 0.0 || RotationUtil.angle(server, rotations) <= maxAngle) {
            return rotations;
        }
        return RotationUtil.faceSmoothly(server[0], server[1], rotations[0], rotations[1], maxAngle, maxAngle);
    }

    public static float[] getRotations(double x, double y, double z, double fromX, double fromY, double fromZ, float fromHeight) {
        double xDiff = x - fromX;
        double yDiff = y - (fromY + (double)fromHeight);
        double zDiff = z - fromZ;
        double dist = MathHelper.sqrt((double)(xDiff * xDiff + zDiff * zDiff));
        float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-(Math.atan2(yDiff, dist) * 180.0 / Math.PI));
        float prevYaw = RotationUtil.mc.player.prevCameraYaw;
        float diff = yaw - prevYaw;
        if (diff < -180.0f || diff > 180.0f) {
            float round = Math.round(Math.abs(diff / 360.0f));
            diff = diff < 0.0f ? diff + 360.0f * round : diff - 360.0f * round;
        }
        return new float[]{prevYaw + diff, pitch};
    }

    public static float[] getRotations(Vec3d vec3d) {
        return RotationUtil.getRotations(vec3d.x, vec3d.y, vec3d.z);
    }

    public static float[] getRotations(BlockPos pos) {
        return RotationUtil.getRotations((double)pos.getX() + 0.5, pos.getY(), (double)pos.getZ() + 0.5);
    }

    public static float[] getRotations(Entity entity) {
        return RotationUtil.getRotations(entity.posX, entity.posY + (double)entity.getEyeHeight(), entity.posZ);
    }

    public static float[] getRotations(double x, double y, double z) {
        double xDiff = x - RotationUtil.mc.player.posX;
        double yDiff = y - PositionUtil.getEyeHeight((Entity)RotationUtil.mc.player);
        double zDiff = z - RotationUtil.mc.player.posZ;
        double dist = MathHelper.sqrt((double)(xDiff * xDiff + zDiff * zDiff));
        float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-(Math.atan2(yDiff, dist) * 180.0 / Math.PI));
        float diff = yaw - RotationUtil.mc.player.rotationYaw;
        if (diff < -180.0f || diff > 180.0f) {
            float round = Math.round(Math.abs(diff / 360.0f));
            diff = diff < 0.0f ? diff + 360.0f * round : diff - 360.0f * round;
        }
        return new float[]{RotationUtil.mc.player.rotationYaw + diff, pitch};
    }

    public static Vec3d getVec3d(float yaw, float pitch) {
        float vx = -MathHelper.sin((float)MathUtil.rad(yaw)) * MathHelper.cos((float)MathUtil.rad(pitch));
        float vz = MathHelper.cos((float)MathUtil.rad(yaw)) * MathHelper.cos((float)MathUtil.rad(pitch));
        float vy = -MathHelper.sin((float)MathUtil.rad(pitch));
        return new Vec3d((double)vx, (double)vy, (double)vz);
    }

    public static double getAngle(Entity entity, double yOffset) {
        Vec3d vec3d = MathUtil.fromTo(Interpolation.interpolatedEyePos(), entity.posX, entity.posY + yOffset, entity.posZ);
        return MathUtil.angle(vec3d, Interpolation.interpolatedEyeVec());
    }

    public static void doRotation(RotationsEnum rotation, float[] angles) {
        switch (rotation) {
            case PACKET: {
                RotationUtil.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(angles[0], angles[1], RotationUtil.mc.player.onGround));
                break;
            }
            case NORMAL: {
                Managers.ROTATION.setRotations(angles[0], angles[1]);
            }
        }
    }

    public static float[] faceSmoothly(double curYaw, double curPitch, double intendedYaw, double intendedPitch, double yawSpeed, double pitchSpeed) {
        float yaw = RotationUtil.updateRotation((float)curYaw, (float)intendedYaw, (float)yawSpeed);
        float pitch = RotationUtil.updateRotation((float)curPitch, (float)intendedPitch, (float)pitchSpeed);
        return new float[]{yaw, pitch};
    }

    public static double angle(float[] rotation1, float[] rotation2) {
        Vec3d r1Vec = RotationUtil.getVec3d(rotation1[0], rotation1[1]);
        Vec3d r2Vec = RotationUtil.getVec3d(rotation2[0], rotation2[1]);
        return MathUtil.angle(r1Vec, r2Vec);
    }

    public static float updateRotation(float current, float intended, float factor) {
        float updated = MathHelper.wrapDegrees((float)(intended - current));
        if (updated > factor) {
            updated = factor;
        }
        if (updated < -factor) {
            updated = -factor;
        }
        return current + updated;
    }

    public static double normalizeAngle(Double angleIn) {
        double d;
        double angle = angleIn;
        angle %= 360.0;
        if (d >= 180.0) {
            angle -= 360.0;
        }
        if (angle < -180.0) {
            angle += 360.0;
        }
        return angle;
    }

    public static Vec2f getRotationTo(Vec3d posTo, Vec3d posFrom) {
        return RotationUtil.getRotationFromVec(posTo.subtract(posFrom));
    }

    public static Vec2f getRotationFromVec(Vec3d vec) {
        double xz = Math.hypot(vec.x, vec.z);
        float yaw = (float)RotationUtil.normalizeAngle(Math.toDegrees(Math.atan2(vec.z, vec.x)) - 90.0);
        float pitch = (float)RotationUtil.normalizeAngle(Math.toDegrees(-Math.atan2(vec.y, xz)));
        return new Vec2f(yaw, pitch);
    }
}

