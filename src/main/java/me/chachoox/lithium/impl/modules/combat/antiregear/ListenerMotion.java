/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.ItemPickaxe
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer$Rotation
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.tileentity.TileEntity
 *  net.minecraft.tileentity.TileEntityShulkerBox
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.RayTraceResult
 */
package me.chachoox.lithium.impl.modules.combat.antiregear;

import java.util.List;
import me.chachoox.lithium.api.util.entity.EntityUtil;
import me.chachoox.lithium.api.util.math.MathUtil;
import me.chachoox.lithium.api.util.network.PacketUtil;
import me.chachoox.lithium.api.util.rotation.RotationUtil;
import me.chachoox.lithium.api.util.rotation.raytrace.RaytraceUtil;
import me.chachoox.lithium.impl.event.events.movement.MotionUpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.combat.antiregear.AntiRegear;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

public class ListenerMotion
extends ModuleListener<AntiRegear, MotionUpdateEvent> {
    public ListenerMotion(AntiRegear module) {
        super(module, MotionUpdateEvent.class);
    }

    @Override
    public void call(MotionUpdateEvent event) {
        if (((AntiRegear)this.module).timer.passed(((Integer)((AntiRegear)this.module).delay.getValue()).intValue()) && ListenerMotion.mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe) {
            BlockPos shulkerPos = null;
            EntityPlayer target = EntityUtil.getClosestEnemy();
            if (target == null || ListenerMotion.mc.player.getDistanceSq((Entity)target) > (double)MathUtil.square(((Float)((AntiRegear)this.module).enemyRange.getValue()).floatValue())) {
                return;
            }
            List loadedTileEntityList = ListenerMotion.mc.world.loadedTileEntityList;
            for (TileEntity tileEntity : loadedTileEntityList) {
                BlockPos pos;
                if (!(tileEntity instanceof TileEntityShulkerBox) || !(ListenerMotion.mc.player.getDistanceSq(pos = tileEntity.getPos()) < (double)MathUtil.square(((Float)((AntiRegear)this.module).range.getValue()).floatValue())) || ((AntiRegear)this.module).raytrace.getValue().booleanValue() && !RaytraceUtil.canBlockBeSeen((Entity)ListenerMotion.mc.player, pos, true)) continue;
                shulkerPos = pos;
                break;
            }
            if (shulkerPos != null) {
                float[] rotations = RotationUtil.getRotations(shulkerPos);
                Managers.ROTATION.setRotations(rotations[0], rotations[1]);
                ListenerMotion.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(rotations[0], rotations[1], ListenerMotion.mc.player.onGround));
                RayTraceResult result = RaytraceUtil.getRayTraceResult(rotations[0], rotations[1], ((Float)((AntiRegear)this.module).range.getValue()).floatValue());
                PacketUtil.send(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, shulkerPos, result.sideHit));
                PacketUtil.send(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, shulkerPos, result.sideHit));
                ((AntiRegear)this.module).timer.reset();
            }
        }
    }
}

