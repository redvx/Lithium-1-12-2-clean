/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.state.IBlockState
 */
package me.chachoox.lithium.asm.ducks;

import net.minecraft.block.state.IBlockState;

public interface IBlock {
    public void setHarvestLevelNonForge(String var1, int var2);

    public String getHarvestToolNonForge(IBlockState var1);

    public int getHarvestLevelNonForge(IBlockState var1);

    public float getHardness();
}

