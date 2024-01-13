/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.util.math.BlockPos
 */
package me.chachoox.lithium.impl.modules.player.fastbreak;

import me.chachoox.lithium.api.util.blocks.MineUtil;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.player.fastbreak.FastBreak;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.math.BlockPos;

public class ListenerDigging
extends ModuleListener<FastBreak, PacketEvent.Send<CPacketPlayerDigging>> {
    public ListenerDigging(FastBreak module) {
        super(module, PacketEvent.Send.class, CPacketPlayerDigging.class);
    }

    @Override
    public void call(PacketEvent.Send<CPacketPlayerDigging> event) {
        BlockPos pos;
        CPacketPlayerDigging packet = (CPacketPlayerDigging)event.getPacket();
        if ((!ListenerDigging.mc.player.capabilities.isCreativeMode && packet.getAction() == CPacketPlayerDigging.Action.START_DESTROY_BLOCK || packet.getAction() == CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK) && !MineUtil.canBreak(pos = packet.getPosition())) {
            event.setCanceled(true);
        }
    }
}

