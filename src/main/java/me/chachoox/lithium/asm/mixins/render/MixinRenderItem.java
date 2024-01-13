/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.RenderItem
 */
package me.chachoox.lithium.asm.mixins.render;

import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.render.glintmodify.GlintModify;
import net.minecraft.client.renderer.RenderItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(value={RenderItem.class})
public abstract class MixinRenderItem {
    @ModifyArg(method={"renderEffect"}, at=@At(value="INVOKE", target="net/minecraft/client/renderer/RenderItem.renderModel(Lnet/minecraft/client/renderer/block/model/IBakedModel;I)V"))
    private int renderEffect(int glintVal) {
        GlintModify GLINT_MODIFY = Managers.MODULE.get(GlintModify.class);
        return GLINT_MODIFY.isEnabled() ? GLINT_MODIFY.getEnchantColor().getRGB() : glintVal;
    }

    @ModifyArgs(method={"renderEffect"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/renderer/GlStateManager;scale(FFF)V"))
    public void scaleArgsHook(Args args) {
        GlintModify GLINT_MODIFY = Managers.MODULE.get(GlintModify.class);
        float scale = GLINT_MODIFY.getGlintScale();
        if (GLINT_MODIFY.isEnabled() && scale != 0.0f) {
            args.set(0, Float.valueOf(scale));
            args.set(1, Float.valueOf(scale));
            args.set(2, Float.valueOf(scale));
        }
    }

    @ModifyArgs(method={"renderEffect"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/renderer/GlStateManager;translate(FFF)V"))
    public void translateHook(Args args) {
        GlintModify GLINT_MODIFY = Managers.MODULE.get(GlintModify.class);
        if (GLINT_MODIFY.isEnabled()) {
            args.set(0, Float.valueOf(((Float)args.get(0)).floatValue() * GLINT_MODIFY.getFactor()));
        }
    }

    @ModifyArgs(method={"renderEffect"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/renderer/GlStateManager;rotate(FFFF)V"))
    public void rotateHook(Args args) {
        GlintModify GLINT_MODIFY = Managers.MODULE.get(GlintModify.class);
        if (GLINT_MODIFY.isEnabled()) {
            args.set(0, Float.valueOf(((Float)args.get(0)).floatValue() * GLINT_MODIFY.getGlintRotate()));
        }
    }
}

