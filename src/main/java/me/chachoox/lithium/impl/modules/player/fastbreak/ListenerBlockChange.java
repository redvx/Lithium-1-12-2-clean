/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.init.Blocks
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.network.play.server.SPacketBlockChange
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.BlockPos
 */
package me.chachoox.lithium.impl.modules.player.fastbreak;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.player.fastbreak.FastBreak;
import me.chachoox.lithium.impl.modules.player.fastbreak.mode.MineMode;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class ListenerBlockChange
extends ModuleListener<FastBreak, PacketEvent.Receive<SPacketBlockChange>> {
    public ListenerBlockChange(FastBreak module) {
        super(module, PacketEvent.Receive.class, SPacketBlockChange.class);
    }

    @Override
    public void call(PacketEvent.Receive<SPacketBlockChange> event) {
        SPacketBlockChange packet = (SPacketBlockChange)event.getPacket();
        BlockPos packetPos = packet.getBlockPosition();
        if (packetPos.equals((Object)((FastBreak)this.module).pos) && packet.getBlockState().getBlock() == Blocks.AIR) {
            ((FastBreak)this.module).retries = 0;
            ((FastBreak)this.module).resetSwap();
        }
        if (packet.getBlockPosition().equals((Object)((FastBreak)this.module).pos) && packet.getBlockState() == ListenerBlockChange.mc.world.getBlockState(((FastBreak)this.module).pos) && ((FastBreak)this.module).shouldAbort && ((FastBreak)this.module).mode.getValue() == MineMode.INSTANT) {
            ListenerBlockChange.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, ((FastBreak)this.module).pos, EnumFacing.DOWN));
            ((FastBreak)this.module).shouldAbort = false;
        }
    }
}

