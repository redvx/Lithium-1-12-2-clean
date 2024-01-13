/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.util.EnumHand
 */
package me.chachoox.lithium.impl.modules.player.fastbreak;

import me.chachoox.lithium.api.util.blocks.MineUtil;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.api.util.network.PacketUtil;
import me.chachoox.lithium.impl.event.events.blocks.DamageBlockEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.player.fastbreak.FastBreak;
import me.chachoox.lithium.impl.modules.player.fastbreak.mode.MineMode;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumHand;

public class ListenerDamage
extends ModuleListener<FastBreak, DamageBlockEvent> {
    public ListenerDamage(FastBreak module) {
        super(module, DamageBlockEvent.class);
    }

    @Override
    public void call(DamageBlockEvent event) {
        if (MineUtil.canBreak(event.getPos()) && !ListenerDamage.mc.player.capabilities.isCreativeMode && ((FastBreak)this.module).timer.passed(250L) && ((FastBreak)this.module).isBlockValid(ListenerDamage.mc.world.getBlockState(event.getPos()).getBlock())) {
            switch ((MineMode)((Object)((FastBreak)this.module).mode.getValue())) {
                case PACKET: {
                    boolean aborted = false;
                    if (((FastBreak)this.module).pos != null && !((FastBreak)this.module).pos.equals((Object)event.getPos())) {
                        ((FastBreak)this.module).abortCurrentPos();
                        aborted = true;
                    }
                    if (!aborted && ((FastBreak)this.module).pos != null && ((FastBreak)this.module).pos.equals((Object)event.getPos()) && ((FastBreak)this.module).auto.getValue().booleanValue()) {
                        ((FastBreak)this.module).abortCurrentPos();
                        ((FastBreak)this.module).timer.reset();
                        return;
                    }
                    if (((FastBreak)this.module).pos == null || ((FastBreak)this.module).auto.getValue().booleanValue()) {
                        this.setPos(event);
                    }
                    ListenerDamage.mc.player.swingArm(EnumHand.MAIN_HAND);
                    CPacketPlayerDigging packet = new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.getPos(), event.getFacing());
                    PacketUtil.send(packet);
                    if (((FastBreak)this.module).debug.getValue().booleanValue()) {
                        Logger.getLogger().log("\u00a7bSending normal dig packet", false);
                    }
                    event.setCanceled(true);
                    ((FastBreak)this.module).timer.reset();
                    break;
                }
                case INSTANT: {
                    boolean aborted = false;
                    if (((FastBreak)this.module).pos != null && !((FastBreak)this.module).pos.equals((Object)event.getPos())) {
                        ((FastBreak)this.module).abortCurrentPos();
                        aborted = true;
                    }
                    if (!aborted && ((FastBreak)this.module).pos != null && ((FastBreak)this.module).pos.equals((Object)event.getPos()) && ((FastBreak)this.module).auto.getValue().booleanValue()) {
                        ((FastBreak)this.module).abortCurrentPos();
                        ((FastBreak)this.module).timer.reset();
                        return;
                    }
                    if (((FastBreak)this.module).pos == null || ((FastBreak)this.module).auto.getValue().booleanValue()) {
                        this.setPos(event);
                    }
                    ListenerDamage.mc.player.swingArm(EnumHand.MAIN_HAND);
                    CPacketPlayerDigging packet = new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.getPos(), event.getFacing());
                    PacketUtil.send(packet);
                    if (((FastBreak)this.module).debug.getValue().booleanValue()) {
                        Logger.getLogger().log("\u00a7bSending instant dig packet", false);
                    }
                    ((FastBreak)this.module).shouldAbort = true;
                    event.setCanceled(true);
                    ((FastBreak)this.module).timer.reset();
                    break;
                }
            }
        }
    }

    private void setPos(DamageBlockEvent event) {
        ((FastBreak)this.module).reset();
        ((FastBreak)this.module).pos = event.getPos();
        ((FastBreak)this.module).direction = event.getFacing();
    }
}

