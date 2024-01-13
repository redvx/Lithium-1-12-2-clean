/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.other.colours;

import java.awt.Color;
import me.chachoox.lithium.api.event.bus.Listener;
import me.chachoox.lithium.api.event.bus.instance.Bus;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.PersistentModule;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.colors.ColorUtil;
import me.chachoox.lithium.api.util.colors.HSLColor;
import me.chachoox.lithium.impl.event.events.update.TickEvent;

public class Colours
extends PersistentModule {
    public final NumberProperty<Float> hue = new NumberProperty<Float>(Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(360.0f), Float.valueOf(3.0f), new String[]{"Hue", "huee"}, "Hue of the global color.");
    public final NumberProperty<Float> saturation = new NumberProperty<Float>(Float.valueOf(100.0f), Float.valueOf(0.0f), Float.valueOf(100.0f), Float.valueOf(1.0f), new String[]{"Saturation", "satur"}, "Saturation of the global color.");
    public final NumberProperty<Float> lightness = new NumberProperty<Float>(Float.valueOf(45.0f), Float.valueOf(0.0f), Float.valueOf(100.0f), Float.valueOf(1.0f), new String[]{"Lightness", "brightness"}, "Lightness of the global color.");
    public final NumberProperty<Float> friendHue = new NumberProperty<Float>(Float.valueOf(180.0f), Float.valueOf(0.0f), Float.valueOf(360.0f), Float.valueOf(3.0f), new String[]{"FriendHue", "frdhue"}, "Hue of the global friendcolor.");
    public final NumberProperty<Float> friendSaturation = new NumberProperty<Float>(Float.valueOf(100.0f), Float.valueOf(0.0f), Float.valueOf(100.0f), Float.valueOf(1.0f), new String[]{"FriendSaturation", "frdsatur"}, "Saturation of the global friend color.");
    public final NumberProperty<Float> friendLightness = new NumberProperty<Float>(Float.valueOf(45.0f), Float.valueOf(0.0f), Float.valueOf(100.0f), Float.valueOf(1.0f), new String[]{"FriendLightness", "frdlightness", "friendbrightness", "frdbrightness"}, "Lightness of the global friend color.");
    private final Property<Boolean> friendRainbow = new Property<Boolean>(false, new String[]{"FriendRainbow", "frdrainbow"}, "Changes global friend color to a rainbow.");
    private final Property<Boolean> rainbow = new Property<Boolean>(false, new String[]{"Rainbow", "ranbow"}, "Sets the global color to a rainbow.");
    private final NumberProperty<Integer> speed = new NumberProperty<Integer>(50, 0, 100, new String[]{"RainbowSpeed", "rainbowsped"}, "Speed of the rainbow.");
    public final NumberProperty<Float> factor = new NumberProperty<Float>(Float.valueOf(1.0f), Float.valueOf(0.0f), Float.valueOf(5.0f), Float.valueOf(0.1f), new String[]{"RainbowHue", "rainbowfactor"}, "The length of the rainbow.");
    private float rainbowHue;
    private static Colours COLOURS;

    public Colours() {
        super("Colours", new String[]{"Colours", "colors", "color"}, "Global colours.", Category.OTHER);
        this.offerProperties(this.hue, this.saturation, this.lightness, this.friendHue, this.friendSaturation, this.friendLightness, this.friendRainbow, this.rainbow, this.speed, this.factor);
        this.offerListeners(new Listener<TickEvent>(TickEvent.class){

            @Override
            public void call(TickEvent event) {
                Colours.this.update();
            }
        });
        COLOURS = this;
    }

    public static Colours get() {
        return COLOURS;
    }

    @Override
    public void onLoad() {
        this.setEnabled(true);
        Bus.EVENT_BUS.subscribe(this);
    }

    private void update() {
        if ((Integer)this.speed.getValue() == 0) {
            return;
        }
        this.rainbowHue = (float)(System.currentTimeMillis() % (long)(360 * (Integer)this.speed.getValue())) / (360.0f * (float)((Integer)this.speed.getValue()).intValue()) * 360.0f;
    }

    public Color getColour() {
        return this.rainbow.getValue() != false ? this.getRainbow() : HSLColor.toRGB(((Float)this.hue.getValue()).floatValue(), ((Float)this.saturation.getValue()).floatValue(), ((Float)this.lightness.getValue()).floatValue());
    }

    public Color getFriendColour() {
        return this.friendRainbow.getValue() != false ? this.getRainbow() : HSLColor.toRGB(((Float)this.friendHue.getValue()).floatValue(), ((Float)this.friendSaturation.getValue()).floatValue(), ((Float)this.friendLightness.getValue()).floatValue());
    }

    public Color getColourCustomAlpha(int alpha) {
        return ColorUtil.changeAlpha(this.getColour(), alpha);
    }

    public Color getRainbow() {
        return HSLColor.toRGB(this.rainbowHue, ((Float)this.saturation.getValue()).floatValue(), ((Float)this.lightness.getValue()).floatValue());
    }

    public float getRainbowHue() {
        return this.rainbowHue;
    }

    public float getRainbowHueByPosition(double pos) {
        return (float)((double)this.rainbowHue - pos * (double)0.001f);
    }
}

