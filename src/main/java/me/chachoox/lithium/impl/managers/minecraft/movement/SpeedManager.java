/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.Vec3d
 */
package me.chachoox.lithium.impl.managers.minecraft.movement;

import me.chachoox.lithium.api.event.bus.Listener;
import me.chachoox.lithium.api.event.bus.SubscriberImpl;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.api.util.math.MathUtil;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.impl.event.events.update.TickEvent;
import net.minecraft.util.math.Vec3d;

public class SpeedManager
extends SubscriberImpl
implements Minecraftable {
    private final StopWatch timer = new StopWatch();
    private Vec3d last = new Vec3d(0.0, 0.0, 0.0);
    private double speed = 0.0;

    public SpeedManager() {
        this.listeners.add(new Listener<TickEvent>(TickEvent.class){

            @Override
            public void call(TickEvent event) {
                if (event.isSafe() && SpeedManager.this.timer.passed(40L)) {
                    SpeedManager.this.speed = MathUtil.distance2D(Minecraftable.mc.player.getPositionVector(), SpeedManager.this.last);
                    SpeedManager.this.last = Minecraftable.mc.player.getPositionVector();
                    SpeedManager.this.timer.reset();
                }
            }
        });
    }

    public double getSpeed() {
        return this.getSpeedBpS() * 3.6;
    }

    public double getSpeedBpS() {
        return this.speed * 20.0;
    }
}

