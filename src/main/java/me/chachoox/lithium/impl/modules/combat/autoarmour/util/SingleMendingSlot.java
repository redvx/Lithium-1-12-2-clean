/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.inventory.EntityEquipmentSlot
 */
package me.chachoox.lithium.impl.modules.combat.autoarmour.util;

import net.minecraft.inventory.EntityEquipmentSlot;

public class SingleMendingSlot {
    private final EntityEquipmentSlot slot;
    private boolean blocked;

    public SingleMendingSlot(EntityEquipmentSlot slot) {
        this.slot = slot;
    }

    public EntityEquipmentSlot getSlot() {
        return this.slot;
    }

    public boolean isBlocked() {
        return this.blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}

