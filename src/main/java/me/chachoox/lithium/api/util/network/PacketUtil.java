/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketAnimation
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.util.EnumHand
 */
package me.chachoox.lithium.api.util.network;

import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.asm.ducks.INetworkManager;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.EnumHand;

public class PacketUtil
implements Minecraftable {
    public static void send(Packet<?> packet) {
        NetHandlerPlayClient connection = mc.getConnection();
        if (connection != null) {
            connection.sendPacket(packet);
        }
    }

    public static Packet<?> sendPacketNoEvent(Packet<?> packet) {
        NetHandlerPlayClient connection = mc.getConnection();
        if (connection != null) {
            INetworkManager manager = (INetworkManager)connection.getNetworkManager();
            return manager.sendPacketNoEvent(packet);
        }
        return null;
    }

    public static void sneak(boolean sneak) {
        PacketUtil.send(new CPacketEntityAction((Entity)PacketUtil.mc.player, sneak ? CPacketEntityAction.Action.START_SNEAKING : CPacketEntityAction.Action.STOP_SNEAKING));
    }

    public static void swing() {
        PacketUtil.send(new CPacketAnimation(EnumHand.MAIN_HAND));
    }
}

