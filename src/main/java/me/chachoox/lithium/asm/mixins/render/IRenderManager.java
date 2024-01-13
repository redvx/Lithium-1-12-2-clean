/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.entity.RenderManager
 */
package me.chachoox.lithium.asm.mixins.render;

import net.minecraft.client.renderer.entity.RenderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={RenderManager.class})
public interface IRenderManager {
    @Accessor(value="renderPosX")
    public double getRenderPosX();

    @Accessor(value="renderPosY")
    public double getRenderPosY();

    @Accessor(value="renderPosZ")
    public double getRenderPosZ();
}

