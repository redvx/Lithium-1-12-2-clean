/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 */
package me.chachoox.lithium.impl.modules.player.positionspoof;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.player.positionspoof.PositionSpoof;
import net.minecraft.network.play.client.CPacketPlayer;

public class ListenerCPacket
extends ModuleListener<PositionSpoof, PacketEvent.Send<CPacketPlayer.Position>> {
    public ListenerCPacket(PositionSpoof module) {
        super(module, PacketEvent.Send.class, CPacketPlayer.Position.class);
    }

    @Override
    public void call(PacketEvent.Send<CPacketPlayer.Position> event) {
        event.setCanceled(true);
    }
}

