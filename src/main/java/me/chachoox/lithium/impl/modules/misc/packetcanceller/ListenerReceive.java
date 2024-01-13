/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketCloseWindow
 */
package me.chachoox.lithium.impl.modules.misc.packetcanceller;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.misc.packetcanceller.PacketCanceller;
import net.minecraft.network.play.server.SPacketCloseWindow;

public class ListenerReceive
extends ModuleListener<PacketCanceller, PacketEvent.Receive<?>> {
    public ListenerReceive(PacketCanceller module) {
        super(module, PacketEvent.Receive.class, Integer.MAX_VALUE);
    }

    @Override
    public void call(PacketEvent.Receive<?> event) {
        if (event.getPacket() instanceof SPacketCloseWindow && ((PacketCanceller)this.module).sCloseWindow.getValue().booleanValue()) {
            event.setCanceled(true);
        }
    }
}

