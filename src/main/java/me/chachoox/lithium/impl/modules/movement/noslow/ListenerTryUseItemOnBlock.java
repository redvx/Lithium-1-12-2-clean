/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemBow
 *  net.minecraft.item.ItemFood
 *  net.minecraft.item.ItemPotion
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.util.EnumHand
 */
package me.chachoox.lithium.impl.modules.movement.noslow;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.movement.noslow.NoSlow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumHand;

public class ListenerTryUseItemOnBlock
extends ModuleListener<NoSlow, PacketEvent.Post<CPacketPlayerTryUseItemOnBlock>> {
    public ListenerTryUseItemOnBlock(NoSlow module) {
        super(module, PacketEvent.Post.class, CPacketPlayerTryUseItemOnBlock.class);
    }

    @Override
    public void call(PacketEvent.Post<CPacketPlayerTryUseItemOnBlock> event) {
        Item item = ListenerTryUseItemOnBlock.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem();
        if (((NoSlow)this.module).strict.getValue().booleanValue() && (item instanceof ItemFood || item instanceof ItemBow || item instanceof ItemPotion)) {
            ListenerTryUseItemOnBlock.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(ListenerTryUseItemOnBlock.mc.player.inventory.currentItem));
        }
    }
}

