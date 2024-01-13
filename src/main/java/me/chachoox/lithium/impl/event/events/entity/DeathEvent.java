/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EntityLivingBase
 */
package me.chachoox.lithium.impl.event.events.entity;

import net.minecraft.entity.EntityLivingBase;

public class DeathEvent {
    private final EntityLivingBase entity;

    public DeathEvent(EntityLivingBase entity) {
        this.entity = entity;
    }

    public EntityLivingBase getEntity() {
        return this.entity;
    }
}

