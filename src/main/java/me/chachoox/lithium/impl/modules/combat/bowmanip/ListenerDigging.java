/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.item.ItemBow
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.util.EnumHand
 */
package me.chachoox.lithium.impl.modules.combat.bowmanip;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.combat.bowmanip.BowManip;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBow;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumHand;

public class ListenerDigging
extends ModuleListener<BowManip, PacketEvent.Send<CPacketPlayerDigging>> {
    private static final double n = 1.0E-5;

    public ListenerDigging(BowManip module) {
        super(module, PacketEvent.Send.class, CPacketPlayerDigging.class);
    }

    @Override
    public void call(PacketEvent.Send<CPacketPlayerDigging> event) {
        if (ListenerDigging.mc.player != null && ((CPacketPlayerDigging)event.getPacket()).getAction() == CPacketPlayerDigging.Action.RELEASE_USE_ITEM && !ListenerDigging.mc.player.getHeldItem(EnumHand.MAIN_HAND).isEmpty() && ListenerDigging.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemBow && (float)(((BowManip)this.module).timer.getTime() - ((BowManip)this.module).last) >= ((Float)((BowManip)this.module).ticks.getValue()).floatValue() * 50.0f) {
            ((BowManip)this.module).last = System.currentTimeMillis();
            ListenerDigging.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)ListenerDigging.mc.player, CPacketEntityAction.Action.START_SPRINTING));
            for (int i = 0; i < (Integer)((BowManip)this.module).spoofs.getValue(); ++i) {
                ListenerDigging.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(ListenerDigging.mc.player.posX, ListenerDigging.mc.player.posY - 1.0E-5, ListenerDigging.mc.player.posZ, true));
                ListenerDigging.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(ListenerDigging.mc.player.posX, ListenerDigging.mc.player.posY + 1.0E-5, ListenerDigging.mc.player.posZ, false));
            }
        }
    }
}

