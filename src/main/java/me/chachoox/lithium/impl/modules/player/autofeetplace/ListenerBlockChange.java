/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketBlockChange
 *  net.minecraft.util.math.BlockPos
 */
package me.chachoox.lithium.impl.modules.player.autofeetplace;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.player.autofeetplace.AutoFeetPlace;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.util.math.BlockPos;

public class ListenerBlockChange
extends ModuleListener<AutoFeetPlace, PacketEvent.Receive<SPacketBlockChange>> {
    public ListenerBlockChange(AutoFeetPlace module) {
        super(module, PacketEvent.Receive.class, SPacketBlockChange.class);
    }

    @Override
    public void call(PacketEvent.Receive<SPacketBlockChange> event) {
        BlockPos changePos = ((SPacketBlockChange)event.getPacket()).getBlockPosition();
        if (((SPacketBlockChange)event.getPacket()).getBlockState().getMaterial().isReplaceable() && ((AutoFeetPlace)this.module).getPlacements().contains(changePos)) {
            ((AutoFeetPlace)this.module).placeBlock(changePos);
        }
    }
}

