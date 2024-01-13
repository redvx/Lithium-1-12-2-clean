/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockAir
 *  net.minecraft.block.BlockLiquid
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.inventory.Slot
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.play.client.CPacketClickWindow
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.network.play.client.CPacketPlayer$Rotation
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.RayTraceResult
 */
package me.chachoox.lithium.impl.modules.combat.selffill;

import me.chachoox.lithium.api.util.blocks.BlockUtil;
import me.chachoox.lithium.api.util.entity.AttackUtil;
import me.chachoox.lithium.api.util.entity.EntityUtil;
import me.chachoox.lithium.api.util.inventory.ItemUtil;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.api.util.movement.PositionUtil;
import me.chachoox.lithium.api.util.network.PacketUtil;
import me.chachoox.lithium.api.util.rotation.RotationUtil;
import me.chachoox.lithium.api.util.rotation.RotationsEnum;
import me.chachoox.lithium.api.util.rotation.raytrace.RaytraceUtil;
import me.chachoox.lithium.impl.event.events.movement.MotionUpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.combat.selffill.SelfFill;
import me.chachoox.lithium.impl.modules.other.blocks.util.SwingEnum;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

public class ListenerMotion
extends ModuleListener<SelfFill, MotionUpdateEvent> {
    public ListenerMotion(SelfFill module) {
        super(module, MotionUpdateEvent.class);
    }

    @Override
    public void call(MotionUpdateEvent event) {
        BlockPos currentPos;
        if (((SelfFill)this.module).wait.getValue().booleanValue() && !(currentPos = PositionUtil.getPosition()).equals((Object)((SelfFill)this.module).startPos)) {
            ((SelfFill)this.module).disable();
            return;
        }
        if (!((SelfFill)this.module).timer.passed(((Integer)((SelfFill)this.module).delay.getValue()).intValue())) {
            return;
        }
        if (((SelfFill)this.module).isInsideBlock()) {
            return;
        }
        BlockPos pos = PositionUtil.getPosition();
        if (!ListenerMotion.mc.world.getBlockState(pos).getMaterial().isReplaceable() || ListenerMotion.mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid) {
            if (!((SelfFill)this.module).wait.getValue().booleanValue()) {
                ((SelfFill)this.module).disable();
            }
            return;
        }
        BlockPos posDown = pos.down();
        if (ListenerMotion.mc.world.getBlockState(posDown).getBlock() instanceof BlockLiquid || ListenerMotion.mc.world.getBlockState(posDown).getBlock() instanceof BlockAir) {
            if (!((SelfFill)this.module).wait.getValue().booleanValue()) {
                ((SelfFill)this.module).disable();
            }
            return;
        }
        BlockPos posHead = pos.up(2);
        if (!ListenerMotion.mc.world.getBlockState(posHead).getMaterial().isReplaceable() && ((SelfFill)this.module).wait.getValue().booleanValue()) {
            return;
        }
        BlockPos upUp = pos.up(2);
        IBlockState upState = ListenerMotion.mc.world.getBlockState(upUp);
        if (upState.getMaterial().blocksMovement() || ListenerMotion.mc.world.getBlockState(upUp).getBlock() instanceof BlockLiquid) {
            if (!((SelfFill)this.module).wait.getValue().booleanValue()) {
                ((SelfFill)this.module).disable();
            }
            return;
        }
        int startSlot = ListenerMotion.mc.player.inventory.currentItem;
        int slot = -1;
        for (int i = 9; i >= 0; --i) {
            if (!(ListenerMotion.mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemBlock) || !((SelfFill)this.module).isValid(((ItemBlock)ListenerMotion.mc.player.inventory.getStackInSlot(i).getItem()).getBlock())) continue;
            slot = i;
        }
        if (slot == -1) {
            Logger.getLogger().log("\u00a7c<SelfFill> No valid blocks.");
            ((SelfFill)this.module).disable();
            return;
        }
        for (Entity entity : ListenerMotion.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos))) {
            if (entity == null || ListenerMotion.mc.player.equals((Object)entity) || EntityUtil.isDead(entity) || !entity.preventEntitySpawning) continue;
            if (!((SelfFill)this.module).wait.getValue().booleanValue()) {
                ((SelfFill)this.module).disable();
            }
            return;
        }
        if (((SelfFill)this.module).attack.getValue().booleanValue() && ((SelfFill)this.module).breakTimer.passed(((Integer)((SelfFill)this.module).attackDelay.getValue()).intValue())) {
            AttackUtil.attackInPos(pos, ((SelfFill)this.module).rotate.getValue() != false ? RotationsEnum.PACKET : RotationsEnum.NONE, (SwingEnum)((Object)((SelfFill)this.module).swing.getValue()), false);
            ((SelfFill)this.module).breakTimer.reset();
        }
        if (AttackUtil.isInterceptedByCrystal(pos) && ((SelfFill)this.module).strict.getValue().booleanValue()) {
            ((SelfFill)this.module).confirmTimer.reset();
            if (!((SelfFill)this.module).wait.getValue().booleanValue()) {
                ((SelfFill)this.module).disable();
            }
            return;
        }
        if (!((SelfFill)this.module).confirmTimer.passed(250L) && ((SelfFill)this.module).strict.getValue().booleanValue()) {
            return;
        }
        EnumFacing facing = BlockUtil.getFacing(pos);
        if (facing == null) {
            if (!((SelfFill)this.module).wait.getValue().booleanValue()) {
                ((SelfFill)this.module).disable();
            }
            return;
        }
        ItemStack oldItem = ListenerMotion.mc.player.getHeldItemMainhand();
        if (((SelfFill)this.module).altSwap.getValue().booleanValue()) {
            ItemUtil.switchToAlt(slot);
        } else {
            ItemUtil.switchTo(slot);
        }
        ItemStack newItem = ListenerMotion.mc.player.getHeldItemMainhand();
        if (((SelfFill)this.module).rotate.getValue().booleanValue()) {
            BlockPos newPos = ((SelfFill)this.module).startPos.offset(facing);
            float[] angles = RotationUtil.getRotations(newPos, facing.getOpposite(), (Entity)ListenerMotion.mc.player);
            Managers.ROTATION.setRotations(angles[0], angles[1]);
            PacketUtil.send(new CPacketPlayer.Rotation(angles[0], angles[1], ListenerMotion.mc.player.onGround));
        }
        PacketUtil.send(new CPacketPlayer.Position(ListenerMotion.mc.player.posX, ListenerMotion.mc.player.posY + 0.41999848688698, ListenerMotion.mc.player.posZ, false));
        PacketUtil.send(new CPacketPlayer.Position(ListenerMotion.mc.player.posX, ListenerMotion.mc.player.posY + 0.7500015, ListenerMotion.mc.player.posZ, false));
        PacketUtil.send(new CPacketPlayer.Position(ListenerMotion.mc.player.posX, ListenerMotion.mc.player.posY + 0.999997, ListenerMotion.mc.player.posZ, false));
        PacketUtil.send(new CPacketPlayer.Position(ListenerMotion.mc.player.posX, ListenerMotion.mc.player.posY + 1.17000300178814, ListenerMotion.mc.player.posZ, false));
        PacketUtil.send(new CPacketPlayer.Position(ListenerMotion.mc.player.posX, ListenerMotion.mc.player.posY + 1.170010501788138, ListenerMotion.mc.player.posZ, false));
        BlockPos finalPos = pos.offset(facing);
        float[] rotations = RotationUtil.getRotations(finalPos, facing.getOpposite(), (Entity)ListenerMotion.mc.player);
        RayTraceResult result = RaytraceUtil.getRayTraceResult(rotations[0], rotations[1]);
        float[] vec = RaytraceUtil.hitVecToPlaceVec(finalPos, result.hitVec);
        PacketUtil.sneak(true);
        PacketUtil.send(new CPacketPlayerTryUseItemOnBlock(finalPos, facing.getOpposite(), EnumHand.MAIN_HAND, vec[0], vec[1], vec[2]));
        switch ((SwingEnum)((Object)((SelfFill)this.module).swing.getValue())) {
            case NORMAL: {
                ListenerMotion.mc.player.swingArm(EnumHand.MAIN_HAND);
                break;
            }
            case PACKET: {
                PacketUtil.swing();
            }
        }
        PacketUtil.sneak(false);
        PacketUtil.send(new CPacketPlayer.Position(ListenerMotion.mc.player.posX, ((SelfFill)this.module).getY((Entity)ListenerMotion.mc.player), ListenerMotion.mc.player.posZ, false));
        if (startSlot != -1) {
            if (((SelfFill)this.module).altSwap.getValue().booleanValue()) {
                short id = ListenerMotion.mc.player.openContainer.getNextTransactionID(ListenerMotion.mc.player.inventory);
                ItemStack fakeStack = new ItemStack(Items.END_CRYSTAL, 64);
                int newSlot = ItemUtil.hotbarToInventory(slot);
                int altSlot = ItemUtil.hotbarToInventory(startSlot);
                Slot currentSlot = (Slot)ListenerMotion.mc.player.inventoryContainer.inventorySlots.get(altSlot);
                Slot swapSlot = (Slot)ListenerMotion.mc.player.inventoryContainer.inventorySlots.get(newSlot);
                PacketUtil.send(new CPacketClickWindow(0, newSlot, ListenerMotion.mc.player.inventory.currentItem, ClickType.SWAP, fakeStack, id));
                currentSlot.putStack(oldItem);
                swapSlot.putStack(newItem);
            } else {
                ItemUtil.switchTo(startSlot);
            }
        }
        ((SelfFill)this.module).timer.reset();
        if (!((SelfFill)this.module).wait.getValue().booleanValue()) {
            ((SelfFill)this.module).disable();
        }
    }
}

