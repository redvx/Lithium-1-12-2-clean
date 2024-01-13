/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.BlockPos
 */
package me.chachoox.lithium.impl.modules.render.holeesp.util;

import net.minecraft.util.math.BlockPos;

public class TwoBlockPos {
    private final BlockPos one;
    private final BlockPos two;

    public TwoBlockPos(BlockPos one, BlockPos extra) {
        this.one = one;
        this.two = extra;
    }

    public BlockPos getOne() {
        return this.one;
    }

    public BlockPos getTwo() {
        return this.two;
    }
}

