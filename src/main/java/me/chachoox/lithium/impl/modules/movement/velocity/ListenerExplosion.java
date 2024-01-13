/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketExplosion
 */
package me.chachoox.lithium.impl.modules.movement.velocity;

import me.chachoox.lithium.asm.mixins.network.server.ISPacketExplosion;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.movement.velocity.Velocity;
import net.minecraft.network.play.server.SPacketExplosion;

public class ListenerExplosion
extends ModuleListener<Velocity, PacketEvent.Receive<SPacketExplosion>> {
    public ListenerExplosion(Velocity module) {
        super(module, PacketEvent.Receive.class, SPacketExplosion.class);
    }

    @Override
    public void call(PacketEvent.Receive<SPacketExplosion> event) {
        SPacketExplosion packet = (SPacketExplosion)event.getPacket();
        if (ListenerExplosion.mc.player != null) {
            if ((Integer)((Velocity)this.module).horizontal.getValue() != 0 && (Integer)((Velocity)this.module).vertical.getValue() != 0) {
                ((ISPacketExplosion)packet).setMotionX(packet.getMotionX() * (float)((Integer)((Velocity)this.module).horizontal.getValue()).intValue() / 100.0f);
                ((ISPacketExplosion)packet).setMotionY(packet.getMotionY() * (float)((Integer)((Velocity)this.module).vertical.getValue()).intValue() / 100.0f);
                ((ISPacketExplosion)packet).setMotionZ(packet.getMotionZ() * (float)((Integer)((Velocity)this.module).horizontal.getValue()).intValue() / 100.0f);
            } else {
                event.setCanceled(true);
            }
        }
    }
}

