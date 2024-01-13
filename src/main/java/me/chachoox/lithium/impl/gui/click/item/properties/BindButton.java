/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.gui.click.item.properties;

import java.awt.Color;
import me.chachoox.lithium.api.property.BindProperty;
import me.chachoox.lithium.api.property.util.Bind;
import me.chachoox.lithium.api.util.colors.ColorUtil;
import me.chachoox.lithium.api.util.render.Render2DUtil;
import me.chachoox.lithium.api.util.text.DotUtil;
import me.chachoox.lithium.impl.gui.click.Panel;
import me.chachoox.lithium.impl.gui.click.SexMasterGui;
import me.chachoox.lithium.impl.gui.click.item.Button;
import me.chachoox.lithium.impl.modules.other.clickgui.ClickGUI;

public class BindButton
extends Button {
    private boolean listening;
    private final BindProperty bindProperty;

    public BindButton(BindProperty property) {
        super(property.getLabel());
        this.bindProperty = property;
        this.x = this.getX() + 1.0f;
        this.setProperty(this.bindProperty);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Render2DUtil.drawRect(this.x - 1.0f, this.y, this.x, this.y + this.height - 0.5f, ClickGUI.get().getPropertyColor().getRGB());
        Render2DUtil.drawRect(this.x, this.y, this.x + this.width + 6.9f, this.y + this.height - 0.5f, this.getState() ? ClickGUI.get().getEnabledButtonColor().getRGB() : ClickGUI.get().getDisabledButtonColor().getRGB());
        if (this.isHovering(mouseX, mouseY)) {
            if (this.getState()) {
                Render2DUtil.drawRect(this.x, this.y, this.x + this.width + 6.9f, this.y + this.height - 0.5f, ColorUtil.changeAlpha(Color.BLACK, 30).getRGB());
            } else {
                Render2DUtil.drawRect(this.x, this.y, this.x + this.width + 6.9f, this.y + this.height - 0.5f, ColorUtil.changeAlpha(Color.WHITE, 30).getRGB());
            }
        }
        String string = this.listening ? DotUtil.getDots() : (!ClickGUI.get().aliMode.getValue().booleanValue() ? (!ClickGUI.get().lowercaseKeybinds.getValue().booleanValue() ? ((Bind)this.bindProperty.getValue()).toString().toUpperCase() : ((Bind)this.bindProperty.getValue()).toString().toLowerCase()) : ClickGUI.get().aliKeybind());
        this.renderer.drawString(String.format("%s:%s %s", ClickGUI.get().lowercaseProperties.getValue() == false ? this.getLabel() : this.getLabel().toLowerCase() + ClickGUI.get().aliProperty(), ClickGUI.get().whiteResult.getValue() != false ? "\u00a7f" : "\u00a77", string), this.x + 2.3f, this.y + 4.0f, -1);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void onKeyTyped(char typedChar, int keyCode) {
        if (this.listening) {
            Bind bind = new Bind(keyCode);
            if (bind.toString().equalsIgnoreCase("Escape")) {
                return;
            }
            if (bind.toString().equalsIgnoreCase("Delete")) {
                bind = new Bind(-1);
            }
            this.bindProperty.setValue(bind);
            this.toggle();
        }
    }

    @Override
    public float getHeight() {
        return 14.0f;
    }

    @Override
    public void toggle() {
        this.listening = !this.listening;
    }

    @Override
    public boolean getState() {
        return !this.listening;
    }

    @Override
    public boolean isHovering(int mouseX, int mouseY) {
        for (Panel panel : SexMasterGui.getClickGui().getPanels()) {
            if (!panel.drag) continue;
            return false;
        }
        return (float)mouseX >= this.getX() && (float)mouseX <= this.getX() + (this.width + 6.9f) && (float)mouseY >= this.getY() && (float)mouseY <= this.getY() + this.height;
    }
}

