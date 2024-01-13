/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.play.client.CPacketAnimation
 *  net.minecraft.network.play.client.CPacketClickWindow
 *  net.minecraft.network.play.client.CPacketCloseWindow
 *  net.minecraft.network.play.client.CPacketConfirmTeleport
 *  net.minecraft.network.play.client.CPacketConfirmTransaction
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItem
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.util.StringUtils
 *  net.minecraft.world.World
 */
package me.chachoox.lithium.impl.modules.misc.packetlogger;

import me.chachoox.lithium.asm.mixins.network.client.ICPacketCloseWindow;
import me.chachoox.lithium.asm.mixins.network.client.ICPacketConfirmTransaction;
import me.chachoox.lithium.asm.mixins.network.client.ICPacketPlayer;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.misc.packetlogger.PacketLogger;
import me.chachoox.lithium.impl.modules.misc.packetlogger.mode.Packets;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;

public class ListenerSend
extends ModuleListener<PacketLogger, PacketEvent.Send<?>> {
    public ListenerSend(PacketLogger module) {
        super(module, PacketEvent.Send.class, Integer.MIN_VALUE);
    }

    @Override
    public void call(PacketEvent.Send<?> event) {
        if (((PacketLogger)this.module).packets.getValue() == Packets.INCOMING) {
            return;
        }
        String packetInfo = null;
        ((PacketLogger)this.module).initializeWriter();
        if (event.getPacket() instanceof CPacketPlayer && ((PacketLogger)this.module).cPlayer.getValue().booleanValue()) {
            CPacketPlayer packet = (CPacketPlayer)event.getPacket();
            packetInfo = "CPacketPlayer:\n- X: " + ((ICPacketPlayer)packet).getX() + "\n- Y: " + ((ICPacketPlayer)packet).getY() + "\n- Z: " + ((ICPacketPlayer)packet).getZ() + "\n- OnGround: " + packet.isOnGround() + "\n- Pitch: " + ((ICPacketPlayer)packet).getPitch() + "\n- Yaw: " + ((ICPacketPlayer)packet).getYaw();
        } else if (event.getPacket() instanceof CPacketUseEntity && ((PacketLogger)this.module).cUseEntity.getValue().booleanValue()) {
            CPacketUseEntity packet = (CPacketUseEntity)event.getPacket();
            packetInfo = "CPacketUseEntity:\n- Hand: " + (packet.getHand() == null ? "null" : packet.getHand().name()) + "\n- HitVec: " + packet.getHitVec();
            Entity entity = packet.getEntityFromWorld((World)ListenerSend.mc.world);
            if (entity != null) {
                packetInfo = packetInfo + "\n- EntityName: " + entity.getName() + "\n- EntityID: " + entity.getEntityId();
            }
        } else if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock && ((PacketLogger)this.module).cTryUseItemOnBlock.getValue().booleanValue()) {
            CPacketPlayerTryUseItemOnBlock packet = (CPacketPlayerTryUseItemOnBlock)event.getPacket();
            packetInfo = "CPacketPlayerTryUseItemOnBlock:\n- Pos: " + packet.getPos() + "\n- Direction: " + packet.getDirection().getName() + "\n- FacingX: " + packet.getFacingX() + "\n- FacingY: " + packet.getFacingY() + "\n- FacingZ: " + packet.getFacingZ() + "\n- Hand: " + packet.getHand().name();
        } else if (event.getPacket() instanceof CPacketEntityAction && ((PacketLogger)this.module).cEntityAction.getValue().booleanValue()) {
            CPacketEntityAction packet = (CPacketEntityAction)event.getPacket();
            packetInfo = "CPacketEntityAction:\n- Action: " + packet.getAction().name() + "\n- AuxData: " + packet.getAuxData();
        } else if (event.getPacket() instanceof CPacketPlayerDigging && ((PacketLogger)this.module).cDigging.getValue().booleanValue()) {
            CPacketPlayerDigging packet = (CPacketPlayerDigging)event.getPacket();
            packetInfo = "CPacketPlayerDigging:\n- Action: " + packet.getAction().name() + "\n- Pos: " + packet.getPosition() + "\n- Facing: " + packet.getFacing();
        } else if (event.getPacket() instanceof CPacketCloseWindow && ((PacketLogger)this.module).cCloseWindow.getValue().booleanValue()) {
            CPacketCloseWindow packet = (CPacketCloseWindow)event.getPacket();
            packetInfo = "CPacketCloseWindow:\n- WindowID: " + ((ICPacketCloseWindow)packet).getWindowId();
        } else if (event.getPacket() instanceof CPacketClickWindow && ((PacketLogger)this.module).cClickWindow.getValue().booleanValue()) {
            CPacketClickWindow packet = (CPacketClickWindow)event.getPacket();
            packetInfo = "CPacketClickWindow:\n- WindowID: " + packet.getWindowId() + "\n- SlotID: " + packet.getSlotId() + "\n- UsedButton: " + packet.getUsedButton() + "\n- ActionNumber: " + packet.getActionNumber() + "\n- ItemName: " + packet.getClickedItem().getDisplayName() + "\n- ClickType: " + packet.getClickType().name();
        } else if (event.getPacket() instanceof CPacketHeldItemChange && ((PacketLogger)this.module).cHeldItem.getValue().booleanValue()) {
            CPacketHeldItemChange packet = (CPacketHeldItemChange)event.getPacket();
            packetInfo = "CPacketHeldItemChange:\n- SlotID: " + packet.getSlotId();
        } else if (event.getPacket() instanceof CPacketPlayerTryUseItem && ((PacketLogger)this.module).cUseItem.getValue().booleanValue()) {
            CPacketPlayerTryUseItem packet = (CPacketPlayerTryUseItem)event.getPacket();
            packetInfo = "CPacketPlayerTryUseItem:\n- Hand: " + packet.getHand();
        } else if (event.getPacket() instanceof CPacketAnimation && ((PacketLogger)this.module).cAnimation.getValue().booleanValue()) {
            CPacketAnimation packet = (CPacketAnimation)event.getPacket();
            packetInfo = "CPacketAnimation:\n- Hand: " + packet.getHand();
        } else if (event.getPacket() instanceof CPacketConfirmTransaction && ((PacketLogger)this.module).cConfirmTransaction.getValue().booleanValue()) {
            CPacketConfirmTransaction packet = (CPacketConfirmTransaction)event.getPacket();
            packetInfo = "CPacketConfirmTransaction\n- WindowID: " + packet.getWindowId() + "\n- UID: " + packet.getUid() + "\n- Accepted: " + ((ICPacketConfirmTransaction)packet).getAccepted();
        } else if (event.getPacket() instanceof CPacketConfirmTeleport && ((PacketLogger)this.module).cConfirmTeleport.getValue().booleanValue()) {
            CPacketConfirmTeleport packet = (CPacketConfirmTeleport)event.getPacket();
            packetInfo = "CPacketConfirmTeleport:\n- TeleportID: " + packet.getTeleportId();
        }
        if (StringUtils.isNullOrEmpty((String)packetInfo)) {
            return;
        }
        if (((PacketLogger)this.module).showCanceled.getValue().booleanValue()) {
            packetInfo = packetInfo + "\n- Canceled: " + event.isCanceled();
        }
        ((PacketLogger)this.module).log(packetInfo);
        ((PacketLogger)this.module).write(packetInfo);
    }
}

