/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.BlockPos
 */
package me.chachoox.lithium.impl.event.events.blocks;

import me.chachoox.lithium.api.event.events.Event;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class DamageBlockEvent
extends Event {
    private final BlockPos pos;
    private final EnumFacing facing;
    private float damage;
    private int delay;

    public DamageBlockEvent(BlockPos pos, EnumFacing facing, float damage, int delay) {
        this.pos = pos;
        this.facing = facing;
        this.damage = damage;
        this.delay = delay;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public EnumFacing getFacing() {
        return this.facing;
    }

    public float getDamage() {
        return this.damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public int getDelay() {
        return this.delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
}

