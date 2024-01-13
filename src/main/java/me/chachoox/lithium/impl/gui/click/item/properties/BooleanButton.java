/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.gui.click.item.properties;

import java.awt.Color;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.colors.ColorUtil;
import me.chachoox.lithium.api.util.render.Render2DUtil;
import me.chachoox.lithium.impl.gui.click.Panel;
import me.chachoox.lithium.impl.gui.click.SexMasterGui;
import me.chachoox.lithium.impl.gui.click.item.Button;
import me.chachoox.lithium.impl.modules.other.clickgui.ClickGUI;

public class BooleanButton
extends Button {
    private final Property<Boolean> booleanProperty;

    public BooleanButton(Property<Boolean> property) {
        super(property.getLabel());
        this.booleanProperty = property;
        this.x = this.getX() + 1.0f;
        this.setProperty(this.booleanProperty);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Render2DUtil.drawRect(this.x - 1.0f, this.y, this.x, this.y + this.height - 0.5f, ClickGUI.get().getPropertyColor().getRGB());
        Render2DUtil.drawRect(this.x, this.y, this.x + this.width + 6.9f, this.y + this.height - 0.5f, this.getState() ? ClickGUI.get().getEnabledButtonColor().getRGB() : 0x11555555);
        if (this.isHovering(mouseX, mouseY)) {
            if (this.getState()) {
                Render2DUtil.drawRect(this.x, this.y, this.x + this.width + 6.9f, this.y + this.height - 0.5f, ColorUtil.changeAlpha(Color.BLACK, 30).getRGB());
            } else {
                Render2DUtil.drawRect(this.x, this.y, this.x + this.width + 6.9f, this.y + this.height - 0.5f, ColorUtil.changeAlpha(Color.WHITE, 30).getRGB());
            }
        }
        this.renderer.drawString(ClickGUI.get().lowercaseProperties.getValue() == false ? this.getLabel() + ClickGUI.get().aliProperty() : this.getLabel().toLowerCase() + ClickGUI.get().aliProperty(), this.x + 2.3f, this.y + 4.0f, -1);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public float getHeight() {
        return 14.0f;
    }

    @Override
    public void toggle() {
        this.booleanProperty.setValue(this.booleanProperty.getValue() == false);
    }

    @Override
    public boolean getState() {
        return this.booleanProperty.getValue();
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

