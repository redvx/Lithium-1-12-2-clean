/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.managers.minecraft;

import me.chachoox.lithium.api.event.bus.Listener;
import me.chachoox.lithium.api.event.bus.SubscriberImpl;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.impl.event.events.movement.MotionUpdateEvent;

public class RotationManager
extends SubscriberImpl
implements Minecraftable {
    private float yaw;
    private float pitch;
    private boolean rotated;
    private int ticksSinceNoRotate;
    private double x;
    private double y;
    private double z;
    private boolean onGround;

    public RotationManager() {
        this.listeners.add(new Listener<MotionUpdateEvent>(MotionUpdateEvent.class, Integer.MAX_VALUE){

            @Override
            public void call(MotionUpdateEvent event) {
                switch (event.getStage()) {
                    case PRE: {
                        RotationManager.this.x = Minecraftable.mc.player.posX;
                        RotationManager.this.y = Minecraftable.mc.player.posY;
                        RotationManager.this.z = Minecraftable.mc.player.posZ;
                        RotationManager.this.onGround = Minecraftable.mc.player.onGround;
                        RotationManager.this.yaw = Minecraftable.mc.player.rotationYaw;
                        RotationManager.this.pitch = Minecraftable.mc.player.rotationPitch;
                        break;
                    }
                    case POST: {
                        Minecraftable.mc.player.posX = RotationManager.this.x;
                        Minecraftable.mc.player.posY = RotationManager.this.y;
                        Minecraftable.mc.player.posZ = RotationManager.this.z;
                        Minecraftable.mc.player.onGround = RotationManager.this.onGround;
                        RotationManager.this.ticksSinceNoRotate++;
                        if (RotationManager.this.ticksSinceNoRotate > 2) {
                            RotationManager.this.rotated = false;
                        }
                        Minecraftable.mc.player.rotationYaw = RotationManager.this.yaw;
                        Minecraftable.mc.player.rotationYawHead = RotationManager.this.yaw;
                        Minecraftable.mc.player.rotationPitch = RotationManager.this.pitch;
                    }
                }
            }
        });
    }

    public void setRotations(float yaw, float pitch) {
        this.rotated = true;
        this.ticksSinceNoRotate = 0;
        RotationManager.mc.player.rotationYaw = yaw;
        RotationManager.mc.player.rotationYawHead = yaw;
        RotationManager.mc.player.rotationPitch = pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public float getYaw() {
        return this.yaw;
    }

    public boolean isRotated() {
        return this.rotated;
    }
}

