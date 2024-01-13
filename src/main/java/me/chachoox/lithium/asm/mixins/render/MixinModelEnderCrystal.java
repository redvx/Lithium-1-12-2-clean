/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.ModelEnderCrystal
 *  net.minecraft.client.renderer.GlStateManager
 */
package me.chachoox.lithium.asm.mixins.render;

import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.render.chams.Chams;
import net.minecraft.client.model.ModelEnderCrystal;
import net.minecraft.client.renderer.GlStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={ModelEnderCrystal.class})
public abstract class MixinModelEnderCrystal {
    @Redirect(method={"render"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/renderer/GlStateManager;scale(FFF)V"))
    public void render(float x, float y, float z) {
        Chams CHAMS = Managers.MODULE.get(Chams.class);
        float crystalScale = ((Float)CHAMS.smallScale.getValue()).floatValue();
        if (CHAMS.isEnabled()) {
            GlStateManager.scale((float)(x + crystalScale), (float)(y + crystalScale), (float)(z + crystalScale));
        } else {
            GlStateManager.scale((float)x, (float)y, (float)z);
        }
    }
}

