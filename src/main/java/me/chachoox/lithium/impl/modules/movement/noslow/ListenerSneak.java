/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 */
package me.chachoox.lithium.impl.modules.movement.noslow;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.movement.noslow.NoSlow;
import net.minecraft.network.play.client.CPacketEntityAction;

public class ListenerSneak
extends ModuleListener<NoSlow, PacketEvent.Send<CPacketEntityAction>> {
    public ListenerSneak(NoSlow module) {
        super(module, PacketEvent.Send.class, CPacketEntityAction.class);
    }

    @Override
    public void call(PacketEvent.Send<CPacketEntityAction> event) {
        if (((CPacketEntityAction)event.getPacket()).getAction() == CPacketEntityAction.Action.START_SNEAKING && ((NoSlow)this.module).doWeb()) {
            event.setCanceled(true);
        }
    }
}

