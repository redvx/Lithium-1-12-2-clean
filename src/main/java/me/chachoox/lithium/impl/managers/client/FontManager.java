/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.managers.client;

import java.awt.Font;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.api.util.text.font.CFontRenderer;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.misc.nameprotect.NameProtect;
import me.chachoox.lithium.impl.modules.other.font.CustomFont;
import org.apache.commons.lang3.StringUtils;

public class FontManager
implements Minecraftable {
    public CFontRenderer fontRenderer = new CFontRenderer(new Font("Verdana", 0, 18), true, true);
    public boolean cFont;

    public void drawString(String text, float x, float y, int color) {
        if (this.cFont) {
            this.fontRenderer.drawStringWithShadow(text, x, y, color);
        } else {
            FontManager.mc.fontRenderer.drawStringWithShadow(text, x, y, color);
        }
    }

    public void setFontRenderer() {
        CustomFont CUSTOM_FONT = Managers.MODULE.get(CustomFont.class);
        this.fontRenderer = new CFontRenderer(CUSTOM_FONT.getFont(), CUSTOM_FONT.antiAlias.getValue(), CUSTOM_FONT.fractionalMetrics.getValue());
    }

    public int getStringWidth(String text) {
        NameProtect NAME_PROTECT = Managers.MODULE.get(NameProtect.class);
        if (NAME_PROTECT.isEnabled() && FontManager.mc.player != null) {
            text = StringUtils.replace(text, FontManager.mc.player.getName(), NAME_PROTECT.getName());
        }
        if (this.cFont) {
            return this.fontRenderer.getStringWidth(text);
        }
        return FontManager.mc.fontRenderer.getStringWidth(text);
    }
}

