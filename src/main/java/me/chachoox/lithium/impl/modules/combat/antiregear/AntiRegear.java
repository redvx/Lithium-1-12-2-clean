/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.combat.antiregear;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.impl.modules.combat.antiregear.ListenerMotion;

public class AntiRegear
extends Module {
    protected final Property<Boolean> rotation = new Property<Boolean>(false, new String[]{"Rotation", "rotate"}, "Rotates to the current shulker.");
    protected final NumberProperty<Float> enemyRange = new NumberProperty<Float>(Float.valueOf(8.0f), Float.valueOf(1.0f), Float.valueOf(15.0f), Float.valueOf(1.0f), new String[]{"EnemyRange", "enemyrang", "er"}, "How far an enemy has to be before it starts breaking shulkers.");
    protected final NumberProperty<Float> range = new NumberProperty<Float>(Float.valueOf(5.0f), Float.valueOf(1.0f), Float.valueOf(6.0f), Float.valueOf(0.1f), new String[]{"Range", "rang", "r"}, "The range of how far you want to break shulkers from.");
    protected final NumberProperty<Integer> delay = new NumberProperty<Integer>(150, 0, 250, new String[]{"Delay", "d"}, "The delay between switching from one shulker to another.");
    protected final Property<Boolean> raytrace = new Property<Boolean>(false, new String[]{"Raytrace", "ray"}, "Wont break shulkers through blocks.");
    protected final StopWatch timer = new StopWatch();

    public AntiRegear() {
        super("AntiRegear", new String[]{"AntiRegear", "noregear", "regear"}, "Breaks your opponent's shulkers.", Category.COMBAT);
        this.offerProperties(this.rotation, this.enemyRange, this.range, this.delay, this.raytrace);
        this.offerListeners(new ListenerMotion(this));
    }
}

