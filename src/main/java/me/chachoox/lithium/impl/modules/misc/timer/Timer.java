/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.misc.timer;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.NumberProperty;

public class Timer
extends Module {
    public final NumberProperty<Float> speed = new NumberProperty<Float>(Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(10.0f), Float.valueOf(0.1f), new String[]{"Time", "speed", "sped"}, "What we want to change the tick speed to.");

    public Timer() {
        super("Timer", new String[]{"Timer", "time", "gamespeed"}, "Speeds up the game.", Category.MISC);
        this.offerProperties(this.speed);
    }

    @Override
    public String getSuffix() {
        return ((Float)this.speed.getValue()).toString();
    }
}

