/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.enchantment.Enchantment
 *  net.minecraft.entity.player.EntityPlayerMP
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketCreativeInventoryAction
 *  net.minecraft.util.text.translation.I18n
 */
package me.chachoox.lithium.impl.command.commands.player.stack;

import java.util.Objects;
import me.chachoox.lithium.impl.command.Argument;
import me.chachoox.lithium.impl.command.Command;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import net.minecraft.util.text.translation.I18n;

public class EnchantCommand
extends Command {
    public EnchantCommand() {
        super(new String[]{"Enchant", "en"}, new Argument("level"), new Argument("enchantment"));
    }

    @Override
    public String execute() {
        short level = (short)Integer.parseInt(this.getArgument("level").getValue());
        ItemStack stack = EnchantCommand.mc.player.inventory.getCurrentItem();
        if (stack.isEmpty()) {
            return "Hold an item to use this command";
        }
        if (this.getArgument("enchanment").isPresent()) {
            Enchantment enchantment = EnchantCommand.getEnchantment(this.getArgument("enchantment").getValue());
            if (enchantment == null) {
                return "Could find Enchantment " + this.getArgument("enchantment");
            }
            stack.addEnchantment(enchantment, (int)level);
            return this.setStack(stack);
        }
        for (Enchantment enchantment : Enchantment.REGISTRY) {
            if (enchantment.isCurse()) continue;
            stack.addEnchantment(enchantment, (int)level);
        }
        return this.setStack(stack);
    }

    private String setStack(ItemStack stack) {
        int slot = EnchantCommand.mc.player.inventory.currentItem + 36;
        if (EnchantCommand.mc.player.isCreative()) {
            EnchantCommand.mc.player.connection.sendPacket((Packet)new CPacketCreativeInventoryAction(slot, stack));
        } else if (mc.isSingleplayer()) {
            EntityPlayerMP player = Objects.requireNonNull(mc.getIntegratedServer()).getPlayerList().getPlayerByUUID(EnchantCommand.mc.player.getUniqueID());
            if (player != null) {
                player.inventoryContainer.putStackInSlot(slot, stack);
                return "Item enchanted";
            }
        } else {
            return "Not Creative or singleplayer, Enchantments are client sided";
        }
        return "hello young thug";
    }

    public static Enchantment getEnchantment(String prefixIn) {
        String prefix = prefixIn.toLowerCase();
        for (Enchantment enchantment : Enchantment.REGISTRY) {
            String s = I18n.translateToLocal((String)enchantment.getName());
            if (!s.toLowerCase().startsWith(prefix)) continue;
            return enchantment;
        }
        return null;
    }
}

