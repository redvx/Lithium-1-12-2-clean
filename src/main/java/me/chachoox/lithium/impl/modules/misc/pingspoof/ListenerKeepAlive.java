/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketKeepAlive
 */
package me.chachoox.lithium.impl.modules.misc.pingspoof;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.misc.pingspoof.PingSpoof;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketKeepAlive;

public class ListenerKeepAlive
extends ModuleListener<PingSpoof, PacketEvent.Send<CPacketKeepAlive>> {
    public ListenerKeepAlive(PingSpoof module) {
        super(module, PacketEvent.Send.class, CPacketKeepAlive.class);
    }

    @Override
    public void call(PacketEvent.Send<CPacketKeepAlive> event) {
        if (!mc.isSingleplayer()) {
            ((PingSpoof)this.module).onPacket((Packet<?>)event.getPacket());
            event.setCanceled(true);
        }
    }
}

