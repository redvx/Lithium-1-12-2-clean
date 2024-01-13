/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketCloseWindow
 *  net.minecraft.network.play.server.SPacketConfirmTransaction
 *  net.minecraft.network.play.server.SPacketEntityStatus
 *  net.minecraft.network.play.server.SPacketOpenWindow
 *  net.minecraft.network.play.server.SPacketPlayerListItem
 *  net.minecraft.network.play.server.SPacketPlayerPosLook
 *  net.minecraft.network.play.server.SPacketResourcePackSend
 *  net.minecraft.network.play.server.SPacketSetSlot
 *  net.minecraft.util.StringUtils
 *  net.minecraft.world.World
 */
package me.chachoox.lithium.impl.modules.misc.packetlogger;

import me.chachoox.lithium.asm.mixins.network.server.ISPacketCloseWindow;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.misc.packetlogger.PacketLogger;
import me.chachoox.lithium.impl.modules.misc.packetlogger.mode.Packets;
import net.minecraft.network.play.server.SPacketCloseWindow;
import net.minecraft.network.play.server.SPacketConfirmTransaction;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketResourcePackSend;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;

public class ListenerReceive
extends ModuleListener<PacketLogger, PacketEvent.Receive<?>> {
    public ListenerReceive(PacketLogger module) {
        super(module, PacketEvent.Receive.class, Integer.MIN_VALUE);
    }

    @Override
    public void call(PacketEvent.Receive<?> event) {
        if (((PacketLogger)this.module).packets.getValue() == Packets.OUTGOING) {
            return;
        }
        ((PacketLogger)this.module).initializeWriter();
        String packetInfo = null;
        if (event.getPacket() instanceof SPacketPlayerPosLook && ((PacketLogger)this.module).sPlayerPosLook.getValue().booleanValue()) {
            SPacketPlayerPosLook packet = (SPacketPlayerPosLook)event.getPacket();
            packetInfo = "SPacketPlayerPosLook:\n- Pitch: " + packet.getPitch() + "\n- Yaw: " + packet.getYaw() + "\n- TeleportID: " + packet.getTeleportId();
        } else if (event.getPacket() instanceof SPacketPlayerListItem && ((PacketLogger)this.module).sPlayerListItem.getValue().booleanValue()) {
            SPacketPlayerListItem packet = (SPacketPlayerListItem)event.getPacket();
            packetInfo = "SPacketPlayerListItem:\n- Action: " + packet.getAction().name() + "\n- Entries: " + packet.getEntries();
        } else if (event.getPacket() instanceof SPacketOpenWindow && ((PacketLogger)this.module).sOpenWindow.getValue().booleanValue()) {
            SPacketOpenWindow packet = (SPacketOpenWindow)event.getPacket();
            packetInfo = "SPacketOpenWindow:\n- WindowID: " + packet.getWindowId() + "\n- EntityID: " + packet.getEntityId() + "\n- WindowTitle: " + packet.getWindowTitle().getUnformattedText() + "\n- GuiID: " + packet.getGuiId() + "\n- SlotCount: " + packet.getSlotCount();
        } else if (event.getPacket() instanceof SPacketCloseWindow && ((PacketLogger)this.module).sCloseWindow.getValue().booleanValue()) {
            SPacketCloseWindow packet = (SPacketCloseWindow)event.getPacket();
            packetInfo = "SPacketCloseWindow:\n- WindowID: " + ((ISPacketCloseWindow)packet).getWindowID();
        } else if (event.getPacket() instanceof SPacketSetSlot && ((PacketLogger)this.module).sSetSlot.getValue().booleanValue()) {
            SPacketSetSlot packet = (SPacketSetSlot)event.getPacket();
            packetInfo = "SPacketSetSlot:\n- WindowID: " + packet.getWindowId() + "\n- SlotID: " + packet.getSlot() + "\n- ItemName: " + packet.getStack().getDisplayName();
        } else if (event.getPacket() instanceof SPacketEntityStatus && ((PacketLogger)this.module).sEntityStatus.getValue().booleanValue()) {
            SPacketEntityStatus packet = (SPacketEntityStatus)event.getPacket();
            packetInfo = "SPacketEntityStatus:\n- EntityId: " + packet.getEntity((World)ListenerReceive.mc.world).getEntityId() + "\n- EntityName: " + packet.getEntity((World)ListenerReceive.mc.world).getName() + "\n- OpCode: " + packet.getOpCode();
        } else if (event.getPacket() instanceof SPacketResourcePackSend && ((PacketLogger)this.module).sPacketResource.getValue().booleanValue()) {
            SPacketResourcePackSend packet = (SPacketResourcePackSend)event.getPacket();
            packetInfo = "SPacketResourcePackSend:\n- URL: " + packet.getURL() + "\n- Hash: " + packet.getHash();
        } else if (event.getPacket() instanceof SPacketConfirmTransaction && ((PacketLogger)this.module).sConfirmTransaction.getValue().booleanValue()) {
            SPacketConfirmTransaction packet = (SPacketConfirmTransaction)event.getPacket();
            packetInfo = "SPacketConfirmTransaction:\n- WindowID: " + packet.getWindowId() + "\n- ActionNumber: " + packet.getActionNumber() + "\n- Accepted: " + packet.wasAccepted();
        }
        if (StringUtils.isNullOrEmpty(packetInfo)) {
            return;
        }
        if (((PacketLogger)this.module).showCanceled.getValue().booleanValue()) {
            packetInfo = packetInfo + "\n- Canceled: " + event.isCanceled();
        }
        ((PacketLogger)this.module).log(packetInfo);
        ((PacketLogger)this.module).write(packetInfo);
    }
}

