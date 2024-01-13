/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.BlockPos
 */
package me.chachoox.lithium.impl.event.events.blocks;

import me.chachoox.lithium.api.event.events.Stage;
import me.chachoox.lithium.api.event.events.StageEvent;
import net.minecraft.util.math.BlockPos;

public class BlockDestroyEvent
extends StageEvent {
    private final BlockPos pos;

    public BlockDestroyEvent(Stage stage, BlockPos pos) {
        super(stage);
        this.pos = pos;
    }

    public BlockPos getPos() {
        return this.pos;
    }
}

