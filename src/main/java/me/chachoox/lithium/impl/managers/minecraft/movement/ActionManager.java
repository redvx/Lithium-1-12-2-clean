/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketEntityAction
 */
package me.chachoox.lithium.impl.managers.minecraft.movement;

import me.chachoox.lithium.api.event.bus.Listener;
import me.chachoox.lithium.api.event.bus.SubscriberImpl;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import net.minecraft.network.play.client.CPacketEntityAction;

public class ActionManager
extends SubscriberImpl {
    private volatile boolean sneaking;
    private volatile boolean sprinting;

    public ActionManager() {
        this.listeners.add(new Listener<PacketEvent.Post<CPacketEntityAction>>(PacketEvent.Post.class, CPacketEntityAction.class){

            @Override
            public void call(PacketEvent.Post<CPacketEntityAction> event) {
                switch (((CPacketEntityAction)event.getPacket()).getAction()) {
                    case START_SPRINTING: {
                        ActionManager.this.sprinting = true;
                        break;
                    }
                    case STOP_SPRINTING: {
                        ActionManager.this.sprinting = false;
                        break;
                    }
                    case START_SNEAKING: {
                        ActionManager.this.sneaking = true;
                        break;
                    }
                    case STOP_SNEAKING: {
                        ActionManager.this.sneaking = false;
                        break;
                    }
                }
            }
        });
    }

    public boolean isSprinting() {
        return this.sprinting;
    }

    public boolean isSneaking() {
        return this.sneaking;
    }
}

