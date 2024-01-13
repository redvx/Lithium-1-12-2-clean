/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.math.BlockPos
 */
package me.chachoox.lithium.impl.modules.movement.tunnelspeed;

import me.chachoox.lithium.impl.event.events.update.TickEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.movement.noclip.NoClip;
import me.chachoox.lithium.impl.modules.movement.packetfly.PacketFly;
import me.chachoox.lithium.impl.modules.movement.tunnelspeed.TunnelSpeed;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class ListenerTick
extends ModuleListener<TunnelSpeed, TickEvent> {
    public ListenerTick(TunnelSpeed module) {
        super(module, TickEvent.class);
    }

    @Override
    public void call(TickEvent event) {
        if (ListenerTick.mc.player != null) {
            BlockPos above = new BlockPos(ListenerTick.mc.player.posX, ListenerTick.mc.player.posY + 2.0, ListenerTick.mc.player.posZ);
            BlockPos below = new BlockPos(ListenerTick.mc.player.posX, ListenerTick.mc.player.posY - 1.0, ListenerTick.mc.player.posZ);
            if (ListenerTick.mc.world.getBlockState(above).getBlock() != Blocks.AIR && ListenerTick.mc.world.getBlockState(above).getBlock() != Blocks.LAVA && ListenerTick.mc.world.getBlockState(above).getBlock() != Blocks.FLOWING_LAVA && ListenerTick.mc.world.getBlockState(above).getBlock() != Blocks.WATER && ListenerTick.mc.world.getBlockState(above).getBlock() != Blocks.FLOWING_WATER && ListenerTick.mc.world.getBlockState(below).getBlock() != Blocks.PACKED_ICE && ListenerTick.mc.world.getBlockState(below).getBlock() != Blocks.ICE && ListenerTick.mc.world.getBlockState(below).getBlock() != Blocks.FROSTED_ICE && ListenerTick.mc.world.getBlockState(below).getBlock() != Blocks.WATER && ListenerTick.mc.world.getBlockState(below).getBlock() != Blocks.FLOWING_WATER && ListenerTick.mc.world.getBlockState(below).getBlock() != Blocks.LAVA && ListenerTick.mc.world.getBlockState(below).getBlock() != Blocks.FLOWING_LAVA && ListenerTick.mc.gameSettings.keyBindForward.isKeyDown() && !ListenerTick.mc.gameSettings.keyBindSneak.isKeyDown() && !Managers.MODULE.get(PacketFly.class).isEnabled() && !Managers.MODULE.get(NoClip.class).isEnabled() && ListenerTick.mc.player.onGround) {
                ((TunnelSpeed)this.module).tunnel = true;
                ListenerTick.mc.player.motionX -= Math.sin(Math.toRadians(ListenerTick.mc.player.rotationYaw)) * (double)0.15f;
                ListenerTick.mc.player.motionZ += Math.cos(Math.toRadians(ListenerTick.mc.player.rotationYaw)) * (double)0.15f;
            } else {
                ((TunnelSpeed)this.module).tunnel = false;
            }
        }
    }
}

