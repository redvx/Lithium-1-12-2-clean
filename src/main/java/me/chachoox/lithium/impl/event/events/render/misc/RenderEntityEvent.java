/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.culling.ICamera
 *  net.minecraft.entity.Entity
 */
package me.chachoox.lithium.impl.event.events.render.misc;

import me.chachoox.lithium.api.event.events.Event;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.Entity;

public class RenderEntityEvent
extends Event {
    private final Entity entity;

    public RenderEntityEvent(Entity entityIn, ICamera camera, double camX, double camY, double camZ) {
        this.entity = entityIn;
    }

    public Entity getEntity() {
        return this.entity;
    }
}

