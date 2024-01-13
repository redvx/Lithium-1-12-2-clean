/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.util.EnumHand
 *  net.minecraft.world.World
 *  org.lwjgl.input.Mouse
 */
package me.chachoox.lithium.impl.modules.misc.middleclick;

import me.chachoox.lithium.api.util.inventory.ItemUtil;
import me.chachoox.lithium.impl.event.events.update.UpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.misc.middleclick.MiddleClick;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import org.lwjgl.input.Mouse;

public class ListenerUpdate
extends ModuleListener<MiddleClick, UpdateEvent> {
    public ListenerUpdate(MiddleClick module) {
        super(module, UpdateEvent.class);
    }

    @Override
    public void call(UpdateEvent event) {
        if (Mouse.isButtonDown((int)2)) {
            if (!((MiddleClick)this.module).clicked && ListenerUpdate.mc.currentScreen == null) {
                if (((MiddleClick)this.module).friend.getValue().booleanValue()) {
                    ((MiddleClick)this.module).onClick();
                }
                if (((MiddleClick)this.module).pearl.getValue().booleanValue()) {
                    if (((MiddleClick)this.module).onEntity()) {
                        return;
                    }
                    int pearlSlot = ItemUtil.getItemFromHotbar(Items.ENDER_PEARL);
                    if (pearlSlot != -1 || ListenerUpdate.mc.player.getHeldItemOffhand().getItem() == Items.ENDER_PEARL) {
                        int oldSlot = ListenerUpdate.mc.player.inventory.currentItem;
                        if (ListenerUpdate.mc.player.getHeldItemOffhand().getItem() != Items.ENDER_PEARL) {
                            ItemUtil.switchTo(pearlSlot);
                        }
                        ListenerUpdate.mc.playerController.processRightClick((EntityPlayer)ListenerUpdate.mc.player, (World)ListenerUpdate.mc.world, ListenerUpdate.mc.player.getHeldItemOffhand().getItem() == Items.ENDER_PEARL ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
                        if (ListenerUpdate.mc.player.getHeldItemOffhand().getItem() != Items.ENDER_PEARL) {
                            ItemUtil.switchTo(oldSlot);
                        }
                    }
                }
            }
            ((MiddleClick)this.module).clicked = true;
        } else {
            ((MiddleClick)this.module).clicked = false;
        }
    }
}

