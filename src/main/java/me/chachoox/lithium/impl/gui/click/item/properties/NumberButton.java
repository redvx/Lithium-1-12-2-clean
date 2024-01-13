/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.MathHelper
 */
package me.chachoox.lithium.impl.gui.click.item.properties;

import java.awt.Color;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.util.colors.ColorUtil;
import me.chachoox.lithium.api.util.math.RoundingUtil;
import me.chachoox.lithium.api.util.render.Render2DUtil;
import me.chachoox.lithium.impl.gui.click.Panel;
import me.chachoox.lithium.impl.gui.click.SexMasterGui;
import me.chachoox.lithium.impl.gui.click.item.Item;
import me.chachoox.lithium.impl.modules.other.clickgui.ClickGUI;
import net.minecraft.util.math.MathHelper;

public class NumberButton
extends Item
implements Minecraftable {
    private final NumberProperty<Number> numberProperty;
    private final Number min;
    private final Number max;
    private final int difference;
    private boolean dragging;

    public NumberButton(NumberProperty<Number> numberProperty) {
        super(numberProperty.getLabel());
        this.numberProperty = numberProperty;
        this.min = numberProperty.getMinimum();
        this.max = numberProperty.getMaximum();
        this.difference = this.max.intValue() - this.min.intValue();
        this.setProperty(numberProperty);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Render2DUtil.drawRect(this.x - 1.0f, this.y, this.x, this.y + this.height - 0.5f, ClickGUI.get().getPropertyColor().getRGB());
        Render2DUtil.drawRect(this.x, this.y, ((Number)this.numberProperty.getValue()).floatValue() <= this.min.floatValue() ? this.x : this.x + (this.width + 6.9f) * this.partialMultiplier(), this.y + this.height - 0.5f, ClickGUI.get().getEnabledButtonColor().getRGB());
        if (this.isHovering(mouseX, mouseY)) {
            Render2DUtil.drawRect(this.x, this.y, this.x + (this.width + 6.9f) * this.partialMultiplier(), this.y + this.height - 0.5f, ColorUtil.changeAlpha(Color.BLACK, 30).getRGB());
        }
        if (this.dragging) {
            this.setSettingFromX(mouseX);
        }
        String value = String.format("%.2f", Float.valueOf(((Number)this.numberProperty.getValue()).floatValue()));
        this.renderer.drawString(String.format("%s:%s %s", ClickGUI.get().lowercaseProperties.getValue() == false ? this.getLabel() + ClickGUI.get().aliProperty() : this.getLabel().toLowerCase() + ClickGUI.get().aliProperty(), ClickGUI.get().whiteResult.getValue() != false ? "\u00a7f" : "\u00a77", value), this.x + 2.3f, this.y + 4.0f, -1);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovering(mouseX, mouseY) && mouseButton == 0) {
            this.dragging = true;
        }
    }

    private void setSettingFromX(int mouseX) {
        float percent = ((float)mouseX - this.x) / (this.width + 6.9f);
        if (this.numberProperty.getValue() instanceof Double) {
            double result = (Double)this.numberProperty.getMinimum() + (double)((float)this.difference * percent);
            this.numberProperty.setValue(MathHelper.clamp((double)RoundingUtil.roundDouble(RoundingUtil.roundToStep(result, (Double)this.numberProperty.getSteps()), 2), (double)((Double)this.numberProperty.getMinimum()), (double)((Double)this.numberProperty.getMaximum())));
        } else if (this.numberProperty.getValue() instanceof Float) {
            float result = ((Float)this.numberProperty.getMinimum()).floatValue() + (float)this.difference * percent;
            this.numberProperty.setValue(Float.valueOf(MathHelper.clamp((float)RoundingUtil.roundFloat(RoundingUtil.roundToStep(result, ((Float)this.numberProperty.getSteps()).floatValue()), 2), (float)((Float)this.numberProperty.getMinimum()).floatValue(), (float)((Float)this.numberProperty.getMaximum()).floatValue())));
        } else if (this.numberProperty.getValue() instanceof Integer) {
            this.numberProperty.setValue((Integer)this.numberProperty.getMinimum() + (int)((float)this.difference * percent));
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        this.dragging = false;
    }

    @Override
    public float getHeight() {
        return 14.0f;
    }

    private float middle() {
        return this.max.floatValue() - this.min.floatValue();
    }

    private float part() {
        return ((Number)this.numberProperty.getValue()).floatValue() - this.min.floatValue();
    }

    private float partialMultiplier() {
        return this.part() / this.middle();
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

