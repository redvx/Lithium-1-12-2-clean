/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.MoverType
 */
package me.chachoox.lithium.impl.event.events.movement.actions;

import me.chachoox.lithium.api.event.events.Event;
import net.minecraft.entity.MoverType;

public class MoveEvent
extends Event {
    private final MoverType type;
    private double x;
    private double y;
    private double z;
    private boolean sneaking;

    public MoveEvent(MoverType type, double x, double y, double z, boolean sneaking) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
        this.sneaking = sneaking;
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public MoverType getType() {
        return this.type;
    }

    public boolean isSneaking() {
        return this.sneaking;
    }

    public void setSneaking(boolean sneaking) {
        this.sneaking = sneaking;
    }
}

