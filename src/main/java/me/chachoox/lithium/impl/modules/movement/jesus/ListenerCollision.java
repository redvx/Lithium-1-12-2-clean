/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockLiquid
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 */
package me.chachoox.lithium.impl.modules.movement.jesus;

import java.util.Objects;
import me.chachoox.lithium.api.util.movement.PositionUtil;
import me.chachoox.lithium.impl.event.events.blocks.CollisionEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.movement.jesus.Jesus;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class ListenerCollision
extends ModuleListener<Jesus, CollisionEvent> {
    public ListenerCollision(Jesus module) {
        super(module, CollisionEvent.class);
    }

    @Override
    public void call(CollisionEvent event) {
        if (event.getEntity() != null && ListenerCollision.mc.player != null && (event.getEntity().equals((Object)ListenerCollision.mc.player) || event.getEntity().getControllingPassenger() != null && Objects.equals(event.getEntity().getControllingPassenger(), ListenerCollision.mc.player)) && event.getBlock() instanceof BlockLiquid && !ListenerCollision.mc.player.isSneaking() && ListenerCollision.mc.player.fallDistance < 3.0f && !PositionUtil.inLiquid() && PositionUtil.inLiquid(false) && ListenerCollision.isAbove(event.getPos())) {
            BlockPos pos = event.getPos();
            event.setBB(new AxisAlignedBB((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), (double)(pos.getX() + 1), (double)pos.getY() + 0.99, (double)(pos.getZ() + 1)));
        }
    }

    private static boolean isAbove(BlockPos pos) {
        return ListenerCollision.mc.player.getEntityBoundingBox().minY >= (double)pos.getY();
    }
}

