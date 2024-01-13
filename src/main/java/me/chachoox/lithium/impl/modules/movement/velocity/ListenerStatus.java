/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.projectile.EntityFishHook
 *  net.minecraft.network.play.INetHandlerPlayClient
 *  net.minecraft.network.play.server.SPacketEntityStatus
 *  net.minecraft.world.World
 */
package me.chachoox.lithium.impl.modules.movement.velocity;

import java.util.Objects;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.movement.velocity.Velocity;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.world.World;

public class ListenerStatus
extends ModuleListener<Velocity, PacketEvent.Receive<SPacketEntityStatus>> {
    public ListenerStatus(Velocity module) {
        super(module, PacketEvent.Receive.class, SPacketEntityStatus.class);
    }

    @Override
    public void call(PacketEvent.Receive<SPacketEntityStatus> event) {
        SPacketEntityStatus packet = (SPacketEntityStatus)event.getPacket();
        if (((Velocity)this.module).fishingRod.getValue().booleanValue() && packet.getOpCode() == 31 && !event.isCanceled() && packet.getEntity((World)ListenerStatus.mc.world) instanceof EntityFishHook) {
            EntityFishHook fishHook = (EntityFishHook)packet.getEntity((World)ListenerStatus.mc.world);
            if (fishHook.caughtEntity != null && !fishHook.caughtEntity.equals((Object)ListenerStatus.mc.player)) {
                packet.processPacket((INetHandlerPlayClient)Objects.requireNonNull(mc.getConnection()));
            }
        }
    }
}

