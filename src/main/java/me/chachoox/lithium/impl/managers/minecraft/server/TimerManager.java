/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.managers.minecraft.server;

import me.chachoox.lithium.api.event.bus.Listener;
import me.chachoox.lithium.api.event.bus.SubscriberImpl;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.asm.ducks.IMinecraft;
import me.chachoox.lithium.asm.mixins.util.ITimer;
import me.chachoox.lithium.impl.event.events.update.TickEvent;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.misc.timer.Timer;

public class TimerManager
extends SubscriberImpl
implements Minecraftable {
    private float timer;

    public TimerManager() {
        this.listeners.add(new Listener<TickEvent>(TickEvent.class){

            @Override
            public void call(TickEvent event) {
                if (Minecraftable.mc.player == null) {
                    TimerManager.this.reset();
                } else {
                    TimerManager.this.update();
                }
            }
        });
    }

    private void update() {
        Timer TIMER_MODULE = Managers.MODULE.get(Timer.class);
        if (TIMER_MODULE.isEnabled()) {
            ((ITimer)((IMinecraft)mc).getTimer()).setTickLength(50.0f / ((Float)TIMER_MODULE.speed.getValue()).floatValue());
        } else {
            ((ITimer)((IMinecraft)mc).getTimer()).setTickLength(50.0f / this.timer);
        }
    }

    public void set(float timer) {
        this.timer = timer <= 0.0f ? 0.1f : timer;
    }

    public float getTimer() {
        return this.timer;
    }

    public void reset() {
        this.timer = 1.0f;
    }
}

