/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.network.play.server.SPacketSoundEffect
 *  net.minecraft.util.math.AxisAlignedBB
 */
package me.chachoox.lithium.impl.modules.misc.pvpinfo;

import java.util.List;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.misc.pvpinfo.PvPInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.math.AxisAlignedBB;

public class ListenerSound
extends ModuleListener<PvPInfo, PacketEvent.Receive<SPacketSoundEffect>> {
    public ListenerSound(PvPInfo module) {
        super(module, PacketEvent.Receive.class, SPacketSoundEffect.class);
    }

    @Override
    public void call(PacketEvent.Receive<SPacketSoundEffect> event) {
        SPacketSoundEffect packet = (SPacketSoundEffect)event.getPacket();
    }

    public List<EntityPlayer> getPlayersViaSoundPacket(SPacketSoundEffect packet) {
        return ListenerSound.mc.world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(packet.getX() - 1.0, packet.getY() - 1.0, packet.getZ() - 1.0, packet.getX() + 1.0, packet.getY() + 1.0, packet.getZ() + 1.0));
    }
}

