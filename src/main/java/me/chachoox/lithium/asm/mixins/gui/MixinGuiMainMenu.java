/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiMainMenu
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GL20
 */
package me.chachoox.lithium.asm.mixins.gui;

import me.chachoox.lithium.api.util.render.Render2DUtil;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.render.displaytweaks.DisplayTweaks;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GuiMainMenu.class})
public abstract class MixinGuiMainMenu
extends GuiScreen {
    private long initTime;

    @Shadow
    protected abstract void renderSkybox(int var1, int var2, float var3);

    @Inject(method={"initGui"}, at={@At(value="RETURN")}, cancellable=true)
    public void initGuiHook(CallbackInfo info) {
        this.initTime = System.currentTimeMillis();
    }

    @Inject(method={"drawScreen"}, at={@At(value="RETURN")})
    public void drawScreenHook0(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        String text = "Logged in as " + this.mc.getSession().getUsername();
        int textWidth = this.mc.fontRenderer.getStringWidth(text);
        int j = this.height / 4 + 48;
        this.mc.fontRenderer.drawString(String.format("%s%s", Managers.MODULE.get(DisplayTweaks.class).getAccountTextColor(), text), (float)this.width / 2.0f - (float)textWidth / 2.0f - 2.0f, (float)(j + 72 + Managers.MODULE.get(DisplayTweaks.class).getAccountMessagePosY()), -1, true);
    }

    @Redirect(method={"drawScreen"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/gui/GuiMainMenu;renderSkybox(IIF)V"))
    private void drawScreenHook1(GuiMainMenu guiMainMenu, int mouseX, int mouseY, float partialTicks) {
        if (!Managers.MODULE.get(DisplayTweaks.class).isCustomTitle()) {
            this.renderSkybox(mouseX, mouseY, partialTicks);
        }
    }

    @Redirect(method={"drawScreen"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/gui/GuiMainMenu;drawGradientRect(IIIIII)V", ordinal=0))
    private void drawScreenHook2(GuiMainMenu guiMainMenu, int left, int top, int right, int bottom, int startColor, int endColor) {
        if (!Managers.MODULE.get(DisplayTweaks.class).isCustomTitle()) {
            this.drawGradientRect(left, top, right, bottom, startColor, endColor);
        }
    }

    @Redirect(method={"drawScreen"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/gui/GuiMainMenu;drawGradientRect(IIIIII)V", ordinal=1))
    private void drawScreenHook3(GuiMainMenu guiMainMenu, int left, int top, int right, int bottom, int startColor, int endColor) {
        if (!Managers.MODULE.get(DisplayTweaks.class).isCustomTitle()) {
            this.drawGradientRect(left, top, right, bottom, startColor, endColor);
        }
    }

    @Inject(method={"drawScreen"}, at={@At(value="HEAD")}, cancellable=true)
    public void drawScreenShader(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        DisplayTweaks DISPLAY_TWEAKS = Managers.MODULE.get(DisplayTweaks.class);
        if (DISPLAY_TWEAKS.isShader() && DISPLAY_TWEAKS.GLSL_SHADER != null) {
            GlStateManager.disableCull();
            DISPLAY_TWEAKS.GLSL_SHADER.useShader(this.width * 2, this.height * 2, mouseX * 2, mouseY * 2, (float)(System.currentTimeMillis() - this.initTime) / 1000.0f);
            GL11.glBegin((int)7);
            GL11.glVertex2f((float)-1.0f, (float)-1.0f);
            GL11.glVertex2f((float)-1.0f, (float)1.0f);
            GL11.glVertex2f((float)1.0f, (float)1.0f);
            GL11.glVertex2f((float)1.0f, (float)-1.0f);
            GL11.glEnd();
            GL20.glUseProgram((int)0);
        } else if (DISPLAY_TWEAKS.isImage()) {
            this.mc.getTextureManager().bindTexture(new ResourceLocation(DISPLAY_TWEAKS.getImage()));
            MixinGuiMainMenu.drawModalRectWithCustomSizedTexture((int)0, (int)0, (float)0.0f, (float)0.0f, (int)this.width, (int)this.height, (float)this.width, (float)this.height);
        } else if (DISPLAY_TWEAKS.isColor()) {
            Render2DUtil.drawGradientRect(-1.0f, -1.0f, this.width, this.height, false, DISPLAY_TWEAKS.getTitleColorOne().getRGB(), DISPLAY_TWEAKS.getTitleColorTwo().getRGB());
        }
    }
}

