/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.ItemBow
 */
package me.chachoox.lithium.impl.modules.player.quiver;

import me.chachoox.lithium.impl.event.events.misc.RightClickItemEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.player.quiver.Quiver;
import net.minecraft.item.ItemBow;

public class ListenerUseItem
extends ModuleListener<Quiver, RightClickItemEvent> {
    public ListenerUseItem(Quiver module) {
        super(module, RightClickItemEvent.class);
    }

    @Override
    public void call(RightClickItemEvent event) {
        if (ListenerUseItem.mc.player.getHeldItem(event.getHand()).getItem() instanceof ItemBow && (Integer)((Quiver)this.module).cancelTime.getValue() != 0 && !((Quiver)this.module).timer.passed(((Integer)((Quiver)this.module).cancelTime.getValue()).intValue()) && ((Quiver)this.module).fast) {
            event.setCanceled(true);
        }
    }
}

