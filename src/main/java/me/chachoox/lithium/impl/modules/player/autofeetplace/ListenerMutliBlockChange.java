/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketMultiBlockChange
 *  net.minecraft.network.play.server.SPacketMultiBlockChange$BlockUpdateData
 *  net.minecraft.util.math.BlockPos
 */
package me.chachoox.lithium.impl.modules.player.autofeetplace;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.player.autofeetplace.AutoFeetPlace;
import net.minecraft.network.play.server.SPacketMultiBlockChange;
import net.minecraft.util.math.BlockPos;

public class ListenerMutliBlockChange
extends ModuleListener<AutoFeetPlace, PacketEvent.Receive<SPacketMultiBlockChange>> {
    public ListenerMutliBlockChange(AutoFeetPlace module) {
        super(module, PacketEvent.Receive.class, SPacketMultiBlockChange.class);
    }

    @Override
    public void call(PacketEvent.Receive<SPacketMultiBlockChange> event) {
        for (SPacketMultiBlockChange.BlockUpdateData blockUpdateData : ((SPacketMultiBlockChange)event.getPacket()).getChangedBlocks()) {
            BlockPos changePos = blockUpdateData.getPos();
            if (!blockUpdateData.getBlockState().getMaterial().isReplaceable() || !((AutoFeetPlace)this.module).getPlacements().contains(changePos)) continue;
            ((AutoFeetPlace)this.module).placeBlock(changePos);
        }
    }
}

