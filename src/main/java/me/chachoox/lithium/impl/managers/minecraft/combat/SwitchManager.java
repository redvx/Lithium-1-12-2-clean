/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 */
package me.chachoox.lithium.impl.managers.minecraft.combat;

import me.chachoox.lithium.api.event.bus.Listener;
import me.chachoox.lithium.api.event.bus.SubscriberImpl;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import net.minecraft.network.play.client.CPacketHeldItemChange;

public class SwitchManager
extends SubscriberImpl
implements Minecraftable {
    private final StopWatch timer = new StopWatch();
    private int slot = -1;

    public SwitchManager() {
        this.listeners.add(new Listener<PacketEvent.Send<CPacketHeldItemChange>>(PacketEvent.Send.class, Integer.MIN_VALUE, CPacketHeldItemChange.class){

            @Override
            public void call(PacketEvent.Send<CPacketHeldItemChange> event) {
                CPacketHeldItemChange packet = (CPacketHeldItemChange)event.getPacket();
                int slotId = packet.getSlotId();
                if (SwitchManager.this.slot == -1) {
                    SwitchManager.this.slot = Minecraftable.mc.player.inventory.currentItem;
                }
                if (SwitchManager.this.slot != slotId) {
                    SwitchManager.this.slot = slotId;
                }
                SwitchManager.this.timer.reset();
            }
        });
    }

    public boolean passed(long time) {
        return this.timer.passed(time);
    }

    public int getSlot() {
        return this.slot;
    }
}

