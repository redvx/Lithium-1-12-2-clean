/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.render.animations;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.impl.modules.render.animations.ListenerSwing;
import me.chachoox.lithium.impl.modules.render.animations.ListenerUpdate;
import me.chachoox.lithium.impl.modules.render.animations.SwingEnum;

public class Animations
extends Module {
    protected final EnumProperty<SwingEnum> swing = new EnumProperty<SwingEnum>(SwingEnum.NONE, new String[]{"Swing", "swin"}, "Type of swinging.");
    protected final Property<Boolean> oldSwing = new Property<Boolean>(false, new String[]{"OldSwing", "olswin"}, "Swinging animation from 1.8.");
    protected final NumberProperty<Double> animationSpeed = new NumberProperty<Double>(27.0, 0.0, 70.0, 1.0, new String[]{"AnimationSpeed", "animspeed"}, "Delay of the eat animation");
    protected final NumberProperty<Float> eatingSpeed = new NumberProperty<Float>(Float.valueOf(4.0f), Float.valueOf(0.0f), Float.valueOf(40.0f), Float.valueOf(1.0f), new String[]{"EatingSpeed", "EatSpeed"}, "How fast u gonna eat fat nigga.");
    protected final NumberProperty<Float> eatHeight = new NumberProperty<Float>(Float.valueOf(0.1f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.1f), new String[]{"Vertical", "ver"}, "Height of the eating animation");

    public Animations() {
        super("Animations", new String[]{"Animations", "anim", "swing"}, "Modifies certain animations", Category.RENDER);
        this.offerProperties(this.swing, this.oldSwing, this.animationSpeed, this.eatingSpeed, this.eatHeight);
        this.offerListeners(new ListenerUpdate(this), new ListenerSwing(this));
    }

    public boolean isOldSwing() {
        return this.isEnabled() && this.oldSwing.getValue() != false;
    }

    public double getAnimationSpeed() {
        return (Double)this.animationSpeed.getValue();
    }

    public float getEatingSpeed() {
        return ((Float)this.eatingSpeed.getValue()).floatValue();
    }

    public float getEatHeight() {
        return ((Float)this.eatHeight.getValue()).floatValue();
    }
}

