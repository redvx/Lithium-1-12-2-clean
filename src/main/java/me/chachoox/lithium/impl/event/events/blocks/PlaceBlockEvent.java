/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.math.BlockPos
 */
package me.chachoox.lithium.impl.event.events.blocks;

import me.chachoox.lithium.api.event.events.Event;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class PlaceBlockEvent
extends Event {
    private final BlockPos pos;
    private final ItemStack stack;

    public PlaceBlockEvent(BlockPos pos, ItemStack stack) {
        this.pos = pos;
        this.stack = stack;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public ItemStack getStack() {
        return this.stack;
    }
}

