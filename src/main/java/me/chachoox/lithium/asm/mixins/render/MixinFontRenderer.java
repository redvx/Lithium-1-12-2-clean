/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.renderer.GlStateManager
 */
package me.chachoox.lithium.asm.mixins.render;

import java.util.regex.Pattern;
import me.chachoox.lithium.api.util.colors.HSLColor;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.misc.nameprotect.NameProtect;
import me.chachoox.lithium.impl.modules.other.colours.Colours;
import me.chachoox.lithium.impl.modules.other.font.CustomFont;
import me.chachoox.lithium.impl.modules.other.hud.Hud;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={FontRenderer.class})
public abstract class MixinFontRenderer {
    private static final String COLOR_CODES = "0123456789abcdefklmnorzy+-=";
    private static final Pattern CUSTOM_PATTERN = Pattern.compile("(?i)\u00a7Z[0-9A-F]{8}");
    @Shadow
    private boolean randomStyle;
    @Shadow
    private boolean boldStyle;
    @Shadow
    private boolean italicStyle;
    @Shadow
    private boolean underlineStyle;
    @Shadow
    private boolean strikethroughStyle;
    @Shadow
    private int textColor;
    @Shadow
    protected float posX;
    @Shadow
    protected float posY;
    @Shadow
    private float alpha;
    private int skip;
    private int currentIndex;
    private boolean currentShadow;
    private String currentText;
    private boolean rainbowPlus;
    private boolean rainbowMinus;
    private boolean rainbowExtra;

    @Shadow
    protected abstract int renderString(String var1, float var2, float var3, int var4, boolean var5);

    @Shadow
    protected abstract void renderStringAtPos(String var1, boolean var2);

    @Shadow
    protected abstract int getCharWidth(char var1);

    @Redirect(method={"renderString(Ljava/lang/String;FFIZ)I"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/gui/FontRenderer;renderStringAtPos(Ljava/lang/String;Z)V"))
    public void renderStringAtPosHook(FontRenderer renderer, String text, boolean shadow) {
        NameProtect NAME_PROTECT = Managers.MODULE.get(NameProtect.class);
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (NAME_PROTECT != null && NAME_PROTECT.isEnabled() && player != null) {
            this.renderStringAtPos(text.replace(player.getName(), NAME_PROTECT.getName()), shadow);
        } else {
            this.renderStringAtPos(text, shadow);
        }
    }

    @Inject(method={"drawString(Ljava/lang/String;FFIZ)I"}, at={@At(value="HEAD")}, cancellable=true)
    public void renderStringHook2(String text, float x, float y, int color, boolean dropShadow, CallbackInfoReturnable<Integer> infoReturnable) {
        if (Managers.MODULE.get(CustomFont.class) != null && Managers.MODULE.get(CustomFont.class).isFull()) {
            if (dropShadow) {
                Managers.FONT.fontRenderer.drawStringWithShadow(text, x, y, color);
            } else {
                Managers.FONT.fontRenderer.drawString(text, x, y, color);
            }
            infoReturnable.setReturnValue((int)((float)Managers.FONT.getStringWidth(text) + x));
        }
    }

    @Overwrite
    public int getStringWidth(String text) {
        if (text == null) {
            return 0;
        }
        int i = 0;
        boolean flag = false;
        NameProtect NAME_PROTECT = Managers.MODULE.get(NameProtect.class);
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (NAME_PROTECT != null && NAME_PROTECT.isEnabled() && player != null) {
            text = text.replace(player.getName(), NAME_PROTECT.getName());
        }
        for (int j = 0; j < text.length(); ++j) {
            char c0 = text.charAt(j);
            int k = this.getCharWidth(c0);
            if (k < 0 && j < text.length() - 1) {
                if ((c0 = text.charAt(++j)) != 'l' && c0 != 'L') {
                    if (c0 == 'r' || c0 == 'R') {
                        flag = false;
                    }
                } else {
                    flag = true;
                }
                k = 0;
            }
            i += k;
            if (!flag || k <= 0) continue;
            ++i;
        }
        CustomFont CUSTOM_FONT = Managers.MODULE.get(CustomFont.class);
        if (CUSTOM_FONT != null && CUSTOM_FONT.isFull()) {
            i = Managers.FONT.fontRenderer.getStringWidth(text);
        }
        return i;
    }

    @Redirect(method={"drawString(Ljava/lang/String;FFIZ)I"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/gui/FontRenderer;renderString(Ljava/lang/String;FFIZ)I"))
    public int renderStringHook(FontRenderer fontrenderer, String text, float x, float y, int color, boolean dropShadow) {
        if (dropShadow && !Managers.MODULE.get(Hud.class).shadow.getValue().booleanValue()) {
            x -= 0.4f;
            y -= 0.4f;
        }
        return this.renderString(text, x, y, color, dropShadow);
    }

    @Inject(method={"renderStringAtPos"}, at={@At(value="HEAD")})
    public void resetSkip(String text, boolean shadow, CallbackInfo info) {
        this.skip = 0;
        this.currentIndex = 0;
        this.currentText = text;
        this.currentShadow = shadow;
    }

    @Redirect(method={"renderStringAtPos"}, at=@At(value="INVOKE", target="Ljava/lang/String;charAt(I)C", ordinal=0))
    public char charAtHook(String text, int index) {
        this.currentIndex = index;
        return this.getCharAt(text, index);
    }

    @Redirect(method={"renderStringAtPos"}, at=@At(value="INVOKE", target="Ljava/lang/String;charAt(I)C", ordinal=1))
    public char charAtHook1(String text, int index) {
        return this.getCharAt(text, index);
    }

    @Redirect(method={"renderStringAtPos"}, at=@At(value="INVOKE", target="Ljava/lang/String;length()I", ordinal=0))
    public int lengthHook(String string) {
        return string.length() - this.skip;
    }

    @Redirect(method={"renderStringAtPos"}, at=@At(value="INVOKE", target="Ljava/lang/String;length()I", ordinal=1))
    public int lengthHook1(String string) {
        return string.length() - this.skip;
    }

    @Redirect(method={"renderStringAtPos"}, at=@At(value="INVOKE", target="Ljava/lang/String;indexOf(I)I", ordinal=0))
    public int colorCodeHook(String colorCode, int ch) {
        int result = COLOR_CODES.indexOf(String.valueOf(this.currentText.charAt(this.currentIndex + this.skip + 1)).toLowerCase().charAt(0));
        if (result == 22) {
            this.randomStyle = false;
            this.boldStyle = false;
            this.strikethroughStyle = false;
            this.underlineStyle = false;
            this.italicStyle = false;
            this.rainbowPlus = false;
            this.rainbowMinus = false;
            this.rainbowExtra = false;
            char[] h = new char[8];
            try {
                for (int j = 0; j < 8; ++j) {
                    h[j] = this.currentText.charAt(this.currentIndex + this.skip + j + 2);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                return result;
            }
            int colorcode = -1;
            try {
                colorcode = (int)Long.parseLong(new String(h), 16);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            this.textColor = colorcode;
            GlStateManager.color((float)((float)(colorcode >> 16 & 0xFF) / 255.0f / (float)(this.currentShadow ? 4 : 1)), (float)((float)(colorcode >> 8 & 0xFF) / 255.0f / (float)(this.currentShadow ? 4 : 1)), (float)((float)(colorcode & 0xFF) / 255.0f / (float)(this.currentShadow ? 4 : 1)), (float)((float)(colorcode >> 24 & 0xFF) / 255.0f));
            this.skip += 8;
        } else if (result == 23) {
            this.randomStyle = false;
            this.boldStyle = false;
            this.strikethroughStyle = false;
            this.underlineStyle = false;
            this.italicStyle = false;
            this.rainbowPlus = false;
            this.rainbowMinus = false;
            this.rainbowExtra = false;
            int rainbow = HSLColor.toRGB(Colours.get().getRainbowHue(), ((Float)Colours.get().saturation.getValue()).floatValue(), ((Float)Colours.get().lightness.getValue()).floatValue()).getRGB();
            GlStateManager.color((float)((float)(rainbow >> 16 & 0xFF) / 255.0f / (float)(this.currentShadow ? 4 : 1)), (float)((float)(rainbow >> 8 & 0xFF) / 255.0f / (float)(this.currentShadow ? 4 : 1)), (float)((float)(rainbow & 0xFF) / 255.0f / (float)(this.currentShadow ? 4 : 1)), (float)((float)(rainbow >> 24 & 0xFF) / 255.0f));
        } else if (result == 24) {
            this.randomStyle = false;
            this.boldStyle = false;
            this.strikethroughStyle = false;
            this.underlineStyle = false;
            this.italicStyle = false;
            this.rainbowPlus = true;
            this.rainbowMinus = false;
            this.rainbowExtra = false;
        } else if (result == 25) {
            this.randomStyle = false;
            this.boldStyle = false;
            this.strikethroughStyle = false;
            this.underlineStyle = false;
            this.italicStyle = false;
            this.rainbowPlus = false;
            this.rainbowMinus = true;
            this.rainbowExtra = false;
        } else if (result == 26) {
            this.randomStyle = false;
            this.boldStyle = false;
            this.strikethroughStyle = false;
            this.underlineStyle = false;
            this.italicStyle = false;
            this.rainbowPlus = false;
            this.rainbowMinus = false;
            this.rainbowExtra = true;
        } else {
            this.rainbowPlus = false;
            this.rainbowMinus = false;
            this.rainbowExtra = false;
        }
        return result;
    }

    @Inject(method={"resetStyles"}, at={@At(value="HEAD")})
    public void resetStylesHook(CallbackInfo info) {
        this.rainbowPlus = false;
        this.rainbowMinus = false;
        this.rainbowExtra = false;
    }

    @Inject(method={"renderStringAtPos"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/gui/FontRenderer;renderChar(CZ)F", shift=At.Shift.BEFORE, ordinal=0)})
    public void renderCharHook(String text, boolean shadow, CallbackInfo info) {
        if (this.rainbowPlus || this.rainbowMinus || this.rainbowExtra) {
            int rainbow = HSLColor.toRGB(Colours.get().getRainbowHueByPosition(this.getRainbowPos(this.rainbowPlus, this.rainbowMinus, this.rainbowExtra) * ((Float)Colours.get().factor.getValue()).floatValue()), ((Float)Colours.get().saturation.getValue()).floatValue(), ((Float)Colours.get().lightness.getValue()).floatValue()).getRGB();
            GlStateManager.color((float)((float)(rainbow >> 16 & 0xFF) / 255.0f / (float)(shadow ? 4 : 1)), (float)((float)(rainbow >> 8 & 0xFF) / 255.0f / (float)(shadow ? 4 : 1)), (float)((float)(rainbow & 0xFF) / 255.0f / (float)(shadow ? 4 : 1)), (float)this.alpha);
        }
    }

    @ModifyVariable(method={"getStringWidth"}, at=@At(value="HEAD"), ordinal=0)
    private String setText(String text) {
        return text == null ? null : CUSTOM_PATTERN.matcher(text).replaceAll("\u00a7b");
    }

    private char getCharAt(String text, int index) {
        if (index + this.skip >= text.length()) {
            return text.charAt(text.length() - 1);
        }
        return text.charAt(index + this.skip);
    }

    private float getRainbowPos(boolean plus, boolean minus, boolean extra) {
        if (plus) {
            return this.posX;
        }
        if (minus) {
            return this.posY;
        }
        if (extra) {
            return this.posX + this.posY;
        }
        return 0.0f;
    }
}

