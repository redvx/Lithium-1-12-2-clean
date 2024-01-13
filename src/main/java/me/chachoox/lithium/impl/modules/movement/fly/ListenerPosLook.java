/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketPlayerPosLook
 */
package me.chachoox.lithium.impl.modules.movement.fly;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.movement.fly.Fly;
import net.minecraft.network.play.server.SPacketPlayerPosLook;

public class ListenerPosLook
extends ModuleListener<Fly, PacketEvent.Receive<SPacketPlayerPosLook>> {
    public ListenerPosLook(Fly module) {
        super(module, PacketEvent.Receive.class, SPacketPlayerPosLook.class);
    }

    @Override
    public void call(PacketEvent.Receive<SPacketPlayerPosLook> event) {
        if (((Fly)this.module).autoDisable.getValue().booleanValue()) {
            ((Fly)this.module).disable();
        }
    }
}

