/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketEntityVelocity
 */
package me.chachoox.lithium.impl.modules.movement.velocity;

import me.chachoox.lithium.asm.mixins.network.server.ISPacketEntityVelocity;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.movement.velocity.Velocity;
import net.minecraft.network.play.server.SPacketEntityVelocity;

public class ListenerVelocity
extends ModuleListener<Velocity, PacketEvent.Receive<SPacketEntityVelocity>> {
    public ListenerVelocity(Velocity module) {
        super(module, PacketEvent.Receive.class, SPacketEntityVelocity.class);
    }

    @Override
    public void call(PacketEvent.Receive<SPacketEntityVelocity> event) {
        SPacketEntityVelocity packet = (SPacketEntityVelocity)event.getPacket();
        if (ListenerVelocity.mc.player != null) {
            if (packet.getEntityID() == ListenerVelocity.mc.player.getEntityId() && (Integer)((Velocity)this.module).horizontal.getValue() != 0 && (Integer)((Velocity)this.module).horizontal.getValue() != 0) {
                ((ISPacketEntityVelocity)packet).setX(packet.getMotionX() * (Integer)((Velocity)this.module).horizontal.getValue() / 100);
                ((ISPacketEntityVelocity)packet).setY(packet.getMotionY() * (Integer)((Velocity)this.module).vertical.getValue() / 100);
                ((ISPacketEntityVelocity)packet).setZ(packet.getMotionZ() * (Integer)((Velocity)this.module).horizontal.getValue() / 100);
            } else if (packet.getEntityID() == ListenerVelocity.mc.player.getEntityId()) {
                event.setCanceled(true);
            }
        }
    }
}

