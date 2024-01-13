/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.player.fakerotation;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.impl.modules.player.fakerotation.ListenerMotion;
import me.chachoox.lithium.impl.modules.player.fakerotation.enums.Pitch;

public class FakeRotation
extends Module {
    protected final Property<Boolean> randomize = new Property<Boolean>(false, new String[]{"Randomize", "rand", "random"}, "Randomizes yaw and pitch not compatible with any other mode.");
    protected final Property<Boolean> jitter = new Property<Boolean>(false, new String[]{"Jitter", "jit", "j"}, "Shakes your head.");
    protected final NumberProperty<Integer> speed = new NumberProperty<Integer>(20, 1, 20, new String[]{"Speed", "jitterspeed", "js"}, "How fast we want to jitter.");
    protected final NumberProperty<Integer> limit = new NumberProperty<Integer>(35, 0, 180, new String[]{"Limit", "max", "limiter"}, "The max amount of yaw we can jitter.");
    protected final EnumProperty<Pitch> pitch = new EnumProperty<Pitch>(Pitch.UP, new String[]{"Pitch", "pit"}, "Forces your pitch upwards or downwards.");
    protected final Property<Boolean> strict = new Property<Boolean>(false, new String[]{"Strict", "NoItems", "rotationfix"}, "Stops faking rotations when we are using a keybind like attack.");
    protected boolean check;

    public FakeRotation() {
        super("FakeRotation", new String[]{"FakeRotation", "FakeRot", "AntiAim"}, "Spoofs your rotations.", Category.PLAYER);
        this.offerProperties(this.randomize, this.jitter, this.speed, this.limit, this.pitch, this.strict);
        this.offerListeners(new ListenerMotion(this));
    }
}

