/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiChat
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.text.ITextComponent
 *  org.lwjgl.input.Mouse
 */
package me.chachoox.lithium.asm.mixins.gui;

import me.chachoox.lithium.asm.mixins.gui.MixinGuiScreen;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.render.betterchat.BetterChat;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GuiChat.class})
public abstract class MixinGuiChat
extends MixinGuiScreen {
    private boolean loaded = false;

    @Inject(method={"initGui"}, at={@At(value="HEAD")})
    public void initGuiPatch(CallbackInfo callbackInfo) {
        if (Managers.MODULE.get(BetterChat.class).drawBlur()) {
            this.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
            this.loaded = true;
        }
    }

    @Inject(method={"onGuiClosed"}, at={@At(value="TAIL")})
    public void onGuiClosedPatch(CallbackInfo callbackInfo) {
        if (this.loaded) {
            this.mc.entityRenderer.stopUseShader();
            this.loaded = false;
        }
    }

    @Inject(method={"mouseClicked"}, at={@At(value="HEAD")}, cancellable=true)
    protected void mouseClickedHook(int mouseX, int mouseY, int mouseButton, CallbackInfo info) {
        ITextComponent tc;
        if ((mouseButton == 1 || mouseButton == 2) && this.handleClick(tc = this.mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY()), mouseButton)) {
            info.cancel();
        }
    }
}

