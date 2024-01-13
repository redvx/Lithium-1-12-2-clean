/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiDownloadTerrain
 *  net.minecraft.network.play.server.SPacketPlayerPosLook
 *  net.minecraft.util.math.BlockPos
 */
package me.chachoox.lithium.impl.modules.movement.packetfly;

import me.chachoox.lithium.asm.mixins.network.server.ISPacketPlayerPosLook;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.movement.packetfly.PacketFly;
import me.chachoox.lithium.impl.modules.movement.packetfly.mode.PacketFlyMode;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.BlockPos;

public class ListenerPosLook
extends ModuleListener<PacketFly, PacketEvent.Receive<SPacketPlayerPosLook>> {
    public ListenerPosLook(PacketFly module) {
        super(module, PacketEvent.Receive.class, SPacketPlayerPosLook.class);
    }

    @Override
    public void call(PacketEvent.Receive<SPacketPlayerPosLook> event) {
        if (ListenerPosLook.mc.player != null) {
            SPacketPlayerPosLook packet = (SPacketPlayerPosLook)event.getPacket();
            if (ListenerPosLook.mc.player.isEntityAlive() && ListenerPosLook.mc.world.isBlockLoaded(new BlockPos(ListenerPosLook.mc.player.posX, ListenerPosLook.mc.player.posY, ListenerPosLook.mc.player.posZ), false) && !(ListenerPosLook.mc.currentScreen instanceof GuiDownloadTerrain) && ((PacketFly)this.module).mode.getValue() != PacketFlyMode.SETBACK) {
                ((PacketFly)this.module).teleportMap.remove(packet.getTeleportId());
            }
            ((ISPacketPlayerPosLook)packet).setYaw(ListenerPosLook.mc.player.rotationYaw);
            ((ISPacketPlayerPosLook)packet).setYaw(ListenerPosLook.mc.player.rotationPitch);
            ((PacketFly)this.module).lagTime = 10;
            ((PacketFly)this.module).lastTpID = packet.getTeleportId();
        }
    }
}

