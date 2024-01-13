/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.material.Material
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.resources.I18n
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.item.ItemAir
 *  net.minecraft.item.ItemStack
 *  net.minecraft.potion.Potion
 *  net.minecraft.potion.PotionEffect
 *  net.minecraft.util.math.MathHelper
 */
package me.chachoox.lithium.impl.modules.other.hud;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.property.StringProperty;
import me.chachoox.lithium.api.util.colors.ColorUtil;
import me.chachoox.lithium.api.util.entity.EntityUtil;
import me.chachoox.lithium.api.util.inventory.ItemUtil;
import me.chachoox.lithium.api.util.math.MathUtil;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.api.util.network.NetworkUtil;
import me.chachoox.lithium.api.util.render.animation.Animation;
import me.chachoox.lithium.api.util.render.animation.Direction;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.misc.pingspoof.PingSpoof;
import me.chachoox.lithium.impl.modules.other.colours.Colours;
import me.chachoox.lithium.impl.modules.other.hud.ListenerGameLoop;
import me.chachoox.lithium.impl.modules.other.hud.ListenerReceive;
import me.chachoox.lithium.impl.modules.other.hud.ListenerRender;
import me.chachoox.lithium.impl.modules.other.hud.ListenerSend;
import me.chachoox.lithium.impl.modules.other.hud.ListenerUpdate;
import me.chachoox.lithium.impl.modules.other.hud.mode.ArmorMode;
import me.chachoox.lithium.impl.modules.other.hud.mode.ArmorText;
import me.chachoox.lithium.impl.modules.other.hud.mode.ArrayListMode;
import me.chachoox.lithium.impl.modules.other.hud.mode.DurationColour;
import me.chachoox.lithium.impl.modules.other.hud.mode.HudColour;
import me.chachoox.lithium.impl.modules.other.hud.mode.HudRainbow;
import me.chachoox.lithium.impl.modules.other.hud.mode.InfoColour;
import me.chachoox.lithium.impl.modules.other.hud.mode.Organize;
import me.chachoox.lithium.impl.modules.other.hud.mode.PotionMode;
import me.chachoox.lithium.impl.modules.other.hud.mode.Rendering;
import me.chachoox.lithium.impl.modules.other.hud.mode.TpsMode;
import me.chachoox.lithium.impl.modules.other.hud.mode.Watermark;
import me.chachoox.lithium.impl.modules.other.hud.mode.Welcomer;
import me.chachoox.lithium.impl.modules.other.hud.util.ItemHolder;
import me.chachoox.lithium.impl.modules.other.hud.util.TextRadarUtil;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.MathHelper;

public class Hud
extends Module {
    protected final EnumProperty<Rendering> rendering = new EnumProperty<Rendering>(Rendering.UP, new String[]{"Rendering", "rendr"}, "Direction to render stuff.");
    protected final EnumProperty<Organize> organize = new EnumProperty<Organize>(Organize.LENGTH, new String[]{"Organize", "org"}, "The ordering of the arraylist.");
    protected final EnumProperty<ArrayListMode> arrayList = new EnumProperty<ArrayListMode>(ArrayListMode.GRAY, new String[]{"Arraylist", "modlist", "ArraylistColor"}, "Renders the active modules.");
    protected final EnumProperty<HudColour> hudColour = new EnumProperty<HudColour>(HudColour.GLOBAL, new String[]{"HudColour", "hudcolor"}, "The color of the hud.");
    protected final EnumProperty<HudRainbow> rainbow = new EnumProperty<HudRainbow>(HudRainbow.HORIZONTAL, new String[]{"Rainbow", "hudrainbow"}, "The type of rainbow.");
    protected final EnumProperty<InfoColour> infoColour = new EnumProperty<InfoColour>(InfoColour.GLOBAL, new String[]{"InfoColour", "infocolor"}, "Color of information.");
    protected final EnumProperty<PotionMode> potionColour = new EnumProperty<PotionMode>(PotionMode.VANILLA, new String[]{"Potions", "potioncol", "potioncolor"}, "Color of the potions.");
    protected final EnumProperty<DurationColour> potionNumberColor = new EnumProperty<DurationColour>(DurationColour.GRAY, new String[]{"DurationColour", "potionnumbercolor"}, "Changes the duration color of potion effects.");
    protected final Property<Boolean> capes = new Property<Boolean>(false, new String[]{"Capes", "cap", "capejamin"}, "Capes for client users.");
    protected final Property<Boolean> rainbowWatermark = new Property<Boolean>(false, new String[]{"RainbowWatermark", "rainbowmark"}, "Changes the messages prefix to a rainbow");
    protected final Property<Boolean> announceModules = new Property<Boolean>(false, new String[]{"AnnounceModules", "modulenotify"}, "Announces in chat when you toggle a module.");
    public final Property<Boolean> shadow = new Property<Boolean>(true, new String[]{"Shadow", "shadw"}, "Tweaks the shadows in the font.");
    protected final EnumProperty<Watermark> watermark = new EnumProperty<Watermark>(Watermark.LITHIUM, new String[]{"Watermark", "mark"}, "Client watermark.");
    protected final Property<Boolean> offsetWatermark = new Property<Boolean>(false, new String[]{"OffsetWatermark", "offsetmark"}, "Vertical offset of the watermark");
    protected final StringProperty customWatermark = new StringProperty("SexMaster.CC", new String[]{"CustomMark", "customwatermark"});
    protected final EnumProperty<Welcomer> welcomer = new EnumProperty<Welcomer>(Welcomer.NONE, new String[]{"Welcomer", "welcome"}, "Hello >:3.");
    protected final StringProperty customWelcome = new StringProperty("Selamat datang <Player>", new String[]{"CustomWelcome", "customwelcomer"});
    protected final Property<Boolean> leftSideWelcomer = new Property<Boolean>(false, new String[]{"LeftSideWelcomer", "leftsidewelcome"}, "Draws the welcomer under the watermark if you have it enabled.");
    protected final Property<Boolean> textRadar = new Property<Boolean>(false, new String[]{"TextRadar", "texradar"}, "Draws a list of the people in our render.");
    protected final EnumProperty<ArmorMode> armor = new EnumProperty<ArmorMode>(ArmorMode.DAMAGE, new String[]{"Armor", "aa"}, "Damage: - Colors damage text by damage percent / Global: - Colors damage text as the global color.");
    protected final EnumProperty<ArmorText> armorText = new EnumProperty<ArmorText>(ArmorText.NEW, new String[]{"ArmorText", "armortex"}, "Draws the durability of our armor.");
    protected final Property<Boolean> totems = new Property<Boolean>(false, new String[]{"Totems", "tots"}, "Draws how many totems we carry.");
    protected final Property<Boolean> items = new Property<Boolean>(false, new String[]{"Items", "item"}, "Draws how many gear we carry.");
    protected final Property<Boolean> rotations = new Property<Boolean>(false, new String[]{"Rotations", "rots"}, "Draws our current rotations.");
    protected final Property<Boolean> coords = new Property<Boolean>(true, new String[]{"Coordinates", "coords", "coord"}, "Draws our current coordinates.");
    protected final Property<Boolean> kmh = new Property<Boolean>(true, new String[]{"Speed", "sped", "kmh", "bps"}, "Draws our current movement speed.");
    protected final Property<Boolean> brand = new Property<Boolean>(false, new String[]{"ServerBrand", "brand"}, "Draws the server brand.");
    protected final EnumProperty<TpsMode> tps = new EnumProperty<TpsMode>(TpsMode.BOTH, new String[]{"Tps", "Ticks", "TicksPerSecond"}, "Renders the servers ticks per second.");
    protected final Property<Boolean> packets = new Property<Boolean>(false, new String[]{"Packets", "packet", "packetpersecond"}, "Draws how many packets we sent per second.");
    protected final Property<Boolean> ping = new Property<Boolean>(true, new String[]{"Ping", "p", "latency"}, "Draws our ping.");
    protected final Property<Boolean> fps = new Property<Boolean>(false, new String[]{"Fps", "frames", "fp"}, "Draws our fps.");
    protected final Property<Boolean> lag = new Property<Boolean>(false, new String[]{"Lag", "ddos", "impcatnotify", "4/9/2023"}, "Draws how long the server has stopped responding.");
    protected int outgoingPackets;
    protected int incomingPackets;
    protected StopWatch timer = new StopWatch();
    protected StopWatch lagTimer = new StopWatch();
    protected Collection<Module> modules;
    protected final LinkedList<Long> frames = new LinkedList();
    protected int fpsCount;
    private final ItemStack TOTEM = new ItemStack(Items.TOTEM_OF_UNDYING);
    private final ItemStack EXPERIENCE = new ItemStack(Items.EXPERIENCE_BOTTLE);
    private final ItemStack GAP = new ItemStack(Items.GOLDEN_APPLE);
    private final ItemStack CRYSTAL = new ItemStack(Items.END_CRYSTAL);

    public Hud() {
        super("HUD", new String[]{"HUD", "huud", "huuud"}, "Hud elements.", Category.OTHER);
        this.offerProperties(this.rendering, this.organize, this.arrayList, this.hudColour, this.rainbow, this.infoColour, this.potionColour, this.potionNumberColor, this.capes, this.rainbowWatermark, this.announceModules, this.shadow, this.watermark, this.offsetWatermark, this.customWatermark, this.welcomer, this.customWelcome, this.leftSideWelcomer, this.textRadar, this.armor, this.armorText, this.totems, this.items, this.rotations, this.coords, this.kmh, this.brand, this.tps, this.packets, this.ping, this.fps, this.lag);
        this.offerListeners(new ListenerUpdate(this), new ListenerGameLoop(this), new ListenerReceive(this), new ListenerSend(this), new ListenerRender(this));
        this.arrayList.addObserver(event -> this.resetAnimation());
    }

    @Override
    public void onLoad() {
        this.modules = Managers.MODULE.getModules();
    }

    @Override
    public void onWorldLoad() {
        this.resetAnimation();
    }

    private void resetAnimation() {
        for (Module module : this.modules) {
            if (module.isHidden()) {
                module.getAnimation().finished(Direction.BACKWARDS);
                continue;
            }
            module.getAnimation().finished(Direction.FORWARDS);
        }
    }

    protected void onRender(ScaledResolution resolution) {
        int width = resolution.getScaledWidth();
        int height = resolution.getScaledHeight();
        if (this.watermark.getValue() != Watermark.NONE) {
            this.renderText(this.watermark.getValue() == Watermark.CUSTOM ? (String)this.customWatermark.getValue() : ((Watermark)((Object)this.watermark.getValue())).getWatermark(), 2.0f, this.offsetWatermark.getValue() != false ? 12.0f : 2.0f);
        }
        if (this.welcomer.getValue() != Welcomer.NONE) {
            String welcome = ((Welcomer)((Object)this.welcomer.getValue())).getWelcomer();
            if (this.welcomer.getValue() == Welcomer.CUSTOM) {
                welcome = (String)this.customWelcome.getValue();
            }
            welcome = welcome.replace("<Player>", Hud.mc.player.getName());
            welcome = welcome.replace("<Time>", this.getTimeOfDay());
            if (!this.leftSideWelcomer.getValue().booleanValue()) {
                this.renderText(welcome, (float)resolution.getScaledWidth() / 2.0f - (float)Managers.FONT.getStringWidth(welcome) / 2.0f + 2.0f, 2.0f);
            } else {
                this.renderText(welcome, 2.0f, this.offsetWatermark.getValue() != false ? 24.0f : 12.0f);
            }
        }
        if (this.lag.getValue().booleanValue() && this.lagTimer.passed(2500L) && !mc.isSingleplayer()) {
            String lagString = "Server hasn't responded in " + String.format("%.2f", Float.valueOf((float)this.lagTimer.getTime() / 1000.0f)) + "s";
            this.renderText(lagString, (float)resolution.getScaledWidth() / 2.0f - (float)Managers.FONT.getStringWidth(lagString) / 2.0f + 2.0f, this.welcomer.getValue() != Welcomer.NONE ? 12.0f : 2.0f);
        }
        if (this.textRadar.getValue().booleanValue()) {
            this.renderTextRadar((this.offsetWatermark.getValue() != false && this.watermark.getValue() != Watermark.NONE ? 28 : 18) + (this.welcomer.getValue() != Welcomer.NONE && this.leftSideWelcomer.getValue() != false ? 10 : 0));
        }
        boolean renderingUp = this.rendering.getValue() == Rendering.UP;
        boolean chatOpened = Hud.mc.ingameGUI.getChatGUI().getChatOpen();
        if (this.arrayList.getValue() != ArrayListMode.NONE) {
            this.modules = Managers.MODULE.getModules();
            ArrayList<Module> modulesList = new ArrayList<Module>(this.modules);
            int offset = renderingUp ? 2 : height - (chatOpened ? 24 : 10);
            switch ((Organize)((Object)this.organize.getValue())) {
                case ABC: {
                    modulesList.sort(Comparator.comparing(mod -> (String)mod.displayLabel.getValue()));
                    break;
                }
                case LENGTH: {
                    modulesList.sort((mod1, mod2) -> Managers.FONT.getStringWidth(mod2.getFullLabel()) - Managers.FONT.getStringWidth(mod1.getFullLabel()));
                }
            }
            for (Module module : modulesList) {
                if (module.isHidden()) continue;
                Animation moduleAnimation = module.getAnimation();
                moduleAnimation.setDirection(module.isEnabled() ? Direction.FORWARDS : Direction.BACKWARDS);
                if (!module.isEnabled() && moduleAnimation.finished(Direction.BACKWARDS)) continue;
                String fullLabel = module.getFullLabel();
                int x = width - Managers.FONT.getStringWidth(fullLabel);
                x = (int)((double)x + Math.abs((moduleAnimation.getOutput() - 1.0) * (double)Managers.FONT.getStringWidth(fullLabel)));
                this.renderArrayList(module, fullLabel, x - 2, offset);
                offset += renderingUp ? 10 : -10;
            }
        }
        int offset = renderingUp ? (chatOpened ? 24 : 10) : 2;
        int y = 10;
        if (this.potionColour.getValue() != PotionMode.NONE) {
            for (PotionEffect effect : Hud.mc.player.getActivePotionEffects()) {
                int amplifier = effect.getAmplifier();
                String potionString = I18n.format((String)effect.getEffectName(), (Object[])new Object[0]) + (amplifier > 0 ? " " + (amplifier + 1) + "" : "") + ": " + this.getPotionNumberColor((DurationColour)((Object)this.potionNumberColor.getValue())) + Potion.getPotionDurationString((PotionEffect)effect, (float)1.0f);
                int potionColor = effect.getPotion().getLiquidColor();
                if (this.potionColour.getValue() != PotionMode.GLOBAL) {
                    Managers.FONT.drawString(potionString, width - Managers.FONT.getStringWidth(potionString) - 2, renderingUp ? (float)(height - offset) : (float)offset, potionColor);
                } else {
                    this.renderText(potionString, width - Managers.FONT.getStringWidth(potionString) - 2, renderingUp ? (float)(height - offset) : (float)offset);
                }
                offset += y;
            }
        }
        if (this.brand.getValue().booleanValue()) {
            String brandString = this.getInfoColour(false) + (mc.getCurrentServerData() == null ? "Vanilla" : Hud.mc.player.getServerBrand());
            this.renderText(brandString, width - Managers.FONT.getStringWidth(brandString) - 2, renderingUp ? (float)(height - offset) : (float)offset);
            offset += y;
        }
        if (this.kmh.getValue().booleanValue()) {
            String kmhString = this.getInfoColour(false) + "Speed: " + this.getInfoColour(true) + MathUtil.round(Managers.SPEED.getSpeed(), 2) + "km/h";
            this.renderText(kmhString, width - Managers.FONT.getStringWidth(kmhString) - 2, renderingUp ? (float)(height - offset) : (float)offset);
            offset += y;
        }
        if (this.tps.getValue() != TpsMode.NONE) {
            String tpsString = null;
            switch ((TpsMode)((Object)this.tps.getValue())) {
                case NORMAL: {
                    tpsString = this.getInfoColour(false) + "TPS: " + this.getInfoColour(true) + MathUtil.round(Managers.TPS.getTps(), 2);
                    break;
                }
                case BOTH: {
                    tpsString = this.getInfoColour(false) + "TPS: " + this.getInfoColour(true) + MathUtil.round(Managers.TPS.getTps(), 2) + "\u00a77" + " (" + this.getInfoColour(true) + MathUtil.round(Managers.TPS.getCurrentTps(), 2) + "\u00a77" + ")";
                }
            }
            this.renderText(tpsString, width - Managers.FONT.getStringWidth(tpsString) - 2, renderingUp ? (float)(height - offset) : (float)offset);
            offset += y;
        }
        if (this.packets.getValue().booleanValue()) {
            String packetString = this.getInfoColour(false) + "Packets: " + this.getInfoColour(true) + this.outgoingPackets + "/s";
            this.renderText(packetString, width - Managers.FONT.getStringWidth(packetString) - 2, renderingUp ? (float)(height - offset) : (float)offset);
            offset += y;
        }
        if (this.ping.getValue().booleanValue() && !mc.isSingleplayer() && mc.getConnection().getPlayerInfo(Hud.mc.player.getUniqueID()) != null) {
            String pingString = this.getInfoColour(false) + "Ping: " + this.getInfoColour(true) + NetworkUtil.getLatency() + "ms" + (Managers.MODULE.get(PingSpoof.class).isEnabled() ? "\u00a77 (" + this.getInfoColour(true) + NetworkUtil.getLatencyNoSpoof() + "ms" + "\u00a77" + ")" : "").replace("-", "");
            this.renderText(pingString, width - Managers.FONT.getStringWidth(pingString) - 2, renderingUp ? (float)(height - offset) : (float)offset);
            offset += y;
        }
        if (this.fps.getValue().booleanValue()) {
            String fpsString = this.getInfoColour(false) + "FPS: " + this.getInfoColour(true) + this.fpsCount;
            this.renderText(fpsString, width - Managers.FONT.getStringWidth(fpsString) - 2, renderingUp ? (float)(height - offset) : (float)offset);
        }
        if (this.rotations.getValue().booleanValue()) {
            String colorFix = this.hudColour.getValue() == HudColour.RAINBOW ? ((HudRainbow)((Object)this.rainbow.getValue())).getColor() : this.getInfoColour(false);
            this.renderText(this.getInfoColour(false) + "Pitch: " + this.getInfoColour(true) + String.format("%.2f", Float.valueOf(MathHelper.wrapDegrees((float)Hud.mc.player.rotationPitch))) + colorFix + " Yaw: " + this.getInfoColour(true) + String.format("%.2f", Float.valueOf(MathHelper.wrapDegrees((float)Hud.mc.player.rotationYaw))), 2.0f, height - (this.coords.getValue().booleanValue() ? (chatOpened ? 34 : 20) : (chatOpened ? 24 : 10)));
        }
        if (this.coords.getValue().booleanValue()) {
            String directionString = this.getDirectionForDisplay();
            String coordsString = this.getInfoColour(false) + "XYZ: " + this.getInfoColour(true) + this.getRoundedDouble(Hud.mc.player.posX) + "\u00a77" + ", " + this.getInfoColour(true) + this.getRoundedDouble(Hud.mc.player.posY) + "\u00a77" + ", " + this.getInfoColour(true) + this.getRoundedDouble(Hud.mc.player.posZ);
            if (Hud.mc.player.dimension != 1) {
                coordsString = coordsString + "\u00a77 (" + this.getInfoColour(true) + this.getRoundedDouble(this.getDimensionCoord(Hud.mc.player.posX)) + "\u00a77" + ", " + this.getInfoColour(true) + this.getRoundedDouble(this.getDimensionCoord(Hud.mc.player.posZ)) + "\u00a77" + ")";
            }
            this.renderText(coordsString + directionString, 2.0f, height - (chatOpened ? 24 : 10));
        }
        if (!Hud.mc.player.isSpectator()) {
            this.renderArmorHUD(resolution);
            if (this.totems.getValue().booleanValue()) {
                this.renderTotemHUD(resolution);
            }
            if (this.items.getValue().booleanValue()) {
                this.renderItemHUD(resolution);
            }
        }
    }

    public int getArmorY() {
        int y;
        if (Hud.mc.player.isInsideOfMaterial(Material.WATER) && Hud.mc.player.getAir() > 0 && !Hud.mc.player.capabilities.isCreativeMode) {
            y = 65;
        } else if (Hud.mc.player.getRidingEntity() != null && !Hud.mc.player.capabilities.isCreativeMode) {
            if (Hud.mc.player.getRidingEntity() instanceof EntityLivingBase) {
                EntityLivingBase entity = (EntityLivingBase)Hud.mc.player.getRidingEntity();
                y = (int)(45.0 + Math.ceil((entity.getMaxHealth() - 1.0f) / 20.0f) * 10.0);
            } else {
                y = 45;
            }
        } else {
            y = Hud.mc.player.capabilities.isCreativeMode ? (Hud.mc.player.isRidingHorse() ? 45 : 38) : 55;
        }
        return y;
    }

    public void renderArmorHUD(ScaledResolution resolution) {
        GlStateManager.enableTexture2D();
        int width = resolution.getScaledWidth() >> 1;
        int height = resolution.getScaledHeight();
        int i2 = 15;
        int i1 = 3;
        int i3 = 3;
        while (i3 >= 0) {
            ItemStack stack = (ItemStack)Hud.mc.player.inventory.armorInventory.get(i1);
            if (!(stack.getItem() instanceof ItemAir)) {
                int color;
                boolean renderArmor = this.armor.getValue() != ArmorMode.NONE;
                int y = height - this.getArmorY();
                int x = width + i2;
                int n = color = this.armor.getValue() != ArmorMode.GLOBAL ? stack.getItem().getRGBDurabilityForDisplay(stack) : Colours.get().getColour().getRGB();
                if (renderArmor) {
                    GlStateManager.enableDepth();
                    Hud.mc.getRenderItem().zLevel = 200.0f;
                    mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
                    mc.getRenderItem().renderItemOverlayIntoGUI(Hud.mc.fontRenderer, stack, x, y, "");
                    Hud.mc.getRenderItem().zLevel = 0.0f;
                    GlStateManager.enableTexture2D();
                    GlStateManager.disableLighting();
                    GlStateManager.disableDepth();
                    String count = stack.getCount() > 1 ? stack.getCount() + "" : "";
                    Managers.FONT.drawString(count, x + 19 - 2 - Managers.FONT.getStringWidth(count), y + 9, -1);
                }
                i2 += 18;
                int dmg = (int)ItemUtil.getDamageInPercent(stack);
                switch ((ArmorText)((Object)this.armorText.getValue())) {
                    case OLD: {
                        Managers.FONT.drawString(dmg + "", x + 8 - (Hud.mc.fontRenderer.getStringWidth(dmg + "") >> 1), y + (renderArmor ? -8 : 6), color);
                        break;
                    }
                    case NEW: {
                        GlStateManager.pushMatrix();
                        GlStateManager.scale((float)0.625f, (float)0.625f, (float)0.625f);
                        GlStateManager.disableDepth();
                        Hud.mc.fontRenderer.drawStringWithShadow(dmg + "%", (float)(x + this.getFixedArmorOffset(dmg)) * 1.6f, (float)y * 1.6f + (float)(renderArmor ? -8 : 16), color);
                        GlStateManager.enableDepth();
                        GlStateManager.scale((float)1.0f, (float)1.0f, (float)1.0f);
                        GlStateManager.popMatrix();
                    }
                }
            }
            i3 = --i1;
        }
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
    }

    public int getFixedArmorOffset(int percent) {
        if (percent == 100) {
            return 1;
        }
        if (percent < 10) {
            return 5;
        }
        return 3;
    }

    private void renderTotemHUD(ScaledResolution resolution) {
        int totems = ItemUtil.getItemCount(Items.TOTEM_OF_UNDYING);
        if (totems > 0) {
            GlStateManager.enableTexture2D();
            int width = resolution.getScaledWidth();
            int height = resolution.getScaledHeight();
            int i = width / 2;
            int y = height - this.getArmorY();
            int x = i - 189 + 180 + 2;
            this.drawItem(this.TOTEM, totems, x, y);
        }
    }

    private void renderItemHUD(ScaledResolution resolution) {
        ItemHolder experience = new ItemHolder(this.EXPERIENCE, ItemUtil.getItemCount(Items.EXPERIENCE_BOTTLE));
        ItemHolder gaps = new ItemHolder(this.GAP, ItemUtil.getItemCount(Items.GOLDEN_APPLE));
        ItemHolder crystal = new ItemHolder(this.CRYSTAL, ItemUtil.getItemCount(Items.END_CRYSTAL));
        ArrayList<ItemHolder> holders = new ArrayList<ItemHolder>();
        holders.add(experience);
        holders.add(gaps);
        holders.add(crystal);
        int offsetY = 0;
        int i = resolution.getScaledWidth() / 2;
        int y = resolution.getScaledHeight() - 18;
        int x = i - 90 + 180 + 2;
        for (ItemHolder holder : holders) {
            this.drawItem(holder.getStack(), holder.getCount(), x, y - offsetY);
            offsetY += 16;
        }
    }

    private double getDimensionCoord(double coord) {
        if (Hud.mc.player.dimension == -1) {
            return coord * 8.0;
        }
        if (Hud.mc.player.dimension == 0) {
            return coord / 8.0;
        }
        return coord;
    }

    public void renderText(String text, float x, float y) {
        String colorCode = this.hudColour.getValue() == HudColour.RAINBOW ? ((HudRainbow)((Object)this.rainbow.getValue())).getColor() : "";
        Managers.FONT.drawString(colorCode + text, x, y, this.hudColour.getValue() == HudColour.GLOBAL || this.hudColour.getValue() == HudColour.ELITE ? Colours.get().getColour().getRGB() : (this.hudColour.getValue() == HudColour.STATIC ? ColorUtil.staticRainbow((y + 1.0f) * 0.89f, Colours.get().getColour()) : -1));
    }

    public void renderArrayList(Module module, String text, float x, float y) {
        if (this.hudColour.getValue() == HudColour.ELITE) {
            int color = this.getColorByCategory(module.getCategory());
            Managers.FONT.drawString(text, x, y, color);
        } else {
            this.renderText(text, x, y);
        }
    }

    public int getColorByCategory(Category category) {
        switch (category) {
            case COMBAT: {
                return new Color(6, 59, 138).getRGB();
            }
            case MISC: {
                return new Color(178, 229, 208).getRGB();
            }
            case MOVEMENT: {
                return new Color(136, 221, 235).getRGB();
            }
            case PLAYER: {
                return new Color(225, 174, 195).getRGB();
            }
            case RENDER: {
                return new Color(60, 130, 0).getRGB();
            }
            case OTHER: {
                return new Color(255, 107, 107).getRGB();
            }
        }
        return -1;
    }

    public void renderTextRadar(int start) {
        int offset = 0;
        List playersList = Hud.mc.world.playerEntities;
        Map<String, Integer> players = new HashMap();
        for (EntityPlayer entityPlayer : playersList) {
            if (entityPlayer == null || entityPlayer == Hud.mc.player || entityPlayer.isDead) continue;
            String playerName = entityPlayer.getName();
            int health = (int)EntityUtil.getHealth(entityPlayer);
            String hp = String.valueOf(health);
            String hpStr = TextRadarUtil.getHealthColor(health) + hp;
            float distance = Hud.mc.player.getDistance((Entity)entityPlayer);
            String dist = String.valueOf((int)distance);
            String distStr = "\u00a7f[" + TextRadarUtil.getDistanceColor(distance) + dist + "m" + "\u00a7f" + "] ";
            String pops = "";
            Map<String, Integer> registry = Managers.TOTEM.getPopMap();
            pops = pops + (registry.containsKey(entityPlayer.getName()) ? " -" + registry.get(entityPlayer.getName()) : "");
            String radarStr = distStr + "\u00a7r" + (Managers.FRIEND.isFriend(playerName) ? "\u00a7b" : "") + playerName + " " + hpStr + "\u00a7f" + pops;
            players.put(radarStr, (int)Hud.mc.player.getDistance((Entity)entityPlayer));
        }
        if (players.isEmpty()) {
            return;
        }
        players = TextRadarUtil.sortByValue(players);
        for (Map.Entry entry : players.entrySet()) {
            Managers.FONT.drawString((String)entry.getKey(), 2.0f, start + offset, Colours.get().getColour().getRGB());
            offset += 10;
        }
    }

    private void drawItem(ItemStack item, int count, int x, int y) {
        GlStateManager.enableDepth();
        Hud.mc.getRenderItem().zLevel = 200.0f;
        mc.getRenderItem().renderItemAndEffectIntoGUI(item, x, y);
        mc.getRenderItem().renderItemOverlayIntoGUI(Hud.mc.fontRenderer, item, x, y, "");
        Hud.mc.getRenderItem().zLevel = 0.0f;
        GlStateManager.enableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        Managers.FONT.drawString("\u00a7r" + count + "", x + 19 - 2 - Managers.FONT.getStringWidth(count + ""), y + 9, Colours.get().getColour().getRGB());
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
    }

    private String getRoundedDouble(double pos) {
        return String.format("%.2f", pos);
    }

    private String getTimeOfDay() {
        Calendar calendar = Calendar.getInstance();
        int timeOfDay = calendar.get(11);
        if (timeOfDay < 12) {
            return "Good Morning";
        }
        if (timeOfDay < 16) {
            return "Good Afternoon";
        }
        if (timeOfDay < 21) {
            return "Good Evening";
        }
        return "Good Night";
    }

    public static int getDirection4D() {
        return MathHelper.floor((double)((double)(Hud.mc.player.rotationYaw * 4.0f / 360.0f) + 0.5)) & 3;
    }

    private String getDirectionForDisplay() {
        switch (Hud.getDirection4D()) {
            case 0: {
                return "\u00a77 [" + this.getInfoColour(true) + "+Z" + "\u00a77" + "]";
            }
            case 1: {
                return "\u00a77 [" + this.getInfoColour(true) + "-X" + "\u00a77" + "]";
            }
            case 2: {
                return "\u00a77 [" + this.getInfoColour(true) + "-Z" + "\u00a77" + "]";
            }
        }
        return "\u00a77 [" + this.getInfoColour(true) + "+X" + "\u00a77" + "]";
    }

    public String getInfoColour(boolean info) {
        if (info) {
            return this.infoColour.getValue() == InfoColour.GLOBAL ? "\u00a7f" : (this.hudColour.getValue() == HudColour.RAINBOW ? ((HudRainbow)((Object)this.rainbow.getValue())).getColor() : "\u00a7r");
        }
        return this.infoColour.getValue() == InfoColour.GLOBAL ? (this.hudColour.getValue() == HudColour.RAINBOW ? "" : "\u00a7r") : "\u00a77";
    }

    public String getPotionNumberColor(DurationColour colour) {
        switch (colour) {
            case GRAY: {
                return "\u00a77";
            }
            case WHITE: {
                return "\u00a7f";
            }
        }
        return "";
    }

    public String getWatermark() {
        String WATERMARK = "\u00a75[\u00a7dSexMaster.CC\u00a75] ";
        String RAINBOW_MARK = ((HudRainbow)((Object)this.rainbow.getValue())).getColor() + "[SexMaster.CC] ";
        return this.rainbowWatermark.getValue() != false ? RAINBOW_MARK : "\u00a75[\u00a7dSexMaster.CC\u00a75] ";
    }

    public boolean isAnnouncingModules() {
        return this.announceModules.getValue();
    }

    public boolean isCapeEnabled() {
        return this.capes.getValue();
    }

    public boolean whiteBrackets() {
        return this.arrayList.getValue() == ArrayListMode.WHITE;
    }
}

