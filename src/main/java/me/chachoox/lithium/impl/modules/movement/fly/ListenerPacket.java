/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketPlayer
 */
package me.chachoox.lithium.impl.modules.movement.fly;

import me.chachoox.lithium.asm.mixins.network.client.ICPacketPlayer;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.movement.fly.Fly;
import net.minecraft.network.play.client.CPacketPlayer;

public class ListenerPacket
extends ModuleListener<Fly, PacketEvent.Send<CPacketPlayer>> {
    public ListenerPacket(Fly module) {
        super(module, PacketEvent.Send.class, CPacketPlayer.class);
    }

    @Override
    public void call(PacketEvent.Send<CPacketPlayer> event) {
        ((ICPacketPlayer)event.getPacket()).setOnGround(true);
    }
}

