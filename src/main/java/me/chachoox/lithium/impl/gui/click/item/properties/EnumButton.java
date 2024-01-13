/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.audio.ISound
 *  net.minecraft.client.audio.PositionedSoundRecord
 *  net.minecraft.init.SoundEvents
 *  net.minecraft.util.SoundEvent
 */
package me.chachoox.lithium.impl.gui.click.item.properties;

import java.awt.Color;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.util.colors.ColorUtil;
import me.chachoox.lithium.api.util.render.Render2DUtil;
import me.chachoox.lithium.impl.gui.click.Panel;
import me.chachoox.lithium.impl.gui.click.SexMasterGui;
import me.chachoox.lithium.impl.gui.click.item.Button;
import me.chachoox.lithium.impl.modules.other.clickgui.ClickGUI;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;

public class EnumButton
extends Button {
    private final EnumProperty<?> property;

    public EnumButton(EnumProperty<?> property) {
        super(property.getLabel());
        this.property = property;
        this.x = this.getX() + 1.0f;
        this.setProperty(property);
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
        this.renderer.drawString(String.format("%s:%s %s", ClickGUI.get().lowercaseProperties.getValue() == false ? this.getLabel() + ClickGUI.get().aliProperty() : this.getLabel().toLowerCase() + ClickGUI.get().aliProperty(), ClickGUI.get().whiteResult.getValue() != false ? "\u00a7f" : "\u00a77", ClickGUI.get().lowercaseProperties.getValue() == false ? this.property.getFixedValue() : this.property.getFixedValue().toLowerCase()), this.x + 2.3f, this.y + 4.0f, -1);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovering(mouseX, mouseY)) {
            if (mouseButton == 0) {
                this.property.increment();
            } else if (mouseButton == 1) {
                mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.getMasterRecord((SoundEvent)SoundEvents.UI_BUTTON_CLICK, (float)1.0f));
                this.property.decrement();
            }
        }
    }

    @Override
    public float getHeight() {
        return 14.0f;
    }

    @Override
    public void toggle() {
    }

    @Override
    public boolean getState() {
        return true;
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

