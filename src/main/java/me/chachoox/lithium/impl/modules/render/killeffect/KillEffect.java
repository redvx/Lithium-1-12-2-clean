/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.render.killeffect;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.impl.modules.render.killeffect.ListenerDeath;

public class KillEffect
extends Module {
    protected final Property<Boolean> sound = new Property<Boolean>(false, new String[]{"Sound", "sounds", "thunder", "boomcrashbang"}, "Plays the lightning sound whenever someone dies aswell.");

    public KillEffect() {
        super("KillEffect", new String[]{"KillEffect", "KillEffects", "LightningDeath", "Lightning"}, "Draws a strike on players death position", Category.RENDER);
        this.offerProperties(this.sound);
        this.offerListeners(new ListenerDeath(this));
    }
}

