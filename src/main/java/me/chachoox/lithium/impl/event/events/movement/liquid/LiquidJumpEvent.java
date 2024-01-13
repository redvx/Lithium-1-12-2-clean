/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EntityLivingBase
 */
package me.chachoox.lithium.impl.event.events.movement.liquid;

import me.chachoox.lithium.api.event.events.Event;
import net.minecraft.entity.EntityLivingBase;

public class LiquidJumpEvent
extends Event {
    private final EntityLivingBase entity;

    public LiquidJumpEvent(EntityLivingBase entity) {
        this.entity = entity;
    }

    public EntityLivingBase getEntity() {
        return this.entity;
    }
}

