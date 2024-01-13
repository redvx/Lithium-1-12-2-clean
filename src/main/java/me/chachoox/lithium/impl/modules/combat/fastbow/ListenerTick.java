/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.ItemBow
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 */
package me.chachoox.lithium.impl.modules.combat.fastbow;

import me.chachoox.lithium.api.util.network.PacketUtil;
import me.chachoox.lithium.impl.event.events.update.TickEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.combat.fastbow.FastBow;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class ListenerTick
extends ModuleListener<FastBow, TickEvent> {
    public ListenerTick(FastBow module) {
        super(module, TickEvent.class);
    }

    @Override
    public void call(TickEvent event) {
        if (ListenerTick.mc.player != null && this.holdingBow() && ListenerTick.mc.player.isHandActive() && ListenerTick.mc.player.getItemInUseMaxCount() > (Integer)((FastBow)this.module).ticks.getValue()) {
            PacketUtil.send(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            ListenerTick.mc.player.stopActiveHand();
        }
    }

    private boolean holdingBow() {
        return ListenerTick.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemBow || ListenerTick.mc.player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof ItemBow;
    }
}

