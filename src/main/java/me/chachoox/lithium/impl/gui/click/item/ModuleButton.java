/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.audio.ISound
 *  net.minecraft.client.audio.PositionedSoundRecord
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.init.SoundEvents
 *  net.minecraft.util.SoundEvent
 *  net.minecraft.util.StringUtils
 */
package me.chachoox.lithium.impl.gui.click.item;

import java.util.ArrayList;
import java.util.List;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.BindProperty;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.render.Render2DUtil;
import me.chachoox.lithium.impl.gui.click.item.Button;
import me.chachoox.lithium.impl.gui.click.item.Item;
import me.chachoox.lithium.impl.gui.click.item.properties.BindButton;
import me.chachoox.lithium.impl.gui.click.item.properties.BooleanButton;
import me.chachoox.lithium.impl.gui.click.item.properties.EnumButton;
import me.chachoox.lithium.impl.gui.click.item.properties.NumberButton;
import me.chachoox.lithium.impl.modules.other.clickgui.ClickGUI;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.StringUtils;

public class ModuleButton
extends Button
implements Minecraftable {
    private final Module module;
    private final List<Item> items = new ArrayList<Item>();
    private boolean subOpen;

    public ModuleButton(Module module) {
        super(module.getLabel());
        this.module = module;
        if (!module.getProperties().isEmpty()) {
            for (Property<Boolean> property : module.getProperties()) {
                if (property.getValue() instanceof Boolean && !property.getLabel().equalsIgnoreCase("Enabled") && !property.getLabel().equalsIgnoreCase("Drawn")) {
                    this.items.add(new BooleanButton(property));
                }
                if (property instanceof EnumProperty) {
                    this.items.add(new EnumButton((EnumProperty)property));
                }
                if (property instanceof NumberProperty) {
                    this.items.add(new NumberButton((NumberProperty)property));
                }
                if (!(property instanceof BindProperty) || property.getLabel().equalsIgnoreCase("Keybind")) continue;
                this.items.add(new BindButton((BindProperty)property));
            }
        }
        if (!module.getCategory().getLabel().equalsIgnoreCase("Other")) {
            this.items.add(new BindButton((BindProperty)module.getProperty("Keybind")));
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution resolution = new ScaledResolution(mc);
        GlStateManager.translate((float)0.0f, (float)0.0f, (float)1.0f);
        if (this.isHovering(mouseX, mouseY) && this.module.getDescription() != null) {
            Render2DUtil.drawBorderedRect(-1.0f, resolution.getScaledHeight() - 12, this.renderer.getStringWidth(ClickGUI.get().lowercaseDescriptions.getValue() == false ? this.module.getDescription() + 9 : this.module.getDescription().toLowerCase() + 9), resolution.getScaledHeight() + ModuleButton.mc.fontRenderer.FONT_HEIGHT + 10, ClickGUI.get().getDescriptionColor().getRGB(), ClickGUI.get().getDescriptionOutlineColor().getRGB());
            this.renderer.drawString(ClickGUI.get().lowercaseDescriptions.getValue() == false ? this.module.getDescription() : this.module.getDescription().toLowerCase(), 2.0f, resolution.getScaledHeight() - 10, ClickGUI.get().getDescriptionTextColor().getRGB());
        }
        GlStateManager.translate((float)0.0f, (float)0.0f, (float)-1.0f);
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (!this.items.isEmpty() && this.subOpen) {
            float height = 1.0f;
            for (Item item : this.items) {
                item.setLocation(this.x + 1.0f, this.y + (height += 15.5f));
                item.setHeight(15.0f);
                item.setWidth(this.width - 9.0f);
                GlStateManager.translate((float)0.0f, (float)0.0f, (float)1.0f);
                if (item.isHovering(mouseX, mouseY) && !StringUtils.isNullOrEmpty((String)item.getProperty().getDescription())) {
                    Render2DUtil.drawBorderedRect(-1.0f, resolution.getScaledHeight() - 12, this.renderer.getStringWidth(ClickGUI.get().lowercaseDescriptions.getValue() == false ? item.getProperty().getDescription() + 9 : item.getProperty().getDescription().toLowerCase() + 9), resolution.getScaledHeight() + ModuleButton.mc.fontRenderer.FONT_HEIGHT + 10, ClickGUI.get().getDescriptionColor().getRGB(), ClickGUI.get().getDescriptionOutlineColor().getRGB());
                    this.renderer.drawString(ClickGUI.get().lowercaseDescriptions.getValue() == false ? item.getProperty().getDescription() : item.getProperty().getDescription().toLowerCase(), 2.0f, resolution.getScaledHeight() - 10, ClickGUI.get().getDescriptionTextColor().getRGB());
                }
                GlStateManager.translate((float)0.0f, (float)0.0f, (float)-1.0f);
                item.drawScreen(mouseX, mouseY, partialTicks);
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (!this.items.isEmpty()) {
            if (mouseButton == 1 && this.isHovering(mouseX, mouseY)) {
                this.subOpen = !this.subOpen;
                mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.getMasterRecord((SoundEvent)SoundEvents.UI_BUTTON_CLICK, (float)1.0f));
            }
            if (this.subOpen) {
                for (Item item : this.items) {
                    item.mouseClicked(mouseX, mouseY, mouseButton);
                }
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
        this.items.forEach(item -> item.mouseReleased(mouseX, mouseY, releaseButton));
        super.mouseReleased(mouseX, mouseY, releaseButton);
    }

    @Override
    public void onKeyTyped(char typedChar, int keyCode) {
        if (!this.subOpen) {
            return;
        }
        this.items.forEach(item -> item.onKeyTyped(typedChar, keyCode));
    }

    @Override
    public float getHeight() {
        if (this.subOpen) {
            float height = 14.0f;
            for (Item item : this.items) {
                height += item.getHeight() + 1.5f;
            }
            return height + 2.0f;
        }
        return 14.0f;
    }

    @Override
    public void toggle() {
        this.module.toggle();
    }

    @Override
    public boolean getState() {
        return this.module.isEnabled();
    }
}

