/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.RayTraceResult
 */
package me.chachoox.lithium.impl.modules.player.scaffold;

import me.chachoox.lithium.api.event.events.Stage;
import me.chachoox.lithium.api.util.blocks.BlockUtil;
import me.chachoox.lithium.api.util.inventory.ItemUtil;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.api.util.movement.MovementUtil;
import me.chachoox.lithium.api.util.network.PacketUtil;
import me.chachoox.lithium.api.util.rotation.RotationUtil;
import me.chachoox.lithium.api.util.rotation.RotationsEnum;
import me.chachoox.lithium.api.util.rotation.raytrace.RaytraceUtil;
import me.chachoox.lithium.asm.ducks.IMinecraft;
import me.chachoox.lithium.impl.event.events.movement.MotionUpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.other.blocks.util.SwingEnum;
import me.chachoox.lithium.impl.modules.player.scaffold.Scaffold;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import org.apache.logging.log4j.Level;

public class ListenerMotion
extends ModuleListener<Scaffold, MotionUpdateEvent> {
    public ListenerMotion(Scaffold module) {
        super(module, MotionUpdateEvent.class);
    }

    @Override
    public void call(MotionUpdateEvent event) {
        if (event.getStage() == Stage.PRE) {
            ((Scaffold)this.module).facing = null;
            BlockPos prev = ((Scaffold)this.module).pos;
            ((Scaffold)this.module).pos = null;
            ((Scaffold)this.module).pos = ((Scaffold)this.module).findNextPos();
            if (((Scaffold)this.module).pos != null) {
                ((Scaffold)this.module).rot = ((Scaffold)this.module).pos;
                if (!((Scaffold)this.module).pos.equals((Object)prev)) {
                    ((Scaffold)this.module).rotationTimer.reset();
                }
                this.setRotations(((Scaffold)this.module).pos);
            } else if (((Scaffold)this.module).rot != null && ((Scaffold)this.module).rotation.getValue() != RotationsEnum.NONE && !((Scaffold)this.module).rotationTimer.passed(500L)) {
                this.setRotations(((Scaffold)this.module).rot);
            } else {
                ((Scaffold)this.module).rot = null;
            }
        } else {
            if (((Scaffold)this.module).pos == null || ((Scaffold)this.module).facing == null && ((Scaffold)this.module).rotation.getValue() != RotationsEnum.NONE) {
                return;
            }
            int slot = -1;
            for (int i = 9; i >= 0; --i) {
                if (!(ListenerMotion.mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemBlock) || !((Scaffold)this.module).isValid(((ItemBlock)ListenerMotion.mc.player.inventory.getStackInSlot(i).getItem()).getBlock())) continue;
                slot = i;
                if (i == ListenerMotion.mc.player.inventory.currentItem) break;
            }
            if (slot != -1) {
                boolean sneak;
                boolean jump = ListenerMotion.mc.player.movementInput.jump && ((Scaffold)this.module).tower.getValue() != false;
                boolean bl = sneak = ListenerMotion.mc.player.movementInput.sneak && ((Scaffold)this.module).down.getValue() != false;
                if (jump && !sneak && !MovementUtil.isMoving()) {
                    ((IMinecraft)mc).setRightClickDelay(3);
                    ListenerMotion.mc.player.jump();
                    if (((Scaffold)this.module).towerTimer.passed(1500L)) {
                        ListenerMotion.mc.player.motionY = -0.28;
                        ((Scaffold)this.module).towerTimer.reset();
                    }
                    this.placeBlocks(slot, true);
                } else {
                    ((Scaffold)this.module).towerTimer.reset();
                }
                try {
                    if (((Scaffold)this.module).placeTimer.passed(((Integer)((Scaffold)this.module).delay.getValue()).intValue())) {
                        this.placeBlocks(slot, false);
                    }
                }
                catch (Exception e) {
                    Logger.getLogger().log(Level.ERROR, e.getMessage());
                    Logger.getLogger().log("\u00a7cError while placing block at X: " + ((Scaffold)this.module).pos.getX() + " Y: " + ((Scaffold)this.module).pos.getY() + " Z: " + ((Scaffold)this.module).pos.getZ() + ".");
                }
            }
        }
    }

    private void setRotations(BlockPos pos) {
        ((Scaffold)this.module).facing = BlockUtil.getFacing(pos);
        if (((Scaffold)this.module).facing != null) {
            this.setRotations(pos, ((Scaffold)this.module).facing);
            return;
        }
        for (EnumFacing facing : EnumFacing.values()) {
            BlockPos p = pos.offset(facing);
            EnumFacing f = BlockUtil.getFacing(p);
            if (f == null) continue;
            ((Scaffold)this.module).facing = f;
            ((Scaffold)this.module).pos = p;
            this.setRotations(p, f);
        }
    }

    private void setRotations(BlockPos pos, EnumFacing facing) {
        ((Scaffold)this.module).rotations = RotationUtil.getRotations(pos.offset(facing), facing.getOpposite());
        if (((Scaffold)this.module).rotation.getValue() != RotationsEnum.NONE && ((Scaffold)this.module).rotations != null) {
            RotationUtil.doRotation((RotationsEnum)((Object)((Scaffold)this.module).rotation.getValue()), ((Scaffold)this.module).rotations);
        }
    }

    private void placeBlocks(int slot, boolean tower) {
        int lastSlot = ListenerMotion.mc.player.inventory.currentItem;
        ItemUtil.switchTo(slot);
        RayTraceResult result = RaytraceUtil.getRayTraceResult(((Scaffold)this.module).rotations[0], ((Scaffold)this.module).rotations[1]);
        if (((Scaffold)this.module).clickTimer.passed(!tower ? 25L : 0L)) {
            try {
                ListenerMotion.mc.playerController.processRightClickBlock(ListenerMotion.mc.player, ListenerMotion.mc.world, ((Scaffold)this.module).pos.offset(((Scaffold)this.module).facing), ((Scaffold)this.module).facing.getOpposite(), result.hitVec, ItemUtil.getHand(slot));
                ((Scaffold)this.module).placeTimer.reset();
            }
            catch (Exception e) {
                Logger.getLogger().log(Level.ERROR, e.getMessage());
                Logger.getLogger().log("\u00a7cError while placing block at X: " + ((Scaffold)this.module).pos.getX() + " Y: " + ((Scaffold)this.module).pos.getY() + " Z: " + ((Scaffold)this.module).pos.getZ() + ".");
            }
            ((Scaffold)this.module).clickTimer.reset();
        }
        ItemUtil.switchTo(lastSlot);
        switch ((SwingEnum)((Object)((Scaffold)this.module).swing.getValue())) {
            case NONE: {
                break;
            }
            case NORMAL: {
                ListenerMotion.mc.player.swingArm(EnumHand.MAIN_HAND);
                break;
            }
            case PACKET: {
                PacketUtil.swing();
            }
        }
    }
}

