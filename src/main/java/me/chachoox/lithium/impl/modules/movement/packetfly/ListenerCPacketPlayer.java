/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 */
package me.chachoox.lithium.impl.modules.movement.packetfly;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.movement.packetfly.PacketFly;
import net.minecraft.network.play.client.CPacketPlayer;

public class ListenerCPacketPlayer
extends ModuleListener<PacketFly, PacketEvent.Send<CPacketPlayer.Position>> {
    public ListenerCPacketPlayer(PacketFly module) {
        super(module, PacketEvent.Send.class, CPacketPlayer.Position.class);
    }

    @Override
    public void call(PacketEvent.Send<CPacketPlayer.Position> event) {
        CPacketPlayer packet = (CPacketPlayer)event.getPacket();
        if (((PacketFly)this.module).packets.contains((Object)packet)) {
            ((PacketFly)this.module).packets.remove((Object)packet);
            return;
        }
        event.setCanceled(true);
    }
}

