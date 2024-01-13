/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockLiquid
 *  net.minecraft.util.math.BlockPos
 */
package me.chachoox.lithium.impl.modules.movement.jesus;

import me.chachoox.lithium.api.event.events.Stage;
import me.chachoox.lithium.api.util.movement.PositionUtil;
import me.chachoox.lithium.impl.event.events.movement.MotionUpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.movement.jesus.Jesus;
import me.chachoox.lithium.impl.modules.movement.jesus.JesusMode;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.math.BlockPos;

public class ListenerMotion
extends ModuleListener<Jesus, MotionUpdateEvent> {
    public ListenerMotion(Jesus module) {
        super(module, MotionUpdateEvent.class);
    }

    @Override
    public void call(MotionUpdateEvent event) {
        if (ListenerMotion.mc.player.isDead || ListenerMotion.mc.player.isSneaking() && ((Jesus)this.module).timer.passed(600L)) {
            return;
        }
        if (event.getStage() == Stage.PRE && ((Jesus)this.module).mode.getValue() == JesusMode.TRAMPOLINE) {
            if (PositionUtil.inLiquid(false) && !ListenerMotion.mc.player.isSneaking()) {
                ListenerMotion.mc.player.onGround = false;
            }
            Block block = ListenerMotion.mc.world.getBlockState(new BlockPos(ListenerMotion.mc.player.posX, ListenerMotion.mc.player.posY, ListenerMotion.mc.player.posZ)).getBlock();
            if (((Jesus)this.module).jumped && !ListenerMotion.mc.player.capabilities.isFlying && !ListenerMotion.mc.player.isInWater()) {
                if (ListenerMotion.mc.player.motionY < -0.3 || ListenerMotion.mc.player.onGround || ListenerMotion.mc.player.isOnLadder()) {
                    ((Jesus)this.module).jumped = false;
                    return;
                }
                ListenerMotion.mc.player.motionY = ListenerMotion.mc.player.motionY / (double)0.98f + 0.08;
                ListenerMotion.mc.player.motionY -= 0.03120000000005;
            }
            if (ListenerMotion.mc.player.isInWater() || ListenerMotion.mc.player.isInLava()) {
                ListenerMotion.mc.player.motionY = 0.1;
            }
            if (!ListenerMotion.mc.player.isInLava() && block instanceof BlockLiquid && ListenerMotion.mc.player.motionY < 0.2) {
                ListenerMotion.mc.player.motionY = 0.5;
                ((Jesus)this.module).jumped = true;
            }
        }
        if (event.getStage() == Stage.PRE && !PositionUtil.inLiquid() && PositionUtil.inLiquid(true) && !PositionUtil.isMovementBlocked() && ListenerMotion.mc.player.ticksExisted % 2 == 0) {
            event.setY(event.getY() + 0.02);
        }
    }
}

