/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.render.glintmodify;

import java.awt.Color;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.ColorProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;

public class GlintModify
extends Module {
    private final Property<Boolean> armor = new Property<Boolean>(true, new String[]{"Armor", "armour"}, "Modifies enchantment glint on armor.");
    private final ColorProperty armorGlint = new ColorProperty(new Color(0, 0, 255), false, new String[]{"ArmorColor", "armourColor", "ArmorColour", "ArmourColour"});
    private final ColorProperty itemGlint = new ColorProperty(new Color(0, 0, 255), false, new String[]{"ItemColor", "ItemColour"});
    protected final NumberProperty<Float> glintScale = new NumberProperty<Float>(Float.valueOf(8.0f), Float.valueOf(0.0f), Float.valueOf(20.0f), Float.valueOf(0.1f), new String[]{"GlintScale", "scale", "gs"}, "The scale of the glint.");
    protected final NumberProperty<Float> glintMult = new NumberProperty<Float>(Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(10.0f), Float.valueOf(0.1f), new String[]{"GlintMult", "Glintmultiplier", "gm"}, "The multipler of the glint.");
    protected final NumberProperty<Float> glintRotate = new NumberProperty<Float>(Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(10.0f), Float.valueOf(0.1f), new String[]{"GlintRotate", "glintrot", "gr"}, "The rotations of the glint.");

    public GlintModify() {
        super("GlintModify", new String[]{"GlintModify", "EnchantColor", "EnchantTweaks", "GlintEdit"}, "Modifies minecrafts enchanting glint.", Category.RENDER);
        this.offerProperties(this.armor, this.armorGlint, this.itemGlint, this.glintScale, this.glintMult, this.glintRotate);
    }

    public Color getEnchantColor() {
        return this.itemGlint.getColor();
    }

    public Color getArmorGlintColor() {
        return this.armorGlint.getColor();
    }

    public boolean isArmorGlint() {
        return this.isEnabled() && this.armor.getValue() != false;
    }

    public float getGlintScale() {
        return ((Float)this.glintScale.getValue()).floatValue();
    }

    public float getFactor() {
        return ((Float)this.glintMult.getValue()).floatValue();
    }

    public float getGlintRotate() {
        return ((Float)this.glintRotate.getValue()).floatValue();
    }
}

