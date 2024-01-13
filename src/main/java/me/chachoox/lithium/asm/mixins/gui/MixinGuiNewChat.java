/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.ChatLine
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.GuiNewChat
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.player.EntityPlayer$EnumChatVisibility
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.text.ITextComponent
 */
package me.chachoox.lithium.asm.mixins.gui;

import java.util.List;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.api.util.text.TextUtil;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.other.font.CustomFont;
import me.chachoox.lithium.impl.modules.render.betterchat.BetterChat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GuiNewChat.class})
public abstract class MixinGuiNewChat
extends Gui {
    @Shadow
    public List<ChatLine> drawnChatLines;
    @Shadow
    public int scrollPos;
    @Shadow
    public Minecraft mc;
    @Shadow
    public boolean isScrolled;

    @Shadow
    public abstract boolean getChatOpen();

    @Shadow
    public abstract int getLineCount();

    @Shadow
    public abstract float getChatScale();

    @Shadow
    public abstract int getChatWidth();

    @ModifyConstant(method={"setChatLine"}, constant={@Constant(intValue=100)})
    public int setChatLineHook(int line) {
        return Managers.MODULE.get(BetterChat.class).isInfinite() ? Integer.MAX_VALUE : line;
    }

    @ModifyVariable(method={"setChatLine"}, at=@At(value="HEAD"), ordinal=0)
    private int setChatLineHook2(int line) {
        if (line == Logger.PERMANENT_ID) {
            line = 0;
        }
        return line;
    }

    @Inject(method={"printChatMessageWithOptionalDeletion"}, at={@At(value="INVOKE", target="net/minecraft/client/gui/GuiNewChat.setChatLine(Lnet/minecraft/util/text/ITextComponent;IIZ)V", shift=At.Shift.AFTER)}, cancellable=true)
    private void printChatHook(ITextComponent component, int line, CallbackInfo callbackInfo) {
        if (line == -2147442069 || line == Logger.CUSTOM_ID || line == Logger.PERMANENT_ID) {
            callbackInfo.cancel();
        }
    }

    @Overwrite
    public void drawChat(int updateCounter) {
        if (this.mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN) {
            int i = this.getLineCount();
            int j = this.drawnChatLines.size();
            float f = this.mc.gameSettings.chatOpacity * 0.9f + 0.1f;
            if (j > 0) {
                float rectAlpha;
                boolean flag = false;
                if (this.getChatOpen()) {
                    flag = true;
                }
                float f1 = this.getChatScale();
                int k = MathHelper.ceil((float)((float)this.getChatWidth() / f1));
                GlStateManager.pushMatrix();
                GlStateManager.translate((float)2.0f, (float)8.0f, (float)0.0f);
                GlStateManager.scale((float)f1, (float)f1, (float)1.0f);
                BetterChat BETTER_CHAT = Managers.MODULE.get(BetterChat.class);
                float f2 = rectAlpha = BETTER_CHAT.isEnabled() ? BETTER_CHAT.getRectAlpha() : 1.0f;
                if (rectAlpha > 0.0f) {
                    int height = 0;
                    for (int i3 = 0; i3 + this.scrollPos < this.drawnChatLines.size() && i3 < i; ++i3) {
                        double denormalizeClamp;
                        ChatLine chatLine = this.drawnChatLines.get(i3 + this.scrollPos);
                        if (chatLine == null || !(255.0 * ((denormalizeClamp = MathHelper.clamp((double)((1.0 - (double)(updateCounter - chatLine.getUpdatedCounter()) / 200.0) * 10.0), (double)0.0, (double)1.0)) * denormalizeClamp) >= 255.0) && !flag) continue;
                        height -= this.mc.fontRenderer.FONT_HEIGHT;
                    }
                    Gui.drawRect((int)-2, (int)0, (int)(k + 4), (int)height, (int)((int)(127.0f * rectAlpha) << 24));
                }
                int l = 0;
                for (int i1 = 0; i1 + this.scrollPos < this.drawnChatLines.size() && i1 < i; ++i1) {
                    CustomFont CUSTOM_FONT;
                    int j1;
                    ChatLine chatline = this.drawnChatLines.get(i1 + this.scrollPos);
                    if (chatline == null || (j1 = updateCounter - chatline.getUpdatedCounter()) >= 200 && !flag) continue;
                    double d0 = (double)j1 / 200.0;
                    d0 = 1.0 - d0;
                    d0 *= 10.0;
                    d0 = MathHelper.clamp((double)d0, (double)0.0, (double)1.0);
                    d0 *= d0;
                    int l1 = (int)(255.0 * d0);
                    if (flag) {
                        l1 = 255;
                    }
                    l1 = (int)((float)l1 * f);
                    ++l;
                    if (l1 <= 3) continue;
                    int j2 = -i1 * 9;
                    if (rectAlpha > 0.0f && l1 < 255) {
                        MixinGuiNewChat.drawRect((int)-2, (int)(j2 - 9), (int)(k + 4), (int)j2, (int)((int)(rectAlpha * (float)(l1 / 2)) << 24));
                    }
                    String s = chatline.getChatComponent().getFormattedText();
                    GlStateManager.enableBlend();
                    String name = this.mc.player.getName();
                    if (BETTER_CHAT.isEnabled()) {
                        StringBuilder sb = new StringBuilder();
                        for (int i4 = 0; i4 < s.length(); ++i4) {
                            if (s.regionMatches(true, i4, name, 0, name.length())) {
                                sb.append(BETTER_CHAT.getPlayerColor()).append(s, i4, i4 + name.length()).append(TextUtil.findMatch(s.substring(0, i4 + name.length())));
                                i4 += name.length() - 1;
                                continue;
                            }
                            sb.append(s.charAt(i4));
                        }
                        s = sb.toString();
                    }
                    if (BETTER_CHAT.noAnnoyingPeople()) {
                        s = s.toLowerCase();
                    }
                    if ((CUSTOM_FONT = Managers.MODULE.get(CustomFont.class)).isChat() && !CUSTOM_FONT.isFull()) {
                        Managers.FONT.fontRenderer.drawStringWithShadow(s, 0.0, j2 - 8, 0xFFFFFF + (l1 << 24));
                    } else {
                        this.mc.fontRenderer.drawStringWithShadow(s, 0.0f, (float)(j2 - 8), 0xFFFFFF + (l1 << 24));
                    }
                    GlStateManager.disableAlpha();
                    GlStateManager.disableBlend();
                }
                if (flag) {
                    int k2 = this.mc.fontRenderer.FONT_HEIGHT;
                    GlStateManager.translate((float)-3.0f, (float)0.0f, (float)0.0f);
                    int l2 = j * k2 + j;
                    int i3 = l * k2 + l;
                    int j3 = this.scrollPos * i3 / j;
                    int k1 = i3 * i3 / l2;
                    if (l2 != i3) {
                        int k3 = j3 > 0 ? 170 : 96;
                        int l3 = this.isScrolled ? 0xCC3333 : 0x3333AA;
                        MixinGuiNewChat.drawRect((int)0, (int)(-j3), (int)2, (int)(-j3 - k1), (int)(l3 + (k3 << 24)));
                        MixinGuiNewChat.drawRect((int)2, (int)(-j3), (int)1, (int)(-j3 - k1), (int)(0xCCCCCC + (k3 << 24)));
                    }
                }
                GlStateManager.popMatrix();
            }
        }
    }
}

