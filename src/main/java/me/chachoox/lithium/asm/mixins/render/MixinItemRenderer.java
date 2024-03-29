/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.AbstractClientPlayer
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.ItemRenderer
 *  net.minecraft.client.renderer.block.model.ItemCameraTransforms$TransformType
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.EnumHandSide
 *  net.minecraft.util.math.MathHelper
 *  org.lwjgl.opengl.GL11
 */
package me.chachoox.lithium.asm.mixins.render;

import me.chachoox.lithium.api.event.bus.instance.Bus;
import me.chachoox.lithium.impl.event.events.render.item.RenderFirstPersonEvent;
import me.chachoox.lithium.impl.event.events.render.item.RenderItemSideEvent;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.render.animations.Animations;
import me.chachoox.lithium.impl.modules.render.modelchanger.ModelChanger;
import me.chachoox.lithium.impl.modules.render.norender.NoRender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ItemRenderer.class})
public abstract class MixinItemRenderer {
    @Shadow
    public Minecraft mc;
    private boolean injection = true;

    @Shadow
    public abstract void renderItemInFirstPerson(AbstractClientPlayer var1, float var2, float var3, EnumHand var4, float var5, ItemStack var6, float var7);

    @Inject(method={"renderItemInFirstPerson(Lnet/minecraft/client/entity/AbstractClientPlayer;FFLnet/minecraft/util/EnumHand;FLnet/minecraft/item/ItemStack;F)V"}, at={@At(value="HEAD")}, cancellable=true)
    public void renderItemInFirstPersonHook(AbstractClientPlayer player, float p_187457_2_, float p_187457_3_, EnumHand hand, float p_187457_5_, ItemStack stack, float p_187457_7_, CallbackInfo info) {
        if (this.injection) {
            info.cancel();
            ModelChanger MODEL_CHANGER = Managers.MODULE.get(ModelChanger.class);
            float xOffset = MODEL_CHANGER.isEnabled() ? (hand == EnumHand.MAIN_HAND ? ((Float)MODEL_CHANGER.offsetMain.getValue()).floatValue() : ((Float)MODEL_CHANGER.offsetOff.getValue()).floatValue()) : 0.0f;
            this.injection = false;
            this.renderItemInFirstPerson(player, p_187457_2_, p_187457_3_, hand, p_187457_5_ + xOffset, stack, p_187457_7_);
            this.injection = true;
        }
    }

    @Redirect(method={"updateEquippedItem"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/entity/EntityPlayerSP;getCooledAttackStrength(F)F"))
    public float oldSwingHook(EntityPlayerSP instance, float v) {
        if (Managers.MODULE.get(Animations.class).isOldSwing()) {
            return 1.0f;
        }
        return instance.getCooledAttackStrength(1.0f);
    }

    @Overwrite
    private void transformEatFirstPerson(float p_187454_1_, EnumHandSide hand, ItemStack stack) {
        float f = (float)this.mc.player.getItemInUseCount() - p_187454_1_ + 1.0f;
        float f1 = f / (float)stack.getMaxItemUseDuration();
        Animations ANIMATIONS = Managers.MODULE.get(Animations.class);
        if (f1 < 0.8f) {
            float eat = ANIMATIONS.isEnabled() ? ANIMATIONS.getEatingSpeed() : 4.0f;
            float height = ANIMATIONS.isEnabled() ? ANIMATIONS.getEatHeight() : 0.1f;
            float f2 = MathHelper.abs((float)(MathHelper.cos((float)(f / eat * (float)Math.PI)) * height));
            GlStateManager.translate((float)0.0f, (float)f2, (float)0.0f);
        }
        double animation = ANIMATIONS.isEnabled() ? ANIMATIONS.getAnimationSpeed() : 27.0;
        float f3 = 1.0f - (float)Math.pow(f1, animation);
        int i = hand == EnumHandSide.RIGHT ? 1 : -1;
        GlStateManager.translate((float)(f3 * 0.6f * (float)i), (float)(f3 * -0.5f), (float)(f3 * 0.0f));
        GlStateManager.rotate((float)((float)i * f3 * 90.0f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)(f3 * 10.0f), (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.rotate((float)((float)i * f3 * 30.0f), (float)0.0f, (float)0.0f, (float)1.0f);
    }

    @Inject(method={"transformFirstPerson"}, at={@At(value="TAIL")})
    public void transformFirstPersonTail(EnumHandSide hand, float f, CallbackInfo callbackInfo) {
        RenderFirstPersonEvent.Post eventRenderPostFirstPerson = new RenderFirstPersonEvent.Post(hand);
        Bus.EVENT_BUS.dispatch(eventRenderPostFirstPerson);
    }

    @Inject(method={"renderFireInFirstPerson"}, at={@At(value="HEAD")}, cancellable=true)
    public void renderFireInFirstPersonHook(CallbackInfo info) {
        if (Managers.MODULE.get(NoRender.class).getFire()) {
            info.cancel();
        }
    }

    @Inject(method={"transformFirstPerson"}, at={@At(value="HEAD")})
    public void transformFirstPersonHead(EnumHandSide hand, float f, CallbackInfo callbackInfo) {
        RenderFirstPersonEvent.Pre eventRenderPreFirstPerson = new RenderFirstPersonEvent.Pre(hand);
        Bus.EVENT_BUS.dispatch(eventRenderPreFirstPerson);
    }

    @Inject(method={"renderItemInFirstPerson(Lnet/minecraft/client/entity/AbstractClientPlayer;FFLnet/minecraft/util/EnumHand;FLnet/minecraft/item/ItemStack;F)V"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/renderer/GlStateManager;pushMatrix()V", shift=At.Shift.AFTER)})
    public void pushMatrixHook(CallbackInfo info) {
        if (Managers.MODULE.get(ModelChanger.class).isEnabled()) {
            GL11.glRotatef((float)(((Float)Managers.MODULE.get(ModelChanger.class).angleTranslate.getValue()).floatValue() * 2.0f), (float)1.0f, (float)1.0f, (float)1.0f);
        }
    }

    @Redirect(method={"renderOverlays"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/entity/EntityPlayerSP;isEntityInsideOpaqueBlock()Z"))
    public boolean renderSuffocationOverlay(EntityPlayerSP instance) {
        if (Managers.MODULE.get(NoRender.class).getSuffocation()) {
            return false;
        }
        return instance.isEntityInsideOpaqueBlock();
    }

    @Inject(method={"renderItemSide"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/renderer/GlStateManager;pushMatrix()V", shift=At.Shift.AFTER)})
    public void renderItemSideInvoke(EntityLivingBase entityLivingBase, ItemStack itemStack, ItemCameraTransforms.TransformType transformType, boolean hand, CallbackInfo callbackInfo) {
        if (Managers.MODULE.get(ModelChanger.class).isEnabled() && this.mc.player != null && entityLivingBase == this.mc.player && (transformType == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND || transformType == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND)) {
            RenderItemSideEvent eventRenderItemSide = new RenderItemSideEvent();
            Bus.EVENT_BUS.dispatch(eventRenderItemSide);
            GlStateManager.scale((float)eventRenderItemSide.getX(), (float)eventRenderItemSide.getY(), (float)eventRenderItemSide.getZ());
        }
    }

    @Inject(method={"rotateArm"}, at={@At(value="HEAD")}, cancellable=true)
    public void rotateArmHook(float p_187458_1_, CallbackInfo ci) {
        if (Managers.MODULE.get(ModelChanger.class).isEnabled() && Managers.MODULE.get(ModelChanger.class).noSway.getValue().booleanValue()) {
            ci.cancel();
        }
    }
}

