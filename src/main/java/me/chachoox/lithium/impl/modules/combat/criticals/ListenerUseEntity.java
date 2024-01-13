/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.item.EntityBoat
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.network.play.client.CPacketUseEntity$Action
 *  net.minecraft.world.World
 */
package me.chachoox.lithium.impl.modules.combat.criticals;

import me.chachoox.lithium.api.util.network.PacketUtil;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.combat.criticals.Criticals;
import me.chachoox.lithium.impl.modules.combat.criticals.CriticalsType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.world.World;

public class ListenerUseEntity
extends ModuleListener<Criticals, PacketEvent.Send<CPacketUseEntity>> {
    public ListenerUseEntity(Criticals module) {
        super(module, PacketEvent.Send.class, CPacketUseEntity.class);
    }

    @Override
    public void call(PacketEvent.Send<CPacketUseEntity> event) {
        CPacketUseEntity packet = (CPacketUseEntity)event.getPacket();
        if (ListenerUseEntity.mc.player.onGround && packet.getAction() == CPacketUseEntity.Action.ATTACK) {
            Entity entity = packet.getEntityFromWorld((World)ListenerUseEntity.mc.world);
            if (entity == null) {
                return;
            }
            if (((Criticals)this.module).yDifference.getValue().booleanValue() && (double)((int)entity.posY) >= ListenerUseEntity.mc.player.posY) {
                return;
            }
            if (entity instanceof EntityLivingBase && !ListenerUseEntity.mc.player.isInWater() && !ListenerUseEntity.mc.player.isInLava()) {
                switch ((CriticalsType)((Object)((Criticals)this.module).mode.getValue())) {
                    case PACKET: {
                        PacketUtil.send(new CPacketPlayer.Position(ListenerUseEntity.mc.player.posX, ListenerUseEntity.mc.player.posY + (double)0.05f, ListenerUseEntity.mc.player.posZ, false));
                        PacketUtil.send(new CPacketPlayer.Position(ListenerUseEntity.mc.player.posX, ListenerUseEntity.mc.player.posY, ListenerUseEntity.mc.player.posZ, false));
                        PacketUtil.send(new CPacketPlayer.Position(ListenerUseEntity.mc.player.posX, ListenerUseEntity.mc.player.posY + (double)0.03f, ListenerUseEntity.mc.player.posZ, false));
                        PacketUtil.send(new CPacketPlayer.Position(ListenerUseEntity.mc.player.posX, ListenerUseEntity.mc.player.posY, ListenerUseEntity.mc.player.posZ, false));
                        break;
                    }
                    case BYPASS: {
                        PacketUtil.send(new CPacketPlayer.Position(ListenerUseEntity.mc.player.posX, ListenerUseEntity.mc.player.posY + 0.062600301692775, ListenerUseEntity.mc.player.posZ, false));
                        PacketUtil.send(new CPacketPlayer.Position(ListenerUseEntity.mc.player.posX, ListenerUseEntity.mc.player.posY + 0.07260029960661, ListenerUseEntity.mc.player.posZ, false));
                        PacketUtil.send(new CPacketPlayer.Position(ListenerUseEntity.mc.player.posX, ListenerUseEntity.mc.player.posY, ListenerUseEntity.mc.player.posZ, false));
                        PacketUtil.send(new CPacketPlayer.Position(ListenerUseEntity.mc.player.posX, ListenerUseEntity.mc.player.posY, ListenerUseEntity.mc.player.posZ, false));
                    }
                }
            } else if (entity instanceof EntityBoat) {
                for (int i = 0; i < 15; ++i) {
                    PacketUtil.sendPacketNoEvent(new CPacketUseEntity(entity));
                }
            }
        }
    }
}

