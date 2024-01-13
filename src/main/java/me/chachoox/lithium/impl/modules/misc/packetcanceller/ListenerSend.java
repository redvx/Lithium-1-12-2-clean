/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketInput
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.network.play.client.CPacketVehicleMove
 */
package me.chachoox.lithium.impl.modules.misc.packetcanceller;

import me.chachoox.lithium.api.event.events.Event;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.misc.packetcanceller.PacketCanceller;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketVehicleMove;

public class ListenerSend
extends ModuleListener<PacketCanceller, PacketEvent.Send<?>> {
    public ListenerSend(PacketCanceller module) {
        super(module, PacketEvent.Send.class, Integer.MAX_VALUE);
    }

    @Override
    public void call(PacketEvent.Send<?> event) {
        Object packet = event.getPacket();
        if (packet instanceof CPacketPlayerTryUseItemOnBlock && ((PacketCanceller)this.module).cTryUseItemOnBlock.getValue().booleanValue()) {
            this.cancel(event);
        } else if (packet instanceof CPacketPlayerDigging && ((PacketCanceller)this.module).cDigging.getValue().booleanValue()) {
            this.cancel(event);
        } else if (packet instanceof CPacketInput && ((PacketCanceller)this.module).cInput.getValue().booleanValue()) {
            this.cancel(event);
        } else if (packet instanceof CPacketPlayer && ((PacketCanceller)this.module).cPlayer.getValue().booleanValue()) {
            this.cancel(event);
        } else if (packet instanceof CPacketEntityAction && ((PacketCanceller)this.module).cEntityAction.getValue().booleanValue()) {
            this.cancel(event);
        } else if (packet instanceof CPacketUseEntity && ((PacketCanceller)this.module).cUseEntity.getValue().booleanValue()) {
            this.cancel(event);
        } else if (packet instanceof CPacketVehicleMove && ((PacketCanceller)this.module).cVehicleMove.getValue().booleanValue()) {
            this.cancel(event);
        }
    }

    private void cancel(Event event) {
        ++((PacketCanceller)this.module).packets;
        event.setCanceled(true);
    }
}

