/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketPlayer
 */
package me.chachoox.lithium.asm.mixins.network.client;

import net.minecraft.network.play.client.CPacketPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={CPacketPlayer.class})
public interface ICPacketPlayer {
    @Accessor(value="yaw")
    public float getYaw();

    @Accessor(value="yaw")
    public void setYaw(float var1);

    @Accessor(value="pitch")
    public float getPitch();

    @Accessor(value="pitch")
    public void setPitch(float var1);

    @Accessor(value="x")
    public void setX(double var1);

    @Accessor(value="x")
    public double getX();

    @Accessor(value="y")
    public void setY(double var1);

    @Accessor(value="y")
    public double getY();

    @Accessor(value="z")
    public void setZ(double var1);

    @Accessor(value="z")
    public double getZ();

    @Accessor(value="onGround")
    public void setOnGround(boolean var1);

    @Accessor(value="rotating")
    public boolean isRotating();

    @Accessor(value="moving")
    public boolean isMoving();
}

