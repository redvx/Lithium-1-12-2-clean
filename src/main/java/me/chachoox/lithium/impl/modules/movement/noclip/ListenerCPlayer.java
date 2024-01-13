/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketPlayer
 */
package me.chachoox.lithium.impl.modules.movement.noclip;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.movement.noclip.NoClip;
import net.minecraft.network.play.client.CPacketPlayer;

public class ListenerCPlayer
extends ModuleListener<NoClip, PacketEvent.Send<CPacketPlayer>> {
    public ListenerCPlayer(NoClip module) {
        super(module, PacketEvent.Send.class, CPacketPlayer.class);
    }

    @Override
    public void call(PacketEvent.Send<CPacketPlayer> event) {
        if (!((NoClip)this.module).packets.remove(event.getPacket()) && ((NoClip)this.module).cancelPackets.getValue().booleanValue()) {
            event.setCanceled(true);
        }
    }
}

