/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.init.Blocks
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer$Rotation
 *  net.minecraft.util.math.BlockPos
 */
package me.chachoox.lithium.impl.modules.player.fastbreak;

import me.chachoox.lithium.api.event.events.Stage;
import me.chachoox.lithium.api.util.entity.AttackUtil;
import me.chachoox.lithium.api.util.math.MathUtil;
import me.chachoox.lithium.api.util.network.NetworkUtil;
import me.chachoox.lithium.api.util.rotation.RotationUtil;
import me.chachoox.lithium.api.util.rotation.RotationsEnum;
import me.chachoox.lithium.impl.event.events.movement.MotionUpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.other.blocks.util.SwingEnum;
import me.chachoox.lithium.impl.modules.player.fastbreak.FastBreak;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;

public class ListenerMotion
extends ModuleListener<FastBreak, MotionUpdateEvent> {
    public ListenerMotion(FastBreak module) {
        super(module, MotionUpdateEvent.class);
    }

    @Override
    public void call(MotionUpdateEvent event) {
        if (event.getStage() == Stage.PRE) {
            if (((FastBreak)this.module).rotation.getValue() == RotationsEnum.NORMAL && ((FastBreak)this.module).pos != null && !((FastBreak)this.module).rotationTimer.passed(150L)) {
                float[] rotations = RotationUtil.getRotations(((FastBreak)this.module).pos);
                RotationUtil.doRotation(RotationsEnum.NORMAL, rotations);
            }
            Entity entity = null;
            if (((FastBreak)this.module).pos != null && ((FastBreak)this.module).pos.equals((Object)((FastBreak)this.module).crystalPos)) {
                BlockPos newCrystalPos;
                for (Entity crystal : ListenerMotion.mc.world.loadedEntityList) {
                    double d;
                    if (!(crystal instanceof EntityEnderCrystal) || crystal.isDead) continue;
                    double distance = ListenerMotion.mc.player.getDistanceSq(crystal);
                    if (!(d < (double)MathUtil.square(((Float)((FastBreak)this.module).range.getValue()).floatValue())) || !ListenerMotion.mc.player.canEntityBeSeen(crystal) && !(distance < (double)MathUtil.square(3.3f))) continue;
                    entity = crystal;
                }
                if (entity != null && entity.getEntityId() == ((FastBreak)this.module).crystalID && ((FastBreak)this.module).crystalPos.equals((Object)(newCrystalPos = new BlockPos(entity.posX, entity.posY, entity.posZ).down())) && ((FastBreak)this.module).crystalTimer.passed(this.getCrystalDelay().intValue()) && ((FastBreak)this.module).getBlock() == Blocks.AIR) {
                    if (((FastBreak)this.module).crystalRetries >= 8) {
                        ((FastBreak)this.module).crystalID = -1;
                        ((FastBreak)this.module).crystalRetries = 0;
                        return;
                    }
                    float[] angle = RotationUtil.getRotations(entity.posX, entity.posY, entity.posZ);
                    if (((FastBreak)this.module).rotation.getValue() == RotationsEnum.NORMAL) {
                        Managers.ROTATION.setRotations(angle[0], angle[1]);
                    } else if (((FastBreak)this.module).rotation.getValue() == RotationsEnum.PACKET) {
                        ListenerMotion.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(angle[0], angle[1], ListenerMotion.mc.player.onGround));
                    }
                    AttackUtil.attackEntity(entity, (RotationsEnum)((Object)((FastBreak)this.module).rotation.getValue()), SwingEnum.PACKET, true);
                    ++((FastBreak)this.module).crystalRetries;
                    ((FastBreak)this.module).crystalTimer.reset();
                }
            }
        }
    }

    private Integer getCrystalDelay() {
        if (ListenerMotion.mc.player != null) {
            if (NetworkUtil.getLatencyNoSpoof() < 25) {
                return 50;
            }
            if (NetworkUtil.getLatencyNoSpoof() < 50) {
                return 25;
            }
            if (NetworkUtil.getLatencyNoSpoof() < 100) {
                return 15;
            }
            if (NetworkUtil.getLatencyNoSpoof() < 200) {
                return 0;
            }
        }
        return 25;
    }
}

