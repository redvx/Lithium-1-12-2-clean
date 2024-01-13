/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.ItemBoat
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 */
package me.chachoox.lithium.impl.modules.player.fastplace;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.player.fastplace.FastPlace;
import net.minecraft.item.ItemBoat;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;

public class ListenerPlaceBoat
extends ModuleListener<FastPlace, PacketEvent.Send<CPacketPlayerTryUseItemOnBlock>> {
    public ListenerPlaceBoat(FastPlace module) {
        super(module, PacketEvent.Send.class, CPacketPlayerTryUseItemOnBlock.class);
    }

    @Override
    public void call(PacketEvent.Send<CPacketPlayerTryUseItemOnBlock> event) {
        CPacketPlayerTryUseItemOnBlock packet;
        if (((FastPlace)this.module).boatFix.getValue().booleanValue() && ListenerPlaceBoat.mc.player.getHeldItem((packet = (CPacketPlayerTryUseItemOnBlock)event.getPacket()).getHand()).getItem() instanceof ItemBoat) {
            event.setCanceled(true);
        }
    }
}

