/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 */
package me.chachoox.lithium.impl.modules.misc.nobreakanim;

import me.chachoox.lithium.api.util.blocks.MineUtil;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.misc.nobreakanim.NoBreakAnim;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;

public class ListenerPacket
extends ModuleListener<NoBreakAnim, PacketEvent.Post<CPacketPlayerDigging>> {
    public ListenerPacket(NoBreakAnim module) {
        super(module, PacketEvent.Post.class, CPacketPlayerDigging.class);
    }

    @Override
    public void call(PacketEvent.Post<CPacketPlayerDigging> event) {
        if (!ListenerPacket.mc.player.capabilities.isCreativeMode && ((CPacketPlayerDigging)event.getPacket()).getAction() == CPacketPlayerDigging.Action.START_DESTROY_BLOCK && MineUtil.canBreak(((CPacketPlayerDigging)event.getPacket()).getPosition())) {
            ListenerPacket.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, ((CPacketPlayerDigging)event.getPacket()).getPosition(), ((CPacketPlayerDigging)event.getPacket()).getFacing()));
        }
    }
}

