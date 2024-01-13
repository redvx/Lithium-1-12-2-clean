/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.entity.Render
 *  net.minecraft.client.renderer.entity.RenderDragon
 *  net.minecraft.client.renderer.entity.RenderEnderCrystal
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  org.lwjgl.opengl.GL11
 */
package me.chachoox.lithium.asm.mixins.render;

import me.chachoox.lithium.api.event.bus.instance.Bus;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.impl.event.events.render.model.CrystalModelRenderEvent;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.render.chams.Chams;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderDragon;
import net.minecraft.client.renderer.entity.RenderEnderCrystal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={RenderEnderCrystal.class})
public abstract class MixinRenderEnderCrystal
extends Render<EntityEnderCrystal>
implements Minecraftable {
    @Shadow
    @Final
    private static final ResourceLocation ENDER_CRYSTAL_TEXTURES = new ResourceLocation("textures/entity/endercrystal/endercrystal.png");
    @Shadow
    @Final
    private ModelBase modelEnderCrystal;
    @Shadow
    @Final
    private ModelBase modelEnderCrystalNoBase;

    protected MixinRenderEnderCrystal(RenderManager renderManager) {
        super(renderManager);
    }

    @Redirect(method={"doRender"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    public void renderModelBaseHook(ModelBase model, Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        Chams CHAMS = Managers.MODULE.get(Chams.class);
        if (CHAMS.isEnabled() && CHAMS.normal.getValue().booleanValue()) {
            GL11.glEnable((int)32823);
            GL11.glPolygonOffset((float)1.0f, (float)-1100000.0f);
        }
        CrystalModelRenderEvent.Pre event = new CrystalModelRenderEvent.Pre(model, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        Bus.EVENT_BUS.dispatch(event);
        if (event.isCanceled()) {
            Bus.EVENT_BUS.dispatch(new CrystalModelRenderEvent.Post(model, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale));
        } else {
            model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    @Overwrite
    public void doRender(EntityEnderCrystal entity, double x, double y, double z, float entityYaw, float partialTicks) {
        Chams CHAMS = Managers.MODULE.get(Chams.class);
        float defaultSpinSpeed = (float)entity.innerRotation + partialTicks;
        float spinTicks = (float)entity.innerRotation + partialTicks;
        float bounceSpeed = MathHelper.sin((float)(defaultSpinSpeed * 0.2f * ((Float)CHAMS.bounceSpeed.getValue()).floatValue())) / 2.0f + 0.5f;
        float scale = ((Float)CHAMS.scale.getValue()).floatValue();
        float spinSpeed = ((Float)CHAMS.spinSpeed.getValue()).floatValue();
        bounceSpeed = bounceSpeed * bounceSpeed + bounceSpeed;
        ModelBase model = entity.shouldShowBottom() ? this.modelEnderCrystal : this.modelEnderCrystalNoBase;
        GlStateManager.pushMatrix();
        GlStateManager.translate((double)x, (double)y, (double)z);
        this.bindTexture(ENDER_CRYSTAL_TEXTURES);
        float spinFactor = MathHelper.sin((float)(defaultSpinSpeed * 0.2f)) / 2.0f + 0.5f;
        spinFactor += spinFactor * spinFactor;
        if (CHAMS.isEnabled()) {
            if (!CHAMS.normal.getValue().booleanValue()) {
                GlStateManager.scale((float)scale, (float)scale, (float)scale);
                model.render((Entity)entity, 0.0f, spinTicks * 3.0f * spinSpeed, bounceSpeed * 0.2f, 0.0f, 0.0f, 0.0625f);
                if (CHAMS.crystalWires.getValue().booleanValue()) {
                    CHAMS.onWireframeModel(model, (Entity)entity, 0.0f, spinTicks * 3.0f * spinSpeed, bounceSpeed * 0.2f, 0.0f, 0.0f, 0.0625f);
                }
                GlStateManager.scale((float)(1.0f / scale), (float)(1.0f / scale), (float)(1.0f / scale));
            } else {
                GlStateManager.scale((float)scale, (float)scale, (float)scale);
                model.render((Entity)entity, 0.0f, spinTicks * 3.0f * spinSpeed, bounceSpeed * 0.2f, 0.0f, 0.0f, 0.0625f);
                GlStateManager.scale((float)(1.0f / scale), (float)(1.0f / scale), (float)(1.0f / scale));
            }
        } else {
            model.render((Entity)entity, 0.0f, defaultSpinSpeed * 3.0f, spinFactor * 0.2f, 0.0f, 0.0f, 0.0625f);
        }
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.popMatrix();
        BlockPos blockpos = entity.getBeamTarget();
        if (blockpos != null) {
            this.bindTexture(RenderDragon.ENDERCRYSTAL_BEAM_TEXTURES);
            float f2 = (float)blockpos.getX() + 0.5f;
            float f3 = (float)blockpos.getY() + 0.5f;
            float f4 = (float)blockpos.getZ() + 0.5f;
            double d0 = (double)f2 - entity.posX;
            double d1 = (double)f3 - entity.posY;
            double d2 = (double)f4 - entity.posZ;
            RenderDragon.renderCrystalBeams((double)(x + d0), (double)(y - 0.3 + (double)(spinFactor * 0.4f) + d1), (double)(z + d2), (float)partialTicks, (double)f2, (double)f3, (double)f4, (int)entity.innerRotation, (double)entity.posX, (double)entity.posY, (double)entity.posZ);
        }
    }
}

