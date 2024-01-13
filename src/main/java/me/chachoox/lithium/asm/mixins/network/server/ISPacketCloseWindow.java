/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketCloseWindow
 */
package me.chachoox.lithium.asm.mixins.network.server;

import net.minecraft.network.play.server.SPacketCloseWindow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={SPacketCloseWindow.class})
public interface ISPacketCloseWindow {
    @Accessor(value="windowId")
    public int getWindowID();
}

