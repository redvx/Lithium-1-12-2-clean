/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 *  net.minecraft.block.Block
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.BlockPos
 */
package me.chachoox.lithium.api.util.entity;

import com.google.common.collect.Sets;
import java.util.Set;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.api.util.entity.EntityUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class CombatUtil
implements Minecraftable {
    public static final Set<Block> NO_BLAST = Sets.newHashSet((Object[])new Block[]{Blocks.BEDROCK, Blocks.OBSIDIAN, Blocks.ANVIL, Blocks.ENDER_CHEST});

    public static EntityPlayer getTarget(float range) {
        EntityPlayer currentTarget = null;
        for (EntityPlayer player : CombatUtil.mc.world.playerEntities) {
            if (EntityUtil.isntValid(player, range)) continue;
            if (currentTarget == null) {
                currentTarget = player;
                continue;
            }
            if (!(CombatUtil.mc.player.getDistanceSq((Entity)player) < CombatUtil.mc.player.getDistanceSq((Entity)currentTarget))) continue;
            currentTarget = player;
        }
        return currentTarget;
    }

    public static boolean isInHole(EntityPlayer entity) {
        return CombatUtil.isBlockValid(new BlockPos(entity.posX, entity.posY, entity.posZ));
    }

    public static boolean isBlockValid(BlockPos blockPos) {
        return CombatUtil.isBedrockHole(blockPos) || CombatUtil.isObbyHole(blockPos) || CombatUtil.isBothHole(blockPos);
    }

    public static boolean isObbyHole(BlockPos blockPos) {
        BlockPos[] touchingBlocks;
        for (BlockPos pos : touchingBlocks = new BlockPos[]{blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down()}) {
            IBlockState touchingState = CombatUtil.mc.world.getBlockState(pos);
            if (touchingState.getBlock() == Blocks.OBSIDIAN) continue;
            return false;
        }
        return true;
    }

    public static boolean isBedrockHole(BlockPos blockPos) {
        BlockPos[] touchingBlocks;
        for (BlockPos pos : touchingBlocks = new BlockPos[]{blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down()}) {
            IBlockState touchingState = CombatUtil.mc.world.getBlockState(pos);
            if (touchingState.getBlock() == Blocks.BEDROCK) continue;
            return false;
        }
        return true;
    }

    public static boolean isAir(BlockPos pos) {
        return CombatUtil.mc.world.getBlockState(pos).getBlock() == Blocks.AIR;
    }

    public static boolean[] isHole(BlockPos pos, boolean above) {
        boolean[] result = new boolean[]{false, true};
        if (!CombatUtil.isAir(pos) || !CombatUtil.isAir(pos.up()) || above && !CombatUtil.isAir(pos.up(2))) {
            return result;
        }
        return CombatUtil.is1x1(pos, result);
    }

    public static boolean[] is1x1(BlockPos pos, boolean[] result) {
        for (EnumFacing facing : EnumFacing.values()) {
            BlockPos offset;
            IBlockState state;
            if (facing == EnumFacing.UP || (state = CombatUtil.mc.world.getBlockState(offset = pos.offset(facing))).getBlock() == Blocks.BEDROCK) continue;
            if (!NO_BLAST.contains(state.getBlock())) {
                return result;
            }
            result[1] = false;
        }
        result[0] = true;
        return result;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean is2x1(BlockPos pos, boolean upper) {
        if (upper) {
            if (!CombatUtil.isAir(pos)) return false;
            if (!CombatUtil.isAir(pos.up())) return false;
            if (CombatUtil.isAir(pos.down())) {
                return false;
            }
        }
        int airBlocks = 0;
        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            BlockPos offset = pos.offset(facing);
            if (CombatUtil.isAir(offset)) {
                if (!CombatUtil.isAir(offset.up())) return false;
                if (CombatUtil.isAir(offset.down())) return false;
                for (EnumFacing offsetFacing : EnumFacing.HORIZONTALS) {
                    IBlockState state;
                    if (offsetFacing == facing.getOpposite() || NO_BLAST.contains((state = CombatUtil.mc.world.getBlockState(offset.offset(offsetFacing))).getBlock())) continue;
                    return false;
                }
                ++airBlocks;
            }
            if (airBlocks <= true) continue;
            return false;
        }
        if (airBlocks != true) return false;
        return true;
    }

    public static boolean isBothHole(BlockPos blockPos) {
        BlockPos[] touchingBlocks;
        for (BlockPos pos : touchingBlocks = new BlockPos[]{blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down()}) {
            IBlockState touchingState = CombatUtil.mc.world.getBlockState(pos);
            if (touchingState.getBlock() == Blocks.BEDROCK || touchingState.getBlock() == Blocks.OBSIDIAN) continue;
            return false;
        }
        return true;
    }
}

