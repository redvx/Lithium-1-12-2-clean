/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderPearl
 *  net.minecraft.entity.item.EntityExpBottle
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 */
package me.chachoox.lithium.impl.modules.render.esp;

import java.awt.Color;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.ColorProperty;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.colors.ColorUtil;
import me.chachoox.lithium.api.util.math.MathUtil;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.api.util.render.RenderUtil;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.render.esp.ListenerRender;
import me.chachoox.lithium.impl.modules.render.esp.ListenerSound;
import me.chachoox.lithium.impl.modules.render.esp.util.RenderMode;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class ESP
extends Module {
    protected final EnumProperty<RenderMode> mode = new EnumProperty<RenderMode>(RenderMode.OUTLINE, new String[]{"Mode", "type"}, "Outline: - Draws an outline / Box: Draws a box.");
    protected final Property<Boolean> items = new Property<Boolean>(true, new String[]{"Items", "i"}, "Draws esp on items.");
    protected final Property<Boolean> xpBottles = new Property<Boolean>(true, new String[]{"Bottles", "expbottles"}, "Draws esp on xp bottles.");
    protected final Property<Boolean> pearls = new Property<Boolean>(false, new String[]{"Pearls", "pearl", "p"}, "Draws esp on ender pearls.");
    protected final NumberProperty<Float> lineWidth = new NumberProperty<Float>(Float.valueOf(1.0f), Float.valueOf(1.0f), Float.valueOf(4.0f), Float.valueOf(0.1f), new String[]{"WireWidth", "width", "linewidth"}, "Thickness of the line.");
    protected final ColorProperty color = new ColorProperty(new Color(-1), true, new String[]{"Color", "colour"});
    protected final NumberProperty<Integer> boxAlpha = new NumberProperty<Integer>(85, 0, 255, new String[]{"BoxAlpha", "alpha", "b"}, "Alpha of the box.");
    protected final Property<Boolean> itemNametags = new Property<Boolean>(false, new String[]{"ItemNametags", "tags", "itemsname"}, "Draws nametags on dropped items with the item count and the name of the item.");
    protected final Property<Boolean> chorus = new Property<Boolean>(false, new String[]{"ChorusFruit", "Chorus", "Teleports"}, "Draws text on where chorus fruit teleport sounds spawn.");
    protected final NumberProperty<Float> scaling = new NumberProperty<Float>(Float.valueOf(0.3f), Float.valueOf(0.1f), Float.valueOf(1.0f), Float.valueOf(0.1f), new String[]{"Scaling", "scale"}, "Size of the item nametags.");
    protected final ColorProperty nametagColor = new ColorProperty(new Color(-1), false, new String[]{"NametagColor", "nametagcolor", "tagcolor"});
    protected BlockPos teleportPos;
    protected final StopWatch teleportTimer = new StopWatch();

    public ESP() {
        super("ESP", new String[]{"ESP", "ItemEsp", "EntityEsp"}, "Draws boxes and outlines on entites.", Category.RENDER);
        this.offerListeners(new ListenerRender(this), new ListenerSound(this));
        this.offerProperties(this.mode, this.itemNametags, this.chorus, this.xpBottles, this.items, this.pearls, this.lineWidth, this.color, this.boxAlpha, this.scaling, this.nametagColor);
    }

    protected Color getColor() {
        return this.color.getColor();
    }

    protected Color getNametagColor() {
        return this.nametagColor.getColor();
    }

    protected Color getBoxColor() {
        return ColorUtil.changeAlpha(this.color.getColor(), (Integer)this.boxAlpha.getValue());
    }

    protected boolean isValid(Entity entity) {
        boolean valid = false;
        if (entity instanceof EntityItem && this.items.getValue().booleanValue()) {
            valid = true;
        } else if (entity instanceof EntityExpBottle && this.xpBottles.getValue().booleanValue()) {
            valid = true;
        } else if (entity instanceof EntityEnderPearl && this.pearls.getValue().booleanValue()) {
            valid = true;
        }
        return valid;
    }

    protected void doRender(AxisAlignedBB bb) {
        RenderUtil.startRender();
        switch ((RenderMode)((Object)this.mode.getValue())) {
            case BOX: {
                this.renderBox(bb);
                break;
            }
            case OUTLINE: {
                this.renderOutline(bb);
                break;
            }
            case BOTH: {
                this.renderBox(bb);
                this.renderOutline(bb);
            }
        }
        RenderUtil.endRender();
    }

    protected void renderBox(AxisAlignedBB bb) {
        RenderUtil.drawBox(bb, this.getBoxColor());
    }

    public void renderOutline(AxisAlignedBB bb) {
        RenderUtil.drawOutline(bb, ((Float)this.lineWidth.getValue()).floatValue(), this.getColor());
    }

    protected void renderNameTag(String name, double x, double y, double z) {
        double distance = RenderUtil.getEntity().getDistance(x + ESP.mc.getRenderManager().viewerPosX, y + ESP.mc.getRenderManager().viewerPosY, z + ESP.mc.getRenderManager().viewerPosZ);
        int width = Managers.FONT.getStringWidth(name) >> 1;
        double scale = 0.0018 + (double)MathUtil.fixedNametagScaling(((Float)this.scaling.getValue()).floatValue()) * distance;
        if (distance <= 8.0) {
            scale = 0.0245;
        }
        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.translate((float)((float)x), (float)((float)y + 0.4f), (float)((float)z));
        GlStateManager.rotate((float)(-ESP.mc.getRenderManager().playerViewY), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)ESP.mc.getRenderManager().playerViewX, (float)(ESP.mc.gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f), (float)0.0f, (float)0.0f);
        GlStateManager.scale((double)(-scale), (double)(-scale), (double)scale);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        Managers.FONT.drawString(name, -width, -8.0f, this.getNametagColor().getRGB());
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
}

