/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.texture.DynamicTexture
 *  org.lwjgl.opengl.GL11
 */
package me.chachoox.lithium.api.util.text.font;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import me.chachoox.lithium.api.util.colors.HSLColor;
import me.chachoox.lithium.api.util.text.font.CFont;
import me.chachoox.lithium.api.util.text.font.CharData;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.misc.nameprotect.NameProtect;
import me.chachoox.lithium.impl.modules.other.colours.Colours;
import me.chachoox.lithium.impl.modules.other.font.CustomFont;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

public class CFontRenderer
extends CFont {
    protected CharData[] boldChars = new CharData[256];
    protected CharData[] italicChars = new CharData[256];
    protected CharData[] boldItalicChars = new CharData[256];
    private final int[] colorCode = new int[32];
    private final String colorcodeIdentifiers = "0123456789abcdefklmnorzy+-=";
    protected DynamicTexture texBold;
    protected DynamicTexture texItalic;
    protected DynamicTexture texItalicBold;

    public CFontRenderer(Font font, boolean antiAlias, boolean fractionalMetrics) {
        super(font, antiAlias, fractionalMetrics);
        this.setupMinecraftColorcodes();
        this.setupBoldItalicIDs();
    }

    public void drawStringWithShadow(String text, double x, double y, int color) {
        double x2 = x;
        double y2 = y;
        if (!Managers.MODULE.get(CustomFont.class).shadow.getValue().booleanValue()) {
            x2 -= (double)0.4f;
            y2 -= (double)0.4f;
        }
        float shadowWidth = this.drawString(text, x2 + 1.0, y2 + 1.0, color, true);
        this.drawString(text, x, y, color, false);
    }

    public float drawString(String text, float x, float y, int color) {
        return this.drawString(text, x, y, color, false);
    }

    public float drawString(String text, double x, double y, int color, boolean shadow) {
        NameProtect NAME_PROTECT = Managers.MODULE.get(NameProtect.class);
        text = NAME_PROTECT.isEnabled() ? text.replace(Minecraft.getMinecraft().player.getName(), NAME_PROTECT.getName()) : text;
        x -= 1.0;
        if (text == null) {
            return 0.0f;
        }
        if (color == 0x20FFFFFF) {
            color = 0xFFFFFF;
        }
        if ((color & 0xFC000000) == 0) {
            color |= 0xFF000000;
        }
        if (shadow) {
            color = (color & 0xFCFCFC) >> 2 | color & 0xFF000000;
        }
        CharData[] currentData = this.charData;
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        boolean randomCase = false;
        boolean bold = false;
        boolean italic = false;
        boolean strikethrough = false;
        boolean underline = false;
        boolean rainbowP = false;
        boolean rainbowM = false;
        boolean rainbowE = false;
        x *= 2.0;
        y = (y - 3.0) * 2.0;
        GL11.glPushMatrix();
        GlStateManager.scale((double)0.5, (double)0.5, (double)0.5);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc((int)770, (int)771);
        GlStateManager.color((float)((float)(color >> 16 & 0xFF) / 255.0f), (float)((float)(color >> 8 & 0xFF) / 255.0f), (float)((float)(color & 0xFF) / 255.0f), (float)alpha);
        int size = text.length();
        GlStateManager.enableTexture2D();
        GlStateManager.bindTexture((int)this.tex.getGlTextureId());
        GL11.glBindTexture((int)3553, (int)this.tex.getGlTextureId());
        for (int i = 0; i < size; ++i) {
            char character = text.charAt(i);
            if (character == '\u00a7') {
                int colorIndex = 21;
                try {
                    colorIndex = "0123456789abcdefklmnorzy+-=".indexOf(text.charAt(i + 1));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                if (colorIndex < 16) {
                    bold = false;
                    italic = false;
                    underline = false;
                    strikethrough = false;
                    rainbowP = false;
                    rainbowM = false;
                    rainbowE = false;
                    GlStateManager.bindTexture((int)this.tex.getGlTextureId());
                    currentData = this.charData;
                    if (colorIndex < 0) {
                        colorIndex = 15;
                    }
                    if (shadow) {
                        colorIndex += 16;
                    }
                    int colorcode = this.colorCode[colorIndex];
                    GlStateManager.color((float)((float)(colorcode >> 16 & 0xFF) / 255.0f), (float)((float)(colorcode >> 8 & 0xFF) / 255.0f), (float)((float)(colorcode & 0xFF) / 255.0f), (float)alpha);
                } else if (colorIndex == 16) {
                    randomCase = true;
                } else if (colorIndex == 17) {
                    bold = true;
                    if (italic) {
                        GlStateManager.bindTexture((int)this.texItalicBold.getGlTextureId());
                        currentData = this.boldItalicChars;
                    } else {
                        GlStateManager.bindTexture((int)this.texBold.getGlTextureId());
                        currentData = this.boldChars;
                    }
                } else if (colorIndex == 18) {
                    strikethrough = true;
                } else if (colorIndex == 19) {
                    underline = true;
                } else if (colorIndex == 20) {
                    italic = true;
                    if (bold) {
                        GlStateManager.bindTexture((int)this.texItalicBold.getGlTextureId());
                        currentData = this.boldItalicChars;
                    } else {
                        GlStateManager.bindTexture((int)this.texItalic.getGlTextureId());
                        currentData = this.italicChars;
                    }
                } else if (colorIndex == 21) {
                    bold = false;
                    italic = false;
                    underline = false;
                    strikethrough = false;
                    rainbowP = false;
                    rainbowM = false;
                    rainbowE = false;
                    GlStateManager.color((float)((float)(color >> 16 & 0xFF) / 255.0f), (float)((float)(color >> 8 & 0xFF) / 255.0f), (float)((float)(color & 0xFF) / 255.0f), (float)alpha);
                    GlStateManager.bindTexture((int)this.tex.getGlTextureId());
                    currentData = this.charData;
                } else if (colorIndex == 24) {
                    bold = false;
                    italic = false;
                    underline = false;
                    strikethrough = false;
                    rainbowP = true;
                    rainbowM = false;
                    rainbowE = false;
                    GlStateManager.bindTexture((int)this.tex.getGlTextureId());
                    currentData = this.charData;
                } else if (colorIndex == 25) {
                    bold = false;
                    italic = false;
                    underline = false;
                    strikethrough = false;
                    rainbowP = false;
                    rainbowM = true;
                    rainbowE = false;
                    GlStateManager.bindTexture((int)this.tex.getGlTextureId());
                    currentData = this.charData;
                } else {
                    bold = false;
                    italic = false;
                    underline = false;
                    strikethrough = false;
                    rainbowP = false;
                    rainbowM = false;
                    rainbowE = true;
                    GlStateManager.bindTexture((int)this.tex.getGlTextureId());
                    currentData = this.charData;
                }
                ++i;
                continue;
            }
            if (character >= currentData.length) continue;
            if (rainbowP || rainbowM || rainbowE) {
                int rainbow = HSLColor.toRGB(Colours.get().getRainbowHueByPosition(this.getRainbowPos(rainbowP, rainbowM, rainbowE, x, y) * ((Float)Colours.get().factor.getValue()).floatValue()), ((Float)Colours.get().saturation.getValue()).floatValue(), ((Float)Colours.get().lightness.getValue()).floatValue()).getRGB();
                GlStateManager.color((float)((float)(rainbow >> 16 & 0xFF) / 255.0f / (float)(shadow ? 4 : 1)), (float)((float)(rainbow >> 8 & 0xFF) / 255.0f / (float)(shadow ? 4 : 1)), (float)((float)(rainbow & 0xFF) / 255.0f / (float)(shadow ? 4 : 1)), (float)alpha);
            }
            GL11.glBegin((int)4);
            this.drawChar(currentData, character, (float)x, (float)y);
            GL11.glEnd();
            if (strikethrough) {
                this.drawLine(x, y + (double)((float)currentData[character].height / 2.0f), x + (double)currentData[character].width - 8.0, y + (double)((float)currentData[character].height / 2.0f), 1.0f);
            }
            if (underline) {
                this.drawLine(x, y + (double)currentData[character].height - 2.0, x + (double)currentData[character].width - 8.0, y + (double)currentData[character].height - 2.0, 1.0f);
            }
            x += (double)(currentData[character].width - 8 + this.charOffset);
        }
        GL11.glHint((int)3155, (int)4352);
        GL11.glPopMatrix();
        return (float)x / 2.0f;
    }

    @Override
    public int getStringWidth(String text) {
        if (text == null) {
            return 0;
        }
        NameProtect NAME_PROTECT = Managers.MODULE.get(NameProtect.class);
        text = NAME_PROTECT.isEnabled() ? text.replace(Minecraft.getMinecraft().player.getName(), NAME_PROTECT.getName()) : text;
        int width = 0;
        CharData[] currentData = this.charData;
        boolean bold = false;
        boolean italic = false;
        int size = text.length();
        for (int i = 0; i < size; ++i) {
            char character = text.charAt(i);
            if (character == '\u00a7') {
                int colorIndex = "0123456789abcdefklmnorzy+-".indexOf(character);
                if (colorIndex < 16) {
                    bold = false;
                    italic = false;
                } else if (colorIndex == 17) {
                    bold = true;
                    currentData = italic ? this.boldItalicChars : this.boldChars;
                } else if (colorIndex == 20) {
                    italic = true;
                    currentData = bold ? this.boldItalicChars : this.italicChars;
                } else if (colorIndex == 21) {
                    bold = false;
                    italic = false;
                    currentData = this.charData;
                }
                ++i;
                continue;
            }
            if (character >= currentData.length) continue;
            width += currentData[character].width - 8 + this.charOffset;
        }
        return width / 2;
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font);
        this.setupBoldItalicIDs();
    }

    @Override
    public void setAntiAlias(boolean antiAlias) {
        super.setAntiAlias(antiAlias);
        this.setupBoldItalicIDs();
    }

    @Override
    public void setFractionalMetrics(boolean fractionalMetrics) {
        super.setFractionalMetrics(fractionalMetrics);
        this.setupBoldItalicIDs();
    }

    private void setupBoldItalicIDs() {
        this.texBold = this.setupTexture(this.font.deriveFont(1), this.antiAlias, this.fractionalMetrics, this.boldChars);
        this.texItalic = this.setupTexture(this.font.deriveFont(1), this.antiAlias, this.fractionalMetrics, this.italicChars);
        this.texItalicBold = this.setupTexture(this.font.deriveFont(3), this.antiAlias, this.fractionalMetrics, this.boldItalicChars);
    }

    private void drawLine(double x, double y, double x1, double y1, float width) {
        GL11.glDisable((int)3553);
        GL11.glLineWidth((float)width);
        GL11.glBegin((int)1);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x1, (double)y1);
        GL11.glEnd();
        GL11.glEnable((int)3553);
    }

    public List<String> wrapWords(String text, double width) {
        ArrayList<String> finalWords = new ArrayList<String>();
        if ((double)this.getStringWidth(text) > width) {
            String[] words = text.split(" ");
            String currentWord = "";
            char lastColorCode = '\uffff';
            for (String word : words) {
                for (int i = 0; i < word.toCharArray().length; ++i) {
                    char c = word.toCharArray()[i];
                    if (c != '\u00a7' || i >= word.toCharArray().length - 1) continue;
                    lastColorCode = word.toCharArray()[i + 1];
                }
                StringBuilder stringBuilder = new StringBuilder();
                if ((double)this.getStringWidth(stringBuilder.append(currentWord).append(word).append(" ").toString()) < width) {
                    currentWord = currentWord + word + " ";
                    continue;
                }
                finalWords.add(currentWord);
                currentWord = "\u00a7" + lastColorCode + word + " ";
            }
            if (currentWord.length() > 0) {
                if ((double)this.getStringWidth(currentWord) < width) {
                    finalWords.add("\u00a7" + lastColorCode + currentWord + " ");
                    currentWord = "";
                } else {
                    for (String s : this.formatString(currentWord, width)) {
                        finalWords.add(s);
                    }
                }
            }
        } else {
            finalWords.add(text);
        }
        return finalWords;
    }

    public List<String> formatString(String string, double width) {
        ArrayList<String> finalWords = new ArrayList<String>();
        String currentWord = "";
        char lastColorCode = '\uffff';
        char[] chars = string.toCharArray();
        for (int i = 0; i < chars.length; ++i) {
            char c = chars[i];
            if (c == '\u00a7' && i < chars.length - 1) {
                lastColorCode = chars[i + 1];
            }
            StringBuilder stringBuilder = new StringBuilder();
            if ((double)this.getStringWidth(stringBuilder.append(currentWord).append(c).toString()) < width) {
                currentWord = currentWord + c;
                continue;
            }
            finalWords.add(currentWord);
            currentWord = "\u00a7" + lastColorCode + c;
        }
        if (currentWord.length() > 0) {
            finalWords.add(currentWord);
        }
        return finalWords;
    }

    private void setupMinecraftColorcodes() {
        for (int i = 0; i < 32; ++i) {
            int o = (i >> 3 & 1) * 85;
            int r = (i >> 2 & 1) * 170 + o;
            int g = (i >> 1 & 1) * 170 + o;
            int b = (i & 1) * 170 + o;
            if (i == 6) {
                r += 85;
            }
            if (i >= 16) {
                r /= 4;
                g /= 4;
                b /= 4;
            }
            this.colorCode[i] = (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF;
        }
    }

    private float getRainbowPos(boolean plus, boolean minus, boolean extra, double x, double y) {
        if (plus) {
            return (float)x;
        }
        if (minus) {
            return (float)y;
        }
        if (extra) {
            return (float)(x + y);
        }
        return 0.0f;
    }
}

