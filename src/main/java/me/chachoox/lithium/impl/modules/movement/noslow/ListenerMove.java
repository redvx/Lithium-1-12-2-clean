/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.movement.noslow;

import me.chachoox.lithium.api.util.movement.MovementUtil;
import me.chachoox.lithium.impl.event.events.movement.actions.MoveEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.movement.noslow.NoSlow;
import me.chachoox.lithium.impl.modules.movement.noslow.util.AntiWebMode;

public class ListenerMove
extends ModuleListener<NoSlow, MoveEvent> {
    public ListenerMove(NoSlow module) {
        super(module, MoveEvent.class);
    }

    @Override
    public void call(MoveEvent event) {
        if (((NoSlow)this.module).doWeb()) {
            switch ((AntiWebMode)((Object)((NoSlow)this.module).antiWeb.getValue())) {
                case MOTION: {
                    double[] calc = MovementUtil.directionSpeed((double)((Float)((NoSlow)this.module).speed.getValue()).floatValue() / 10.0);
                    event.setX(calc[0]);
                    event.setZ(calc[1]);
                    event.setY(event.getY() - (double)(((Float)((NoSlow)this.module).speed.getValue()).floatValue() / 10.0f));
                    break;
                }
                case TIMER: {
                    if (ListenerMove.mc.player.onGround) break;
                    Managers.TIMER.set(((Float)((NoSlow)this.module).speed.getValue()).floatValue());
                    ((NoSlow)this.module).timerCheck = true;
                }
            }
        }
    }
}

