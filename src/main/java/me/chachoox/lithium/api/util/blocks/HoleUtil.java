/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.material.Material
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.math.BlockPos
 */
package me.chachoox.lithium.api.util.blocks;

import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.impl.modules.render.holeesp.util.TwoBlockPos;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class HoleUtil
implements Minecraftable {
    public static boolean isObbyHole(BlockPos pos) {
        return !(HoleUtil.isBedrockHole(pos) || HoleUtil.mc.world.getBlockState(pos.add(0, -1, 0)).getBlock() != Blocks.OBSIDIAN && HoleUtil.mc.world.getBlockState(pos.add(0, -1, 0)).getBlock() != Blocks.BEDROCK || HoleUtil.mc.world.getBlockState(pos.add(1, 0, 0)).getBlock() != Blocks.OBSIDIAN && HoleUtil.mc.world.getBlockState(pos.add(1, 0, 0)).getBlock() != Blocks.BEDROCK || HoleUtil.mc.world.getBlockState(pos.add(-1, 0, 0)).getBlock() != Blocks.OBSIDIAN && HoleUtil.mc.world.getBlockState(pos.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK || HoleUtil.mc.world.getBlockState(pos.add(0, 0, 1)).getBlock() != Blocks.OBSIDIAN && HoleUtil.mc.world.getBlockState(pos.add(0, 0, 1)).getBlock() != Blocks.BEDROCK || HoleUtil.mc.world.getBlockState(pos.add(0, 0, -1)).getBlock() != Blocks.OBSIDIAN && HoleUtil.mc.world.getBlockState(pos.add(0, 0, -1)).getBlock() != Blocks.BEDROCK || HoleUtil.mc.world.getBlockState(pos).getMaterial() != Material.AIR || HoleUtil.mc.world.getBlockState(pos.add(0, 1, 0)).getMaterial() != Material.AIR || HoleUtil.mc.world.getBlockState(pos.add(0, 2, 0)).getMaterial() != Material.AIR);
    }

    public static boolean isBedrockHole(BlockPos pos) {
        return HoleUtil.mc.world.getBlockState(pos.add(0, -1, 0)).getBlock() == Blocks.BEDROCK && HoleUtil.mc.world.getBlockState(pos.add(1, 0, 0)).getBlock() == Blocks.BEDROCK && HoleUtil.mc.world.getBlockState(pos.add(-1, 0, 0)).getBlock() == Blocks.BEDROCK && HoleUtil.mc.world.getBlockState(pos.add(0, 0, 1)).getBlock() == Blocks.BEDROCK && HoleUtil.mc.world.getBlockState(pos.add(0, 0, -1)).getBlock() == Blocks.BEDROCK && HoleUtil.mc.world.getBlockState(pos).getMaterial() == Material.AIR && HoleUtil.mc.world.getBlockState(pos.add(0, 1, 0)).getMaterial() == Material.AIR && HoleUtil.mc.world.getBlockState(pos.add(0, 2, 0)).getMaterial() == Material.AIR;
    }

    public static BlockPos isDoubleObby(BlockPos pos) {
        if (!(HoleUtil.mc.world.getBlockState(pos.down()).getBlock() != Blocks.OBSIDIAN && HoleUtil.mc.world.getBlockState(pos.down()).getBlock() != Blocks.BEDROCK || HoleUtil.mc.world.getBlockState(pos.west()).getBlock() != Blocks.OBSIDIAN && HoleUtil.mc.world.getBlockState(pos.west()).getBlock() != Blocks.BEDROCK || HoleUtil.mc.world.getBlockState(pos.south()).getBlock() != Blocks.OBSIDIAN && HoleUtil.mc.world.getBlockState(pos.south()).getBlock() != Blocks.BEDROCK || HoleUtil.mc.world.getBlockState(pos.north()).getBlock() != Blocks.OBSIDIAN && HoleUtil.mc.world.getBlockState(pos.north()).getBlock() != Blocks.BEDROCK || HoleUtil.mc.world.getBlockState(pos).getMaterial() != Material.AIR || HoleUtil.mc.world.getBlockState(pos.up()).getMaterial() != Material.AIR || HoleUtil.mc.world.getBlockState(pos.up(2)).getMaterial() != Material.AIR || HoleUtil.mc.world.getBlockState(pos.east().down()).getBlock() != Blocks.OBSIDIAN && HoleUtil.mc.world.getBlockState(pos.east().down()).getBlock() != Blocks.BEDROCK || HoleUtil.mc.world.getBlockState(pos.east(2)).getBlock() != Blocks.OBSIDIAN && HoleUtil.mc.world.getBlockState(pos.east(2)).getBlock() != Blocks.BEDROCK || HoleUtil.mc.world.getBlockState(pos.east().south()).getBlock() != Blocks.OBSIDIAN && HoleUtil.mc.world.getBlockState(pos.east().south()).getBlock() != Blocks.BEDROCK || HoleUtil.mc.world.getBlockState(pos.east().north()).getBlock() != Blocks.OBSIDIAN && HoleUtil.mc.world.getBlockState(pos.east().north()).getBlock() != Blocks.BEDROCK || HoleUtil.mc.world.getBlockState(pos.east()).getMaterial() != Material.AIR || HoleUtil.mc.world.getBlockState(pos.east().up()).getMaterial() != Material.AIR || HoleUtil.mc.world.getBlockState(pos.east().up(2)).getMaterial() != Material.AIR)) {
            return HoleUtil.isDoubleBedrock(pos) == null ? new BlockPos(1, 0, 0) : null;
        }
        if (!(HoleUtil.mc.world.getBlockState(pos.down()).getBlock() != Blocks.OBSIDIAN && HoleUtil.mc.world.getBlockState(pos.down()).getBlock() != Blocks.BEDROCK || HoleUtil.mc.world.getBlockState(pos.west()).getBlock() != Blocks.OBSIDIAN && HoleUtil.mc.world.getBlockState(pos.west()).getBlock() != Blocks.BEDROCK || HoleUtil.mc.world.getBlockState(pos.east()).getBlock() != Blocks.OBSIDIAN && HoleUtil.mc.world.getBlockState(pos.east()).getBlock() != Blocks.BEDROCK || HoleUtil.mc.world.getBlockState(pos.north()).getBlock() != Blocks.OBSIDIAN && HoleUtil.mc.world.getBlockState(pos.north()).getBlock() != Blocks.BEDROCK || HoleUtil.mc.world.getBlockState(pos).getMaterial() != Material.AIR || HoleUtil.mc.world.getBlockState(pos.up()).getMaterial() != Material.AIR || HoleUtil.mc.world.getBlockState(pos.up(2)).getMaterial() != Material.AIR || HoleUtil.mc.world.getBlockState(pos.south().down()).getBlock() != Blocks.OBSIDIAN && HoleUtil.mc.world.getBlockState(pos.south().down()).getBlock() != Blocks.BEDROCK || HoleUtil.mc.world.getBlockState(pos.south(2)).getBlock() != Blocks.OBSIDIAN && HoleUtil.mc.world.getBlockState(pos.south(2)).getBlock() != Blocks.BEDROCK || HoleUtil.mc.world.getBlockState(pos.south().east()).getBlock() != Blocks.OBSIDIAN && HoleUtil.mc.world.getBlockState(pos.south().east()).getBlock() != Blocks.BEDROCK || HoleUtil.mc.world.getBlockState(pos.south().west()).getBlock() != Blocks.OBSIDIAN && HoleUtil.mc.world.getBlockState(pos.south().west()).getBlock() != Blocks.BEDROCK || HoleUtil.mc.world.getBlockState(pos.south()).getMaterial() != Material.AIR || HoleUtil.mc.world.getBlockState(pos.south().up()).getMaterial() != Material.AIR || HoleUtil.mc.world.getBlockState(pos.south().up(2)).getMaterial() != Material.AIR)) {
            return HoleUtil.isDoubleBedrock(pos) == null ? new BlockPos(0, 0, 1) : null;
        }
        return null;
    }

    public static BlockPos isDoubleBedrock(BlockPos pos) {
        if (HoleUtil.mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK && HoleUtil.mc.world.getBlockState(pos.west()).getBlock() == Blocks.BEDROCK && HoleUtil.mc.world.getBlockState(pos.south()).getBlock() == Blocks.BEDROCK && HoleUtil.mc.world.getBlockState(pos.north()).getBlock() == Blocks.BEDROCK && HoleUtil.mc.world.getBlockState(pos).getMaterial() == Material.AIR && HoleUtil.mc.world.getBlockState(pos.up()).getMaterial() == Material.AIR && HoleUtil.mc.world.getBlockState(pos.up(2)).getMaterial() == Material.AIR && HoleUtil.mc.world.getBlockState(pos.east().down()).getBlock() == Blocks.BEDROCK && HoleUtil.mc.world.getBlockState(pos.east(2)).getBlock() == Blocks.BEDROCK && HoleUtil.mc.world.getBlockState(pos.east().south()).getBlock() == Blocks.BEDROCK && HoleUtil.mc.world.getBlockState(pos.east().north()).getBlock() == Blocks.BEDROCK && HoleUtil.mc.world.getBlockState(pos.east()).getMaterial() == Material.AIR && HoleUtil.mc.world.getBlockState(pos.east().up()).getMaterial() == Material.AIR && HoleUtil.mc.world.getBlockState(pos.east().up(2)).getMaterial() == Material.AIR) {
            return new BlockPos(1, 0, 0);
        }
        if (HoleUtil.mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK && HoleUtil.mc.world.getBlockState(pos.west()).getBlock() == Blocks.BEDROCK && HoleUtil.mc.world.getBlockState(pos.east()).getBlock() == Blocks.BEDROCK && HoleUtil.mc.world.getBlockState(pos.north()).getBlock() == Blocks.BEDROCK && HoleUtil.mc.world.getBlockState(pos).getMaterial() == Material.AIR && HoleUtil.mc.world.getBlockState(pos.up()).getMaterial() == Material.AIR && HoleUtil.mc.world.getBlockState(pos.up(2)).getMaterial() == Material.AIR && HoleUtil.mc.world.getBlockState(pos.south().down()).getBlock() == Blocks.BEDROCK && HoleUtil.mc.world.getBlockState(pos.south(2)).getBlock() == Blocks.BEDROCK && HoleUtil.mc.world.getBlockState(pos.south().east()).getBlock() == Blocks.BEDROCK && HoleUtil.mc.world.getBlockState(pos.south().west()).getBlock() == Blocks.BEDROCK && HoleUtil.mc.world.getBlockState(pos.south()).getMaterial() == Material.AIR && HoleUtil.mc.world.getBlockState(pos.south().up()).getMaterial() == Material.AIR && HoleUtil.mc.world.getBlockState(pos.south().up(2)).getMaterial() == Material.AIR) {
            return new BlockPos(0, 0, 1);
        }
        return null;
    }

    public static boolean isHole(BlockPos pos) {
        return HoleUtil.isObbyHole(pos) || HoleUtil.isBedrockHole(pos);
    }

    public static boolean isDoubleHole(BlockPos pos) {
        BlockPos validTwoBlockObby = HoleUtil.isDoubleObby(pos);
        if (validTwoBlockObby != null) {
            return true;
        }
        BlockPos validTwoBlockBedrock = HoleUtil.isDoubleBedrock(pos);
        return validTwoBlockBedrock != null;
    }

    public static TwoBlockPos getDouble(BlockPos pos) {
        BlockPos twoPos = HoleUtil.isDoubleBedrock(pos);
        if (twoPos == null) {
            twoPos = HoleUtil.isDoubleObby(pos);
        }
        TwoBlockPos twoBlockPos = null;
        if (twoPos != null) {
            twoBlockPos = new TwoBlockPos(pos, pos.add(twoPos.getX(), twoPos.getY(), twoPos.getZ()));
        }
        return twoBlockPos;
    }
}

