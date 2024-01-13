/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.ActiveRenderInfo
 */
package me.chachoox.lithium.asm.mixins.client;

import java.nio.FloatBuffer;
import net.minecraft.client.renderer.ActiveRenderInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={ActiveRenderInfo.class})
public interface IActiveRenderInfo {
    @Accessor(value="MODELVIEW")
    public static FloatBuffer getViewport() {
        throw new IllegalStateException();
    }

    @Accessor(value="PROJECTION")
    public static FloatBuffer getProjection() {
        throw new IllegalStateException();
    }

    @Accessor(value="MODELVIEW")
    public static FloatBuffer getModelview() {
        throw new IllegalStateException();
    }
}

