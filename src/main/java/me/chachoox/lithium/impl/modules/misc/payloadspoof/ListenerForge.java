/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.internal.FMLProxyPacket
 */
package me.chachoox.lithium.impl.modules.misc.payloadspoof;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.misc.payloadspoof.PayloadSpoof;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

public class ListenerForge
extends ModuleListener<PayloadSpoof, PacketEvent.Send<FMLProxyPacket>> {
    public ListenerForge(PayloadSpoof module) {
        super(module, PacketEvent.Send.class, FMLProxyPacket.class);
    }

    @Override
    public void call(PacketEvent.Send<FMLProxyPacket> event) {
        if (!mc.isIntegratedServerRunning()) {
            event.setCanceled(true);
        }
    }
}

