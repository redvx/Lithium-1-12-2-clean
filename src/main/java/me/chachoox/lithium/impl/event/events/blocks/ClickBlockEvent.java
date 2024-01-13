/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 */
package me.chachoox.lithium.impl.event.events.blocks;

import me.chachoox.lithium.api.event.events.Event;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class ClickBlockEvent
extends Event {
    private final BlockPos pos;
    private final EnumFacing facing;

    public ClickBlockEvent(BlockPos pos, EnumFacing facing) {
        this.pos = pos;
        this.facing = facing;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public EnumFacing getFacing() {
        return this.facing;
    }

    public static class Right
    extends ClickBlockEvent {
        private final Vec3d vec;
        private final EnumHand hand;

        public Right(BlockPos pos, EnumFacing facing, Vec3d vec, EnumHand hand) {
            super(pos, facing);
            this.vec = vec;
            this.hand = hand;
        }

        public EnumHand getHand() {
            return this.hand;
        }

        public Vec3d getVec() {
            return this.vec;
        }
    }
}

