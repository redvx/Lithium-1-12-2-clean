/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketSoundEffect
 *  net.minecraft.util.math.BlockPos
 */
package me.chachoox.lithium.impl.modules.player.autofeetplace;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.player.autofeetplace.AutoFeetPlace;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.math.BlockPos;

public class ListenerSound
extends ModuleListener<AutoFeetPlace, PacketEvent.Receive<SPacketSoundEffect>> {
    public ListenerSound(AutoFeetPlace module) {
        super(module, PacketEvent.Receive.class, SPacketSoundEffect.class);
    }

    @Override
    public void call(PacketEvent.Receive<SPacketSoundEffect> event) {
        BlockPos explosion = new BlockPos(((SPacketSoundEffect)event.getPacket()).getX(), ((SPacketSoundEffect)event.getPacket()).getY(), ((SPacketSoundEffect)event.getPacket()).getZ());
        if (((AutoFeetPlace)this.module).getPlacements().contains(explosion)) {
            ((AutoFeetPlace)this.module).placeBlock(explosion);
        }
    }
}

