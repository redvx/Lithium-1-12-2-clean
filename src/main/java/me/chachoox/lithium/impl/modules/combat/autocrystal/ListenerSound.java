/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.init.SoundEvents
 *  net.minecraft.network.play.server.SPacketSoundEffect
 *  net.minecraft.util.SoundCategory
 */
package me.chachoox.lithium.impl.modules.combat.autocrystal;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.combat.autocrystal.AutoCrystal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.SoundCategory;

public class ListenerSound
extends ModuleListener<AutoCrystal, PacketEvent.Receive<SPacketSoundEffect>> {
    public ListenerSound(AutoCrystal module) {
        super(module, PacketEvent.Receive.class, SPacketSoundEffect.class);
    }

    @Override
    public void call(PacketEvent.Receive<SPacketSoundEffect> event) {
        SPacketSoundEffect packet = (SPacketSoundEffect)event.getPacket();
        if (packet.getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
            for (Entity entity : ListenerSound.mc.world.loadedEntityList) {
                if (!(entity instanceof EntityEnderCrystal) || !(entity.getDistanceSq(packet.getX(), packet.getY(), packet.getZ()) < 36.0)) continue;
                entity.setDead();
                ListenerSound.mc.world.removeEntity(entity);
            }
        }
    }
}

