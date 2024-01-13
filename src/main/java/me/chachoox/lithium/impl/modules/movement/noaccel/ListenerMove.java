/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.movement.noaccel;

import me.chachoox.lithium.impl.event.events.movement.actions.MoveEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.movement.fly.Fly;
import me.chachoox.lithium.impl.modules.movement.holepull.HolePull;
import me.chachoox.lithium.impl.modules.movement.noaccel.NoAccel;
import me.chachoox.lithium.impl.modules.movement.noclip.NoClip;
import me.chachoox.lithium.impl.modules.movement.packetfly.PacketFly;
import me.chachoox.lithium.impl.modules.movement.speed.Speed;
import me.chachoox.lithium.impl.modules.movement.tunnelspeed.TunnelSpeed;

public class ListenerMove
extends ModuleListener<NoAccel, MoveEvent> {
    public ListenerMove(NoAccel module) {
        super(module, MoveEvent.class);
    }

    @Override
    public void call(MoveEvent event) {
        if (ListenerMove.mc.player.isElytraFlying() || ListenerMove.mc.player.capabilities.isFlying || ListenerMove.mc.player.noClip || Managers.MODULE.get(NoClip.class).isEnabled() || Managers.MODULE.get(HolePull.class).isEnabled() && Managers.MODULE.get(HolePull.class).isAnchoring() || Managers.MODULE.get(PacketFly.class).isEnabled() || Managers.MODULE.get(Speed.class).isEnabled() || Managers.MODULE.get(Fly.class).isEnabled() || Managers.MODULE.get(TunnelSpeed.class).isEnabled() && Managers.MODULE.get(TunnelSpeed.class).isTunnel() || !Managers.STOP.passedLag(200) && ((NoAccel)this.module).stopOnLagback.getValue() != false || !Managers.STOP.passedLiquid() && ((NoAccel)this.module).inWater.getValue() == false || !Managers.STOP.passedPop() && ((NoAccel)this.module).stopOnTotem.getValue() == false || ((NoAccel)this.module).isEating() && ((NoAccel)this.module).whileEating.getValue() == false || ListenerMove.mc.player.isSpectator() || (float)ListenerMove.mc.player.getFoodStats().getFoodLevel() <= 6.0f) {
            return;
        }
        if (!((NoAccel)this.module).sneak.getValue().booleanValue() || !ListenerMove.mc.player.isSneaking()) {
            ((NoAccel)this.module).strafe(event, ((NoAccel)this.module).getSpeed(((NoAccel)this.module).slow.getValue(), ((NoAccel)this.module).speed.getValue()) * (Managers.KNOCKBACK.shouldBoost(((NoAccel)this.module).kbBoost.getValue()) ? (double)((Float)((NoAccel)this.module).boostReduction.getValue()).floatValue() : 1.0));
        }
    }
}

