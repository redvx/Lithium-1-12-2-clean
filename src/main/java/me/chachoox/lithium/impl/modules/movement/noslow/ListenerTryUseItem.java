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
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItem
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
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;

public class ListenerTryUseItem
extends ModuleListener<NoSlow, PacketEvent.Post<CPacketPlayerTryUseItem>> {
    public ListenerTryUseItem(NoSlow module) {
        super(module, PacketEvent.Post.class, CPacketPlayerTryUseItem.class);
    }

    @Override
    public void call(PacketEvent.Post<CPacketPlayerTryUseItem> event) {
        Item item = ListenerTryUseItem.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem();
        if (((NoSlow)this.module).strict.getValue().booleanValue() && (item instanceof ItemFood || item instanceof ItemBow || item instanceof ItemPotion)) {
            ListenerTryUseItem.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(ListenerTryUseItem.mc.player.inventory.currentItem));
        }
    }
}

