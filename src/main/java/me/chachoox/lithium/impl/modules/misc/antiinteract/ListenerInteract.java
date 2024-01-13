/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.ItemFood
 *  net.minecraft.util.math.BlockPos
 */
package me.chachoox.lithium.impl.modules.misc.antiinteract;

import me.chachoox.lithium.impl.event.events.blocks.ClickBlockEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.misc.antiinteract.AntiInteract;
import net.minecraft.item.ItemFood;
import net.minecraft.util.math.BlockPos;

public class ListenerInteract
extends ModuleListener<AntiInteract, ClickBlockEvent.Right> {
    public ListenerInteract(AntiInteract module) {
        super(module, ClickBlockEvent.Right.class);
    }

    @Override
    public void call(ClickBlockEvent.Right event) {
        if (((AntiInteract)this.module).sneak.getValue().booleanValue() && Managers.ACTION.isSneaking()) {
            return;
        }
        if (((AntiInteract)this.module).onlyFood.getValue().booleanValue() && !(ListenerInteract.mc.player.getHeldItemMainhand().getItem() instanceof ItemFood)) {
            return;
        }
        BlockPos pos = event.getPos();
        if (((AntiInteract)this.module).isValid(ListenerInteract.mc.world.getBlockState(pos).getBlock())) {
            event.setCanceled(true);
        }
    }
}

