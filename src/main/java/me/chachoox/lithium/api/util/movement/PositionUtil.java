/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockLiquid
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityBoat
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 */
package me.chachoox.lithium.api.util.movement;

import java.util.HashSet;
import java.util.Set;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.api.util.entity.EntityUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class PositionUtil
implements Minecraftable {
    public static Vec3d getEyesPos(Entity entity) {
        return new Vec3d(entity.posX, PositionUtil.getEyeHeight(entity), entity.posZ);
    }

    public static double getEyeHeight(Entity entity) {
        return entity.posY + (double)entity.getEyeHeight();
    }

    public static BlockPos getPosition() {
        return PositionUtil.getPosition((Entity)PositionUtil.mc.player);
    }

    public static BlockPos getPosition(Entity entity) {
        return PositionUtil.getPosition(entity, 0.0);
    }

    public static BlockPos getPosition(Entity entity, double yOffset) {
        double y = entity.posY + yOffset;
        if (entity.posY - Math.floor(entity.posY) > 0.5) {
            y = Math.ceil(entity.posY);
        }
        return new BlockPos(entity.posX, y, entity.posZ);
    }

    public static Entity getPositionEntity() {
        Entity ridingEntity;
        EntityPlayerSP player = PositionUtil.mc.player;
        return player == null ? null : ((ridingEntity = player.getRidingEntity()) != null && !(ridingEntity instanceof EntityBoat) ? ridingEntity : player);
    }

    public static Set<BlockPos> getBlockedPositions(Entity entity) {
        return PositionUtil.getBlockedPositions(entity.getEntityBoundingBox());
    }

    public static Set<BlockPos> getBlockedPositions(AxisAlignedBB bb) {
        return PositionUtil.getBlockedPositions(bb, 0.5);
    }

    public static Set<BlockPos> getBlockedPositions(AxisAlignedBB bb, double offset) {
        HashSet<BlockPos> positions = new HashSet<BlockPos>();
        double y = bb.minY;
        if (bb.minY - Math.floor(bb.minY) > offset) {
            y = Math.ceil(bb.minY);
        }
        positions.add(new BlockPos(bb.maxX, y, bb.maxZ));
        positions.add(new BlockPos(bb.minX, y, bb.minZ));
        positions.add(new BlockPos(bb.maxX, y, bb.minZ));
        positions.add(new BlockPos(bb.minX, y, bb.maxZ));
        return positions;
    }

    public static boolean inLiquid() {
        return PositionUtil.inLiquid(MathHelper.floor((double)(EntityUtil.requirePositionEntity().getEntityBoundingBox().minY + 0.01)));
    }

    public static boolean inLiquid(boolean feet) {
        return PositionUtil.inLiquid(MathHelper.floor((double)(PositionUtil.mc.player.getEntityBoundingBox().minY - (feet ? 0.03 : 0.2))));
    }

    private static boolean inLiquid(int y) {
        return PositionUtil.getState(y) != null;
    }

    public static boolean isMovementBlocked() {
        IBlockState state = PositionUtil.findState(Block.class, MathHelper.floor((double)(PositionUtil.mc.player.getEntityBoundingBox().minY - 0.01)));
        return state != null && state.getMaterial().blocksMovement();
    }

    private static IBlockState getState(int y) {
        Entity entity = EntityUtil.requirePositionEntity();
        int startX = MathHelper.floor((double)entity.getEntityBoundingBox().minX);
        int startZ = MathHelper.floor((double)entity.getEntityBoundingBox().minZ);
        int endX = MathHelper.ceil((double)entity.getEntityBoundingBox().maxX);
        int endZ = MathHelper.ceil((double)entity.getEntityBoundingBox().maxZ);
        for (int x = startX; x < endX; ++x) {
            for (int z = startZ; z < endZ; ++z) {
                IBlockState s = PositionUtil.mc.world.getBlockState(new BlockPos(x, y, z));
                if (!(s.getBlock() instanceof BlockLiquid)) continue;
                return s;
            }
        }
        return null;
    }

    public static boolean isBoxColliding() {
        return PositionUtil.mc.world.getCollisionBoxes((Entity)PositionUtil.mc.player, PositionUtil.mc.player.getEntityBoundingBox().offset(0.0, 0.21, 0.0)).size() > 0;
    }

    private static IBlockState findState(Class<? extends Block> block, int y) {
        int startX = MathHelper.floor((double)PositionUtil.mc.player.getEntityBoundingBox().minX);
        int startZ = MathHelper.floor((double)PositionUtil.mc.player.getEntityBoundingBox().minZ);
        int endX = MathHelper.ceil((double)PositionUtil.mc.player.getEntityBoundingBox().maxX);
        int endZ = MathHelper.ceil((double)PositionUtil.mc.player.getEntityBoundingBox().maxZ);
        for (int x = startX; x < endX; ++x) {
            for (int z = startZ; z < endZ; ++z) {
                IBlockState s = PositionUtil.mc.world.getBlockState(new BlockPos(x, y, z));
                if (!block.isInstance(s.getBlock())) continue;
                return s;
            }
        }
        return null;
    }
}

