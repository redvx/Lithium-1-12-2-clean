/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 */
package me.chachoox.lithium.impl.event.events.entity;

import me.chachoox.lithium.api.event.events.Event;
import net.minecraft.entity.Entity;

public class EntityWorldEvent
extends Event {
    private final Entity entity;

    public Entity getEntity() {
        return this.entity;
    }

    public EntityWorldEvent(Entity entity) {
        this.entity = entity;
    }

    public static class Remove
    extends EntityWorldEvent {
        public Remove(Entity entity) {
            super(entity);
        }
    }

    public static class Add
    extends EntityWorldEvent {
        public Add(Entity entity) {
            super(entity);
        }
    }
}

