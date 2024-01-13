/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.audio.ISound
 *  net.minecraft.client.audio.PositionedSoundRecord
 *  net.minecraft.init.SoundEvents
 *  net.minecraft.util.SoundEvent
 */
package me.chachoox.lithium.impl.gui.click.item;

import java.awt.Color;
import me.chachoox.lithium.api.interfaces.Labeled;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.api.util.colors.ColorUtil;
import me.chachoox.lithium.api.util.render.Render2DUtil;
import me.chachoox.lithium.impl.gui.click.item.Item;
import me.chachoox.lithium.impl.modules.other.clickgui.ClickGUI;
import me.chachoox.lithium.impl.modules.other.colours.Colours;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;

public class Button
extends Item
implements Labeled,
Minecraftable {
    private boolean state;

    public Button(String label) {
        super(label);
        this.height = 15.0f;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Render2DUtil.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, this.getState() ? ClickGUI.get().getEnabledButtonColor().getRGB() : ClickGUI.get().getDisabledButtonColor().getRGB());
        if (this.isHovering(mouseX, mouseY)) {
            if (this.getState()) {
                Render2DUtil.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, ColorUtil.changeAlpha(Color.BLACK, 30).getRGB());
            } else {
                Render2DUtil.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, Colours.get().getColourCustomAlpha(30).getRGB());
            }
        }
        this.renderer.drawString(ClickGUI.get().lowercaseModules.getValue() == false ? this.getLabel() + ClickGUI.get().aliModule() : this.getLabel().toLowerCase() + ClickGUI.get().aliModule(), this.x + 2.0f, this.y + 4.0f, this.getState() ? ClickGUI.get().getEnabledTextColor().getRGB() : ClickGUI.get().getDisabledTextColor().getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && this.isHovering(mouseX, mouseY)) {
            mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.getMasterRecord((SoundEvent)SoundEvents.UI_BUTTON_CLICK, (float)1.0f));
            this.state = !this.state;
            this.toggle();
        }
    }

    public void toggle() {
    }

    public boolean getState() {
        return this.state;
    }

    @Override
    public float getHeight() {
        return 14.0f;
    }
}

