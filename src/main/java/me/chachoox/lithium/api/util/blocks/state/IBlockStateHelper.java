/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.IBlockAccess
 */
package me.chachoox.lithium.api.util.blocks.state;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface IBlockStateHelper
extends IBlockAccess {
    default public void addAir(BlockPos pos) {
        this.addBlockState(pos, Blocks.AIR.getDefaultState());
    }

    public void addBlockState(BlockPos var1, IBlockState var2);

    public void delete(BlockPos var1);

    public void clearAllStates();
}

