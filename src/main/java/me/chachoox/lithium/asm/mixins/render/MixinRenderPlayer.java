/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.AbstractClientPlayer
 *  net.minecraft.client.renderer.entity.RenderPlayer
 *  net.minecraft.util.ResourceLocation
 */
package me.chachoox.lithium.asm.mixins.render;

import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.misc.nameprotect.NameProtect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value={RenderPlayer.class})
public class MixinRenderPlayer {
    @Final
    @Shadow
    private boolean smallArms;

    @Overwrite
    public ResourceLocation getEntityTexture(AbstractClientPlayer entity) {
        NameProtect NAME_PROTECT = Managers.MODULE.get(NameProtect.class);
        if (NAME_PROTECT.isFakeSkin() && entity == Minecraft.getMinecraft().player) {
            if (this.smallArms) {
                return new ResourceLocation("textures/entity/alex.png");
            }
            return new ResourceLocation("textures/entity/steve.png");
        }
        return entity.getLocationSkin();
    }
}

