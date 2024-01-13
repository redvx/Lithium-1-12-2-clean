/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.render.fovmodifier;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.NumberProperty;

public class FovModifier
extends Module {
    private final NumberProperty<Float> flying = new NumberProperty<Float>(Float.valueOf(1.0f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.01f), new String[]{"Fly", "flight", "Flying"}, "Fov modifier for when we are flying.");
    private final NumberProperty<Float> sprinting = new NumberProperty<Float>(Float.valueOf(0.4f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.01f), new String[]{"Sprint", "Sprinting", "Running"}, "Fov modifier for when we are sprinting.");
    private final NumberProperty<Float> slow = new NumberProperty<Float>(Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.01f), new String[]{"Slowness", "Slow", "Slowed"}, "Fov modifier for when we are slowed.");
    private final NumberProperty<Float> swiftness = new NumberProperty<Float>(Float.valueOf(0.2f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.01f), new String[]{"Swiftness", "Speed", "sped"}, "Fov modifier for when we have speed.");
    private final NumberProperty<Float> aim = new NumberProperty<Float>(Float.valueOf(0.2f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.01f), new String[]{"Aim", "Scoped", "Aimed"}, "Fov modifier for when we are aiming.");
    private static FovModifier FOV_MODIFIER;

    public FovModifier() {
        super("FovModifier", new String[]{"FovModifier", "FovMod", "FovChanger", "BetterFov"}, "Modifies dynamic fov.", Category.RENDER);
        this.offerProperties(this.flying, this.sprinting, this.slow, this.swiftness, this.aim);
        FOV_MODIFIER = this;
    }

    public static FovModifier get() {
        return FOV_MODIFIER == null ? (FOV_MODIFIER = new FovModifier()) : FOV_MODIFIER;
    }

    public float fly() {
        return ((Float)this.flying.getValue()).floatValue();
    }

    public float sprint() {
        return ((Float)this.sprinting.getValue()).floatValue();
    }

    public float slow() {
        return ((Float)this.slow.getValue()).floatValue();
    }

    public float swiftness() {
        return ((Float)this.swiftness.getValue()).floatValue();
    }

    public float aim() {
        return ((Float)this.aim.getValue()).floatValue();
    }
}

