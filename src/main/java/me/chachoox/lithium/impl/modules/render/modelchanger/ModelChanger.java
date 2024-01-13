/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.render.modelchanger;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.impl.modules.render.modelchanger.ListenerPostRender;
import me.chachoox.lithium.impl.modules.render.modelchanger.ListenerPreRender;
import me.chachoox.lithium.impl.modules.render.modelchanger.ListenerRenderItemSide;

public class ModelChanger
extends Module {
    public final Property<Boolean> noSway = new Property<Boolean>(false, new String[]{"AntiSway", "RemoveSway", "NoSway"}, "Removes arm swaying when swinging your camera.");
    public final NumberProperty<Float> angleTranslate = new NumberProperty<Float>(Float.valueOf(0.0f), Float.valueOf(-180.0f), Float.valueOf(180.0f), Float.valueOf(2.0f), new String[]{"Angle", "AngleTranslate", "AngleTraductor"}, "Sets both hands on this angle.");
    public final NumberProperty<Float> offsetMain = new NumberProperty<Float>(Float.valueOf(0.0f), Float.valueOf(-5.0f), Float.valueOf(5.0f), Float.valueOf(0.1f), new String[]{"OffsetMain", "offsetmainhand"}, "Offset of the offhand items.");
    public final NumberProperty<Float> offsetOff = new NumberProperty<Float>(Float.valueOf(0.0f), Float.valueOf(-5.0f), Float.valueOf(5.0f), Float.valueOf(0.1f), new String[]{"OffsetOff", "offsetoffhand"}, "Offset of the mainhand item.");
    protected final NumberProperty<Float> translateX = new NumberProperty<Float>(Float.valueOf(0.0f), Float.valueOf(-2.0f), Float.valueOf(2.0f), Float.valueOf(0.05f), new String[]{"TranslateX", "transx"}, "Translation of the X axis on both hands.");
    protected final NumberProperty<Float> translateY = new NumberProperty<Float>(Float.valueOf(0.0f), Float.valueOf(-2.0f), Float.valueOf(2.0f), Float.valueOf(0.05f), new String[]{"TranslateY", "transy"}, "Translation of the Y axis on both hands.");
    protected final NumberProperty<Float> translateZ = new NumberProperty<Float>(Float.valueOf(0.0f), Float.valueOf(-2.0f), Float.valueOf(2.0f), Float.valueOf(0.05f), new String[]{"TranslateZ", "transz"}, "Translation of the Z axis on both hands.");
    protected final NumberProperty<Float> scaleX = new NumberProperty<Float>(Float.valueOf(1.0f), Float.valueOf(0.0f), Float.valueOf(2.0f), Float.valueOf(0.05f), new String[]{"ScaleX", "scalx"}, "Scale of the X axis on both hands.");
    protected final NumberProperty<Float> scaleY = new NumberProperty<Float>(Float.valueOf(1.0f), Float.valueOf(0.0f), Float.valueOf(2.0f), Float.valueOf(0.05f), new String[]{"ScaleY", "scaly"}, "Scale of the Y axis on both hands.");
    protected final NumberProperty<Float> scaleZ = new NumberProperty<Float>(Float.valueOf(1.0f), Float.valueOf(0.0f), Float.valueOf(2.0f), Float.valueOf(0.05f), new String[]{"ScaleZ", "scalz"}, "Scale of the Z axis on both hands.");
    protected final NumberProperty<Float> rotateX = new NumberProperty<Float>(Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(180.0f), Float.valueOf(2.0f), new String[]{"RotateX", "rotx"}, "Rotations of the X axis on both hands.");
    protected final NumberProperty<Float> rotateY = new NumberProperty<Float>(Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(180.0f), Float.valueOf(2.0f), new String[]{"RotateY", "roty"}, "Rotations of the Y axis on both hands.");
    protected final NumberProperty<Float> rotateZ = new NumberProperty<Float>(Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(180.0f), Float.valueOf(2.0f), new String[]{"RotateZ", "rotz"}, "Rotations of the Z axis on both hands.");

    public ModelChanger() {
        super("ViewModelChanger", new String[]{"ViewModelChanger", "viewmodel", "modelchanger"}, "Changes your viewmodel.", Category.RENDER);
        this.offerProperties(this.noSway, this.angleTranslate, this.offsetMain, this.offsetOff, this.translateX, this.translateY, this.translateZ, this.scaleX, this.scaleY, this.scaleZ, this.rotateX, this.rotateY, this.rotateZ);
        this.offerListeners(new ListenerRenderItemSide(this), new ListenerPostRender(this), new ListenerPreRender(this));
    }
}

