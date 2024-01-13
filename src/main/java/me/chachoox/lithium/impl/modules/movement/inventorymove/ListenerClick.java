/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketClickWindow
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 */
package me.chachoox.lithium.impl.modules.movement.inventorymove;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.movement.inventorymove.InventoryMove;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketEntityAction;

public class ListenerClick
extends ModuleListener<InventoryMove, PacketEvent.Send<CPacketClickWindow>> {
    public ListenerClick(InventoryMove module) {
        super(module, PacketEvent.Send.class, CPacketClickWindow.class);
    }

    @Override
    public void call(PacketEvent.Send<CPacketClickWindow> event) {
        if (Managers.ACTION.isSneaking()) {
            ListenerClick.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)ListenerClick.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        }
        if (Managers.ACTION.isSprinting()) {
            ListenerClick.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)ListenerClick.mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
        }
    }
}

