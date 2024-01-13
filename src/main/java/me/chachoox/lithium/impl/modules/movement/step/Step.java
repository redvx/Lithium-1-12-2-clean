/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.movement.step;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.impl.modules.movement.step.ListenerStep;
import me.chachoox.lithium.impl.modules.movement.step.ListenerUpdate;
import me.chachoox.lithium.impl.modules.movement.step.mode.StepMode;

public class Step
extends Module {
    protected final EnumProperty<StepMode> mode = new EnumProperty<StepMode>(StepMode.VANILLA, new String[]{"Mode", "Type", "Method"}, "Normal - Sends a position packet to the block we are stepping / Vanilla - Sets your position to the block we are stepping.");
    protected final NumberProperty<Float> height = new NumberProperty<Float>(Float.valueOf(2.0f), Float.valueOf(0.6f), Float.valueOf(5.0f), Float.valueOf(0.1f), new String[]{"StepHeight", "height", "h"}, "How much we want to set our step height to on entities and for ourself.");
    protected final StopWatch timer = new StopWatch();

    public Step() {
        super("Step", new String[]{"Step", "StepHeight", "StepModify"}, "Modifies player step height.", Category.MOVEMENT);
        this.offerProperties(this.mode, this.height);
        this.offerListeners(new ListenerUpdate(this), new ListenerStep(this));
    }

    @Override
    public String getSuffix() {
        return this.mode.getFixedValue();
    }

    @Override
    public void onDisable() {
        Step.mc.player.stepHeight = 0.6f;
    }

    public double[] getOffset(double height) {
        if (height == 0.75) {
            return new double[]{0.42, 0.753, 0.75};
        }
        if (height == 0.8125) {
            return new double[]{0.39, 0.7, 0.8125};
        }
        if (height == 0.875) {
            return new double[]{0.39, 0.7, 0.875};
        }
        if (height == 1.0) {
            return new double[]{0.42, 0.753, 1.0};
        }
        if (height == 1.5) {
            return new double[]{0.42, 0.75, 1.0, 1.16, 1.23, 1.2};
        }
        if (height == 2.0) {
            return new double[]{0.42, 0.78, 0.63, 0.51, 0.9, 1.21, 1.45, 1.43};
        }
        if (height == 2.5) {
            return new double[]{0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.907};
        }
        return null;
    }
}

