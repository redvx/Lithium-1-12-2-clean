/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketPlayerPosLook
 */
package me.chachoox.lithium.asm.mixins.network.server;

import net.minecraft.network.play.server.SPacketPlayerPosLook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={SPacketPlayerPosLook.class})
public interface ISPacketPlayerPosLook {
    @Accessor(value="yaw")
    public void setYaw(float var1);

    @Accessor(value="pitch")
    public void setPitch(float var1);
}

