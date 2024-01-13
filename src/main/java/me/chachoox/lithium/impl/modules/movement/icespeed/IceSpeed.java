/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.init.Blocks
 */
package me.chachoox.lithium.impl.modules.movement.icespeed;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.impl.event.events.update.UpdateEvent;
import me.chachoox.lithium.impl.event.listener.LambdaListener;
import net.minecraft.init.Blocks;

public class IceSpeed
extends Module {
    protected final NumberProperty<Float> iceSpeed = new NumberProperty<Float>(Float.valueOf(0.6f), Float.valueOf(0.1f), Float.valueOf(5.0f), Float.valueOf(0.01f), new String[]{"Slipperiness", "Speed", "Factor", "Sped", "slip", "slilppnyhik6febe.onion"}, "What we will set the blocks slipperiness to.");

    public IceSpeed() {
        super("IceSpeed", new String[]{"IceSpeed", "FastIce", "Ic", "Fi"}, "Modifies ice slipperiness.", Category.MOVEMENT);
        this.offerProperties(this.iceSpeed);
        this.offerListeners(new LambdaListener<UpdateEvent>(UpdateEvent.class, event -> {
            Blocks.ICE.slipperiness = ((Float)this.iceSpeed.getValue()).floatValue();
            Blocks.FROSTED_ICE.slipperiness = ((Float)this.iceSpeed.getValue()).floatValue();
            Blocks.PACKED_ICE.slipperiness = ((Float)this.iceSpeed.getValue()).floatValue();
        }));
    }

    @Override
    public void onDisable() {
        Blocks.ICE.slipperiness = 0.98f;
        Blocks.FROSTED_ICE.slipperiness = 0.98f;
        Blocks.PACKED_ICE.slipperiness = 0.98f;
    }
}

