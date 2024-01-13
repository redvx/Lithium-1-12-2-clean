/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.network.play.server.SPacketExplosion
 */
package me.chachoox.lithium.impl.modules.combat.autocrystal;

import java.util.ArrayList;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.combat.autocrystal.AutoCrystal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.network.play.server.SPacketExplosion;

public class ListenerExplosion
extends ModuleListener<AutoCrystal, PacketEvent.Receive<SPacketExplosion>> {
    public ListenerExplosion(AutoCrystal module) {
        super(module, PacketEvent.Receive.class, SPacketExplosion.class);
    }

    @Override
    public void call(PacketEvent.Receive<SPacketExplosion> event) {
        SPacketExplosion packet = (SPacketExplosion)event.getPacket();
        if (packet.getStrength() == 6.0f) {
            for (Entity entity : new ArrayList(ListenerExplosion.mc.world.loadedEntityList)) {
                if (!((AutoCrystal)this.module).attackMap.containsKey(entity.getEntityId())) {
                    return;
                }
                if (!(entity instanceof EntityEnderCrystal) || !(entity.getDistance(packet.getX(), packet.getY(), packet.getZ()) <= 6.0)) continue;
                mc.addScheduledTask(() -> ((Entity)entity).setDead());
                mc.addScheduledTask(() -> ListenerExplosion.mc.world.removeEntity(entity));
            }
        }
    }
}

