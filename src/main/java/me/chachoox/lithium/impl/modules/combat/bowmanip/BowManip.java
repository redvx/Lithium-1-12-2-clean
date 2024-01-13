/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.combat.bowmanip;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.impl.modules.combat.bowmanip.ListenerDigging;
import me.chachoox.lithium.impl.modules.combat.bowmanip.ListenerTick;

public class BowManip
extends Module {
    protected final NumberProperty<Integer> spoofs = new NumberProperty<Integer>(150, 0, 300, new String[]{"Spoofs", "SpoofTicks", "SpoofAmount"}, "How much we want to manipulate the bow, (how many packets, this sends 2 position packets per spoof, usually server limits packets to around 200).");
    protected final NumberProperty<Float> ticks = new NumberProperty<Float>(Float.valueOf(60.0f), Float.valueOf(0.0f), Float.valueOf(200.0f), Float.valueOf(1.0f), new String[]{"Ticks", "TickDelay", "Delay"}, "How long it takes to charge the (manipulated bow, this does not modify the actual minecraft bow, if you shoot a bow normally and this time hasn't passed it will shoot.) bow, time format -> (Minecraft Ticks).");
    protected final NumberProperty<Float> fire = new NumberProperty<Float>(Float.valueOf(60.0f), Float.valueOf(0.0f), Float.valueOf(200.0f), Float.valueOf(1.0f), new String[]{"Fire", "AutoFire", "ReleaseTime"}, "How long to we want to wait to AutoFire the bow, time format -> (Minecraft Ticks), set to 0-3 if you want this to not be used");
    protected long last;
    protected StopWatch timer = new StopWatch();

    public BowManip() {
        super("BowManip", new String[]{"BowManip", "bowkiller", "fastarrows"}, "Manipulates bows to do more damage than normal.", Category.COMBAT);
        this.offerProperties(this.spoofs, this.ticks, this.fire);
        this.offerListeners(new ListenerDigging(this), new ListenerTick(this));
    }

    @Override
    public String getSuffix() {
        if (this.suffixCalculation() > (double)(((Float)this.ticks.getValue()).floatValue() * 50.0f)) {
            return ((Float)this.ticks.getValue()).floatValue() * 50.0f + "";
        }
        return this.suffixCalculation() + "";
    }

    private double suffixCalculation() {
        return (float)(this.timer.getTime() - this.last) + ((Float)this.ticks.getValue()).floatValue() * 50.0f - ((Float)this.ticks.getValue()).floatValue() * 50.0f;
    }
}

