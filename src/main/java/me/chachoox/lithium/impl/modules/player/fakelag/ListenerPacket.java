/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketChatMessage
 *  net.minecraft.network.play.client.CPacketClientStatus
 *  net.minecraft.network.play.client.CPacketConfirmTeleport
 *  net.minecraft.network.play.client.CPacketKeepAlive
 *  net.minecraft.network.play.client.CPacketTabComplete
 */
package me.chachoox.lithium.impl.modules.player.fakelag;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.player.fakelag.FakeLag;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketTabComplete;

public class ListenerPacket
extends ModuleListener<FakeLag, PacketEvent.Send<?>> {
    public ListenerPacket(FakeLag module) {
        super(module, PacketEvent.Send.class);
    }

    @Override
    public void call(PacketEvent.Send<?> event) {
        if (!(event.getPacket() instanceof CPacketChatMessage || event.getPacket() instanceof CPacketConfirmTeleport || event.getPacket() instanceof CPacketKeepAlive || event.getPacket() instanceof CPacketTabComplete || event.getPacket() instanceof CPacketClientStatus || ((FakeLag)this.module).cache.contains(event.getPacket()))) {
            ((FakeLag)this.module).cache.add((Packet<?>)event.getPacket());
            event.setCanceled(true);
        }
    }
}

