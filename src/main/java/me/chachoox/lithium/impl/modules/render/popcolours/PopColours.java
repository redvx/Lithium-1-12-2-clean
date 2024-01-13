/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.render.popcolours;

import java.awt.Color;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.ColorProperty;
import me.chachoox.lithium.api.property.NumberProperty;

public class PopColours
extends Module {
    private final NumberProperty<Float> scale = new NumberProperty<Float>(Float.valueOf(0.75f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.01f), new String[]{"Scale", "scal"}, "The scale of the totem pop particles.");
    private final ColorProperty color = new ColorProperty(Color.GREEN, false, new String[]{"FirstColor", "firstcolour"});
    private final ColorProperty secondColor = new ColorProperty(Color.YELLOW, false, new String[]{"SecondColor", "secondcolour"});
    private final NumberProperty<Float> randomizeRed = new NumberProperty<Float>(Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.1f), new String[]{"FactorRed", "facred"}, "How much we want to randomize the red on the pops.");
    private final NumberProperty<Float> randomizeGreen = new NumberProperty<Float>(Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.1f), new String[]{"FactorGreen", "facgreen"}, "How much we want to randomize the blue on the pops.");
    private final NumberProperty<Float> randomizeBlue = new NumberProperty<Float>(Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.1f), new String[]{"FactorBlue", "facblue"}, "How much we want to randomize the blue on the pops.");

    public PopColours() {
        super("PopColours", new String[]{"PopColours", "popcolor", "totempopcolor"}, "Changes the color of totem pop particles.", Category.RENDER);
        this.offerProperties(this.scale, this.color, this.secondColor, this.randomizeRed, this.randomizeGreen, this.randomizeBlue);
    }

    public Color getColor() {
        return this.color.getColor();
    }

    public Color getSecondColor() {
        return this.secondColor.getColor();
    }

    public Float getScale() {
        return (Float)this.scale.getValue();
    }

    public Float getRandomRed() {
        return (Float)this.randomizeRed.getValue();
    }

    public Float getRandomBlue() {
        return (Float)this.randomizeBlue.getValue();
    }

    public Float getRandomGreen() {
        return (Float)this.randomizeGreen.getValue();
    }
}

