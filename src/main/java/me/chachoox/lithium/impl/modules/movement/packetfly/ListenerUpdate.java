/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 */
package me.chachoox.lithium.impl.modules.movement.packetfly;

import me.chachoox.lithium.api.util.movement.MovementUtil;
import me.chachoox.lithium.impl.event.events.update.UpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.movement.packetfly.PacketFly;
import me.chachoox.lithium.impl.modules.movement.packetfly.mode.LimitMode;
import me.chachoox.lithium.impl.modules.movement.packetfly.mode.PacketFlyMode;
import me.chachoox.lithium.impl.modules.movement.packetfly.mode.PhaseMode;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;

public class ListenerUpdate
extends ModuleListener<PacketFly, UpdateEvent> {
    public ListenerUpdate(PacketFly module) {
        super(module, UpdateEvent.class);
    }

    @Override
    public void call(UpdateEvent event) {
        double speed;
        double rawSpeed;
        ((PacketFly)this.module).teleportMap.entrySet().removeIf(((PacketFly)this.module)::clearMap);
        ListenerUpdate.mc.player.motionZ = 0.0;
        ListenerUpdate.mc.player.motionY = 0.0;
        ListenerUpdate.mc.player.motionX = 0.0;
        if (((PacketFly)this.module).mode.getValue() != PacketFlyMode.SETBACK && ((PacketFly)this.module).lastTpID == 0) {
            if (((PacketFly)this.module).resetCounter(4)) {
                ((PacketFly)this.module).sendPackets(0.0, 0.0, 0.0, true);
            }
            return;
        }
        boolean hitBox = ((PacketFly)this.module).checkHitBox();
        double d = ListenerUpdate.mc.player.movementInput.jump && (hitBox || !MovementUtil.isMoving() || this.lagSpeed()) ? (((PacketFly)this.module).antiKick.getValue().booleanValue() && !hitBox ? (((PacketFly)this.module).resetCounter(((PacketFly)this.module).mode.getValue() == PacketFlyMode.SETBACK ? 10 : 20) ? -0.032 : 0.062) : 0.062) : (ListenerUpdate.mc.player.movementInput.sneak ? -0.062 : (!hitBox ? (((PacketFly)this.module).resetCounter(4) ? (((PacketFly)this.module).antiKick.getValue().booleanValue() ? -0.04 : 0.0) : 0.0) : (rawSpeed = 0.0)));
        double conSpeed = ListenerUpdate.mc.player.movementInput.jump && (hitBox || !MovementUtil.isMoving() || this.lagSpeed()) ? (((PacketFly)this.module).antiKick.getValue().booleanValue() && !hitBox ? (((PacketFly)this.module).resetCounter(((PacketFly)this.module).mode.getValue() == PacketFlyMode.SETBACK ? 10 : 20) ? -((PacketFly)this.module).concealOffset / 2.0 : ((PacketFly)this.module).concealOffset) : ((PacketFly)this.module).concealOffset) : (ListenerUpdate.mc.player.movementInput.sneak ? -((PacketFly)this.module).concealOffset : (!hitBox ? (((PacketFly)this.module).resetCounter(4) ? (((PacketFly)this.module).antiKick.getValue().booleanValue() ? -0.04 : 0.0) : 0.0) : 0.0));
        double d2 = speed = ((PacketFly)this.module).conceal.getValue() != false ? conSpeed : rawSpeed;
        if (((PacketFly)this.module).phase.getValue() == PhaseMode.FULL && hitBox && MovementUtil.isMoving() && speed != 0.0) {
            speed /= 2.5;
        }
        if (ListenerUpdate.mc.player.movementInput.jump && ((PacketFly)this.module).autoClip.getValue().booleanValue()) {
            ListenerUpdate.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)ListenerUpdate.mc.player, ((PacketFly)this.module).isSneaking ? CPacketEntityAction.Action.START_SNEAKING : CPacketEntityAction.Action.STOP_SNEAKING));
            ((PacketFly)this.module).isSneaking = !((PacketFly)this.module).isSneaking;
        }
        double[] dirSpeed = MovementUtil.strafe(((PhaseMode)((Object)((PacketFly)this.module).phase.getValue())).equals((Object)PhaseMode.FULL) && hitBox ? 0.031 : 0.26);
        float rawFactor = ((Float)((PacketFly)this.module).factor.getValue()).floatValue();
        int factorInt = (int)Math.floor(rawFactor);
        if (((PacketFly)this.module).mode.getValue() == PacketFlyMode.FACTOR && this.ticksExisted()) {
            float extraFactor = rawFactor - (float)factorInt;
            if (Math.random() <= (double)extraFactor) {
                ++factorInt;
            }
        }
        for (int i = 1; i <= factorInt; ++i) {
            if (((PacketFly)this.module).mode.getValue() == PacketFlyMode.LIMIT) {
                if (ListenerUpdate.mc.player.ticksExisted % 2 == 0) {
                    if (((PacketFly)this.module).limited && speed >= 0.0) {
                        ((PacketFly)this.module).limited = false;
                        speed = -0.032;
                    }
                    ListenerUpdate.mc.player.motionX = dirSpeed[0] * (double)i;
                    ListenerUpdate.mc.player.motionY = speed * (double)i;
                    ListenerUpdate.mc.player.motionZ = dirSpeed[1] * (double)i;
                    ((PacketFly)this.module).sendPackets(ListenerUpdate.mc.player.motionX, ListenerUpdate.mc.player.motionY, ListenerUpdate.mc.player.motionZ, false);
                    continue;
                }
                if (!(speed < 0.0)) continue;
                ((PacketFly)this.module).limited = true;
                continue;
            }
            ListenerUpdate.mc.player.motionX = dirSpeed[0] * (double)i;
            ListenerUpdate.mc.player.motionY = speed * (double)i;
            ListenerUpdate.mc.player.motionZ = dirSpeed[1] * (double)i;
            ((PacketFly)this.module).sendPackets(ListenerUpdate.mc.player.motionX, ListenerUpdate.mc.player.motionY, ListenerUpdate.mc.player.motionZ, ((PacketFly)this.module).mode.getValue() != PacketFlyMode.SETBACK);
        }
    }

    private boolean ticksExisted() {
        if (((PacketFly)this.module).limit.getValue() == LimitMode.NONE) {
            return true;
        }
        if (((PacketFly)this.module).limit.getValue() == LimitMode.BOTH || ((PacketFly)this.module).limit.getValue() == LimitMode.TICK) {
            return ListenerUpdate.mc.player.ticksExisted % 15 > 0;
        }
        return true;
    }

    private boolean lagSpeed() {
        if (((PacketFly)this.module).limit.getValue() == LimitMode.NONE) {
            return true;
        }
        if (((PacketFly)this.module).limit.getValue() == LimitMode.BOTH || ((PacketFly)this.module).limit.getValue() == LimitMode.SPEED) {
            return --((PacketFly)this.module).lagTime > 0;
        }
        return true;
    }
}

