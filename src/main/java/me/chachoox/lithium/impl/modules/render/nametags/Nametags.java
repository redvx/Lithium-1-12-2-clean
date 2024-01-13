/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.enchantment.Enchantment
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.IBlockAccess
 */
package me.chachoox.lithium.impl.modules.render.nametags;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.entity.EntityUtil;
import me.chachoox.lithium.api.util.inventory.ItemUtil;
import me.chachoox.lithium.api.util.movement.PositionUtil;
import me.chachoox.lithium.api.util.text.TextUtil;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.other.colours.Colours;
import me.chachoox.lithium.impl.modules.render.nametags.ListenerRender;
import me.chachoox.lithium.impl.modules.render.nametags.mode.PingEnum;
import me.chachoox.lithium.impl.modules.render.nametags.mode.SneakEnum;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class Nametags
extends Module {
    protected final Property<Boolean> armor = new Property<Boolean>(true, new String[]{"Armor", "aa"}, "Draws the armor of the player.");
    protected final Property<Boolean> enchants = new Property<Boolean>(true, new String[]{"Enchants", "ench"}, "Draws the enchants of the armor.");
    protected final Property<Boolean> simple = new Property<Boolean>(false, new String[]{"SimpleEnchants", "simpleench"}, "Draws less enchantments.");
    protected final Property<Boolean> lowercase = new Property<Boolean>(false, new String[]{"LowerCase", "lowercaseench"}, "Draws enchantments in lowercase.");
    protected final Property<Boolean> durability = new Property<Boolean>(true, new String[]{"Durability", "dura"}, "Draws the durability of the armor and items.");
    protected final Property<Boolean> itemStack = new Property<Boolean>(true, new String[]{"StackName", "stack"}, "Draws the name of the item being held.");
    protected final NumberProperty<Float> scaling = new NumberProperty<Float>(Float.valueOf(0.3f), Float.valueOf(0.1f), Float.valueOf(1.0f), Float.valueOf(0.1f), new String[]{"Scaling", "scale", "size"}, "The scale of the nametags.");
    protected final Property<Boolean> rectBorder = new Property<Boolean>(true, new String[]{"Rect", "rectborder"}, "Draws a rect to fill the nametag.");
    protected final NumberProperty<Float> opacity = new NumberProperty<Float>(Float.valueOf(0.5f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.01f), new String[]{"Opacity", "rectalpha"}, "The opacity of the rect.");
    protected final NumberProperty<Float> outlineOpacity = new NumberProperty<Float>(Float.valueOf(0.75f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.01f), new String[]{"OutlineOpacity", "outlinealpha"}, "The opacity of the outline.");
    protected final Property<Boolean> invisibles = new Property<Boolean>(true, new String[]{"Invisibles", "invis"}, "Draws nametags on invisibles players.");
    protected final EnumProperty<PingEnum> ping = new EnumProperty<PingEnum>(PingEnum.NORMAL, new String[]{"Ping", "p"}, "Displays the ping of the player.");
    protected final Property<Boolean> entityId = new Property<Boolean>(false, new String[]{"EntityId", "id"}, "Displays the id of the player.");
    protected final Property<Boolean> gameMode = new Property<Boolean>(false, new String[]{"Gamemode", "gamemod"}, "Displays the game mode of the player.");
    protected final Property<Boolean> totemPops = new Property<Boolean>(true, new String[]{"Pops", "pop"}, "Displays how many totems the player has popped.");
    protected final EnumProperty<SneakEnum> sneak = new EnumProperty<SneakEnum>(SneakEnum.NONE, new String[]{"Sneak", "crouch"}, "Changes color if the player is sneaking.");
    protected final Property<Boolean> burrow = new Property<Boolean>(false, new String[]{"Burrow", "burro"}, "Changes color if the player is inside a block.");
    protected final Property<Boolean> syncBorder = new Property<Boolean>(false, new String[]{"GlobalOutline", "syncoutline"}, "Syncs the outline color.");
    protected final Property<Boolean> holeColor = new Property<Boolean>(false, new String[]{"HoleOutlineColor", "safecolor"}, "Changes color if the player is in a safe spot.");
    protected final List<Block> burrowList = Arrays.asList(Blocks.BEDROCK, Blocks.OBSIDIAN, Blocks.ENDER_CHEST, Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.BEACON, Blocks.PISTON, Blocks.REDSTONE_BLOCK, Blocks.ENCHANTING_TABLE, Blocks.ANVIL);

    public Nametags() {
        super("Nametags", new String[]{"Nametags", "nametag", "betternametags"}, "Better player name tags.", Category.RENDER);
        this.offerProperties(this.armor, this.enchants, this.simple, this.lowercase, this.durability, this.itemStack, this.scaling, this.rectBorder, this.opacity, this.outlineOpacity, this.invisibles, this.ping, this.entityId, this.gameMode, this.totemPops, this.sneak, this.burrow, this.syncBorder, this.holeColor);
        this.listeners.add(new ListenerRender(this));
    }

    protected void renderStack(ItemStack stack, int x, int y, int enchHeight) {
        GlStateManager.pushMatrix();
        GlStateManager.depthMask((boolean)true);
        GlStateManager.clear((int)256);
        RenderHelper.enableStandardItemLighting();
        Nametags.mc.getRenderItem().zLevel = -150.0f;
        GlStateManager.disableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.disableCull();
        int height = enchHeight > 4 ? (enchHeight - 4) * 8 / 2 : 0;
        mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y + height);
        mc.getRenderItem().renderItemOverlays(Nametags.mc.fontRenderer, stack, x, y + height);
        Nametags.mc.getRenderItem().zLevel = 0.0f;
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableCull();
        GlStateManager.enableAlpha();
        GlStateManager.scale((float)0.5f, (float)0.5f, (float)0.5f);
        GlStateManager.disableDepth();
        if (this.enchants.getValue().booleanValue()) {
            this.renderEnchants(stack, x, y - 24);
        }
        GlStateManager.enableDepth();
        GlStateManager.scale((float)2.0f, (float)2.0f, (float)2.0f);
        GlStateManager.popMatrix();
    }

    private void renderEnchants(ItemStack stack, int xOffset, int yOffset) {
        Set e = EnchantmentHelper.getEnchantments((ItemStack)stack).keySet();
        ArrayList<String> enchantTexts = new ArrayList<String>(e.size());
        for (Enchantment enchantment : e) {
            if (this.simple.getValue().booleanValue() && !enchantment.getName().contains("all") && !enchantment.getName().contains("knockback") && !enchantment.getName().contains("fire") && !enchantment.getName().contains("arrowDamage") && !enchantment.getName().contains("explosion") && !enchantment.getName().contains("fall") && !enchantment.getName().contains("durability") && !enchantment.getName().contains("mending")) continue;
            enchantTexts.add(this.getEnchantText(enchantment, EnchantmentHelper.getEnchantmentLevel((Enchantment)enchantment, (ItemStack)stack)));
        }
        for (String string : enchantTexts) {
            if (string == null) continue;
            Managers.FONT.drawString(this.lowercase.getValue() != false ? string.toLowerCase() : TextUtil.capitalize(string), (float)xOffset * 2.0f, yOffset, -1);
            yOffset += 8;
        }
        if (stack.getItem().equals(Items.GOLDEN_APPLE) && stack.hasEffect()) {
            Managers.FONT.drawString("God", (float)xOffset * 2.0f, yOffset, -3977919);
        }
    }

    private String getEnchantText(Enchantment ench, int lvl) {
        int lvlOffset;
        ResourceLocation resource = (ResourceLocation)Enchantment.REGISTRY.getNameForObject((Object)ench);
        String name = resource == null ? ench.getName() : resource.toString();
        int n = lvlOffset = lvl > 1 ? 12 : 13;
        if (name.length() > lvlOffset) {
            name = name.substring(10, lvlOffset);
        }
        if (lvl > 1) {
            name = name + lvl;
        }
        return name.length() < 2 ? name : TextUtil.getFixedName(name);
    }

    protected void renderText(ItemStack stack, float y) {
        GlStateManager.scale((float)0.5f, (float)0.5f, (float)0.5f);
        GlStateManager.disableDepth();
        String name = stack.getDisplayName();
        Managers.FONT.drawString(name, -Managers.FONT.getStringWidth(name) >> 1, y, -1);
        GlStateManager.enableDepth();
        GlStateManager.scale((float)2.0f, (float)2.0f, (float)2.0f);
    }

    protected void renderDurability(ItemStack stack, float x, float y) {
        int percent = (int)ItemUtil.getDamageInPercent(stack);
        GlStateManager.scale((float)0.5f, (float)0.5f, (float)0.5f);
        GlStateManager.disableDepth();
        Managers.FONT.drawString(percent + "%", x * 2.0f, y, stack.getItem().getRGBDurabilityForDisplay(stack));
        GlStateManager.enableDepth();
        GlStateManager.scale((float)2.0f, (float)2.0f, (float)2.0f);
    }

    protected String getDisplayTag(EntityPlayer player) {
        String name = player.getDisplayName().getFormattedText();
        double health = Math.ceil(EntityUtil.getHealth(player));
        String color = health > 18.0 ? "\u00a7a" : (health > 16.0 ? "\u00a72" : (health > 12.0 ? "\u00a7e" : (health > 8.0 ? "\u00a76" : (health > 5.0 ? "\u00a7c" : "\u00a74"))));
        String idString = "";
        if (this.entityId.getValue().booleanValue()) {
            idString = idString + " ID: " + player.getEntityId();
        }
        String gameModeStr = "";
        if (this.gameMode.getValue().booleanValue()) {
            gameModeStr = player.isCreative() ? gameModeStr + " [C]" : (player.isSpectator() || player.isInvisible() ? gameModeStr + " [I]" : gameModeStr + " [S]");
        }
        String pingStr = "";
        if (this.ping.getValue() != PingEnum.NONE) {
            try {
                int responseTime = Objects.requireNonNull(mc.getConnection()).getPlayerInfo(player.getUniqueID()).getResponseTime();
                switch ((PingEnum)((Object)this.ping.getValue())) {
                    case COLORED: {
                        pingStr = pingStr + " " + this.getPingColor(responseTime) + responseTime + "ms";
                        break;
                    }
                    case NORMAL: {
                        pingStr = pingStr + " " + responseTime + "ms";
                    }
                }
            }
            catch (Exception responseTime) {
                // empty catch block
            }
        }
        String popStr = "";
        if (this.totemPops.getValue().booleanValue()) {
            Map<String, Integer> registry = Managers.TOTEM.getPopMap();
            popStr = popStr + (registry.containsKey(player.getName()) ? this.getPopColor(registry.get(player.getName())) + " -" + registry.get(player.getName()) : "");
        }
        name = name + idString + gameModeStr + pingStr + color + " " + (int)health + popStr;
        return name;
    }

    protected int getNameColor(EntityPlayer player) {
        BlockPos pos;
        IBlockState state;
        if (Managers.FRIEND.isFriend(player)) {
            return Colours.get().getFriendColour().getRGB();
        }
        if (this.burrow.getValue().booleanValue() && this.burrowList.contains((state = Nametags.mc.world.getBlockState(pos = PositionUtil.getPosition((Entity)player))).getBlock()) && state.getBoundingBox((IBlockAccess)Nametags.mc.world, (BlockPos)pos).offset((BlockPos)pos).maxY > player.posY) {
            return -10611240;
        }
        if (player.isInvisible()) {
            return -56064;
        }
        if (mc.getConnection() != null && mc.getConnection().getPlayerInfo(player.getUniqueID()) == null) {
            return -1113785;
        }
        if (player.isSneaking() && this.sneak.getValue() != SneakEnum.NONE) {
            return this.sneak.getValue() == SneakEnum.LIGHT ? 0xFF9900 : -6676491;
        }
        return -1;
    }

    protected int getBorderColor(EntityPlayer player) {
        if (this.syncBorder.getValue().booleanValue()) {
            BlockPos pos;
            IBlockState state;
            if (Managers.FRIEND.isFriend(player)) {
                return Colours.get().getFriendColour().getRGB();
            }
            if (this.burrow.getValue().booleanValue() && this.burrowList.contains((state = Nametags.mc.world.getBlockState(pos = PositionUtil.getPosition((Entity)player))).getBlock()) && state.getBoundingBox((IBlockAccess)Nametags.mc.world, (BlockPos)pos).offset((BlockPos)pos).maxY > player.posY) {
                return -10611240;
            }
            if (player.isInvisible()) {
                return -56064;
            }
            if (mc.getConnection() != null && mc.getConnection().getPlayerInfo(player.getUniqueID()) == null) {
                return -1113785;
            }
            if (player.isSneaking() && this.sneak.getValue() != SneakEnum.NONE) {
                return this.sneak.getValue() == SneakEnum.LIGHT ? 0xFF9900 : -6676491;
            }
        }
        return this.syncBorder.getValue() != false ? Colours.get().getColour().getRGB() : (int)(127.0f * ((Float)this.outlineOpacity.getValue()).floatValue()) << 24;
    }

    private String getPopColor(int pops) {
        if (pops == 1) {
            return "\u00a7a";
        }
        if (pops == 2) {
            return "\u00a72";
        }
        if (pops == 3) {
            return "\u00a7e";
        }
        if (pops == 4) {
            return "\u00a76";
        }
        if (pops == 5) {
            return "\u00a7c";
        }
        return "\u00a74";
    }

    private String getPingColor(int ping) {
        if (ping > 200) {
            return "\u00a7c";
        }
        if (ping > 100) {
            return "\u00a7e";
        }
        return "\u00a7a";
    }
}

