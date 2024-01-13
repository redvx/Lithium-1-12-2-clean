/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.EnumHand
 */
package me.chachoox.lithium.api.util.inventory;

import java.util.function.Predicate;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.asm.ducks.IPlayerControllerMP;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class ItemUtil
implements Minecraftable {
    public static void switchTo(int slot) {
        if (ItemUtil.mc.player.inventory.currentItem != slot && slot > -1 && slot < 9) {
            ItemUtil.mc.player.inventory.currentItem = slot;
            ItemUtil.syncItem();
        }
    }

    public static void switchToAlt(int slot) {
        if (ItemUtil.mc.player.inventory.currentItem != (slot = ItemUtil.hotbarToInventory(slot)) && slot > 35 && slot < 45) {
            ItemUtil.mc.playerController.windowClick(0, slot, ItemUtil.mc.player.inventory.currentItem, ClickType.SWAP, (EntityPlayer)ItemUtil.mc.player);
        }
    }

    public static int findHotbarItem(Item item) {
        return ItemUtil.findInHotbar(s -> ItemUtil.areSame(s, item));
    }

    public static boolean areSame(Item item1, Item item2) {
        return Item.getIdFromItem((Item)item1) == Item.getIdFromItem((Item)item2);
    }

    public static boolean isHolding(Class<?> clazz) {
        return clazz.isAssignableFrom(ItemUtil.mc.player.getHeldItemMainhand().getItem().getClass()) || clazz.isAssignableFrom(ItemUtil.mc.player.getHeldItemOffhand().getItem().getClass());
    }

    public static EnumHand getHand(int slot) {
        return slot == -2 ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
    }

    public static EnumHand getHand(Item item) {
        return ItemUtil.mc.player.getHeldItemMainhand().getItem() == item ? EnumHand.MAIN_HAND : (ItemUtil.mc.player.getHeldItemOffhand().getItem() == item ? EnumHand.OFF_HAND : null);
    }

    public static int findInHotbar(Predicate<ItemStack> condition) {
        return ItemUtil.findInHotbar(condition, true);
    }

    public static int findInHotbar(Predicate<ItemStack> condition, boolean offhand) {
        if (offhand && condition.test(ItemUtil.mc.player.getHeldItemOffhand())) {
            return -2;
        }
        int result = -1;
        for (int i = 8; i > -1; --i) {
            if (!condition.test(ItemUtil.mc.player.inventory.getStackInSlot(i))) continue;
            result = i;
            if (ItemUtil.mc.player.inventory.currentItem == i) break;
        }
        return result;
    }

    public static boolean areSame(ItemStack stack, Item item) {
        return stack != null && ItemUtil.areSame(stack.getItem(), item);
    }

    public static int getSlotHotbar(Item item) {
        int slot = -1;
        if (ItemUtil.mc.player == null) {
            return slot;
        }
        for (int i = 8; i >= 0; --i) {
            if (ItemUtil.mc.player.inventory.getStackInSlot(i).getItem() != item) continue;
            slot = i;
            break;
        }
        return slot;
    }

    public static void syncItem() {
        ((IPlayerControllerMP)ItemUtil.mc.playerController).syncItem();
    }

    public static int getItemFromHotbar(Item item) {
        int slot = -1;
        for (int i = 8; i >= 0; --i) {
            if (ItemUtil.mc.player.inventory.getStackInSlot(i).getItem() != item) continue;
            slot = i;
        }
        return slot;
    }

    public static int getBlockFromHotbar(Block block) {
        int slot = -1;
        for (int i = 8; i >= 0; --i) {
            if (ItemUtil.mc.player.inventory.getStackInSlot(i).getItem() != Item.getItemFromBlock((Block)block)) continue;
            slot = i;
        }
        return slot;
    }

    public static int getItemSlot(Class<?> clss) {
        int itemSlot = -1;
        for (int i = 45; i > 0; --i) {
            if (ItemUtil.mc.player.inventory.getStackInSlot(i).getItem().getClass() != clss) continue;
            itemSlot = i;
            break;
        }
        return itemSlot;
    }

    public static int getItemSlot(Item item) {
        int itemSlot = -1;
        for (int i = 45; i > 0; --i) {
            if (!ItemUtil.mc.player.inventory.getStackInSlot(i).getItem().equals(item)) continue;
            itemSlot = i;
            break;
        }
        return itemSlot;
    }

    public static int getItemCount(Item item) {
        int count = 0;
        int size = ItemUtil.mc.player.inventory.mainInventory.size();
        for (int i = 0; i < size; ++i) {
            ItemStack itemStack = (ItemStack)ItemUtil.mc.player.inventory.mainInventory.get(i);
            if (itemStack.getItem() != item) continue;
            count += itemStack.getCount();
        }
        ItemStack offhandStack = ItemUtil.mc.player.getHeldItemOffhand();
        if (offhandStack.getItem() == item) {
            count += offhandStack.getCount();
        }
        return count;
    }

    public static int hotbarToInventory(int slot) {
        if (slot == -2) {
            return 45;
        }
        if (slot > -1 && slot < 9) {
            return 36 + slot;
        }
        return slot;
    }

    public static double getDamageInPercent(ItemStack stack) {
        double percent = (double)stack.getItemDamage() / (double)stack.getMaxDamage();
        if (percent == 0.0) {
            return 100.0;
        }
        if (percent == 1.0) {
            return 0.0;
        }
        return 100.0 - percent * 100.0;
    }
}

