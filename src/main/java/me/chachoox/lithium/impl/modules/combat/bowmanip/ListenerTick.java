/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.ItemBow
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 */
package me.chachoox.lithium.impl.modules.combat.bowmanip;

import me.chachoox.lithium.impl.event.events.update.TickEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.combat.bowmanip.BowManip;
import net.minecraft.item.ItemBow;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class ListenerTick
extends ModuleListener<BowManip, TickEvent> {
    public ListenerTick(BowManip module) {
        super(module, TickEvent.class);
    }

    @Override
    public void call(TickEvent event) {
        if (ListenerTick.mc.player != null && ListenerTick.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemBow && ListenerTick.mc.player.isHandActive() && (float)ListenerTick.mc.player.getItemInUseMaxCount() > ((Float)((BowManip)this.module).fire.getValue()).floatValue() && ((Float)((BowManip)this.module).fire.getValue()).floatValue() > 3.0f) {
            ListenerTick.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            ListenerTick.mc.player.stopActiveHand();
        }
    }
}

