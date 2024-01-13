/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.util.math.AxisAlignedBB
 */
package me.chachoox.lithium.impl.modules.render.logoutspots;

import java.awt.Color;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.ColorProperty;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.math.MathUtil;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.api.util.render.Render2DUtil;
import me.chachoox.lithium.api.util.render.RenderUtil;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.other.colours.Colours;
import me.chachoox.lithium.impl.modules.render.logoutspots.ListenerJoin;
import me.chachoox.lithium.impl.modules.render.logoutspots.ListenerLeave;
import me.chachoox.lithium.impl.modules.render.logoutspots.ListenerLogout;
import me.chachoox.lithium.impl.modules.render.logoutspots.ListenerRender;
import me.chachoox.lithium.impl.modules.render.logoutspots.mode.RenderMode;
import me.chachoox.lithium.impl.modules.render.logoutspots.util.LogoutSpot;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.math.AxisAlignedBB;

public class LogoutSpots
extends Module {
    protected final EnumProperty<RenderMode> render = new EnumProperty<RenderMode>(RenderMode.OUTLINE, new String[]{"Render", "ghost", "outline", "mode", "type"}, "Outline: - Renders a outlined box with a nametag at the logout spot / Ghost: - Renders the player's skin and a nametag at the logout spot.");
    protected final NumberProperty<Float> scaling = new NumberProperty<Float>(Float.valueOf(0.3f), Float.valueOf(0.1f), Float.valueOf(1.0f), Float.valueOf(0.1f), new String[]{"Scale", "size"}, "The scale of the nametags.");
    protected final Property<Boolean> rect = new Property<Boolean>(true, new String[]{"Rect", "rectangle"}, "Draws a rectangle behind the nametag text.");
    protected final Property<Boolean> syncBorder = new Property<Boolean>(false, new String[]{"GlobalOutline", "syncoutline"}, "Syncs the outline color.");
    protected final Property<Boolean> message = new Property<Boolean>(false, new String[]{"Message", "msg"}, "Sends a message whenever a cached player joins / leaves the server.");
    protected final ColorProperty textColor = new ColorProperty(new Color(-1), false, new String[]{"TextColor", "infocolor"});
    protected final ColorProperty boxColor = new ColorProperty(new Color(255, 255, 255, 255), true, new String[]{"BoxColor", "OutlineColor"});
    protected final ColorProperty ghostColor = new ColorProperty(new Color(188, 188, 188, 88), false, new String[]{"GhostColor", "modelcolor"});
    public final Map<UUID, LogoutSpot> spots = new ConcurrentHashMap<UUID, LogoutSpot>();
    protected StopWatch timer = new StopWatch();

    public LogoutSpots() {
        super("LogoutSpots", new String[]{"LogoutSpots", "logout", "logspots"}, "Shows where players logged out.", Category.RENDER);
        this.offerProperties(this.render, this.scaling, this.rect, this.syncBorder, this.message, this.textColor, this.boxColor, this.ghostColor);
        this.offerListeners(new ListenerLogout(this), new ListenerJoin(this), new ListenerLeave(this), new ListenerRender(this));
    }

    @Override
    public void onDisable() {
        this.spots.clear();
    }

    @Override
    public void onWorldLoad() {
        this.spots.clear();
        this.timer.reset();
    }

    public Color getTextColor() {
        return this.textColor.getColor();
    }

    public Color getBoxColor() {
        return this.boxColor.getColor();
    }

    public Color getGhostColor() {
        return this.ghostColor.getColor();
    }

    protected void renderNameTag(String text, AxisAlignedBB interpolated) {
        double x = (interpolated.minX + interpolated.maxX) / 2.0;
        double y = (interpolated.minY + interpolated.maxY) / 2.0;
        double z = (interpolated.minZ + interpolated.maxZ) / 2.0;
        this.renderNameTag(text, x, y - 0.2, z);
    }

    protected void renderNameTag(String name, double x, double y, double z) {
        double distance = RenderUtil.getEntity().getDistance(x + LogoutSpots.mc.getRenderManager().viewerPosX, y + LogoutSpots.mc.getRenderManager().viewerPosY, z + LogoutSpots.mc.getRenderManager().viewerPosZ);
        int width = Managers.FONT.getStringWidth(name) >> 1;
        double scale = 0.0018 + (double)MathUtil.fixedNametagScaling(((Float)this.scaling.getValue()).floatValue()) * distance;
        if (distance <= 8.0) {
            scale = 0.0245;
        }
        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.translate((float)((float)x), (float)((float)y + 1.4f), (float)((float)z));
        GlStateManager.rotate((float)(-LogoutSpots.mc.getRenderManager().playerViewY), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)LogoutSpots.mc.getRenderManager().playerViewX, (float)(LogoutSpots.mc.gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f), (float)0.0f, (float)0.0f);
        GlStateManager.scale((double)(-scale), (double)(-scale), (double)scale);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        if (this.rect.getValue().booleanValue()) {
            int borderCol = this.syncBorder.getValue() != false ? Colours.get().getColour().getRGB() : 0x6F000000;
            Render2DUtil.drawNameTagRect(-width - 2, -(LogoutSpots.mc.fontRenderer.FONT_HEIGHT + 1), (float)width + 2.0f, 1.5f, 0x53000000, borderCol, 1.4f);
        }
        Managers.FONT.drawString(name, -width, -8.0f, this.getTextColor().getRGB());
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
}

