/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.math.BlockPos
 */
package me.chachoox.lithium.impl.modules.movement.holepull;

import me.chachoox.lithium.api.util.blocks.HoleUtil;
import me.chachoox.lithium.api.util.entity.EntityUtil;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.api.util.movement.PositionUtil;
import me.chachoox.lithium.impl.event.events.movement.actions.MoveEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.movement.holepull.HolePull;
import me.chachoox.lithium.impl.modules.movement.holepull.mode.PullMode;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class ListenerMove
extends ModuleListener<HolePull, MoveEvent> {
    public ListenerMove(HolePull module) {
        super(module, MoveEvent.class, 1000);
    }

    @Override
    public void call(MoveEvent event) {
        if (ListenerMove.mc.player.isSpectator()) {
            return;
        }
        if (((HolePull)this.module).mode.getValue() == PullMode.PULL) {
            if (((HolePull)this.module).isPitchDown() && !ListenerMove.mc.player.isSneaking()) {
                BlockPos pos = PositionUtil.getPosition();
                if (HoleUtil.isHole(pos.down(1)) || HoleUtil.isHole(pos.down(2)) || HoleUtil.isHole(pos.down(3)) || HoleUtil.isHole(pos.down(4)) || HoleUtil.isHole(pos.down(5))) {
                    ((HolePull)this.module).anchoring = true;
                    ((HolePull)this.module).doPull(event, PositionUtil.getPosition());
                } else {
                    ((HolePull)this.module).anchoring = false;
                }
            } else {
                ((HolePull)this.module).anchoring = false;
            }
        }
        if (((HolePull)this.module).mode.getValue() == PullMode.SNAP) {
            if (EntityUtil.isPlayerSafe((EntityPlayer)ListenerMove.mc.player) || ((HolePull)this.module).isSafe(((HolePull)this.module).twoVec)) {
                Logger.getLogger().log("\u00a7c<HolePull> Entered a hole.", 45088);
                ((HolePull)this.module).disable();
            }
            if (((HolePull)this.module).hole != null && ListenerMove.mc.world.getBlockState(((HolePull)this.module).hole).getBlock() == Blocks.AIR) {
                ((HolePull)this.module).doPull(event, ((HolePull)this.module).hole);
                if (ListenerMove.mc.player.collidedHorizontally && ListenerMove.mc.player.onGround) {
                    ++((HolePull)this.module).stuck;
                    if (((HolePull)this.module).stuck == 10) {
                        Logger.getLogger().log("\u00a7c<HolePull> Player got stuck.", 45088);
                        ((HolePull)this.module).disable();
                    }
                } else {
                    ((HolePull)this.module).stuck = 0;
                }
            } else {
                Logger.getLogger().log("\u00a7c<HolePull> Hole no longer exists.", 45088);
                ((HolePull)this.module).disable();
            }
        }
    }
}

