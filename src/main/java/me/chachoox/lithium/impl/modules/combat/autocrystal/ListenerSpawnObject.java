/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.network.play.client.CPacketUseEntity$Action
 *  net.minecraft.network.play.server.SPacketSpawnObject
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 */
package me.chachoox.lithium.impl.modules.combat.autocrystal;

import me.chachoox.lithium.api.util.network.PacketUtil;
import me.chachoox.lithium.asm.mixins.network.client.ICPacketUseEntity;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.combat.autocrystal.AutoCrystal;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class ListenerSpawnObject
extends ModuleListener<AutoCrystal, PacketEvent.Receive<SPacketSpawnObject>> {
    public ListenerSpawnObject(AutoCrystal module) {
        super(module, PacketEvent.Receive.class, SPacketSpawnObject.class);
    }

    @Override
    public void call(PacketEvent.Receive<SPacketSpawnObject> event) {
        SPacketSpawnObject packet = (SPacketSpawnObject)event.getPacket();
        if (((AutoCrystal)this.module).boost.getValue().booleanValue() && packet.getType() == 51 && ((AutoCrystal)this.module).placeSet.contains(new BlockPos(packet.getX(), packet.getY(), packet.getZ()).down())) {
            ICPacketUseEntity hitPacket = (ICPacketUseEntity)new CPacketUseEntity();
            hitPacket.setEntityId(packet.getEntityID());
            hitPacket.setAction(CPacketUseEntity.Action.ATTACK);
            PacketUtil.send((Packet)hitPacket);
            ((AutoCrystal)this.module).predictedId = packet.getEntityID();
            ((AutoCrystal)this.module).attackMap.put(packet.getEntityID(), ((AutoCrystal)this.module).attackMap.containsKey(packet.getEntityID()) ? ((AutoCrystal)this.module).attackMap.get(packet.getEntityID()) + 1 : 1);
            ListenerSpawnObject.mc.player.swingArm(((AutoCrystal)this.module).offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
            ((AutoCrystal)this.module).breakTimer.reset();
        }
    }
}

