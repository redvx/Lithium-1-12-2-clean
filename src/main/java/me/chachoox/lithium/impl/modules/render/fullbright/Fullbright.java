/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.render.fullbright;

import java.awt.Color;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.ColorProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.impl.modules.render.fullbright.ListenerRender;

public class Fullbright
extends Module {
    protected final Property<Boolean> lightMap = new Property<Boolean>(false, new String[]{"Colored", "coloured", "lightmap", "ambience"}, "Changes the minecraft lightmap color.");
    protected final ColorProperty lightMapColor = new ColorProperty(new Color(85, 75, 255, 125), false, new String[]{"Color", "colour", "lightmapcolor"});
    protected final Property<Boolean> bozeMode = new Property<Boolean>(false, new String[]{"BozeAmbience", "gay"}, "yea.");

    public Fullbright() {
        super("FullBright", new String[]{"Fullbright", "Fullbrightness", "Gamma", "Lightness", "Ambience", "Ambient", "Brightness"}, "Modifies game brightness and the lightmap.", Category.RENDER);
        this.offerProperties(this.lightMap, this.lightMapColor, this.bozeMode);
        this.offerListeners(new ListenerRender(this));
    }

    @Override
    public void onEnable() {
        Fullbright.mc.gameSettings.gammaSetting = 100.0f;
    }

    @Override
    public void onDisable() {
        Fullbright.mc.gameSettings.gammaSetting = 0.5f;
    }

    public boolean customColor() {
        return this.lightMap.getValue() != false && this.bozeMode.getValue() == false;
    }

    public Color getColor() {
        return this.lightMapColor.getColor();
    }
}

