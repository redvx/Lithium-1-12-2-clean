/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketExplosion
 */
package me.chachoox.lithium.asm.mixins.network.server;

import net.minecraft.network.play.server.SPacketExplosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={SPacketExplosion.class})
public interface ISPacketExplosion {
    @Accessor(value="motionX")
    public float getMotionX();

    @Accessor(value="motionX")
    public void setMotionX(float var1);

    @Accessor(value="motionY")
    public float getMotionY();

    @Accessor(value="motionY")
    public void setMotionY(float var1);

    @Accessor(value="motionZ")
    public float getMotionZ();

    @Accessor(value="motionZ")
    public void setMotionZ(float var1);
}

