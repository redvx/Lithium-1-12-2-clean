/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketSpawnObject
 *  net.minecraft.util.math.BlockPos
 */
package me.chachoox.lithium.impl.modules.player.fastbreak;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.player.fastbreak.FastBreak;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.math.BlockPos;

public class ListenerSpawnObject
extends ModuleListener<FastBreak, PacketEvent.Receive<SPacketSpawnObject>> {
    public ListenerSpawnObject(FastBreak module) {
        super(module, PacketEvent.Receive.class, SPacketSpawnObject.class);
    }

    @Override
    public void call(PacketEvent.Receive<SPacketSpawnObject> event) {
        if (((FastBreak)this.module).pos != null) {
            SPacketSpawnObject packet = (SPacketSpawnObject)event.getPacket();
            BlockPos packetPos = new BlockPos(packet.getX(), packet.getY(), packet.getZ()).down();
            if (packet.getType() == 51 && packetPos.equals((Object)((FastBreak)this.module).pos) && ((FastBreak)this.module).crystalAttack) {
                ((FastBreak)this.module).crystalPos = packetPos;
                ((FastBreak)this.module).crystalID = packet.getEntityID();
                ((FastBreak)this.module).crystalAttack = false;
            }
        }
    }
}

