/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemSword
 *  net.minecraft.util.EnumHand
 *  net.minecraft.world.World
 */
package me.chachoox.lithium.impl.modules.combat.offhand;

import me.chachoox.lithium.impl.event.events.blocks.ClickBlockEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.combat.offhand.Offhand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ListenerInteract
extends ModuleListener<Offhand, ClickBlockEvent.Right> {
    public ListenerInteract(Offhand module) {
        super(module, ClickBlockEvent.Right.class);
    }

    @Override
    public void call(ClickBlockEvent.Right event) {
        if (event.getHand() == EnumHand.OFF_HAND && ListenerInteract.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword && ListenerInteract.mc.gameSettings.keyBindUseItem.isKeyDown()) {
            event.setCanceled(true);
        } else if (event.getHand() == EnumHand.MAIN_HAND) {
            Item mainHand = ListenerInteract.mc.player.getHeldItemMainhand().getItem();
            Item offHand = ListenerInteract.mc.player.getHeldItemOffhand().getItem();
            if (mainHand == Items.END_CRYSTAL && offHand == Items.GOLDEN_APPLE && event.getHand() == EnumHand.MAIN_HAND) {
                event.setCanceled(true);
                ListenerInteract.mc.player.setActiveHand(EnumHand.OFF_HAND);
                ListenerInteract.mc.playerController.processRightClick((EntityPlayer)ListenerInteract.mc.player, (World)ListenerInteract.mc.world, EnumHand.OFF_HAND);
            }
        }
    }
}

