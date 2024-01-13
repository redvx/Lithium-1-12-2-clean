/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.effect.EntityLightningBolt
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.SoundEvents
 *  net.minecraft.util.SoundCategory
 *  net.minecraft.world.World
 */
package me.chachoox.lithium.impl.modules.render.killeffect;

import me.chachoox.lithium.impl.event.events.entity.DeathEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.render.killeffect.KillEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ListenerDeath
extends ModuleListener<KillEffect, DeathEvent> {
    public ListenerDeath(KillEffect module) {
        super(module, DeathEvent.class);
    }

    @Override
    public void call(DeathEvent event) {
        if (event.getEntity() instanceof EntityPlayer && event.getEntity() != ListenerDeath.mc.player) {
            EntityLightningBolt bolt = new EntityLightningBolt((World)ListenerDeath.mc.world, event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, false);
            if (((KillEffect)this.module).sound.getValue().booleanValue()) {
                ListenerDeath.mc.world.playSound(event.getEntity().getPosition(), SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.WEATHER, 1.0f, 1.0f, false);
            }
            bolt.setLocationAndAngles(event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, ListenerDeath.mc.player.rotationYaw, ListenerDeath.mc.player.rotationPitch);
            ListenerDeath.mc.world.spawnEntity((Entity)bolt);
        }
    }
}

