/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.render.pearltrace;

import java.awt.Color;
import java.util.concurrent.ConcurrentHashMap;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.ColorProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.impl.modules.render.pearltrace.ListenerRender;
import me.chachoox.lithium.impl.modules.render.pearltrace.ListenerUpdate;
import me.chachoox.lithium.impl.modules.render.pearltrace.util.ThrownEntity;

public class PearlTrace
extends Module {
    protected final NumberProperty<Float> width = new NumberProperty<Float>(Float.valueOf(1.0f), Float.valueOf(1.0f), Float.valueOf(4.0f), Float.valueOf(0.1f), new String[]{"Width", "width", "linewidth"}, "Thickness of the line.");
    protected final NumberProperty<Integer> timeout = new NumberProperty<Integer>(5000, 250, 10000, new String[]{"Timeout", "time"}, "How many milliseconds each trail will last.");
    protected final ColorProperty color = new ColorProperty(new Color(-1), false, new String[]{"Color", "colour"});
    protected long time;
    protected final ConcurrentHashMap<Integer, ThrownEntity> thrownEntities = new ConcurrentHashMap();

    public PearlTrace() {
        super("PearlTrace", new String[]{"PearlTrace", "trails", "pearls"}, "Draws trails to pearls.", Category.RENDER);
        this.offerProperties(this.width, this.timeout, this.color);
        this.offerListeners(new ListenerRender(this), new ListenerUpdate(this));
    }

    protected Color getColor() {
        return this.color.getColor();
    }
}

