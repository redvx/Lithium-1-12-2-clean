/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.client.renderer.culling.Frustum
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 */
package me.chachoox.lithium.impl.modules.render.nametags;

import java.util.Comparator;
import java.util.List;
import me.chachoox.lithium.api.util.entity.EntityUtil;
import me.chachoox.lithium.api.util.math.MathUtil;
import me.chachoox.lithium.api.util.render.Interpolation;
import me.chachoox.lithium.api.util.render.Render2DUtil;
import me.chachoox.lithium.api.util.render.RenderUtil;
import me.chachoox.lithium.impl.event.events.render.main.Render3DEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.render.nametags.Nametags;
import me.chachoox.lithium.impl.modules.render.norender.NoRender;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class ListenerRender
extends ModuleListener<Nametags, Render3DEvent> {
    public ListenerRender(Nametags module) {
        super(module, Render3DEvent.class);
    }

    @Override
    public void call(Render3DEvent event) {
        Entity renderEntity = RenderUtil.getEntity();
        Frustum frustum = Interpolation.createFrustum(renderEntity);
        Vec3d interp = Interpolation.interpolateEntity(renderEntity);
        List playerList = ListenerRender.mc.world.playerEntities;
        playerList.sort(Comparator.comparing(player -> Float.valueOf(ListenerRender.mc.player.getDistance((Entity)((EntityPlayer)player)))).reversed());
        for (EntityPlayer player2 : playerList) {
            AxisAlignedBB bb = player2.getEntityBoundingBox();
            Vec3d vec = Interpolation.interpolateEntity((Entity)player2);
            if (!frustum.isBoundingBoxInFrustum(bb.expand(0.75, 0.75, 0.75)) || player2 == renderEntity || EntityUtil.isDead((Entity)player2) || player2.isInvisible() && !((Nametags)this.module).invisibles.getValue().booleanValue() || Managers.MODULE.get(NoRender.class).getNoSpectators() && player2.isSpectator()) continue;
            this.renderNameTag(player2, vec.x, vec.y, vec.z, interp);
        }
    }

    private void renderNameTag(EntityPlayer player, double x, double y, double z, Vec3d mcPlayerInterpolation) {
        double tempY = y + (player.isSneaking() ? 0.5 : 0.7);
        double xDist = mcPlayerInterpolation.x - x;
        double yDist = mcPlayerInterpolation.y - y;
        double zDist = mcPlayerInterpolation.z - z;
        y = MathHelper.sqrt((double)(xDist * xDist + yDist * yDist + zDist * zDist));
        String displayTag = ((Nametags)this.module).getDisplayTag(player);
        int width = Managers.FONT.getStringWidth(displayTag) / 2;
        double scale = 0.0018 + (double)MathUtil.fixedNametagScaling(((Float)((Nametags)this.module).scaling.getValue()).floatValue()) * y;
        if (y <= 8.0) {
            scale = 0.0245;
        }
        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset((float)1.0f, (float)-1500000.0f);
        GlStateManager.disableLighting();
        GlStateManager.translate((float)((float)x), (float)((float)tempY + 1.4f), (float)((float)z));
        GlStateManager.rotate((float)(-ListenerRender.mc.getRenderManager().playerViewY), (float)0.0f, (float)1.0f, (float)0.0f);
        float xRot = ListenerRender.mc.gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f;
        GlStateManager.rotate((float)ListenerRender.mc.getRenderManager().playerViewX, (float)xRot, (float)0.0f, (float)0.0f);
        GlStateManager.scale((double)(-scale), (double)(-scale), (double)scale);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.enableBlend();
        if (((Nametags)this.module).rectBorder.getValue().booleanValue()) {
            Render2DUtil.drawNameTagRect(-width - 2, -(ListenerRender.mc.fontRenderer.FONT_HEIGHT + 1), (float)width + 2.0f, 1.5f, (int)(127.0f * ((Float)((Nametags)this.module).opacity.getValue()).floatValue()) << 24, ((Nametags)this.module).holeColor.getValue().booleanValue() ? (EntityUtil.isPlayerSafe(player) ? -15415296 : -3669996) : ((Nametags)this.module).getBorderColor(player), 1.4f);
        }
        Managers.FONT.drawString(displayTag, -width, -8.0f, ((Nametags)this.module).getNameColor(player));
        GlStateManager.disableBlend();
        GlStateManager.pushMatrix();
        ItemStack heldItemMainhand = player.getHeldItemMainhand();
        ItemStack heldItemOffhand = player.getHeldItemOffhand();
        int xOffset = 0;
        int enchantOffset = 0;
        int i = 3;
        int armorSize = 3;
        while (i >= 0) {
            ItemStack itemStack = (ItemStack)player.inventory.armorInventory.get(armorSize);
            if (!itemStack.isEmpty()) {
                int size;
                xOffset -= 8;
                if (((Nametags)this.module).enchants.getValue().booleanValue() && !((Nametags)this.module).simple.getValue().booleanValue() && (size = EnchantmentHelper.getEnchantments((ItemStack)itemStack).size()) > enchantOffset) {
                    enchantOffset = size;
                }
            }
            i = --armorSize;
        }
        if (!heldItemOffhand.isEmpty() && ((Nametags)this.module).armor.getValue().booleanValue() || ((Nametags)this.module).durability.getValue().booleanValue() && heldItemOffhand.isItemStackDamageable()) {
            int size2;
            xOffset -= 8;
            if (((Nametags)this.module).enchants.getValue().booleanValue() && !((Nametags)this.module).simple.getValue().booleanValue() && (size2 = EnchantmentHelper.getEnchantments((ItemStack)heldItemOffhand).size()) > enchantOffset) {
                enchantOffset = size2;
            }
        }
        if (!heldItemMainhand.isEmpty()) {
            int size3;
            if (((Nametags)this.module).enchants.getValue().booleanValue() && !((Nametags)this.module).simple.getValue().booleanValue() && (size3 = EnchantmentHelper.getEnchantments((ItemStack)heldItemMainhand).size()) > enchantOffset) {
                enchantOffset = size3;
            }
            int armorOffset = this.getOffset(enchantOffset);
            if (((Nametags)this.module).armor.getValue().booleanValue() || ((Nametags)this.module).durability.getValue().booleanValue() && heldItemMainhand.isItemStackDamageable()) {
                xOffset -= 8;
            }
            if (((Nametags)this.module).armor.getValue().booleanValue()) {
                int oldOffset = armorOffset;
                armorOffset -= 32;
                ((Nametags)this.module).renderStack(heldItemMainhand, xOffset, oldOffset, enchantOffset);
            }
            if (((Nametags)this.module).durability.getValue().booleanValue() && heldItemMainhand.isItemStackDamageable()) {
                ((Nametags)this.module).renderDurability(heldItemMainhand, xOffset, armorOffset);
            }
            if (((Nametags)this.module).itemStack.getValue().booleanValue()) {
                ((Nametags)this.module).renderText(heldItemMainhand, armorOffset - (((Nametags)this.module).durability.getValue() != false ? 10 : 2));
            }
            if (((Nametags)this.module).armor.getValue().booleanValue() || ((Nametags)this.module).durability.getValue().booleanValue() && heldItemMainhand.isItemStackDamageable()) {
                xOffset += 16;
            }
        }
        int i2 = 3;
        int armorSizeI = 3;
        while (i2 >= 0) {
            ItemStack itemStack3 = (ItemStack)player.inventory.armorInventory.get(armorSizeI);
            if (!itemStack3.isEmpty()) {
                int fixedEnchantOffset = this.getOffset(enchantOffset);
                if (((Nametags)this.module).armor.getValue().booleanValue()) {
                    int oldEnchantOffset = fixedEnchantOffset;
                    fixedEnchantOffset -= 32;
                    ((Nametags)this.module).renderStack(itemStack3, xOffset, oldEnchantOffset, enchantOffset);
                }
                if (((Nametags)this.module).durability.getValue().booleanValue() && itemStack3.isItemStackDamageable()) {
                    ((Nametags)this.module).renderDurability(itemStack3, xOffset, fixedEnchantOffset);
                }
                xOffset += 16;
            }
            i2 = --armorSizeI;
        }
        if (!heldItemOffhand.isEmpty()) {
            int fixedEnchantOffsetI = this.getOffset(enchantOffset);
            if (((Nametags)this.module).armor.getValue().booleanValue()) {
                int oldEnchantOffsetI = fixedEnchantOffsetI;
                fixedEnchantOffsetI -= 32;
                ((Nametags)this.module).renderStack(heldItemOffhand, xOffset, oldEnchantOffsetI, enchantOffset);
            }
            if (((Nametags)this.module).durability.getValue().booleanValue() && heldItemOffhand.isItemStackDamageable()) {
                ((Nametags)this.module).renderDurability(heldItemOffhand, xOffset, fixedEnchantOffsetI);
            }
        }
        GlStateManager.popMatrix();
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.disablePolygonOffset();
        GlStateManager.doPolygonOffset((float)1.0f, (float)1500000.0f);
        GlStateManager.popMatrix();
    }

    private int getOffset(int offset) {
        int fixedOffset;
        int n = fixedOffset = ((Nametags)this.module).armor.getValue() != false ? -26 : -27;
        if (offset > 4) {
            fixedOffset -= (offset - 4) * 8;
        }
        return fixedOffset;
    }
}

