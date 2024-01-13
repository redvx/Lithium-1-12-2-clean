/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.EntityEquipmentSlot
 */
package me.chachoox.lithium.impl.modules.movement.elytrafly;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.impl.modules.movement.elytrafly.ListenerMotion;
import me.chachoox.lithium.impl.modules.movement.elytrafly.ListenerMove;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;

public class ElytraFly
extends Module {
    protected final Property<Boolean> stopInWater = new Property<Boolean>(false, new String[]{"StopInWater", "meloveeatbugs", "waterstop", "nowater", "Water"}, "Stops elytra flying in water.");
    protected final NumberProperty<Float> hSpeed = new NumberProperty<Float>(Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(20.0f), Float.valueOf(0.1f), new String[]{"HorizontalSpeed", "hs", "horizontalsped", "speed"}, "Horizontal speed.");
    protected final Property<Boolean> vertical = new Property<Boolean>(false, new String[]{"Vertical", "vert", "v"}, "Allows you to move vertically");
    protected final NumberProperty<Float> vSpeed = new NumberProperty<Float>(Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(20.0f), Float.valueOf(0.1f), new String[]{"VeritcalSpeed", "vs", "verticalsped", "vspeed"}, "Vertical speed.");
    protected final Property<Boolean> wasp = new Property<Boolean>(false, new String[]{"Wasp", "mmmbugs", "NoDownwards", "Flat"}, "Control elytra fly but stops you from moving down at all.");
    protected final Property<Boolean> boost = new Property<Boolean>(false, new String[]{"Boost", "Fast", "NoRocket"}, "Boosts you like you have rockets whenever you press space.");
    protected final Property<Boolean> autoStart = new Property<Boolean>(false, new String[]{"AutoStart", "auto", "instantstart", "start"}, "Automatically starts elytra flying when we jump.");
    protected final StopWatch startTimer = new StopWatch();

    public ElytraFly() {
        super("ElytraFly", new String[]{"ElytraFly", "EFly", "ElytraPlus", "EP", "E+", "ElytraFlight"}, "Tweaks how flying with elytras work.", Category.MOVEMENT);
        this.offerProperties(this.stopInWater, this.hSpeed, this.vertical, this.vSpeed, this.wasp, this.boost, this.autoStart);
        this.offerListeners(new ListenerMove(this), new ListenerMotion(this));
    }

    protected boolean isElytra() {
        return ElytraFly.mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == Items.ELYTRA && ElytraFly.mc.player.isElytraFlying();
    }
}

