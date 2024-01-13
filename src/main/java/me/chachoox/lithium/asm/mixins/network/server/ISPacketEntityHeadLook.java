/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketEntityHeadLook
 */
package me.chachoox.lithium.asm.mixins.network.server;

import net.minecraft.network.play.server.SPacketEntityHeadLook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={SPacketEntityHeadLook.class})
public interface ISPacketEntityHeadLook {
    @Accessor(value="entityId")
    public int getEntityId();
}

