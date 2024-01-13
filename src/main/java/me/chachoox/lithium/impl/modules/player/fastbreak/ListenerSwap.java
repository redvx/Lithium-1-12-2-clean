/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 */
package me.chachoox.lithium.impl.modules.player.fastbreak;

import me.chachoox.lithium.api.util.inventory.Swap;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.player.fastbreak.FastBreak;
import net.minecraft.network.play.client.CPacketHeldItemChange;

public class ListenerSwap
extends ModuleListener<FastBreak, PacketEvent.Send<CPacketHeldItemChange>> {
    public ListenerSwap(FastBreak module) {
        super(module, PacketEvent.Send.class, CPacketHeldItemChange.class);
    }

    @Override
    public void call(PacketEvent.Send<CPacketHeldItemChange> event) {
        CPacketHeldItemChange packet = (CPacketHeldItemChange)event.getPacket();
        if (((FastBreak)this.module).pos != null) {
            if (Managers.SWITCH.getSlot() == packet.getSlotId()) {
                return;
            }
            if (((FastBreak)this.module).swap.getValue() == Swap.ALTERNATIVE) {
                ((FastBreak)this.module).softReset(true);
            }
        }
    }
}

