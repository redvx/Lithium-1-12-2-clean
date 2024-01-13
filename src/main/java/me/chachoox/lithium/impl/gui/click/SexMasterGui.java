/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.input.Mouse
 */
package me.chachoox.lithium.impl.gui.click;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.util.render.Render2DUtil;
import me.chachoox.lithium.impl.gui.click.Panel;
import me.chachoox.lithium.impl.gui.click.item.Item;
import me.chachoox.lithium.impl.gui.click.item.ModuleButton;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.other.clickgui.ClickGUI;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

public class SexMasterGui
extends GuiScreen {
    private static SexMasterGui clickGui;
    private final ArrayList<Panel> panels = new ArrayList();

    public SexMasterGui() {
        this.load();
    }

    public static SexMasterGui getClickGui() {
        return clickGui == null ? (clickGui = new SexMasterGui()) : clickGui;
    }

    private void load() {
        int x = -100;
        for (final Category category : Category.values()) {
            this.panels.add(new Panel(category.getLabel(), x += 102, 4, true){

                @Override
                public void setupItems() {
                    for (Module modules : Managers.MODULE.getModules()) {
                        if (!modules.getCategory().equals((Object)category)) continue;
                        this.addButton(new ModuleButton(modules));
                    }
                }
            });
        }
        this.panels.forEach(panel -> panel.getItems().sort(Comparator.comparing(Item::getLabel)));
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ClickGUI clickGUI = ClickGUI.get();
        if (clickGUI.gradient.getValue().booleanValue()) {
            Render2DUtil.drawGradientRect(0.0f, 0.0f, this.mc.displayWidth, this.mc.displayHeight, clickGUI.sideWays.getValue(), clickGUI.gradientFirstColor.getColor().getRGB(), clickGUI.gradientSecondColor.getColor().getRGB());
        } else {
            Render2DUtil.drawRect(0.0f, 0.0f, this.mc.displayWidth, this.mc.displayHeight, clickGUI.gradientFirstColor.getColor().getRGB());
        }
        this.panels.forEach(panel -> panel.drawScreen(mouseX, mouseY, partialTicks));
    }

    public void initGui() {
        if (ClickGUI.get().blur.getValue().booleanValue()) {
            this.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
            ClickGUI.get().loaded = true;
        }
    }

    public void onGuiClosed() {
        if (ClickGUI.get().loaded) {
            this.mc.entityRenderer.stopUseShader();
            ClickGUI.get().loaded = false;
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int clickedButton) {
        this.panels.forEach(panel -> panel.mouseClicked(mouseX, mouseY, clickedButton));
    }

    public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
        this.panels.forEach(panel -> panel.mouseReleased(mouseX, mouseY, releaseButton));
    }

    public void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        this.panels.forEach(panel -> panel.onKeyTyped(typedChar, keyCode));
    }

    public void handleMouseInput() throws IOException {
        int scrollAmount = 5;
        if (Mouse.getEventDWheel() > 0) {
            for (Panel panel : this.panels) {
                panel.setY(panel.getY() + scrollAmount);
            }
        }
        if (Mouse.getEventDWheel() < 0) {
            for (Panel panel : this.panels) {
                panel.setY(panel.getY() - scrollAmount);
            }
        }
        super.handleMouseInput();
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    public ArrayList<Panel> getPanels() {
        return this.panels;
    }
}

