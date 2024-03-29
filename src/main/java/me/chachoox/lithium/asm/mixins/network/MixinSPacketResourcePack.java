/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.PacketBuffer
 *  net.minecraft.network.play.server.SPacketResourcePackSend
 */
package me.chachoox.lithium.asm.mixins.network;

import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.render.norender.NoRender;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketResourcePackSend;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={SPacketResourcePackSend.class})
public abstract class MixinSPacketResourcePack {
    @Inject(method={"readPacketData"}, at={@At(value="HEAD")}, cancellable=true)
    public void readPacketDataHook(PacketBuffer buf, CallbackInfo ci) {
        if (Managers.MODULE.get(NoRender.class).isEnabled() && Managers.MODULE.get(NoRender.class).getAntiResources()) {
            buf.readerIndex(buf.writerIndex());
            ci.cancel();
        }
    }
}

