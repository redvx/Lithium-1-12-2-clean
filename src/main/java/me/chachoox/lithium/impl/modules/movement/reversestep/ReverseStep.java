/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.movement.reversestep;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.impl.modules.movement.reversestep.ListenerMotion;

public class ReverseStep
extends Module {
    protected final NumberProperty<Double> speed = new NumberProperty<Double>(2.5, 0.1, 10.0, 0.1, new String[]{"Speed", "FallSpeed"}, "How fast we want to fall.");
    protected final NumberProperty<Double> distance = new NumberProperty<Double>(5.0, 0.1, 10.0, 1.0, new String[]{"Distance", "FallDistance"}, "Max fall distance for reverse step.");
    protected boolean jumped;
    protected boolean waitForOnGround;
    protected int packets;

    public ReverseStep() {
        super("ReverseStep", new String[]{"ReverseStep", "FastFall", "InstantFall"}, "Slams you into the fucking ground.", Category.MOVEMENT);
        this.offerProperties(this.speed, this.distance);
        this.offerListeners(new ListenerMotion(this));
    }
}

