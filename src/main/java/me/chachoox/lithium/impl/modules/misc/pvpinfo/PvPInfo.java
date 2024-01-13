/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 */
package me.chachoox.lithium.impl.modules.misc.pvpinfo;

import java.awt.Color;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.property.StringProperty;
import me.chachoox.lithium.api.util.entity.EntityUtil;
import me.chachoox.lithium.api.util.inventory.ItemUtil;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.api.util.network.NetworkUtil;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.misc.pvpinfo.ListenerRender;
import me.chachoox.lithium.impl.modules.misc.pvpinfo.ListenerUpdate;
import me.chachoox.lithium.impl.modules.other.hud.Hud;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;

public class PvPInfo
extends Module {
    private final NumberProperty<Integer> yPos = new NumberProperty<Integer>(0, -400, 40, new String[]{"PosY", "y"}, "Y position of the hud.");
    private final NumberProperty<Integer> xPos = new NumberProperty<Integer>(2, 2, 720, new String[]{"PosX", "x"}, "X position of the hud.");
    private final Property<Boolean> watermark = new Property<Boolean>(true, new String[]{"Watermark", "wmark", "mark"}, "Draws a watermark onto the hud.");
    private final Property<Boolean> offWaterMark = new Property<Boolean>(true, new String[]{"OffsetWatermark", "offwmark"}, "Separates the watermark from the rest of the hud.");
    private final StringProperty watermarkString = new StringProperty("sexmaster.cc", new String[]{"WatermarkName", "markname"});
    private final Property<Boolean> htr = new Property<Boolean>(true, new String[]{"Htr", "ht"}, "Draws hit range helper onto the hud.");
    private final Property<Boolean> plr = new Property<Boolean>(true, new String[]{"Plr", "pl"}, "Draws player range helper onto the hud.");
    private final Property<Boolean> totems = new Property<Boolean>(true, new String[]{"Totems", "tots"}, "Draws totem counter onto the hud.");
    private final Property<Boolean> ping = new Property<Boolean>(true, new String[]{"Ping", "latency"}, "Draws ping counter onto the hud.");
    private final Property<Boolean> lby = new Property<Boolean>(true, new String[]{"Lby", "lb"}, "Draws player range safety onto the hud.");
    private final Property<Boolean> pearls = new Property<Boolean>(true, new String[]{"Pearls", "pearlsnotify"}, "Sends a message saying when a player threw a pearl.");
    private final Property<Boolean> pearlsCooldown = new Property<Boolean>(false, new String[]{"PearlCooldown", "pearlcool", "cooldown"}, "Renders pearl cooldown.");
    private final int GREEN = new Color(75, 255, 0).getRGB();
    private final int RED = Color.RED.getRGB();
    protected final ConcurrentHashMap<UUID, Integer> pearlsUUIDs = new ConcurrentHashMap();
    protected final StopWatch pearlTimer = new StopWatch();
    protected boolean pearlThrown = false;

    public PvPInfo() {
        super("PvPInfo", new String[]{"PvPInfo", "pvpinformation", "dotgodmodule"}, "Information for pvp.", Category.MISC);
        this.offerProperties(this.yPos, this.xPos, this.watermark, this.offWaterMark, this.watermarkString, this.htr, this.plr, this.totems, this.ping, this.lby, this.pearls, this.pearlsCooldown);
        this.offerListeners(new ListenerRender(this), new ListenerUpdate(this));
    }

    protected void onRender(ScaledResolution resolution) {
        int width = resolution.getScaledWidth() / 2;
        int height = resolution.getScaledHeight() / 2;
        int offsetY = (Integer)this.yPos.getValue() + height;
        int x = (Integer)this.xPos.getValue();
        if (this.watermark.getValue().booleanValue()) {
            String watermarkStr = (String)this.watermarkString.getValue();
            Managers.MODULE.get(Hud.class).renderText(watermarkStr, x, this.offWaterMark.getValue() == false ? offsetY : offsetY - 10);
            offsetY += 10;
        }
        if (this.htr.getValue().booleanValue()) {
            String htrStr = "HTR";
            Managers.FONT.drawString(htrStr, x, offsetY, this.getHTRColor());
            offsetY += 10;
        }
        if (this.plr.getValue().booleanValue()) {
            String plrStr = "PLR";
            Managers.FONT.drawString(plrStr, x, offsetY, this.getPLRColor());
            offsetY += 10;
        }
        if (this.totems.getValue().booleanValue()) {
            int totems = ItemUtil.getItemCount(Items.TOTEM_OF_UNDYING);
            String totemStr = String.valueOf(totems);
            Managers.FONT.drawString(totemStr, x, offsetY, this.getTotemColor(totems));
            offsetY += 10;
        }
        if (this.ping.getValue().booleanValue()) {
            String pingStr = "PING " + NetworkUtil.getLatency();
            Managers.FONT.drawString(pingStr, x, offsetY, this.getPingColor());
            offsetY += 10;
        }
        if (this.lby.getValue().booleanValue()) {
            String lbyStr = "LBY";
            Managers.FONT.drawString(lbyStr, x, offsetY, this.getLBYColor());
        }
        if (this.pearlsCooldown.getValue().booleanValue() && !this.pearlTimer.passed(15000L)) {
            int pearlY = resolution.getScaledHeight() - 80;
            int pearlX = width - 189 + 180 + 2;
            String pearlString = String.format("%.2f", Float.valueOf((float)this.pearlTimer.getTime() / 1000.0f)) + "s";
            Managers.MODULE.get(Hud.class).renderText(pearlString, pearlX, pearlY + 9);
        }
    }

    private int getHTRColor() {
        EntityPlayer entity = EntityUtil.getClosestEnemy();
        if (entity != null && PvPInfo.mc.player.getDistance((Entity)entity) < 8.5f) {
            return this.GREEN;
        }
        return this.RED;
    }

    private int getPLRColor() {
        EntityPlayer entity = EntityUtil.getClosestEnemy();
        if (entity != null && PvPInfo.mc.player.getDistance((Entity)entity) < 5.5f) {
            return this.GREEN;
        }
        return this.RED;
    }

    private int getLBYColor() {
        EntityPlayer entity = EntityUtil.getClosestEnemy();
        if (entity != null && PvPInfo.mc.player.getDistance((Entity)entity) < 8.5f && EntityUtil.isPlayerSafe(entity)) {
            return this.GREEN;
        }
        return this.RED;
    }

    private int getTotemColor(int totems) {
        if (totems > 0) {
            return this.GREEN;
        }
        return this.RED;
    }

    private int getPingColor() {
        if (NetworkUtil.getLatency() > 150) {
            return this.RED;
        }
        return this.GREEN;
    }

    protected boolean pearls() {
        return this.pearls.getValue();
    }
}

