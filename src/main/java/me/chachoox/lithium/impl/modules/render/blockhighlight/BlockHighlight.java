/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.render.blockhighlight;

import java.awt.Color;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.ColorProperty;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.impl.modules.render.blockhighlight.ListenerRender;
import me.chachoox.lithium.impl.modules.render.blockhighlight.mode.RenderMode;

public class BlockHighlight
extends Module {
    protected final EnumProperty<RenderMode> renderMode = new EnumProperty<RenderMode>(RenderMode.OUTLINE, new String[]{"Render", "rend"}, "Type of render.");
    protected final NumberProperty<Float> lineWidth = new NumberProperty<Float>(Float.valueOf(1.0f), Float.valueOf(1.0f), Float.valueOf(4.0f), Float.valueOf(0.1f), new String[]{"LineWidth", "width"}, "Current width.");
    protected final ColorProperty outlineColor = new ColorProperty(new Color(-1), false, new String[]{"OutlineColor", "outlinecol"});
    protected final ColorProperty boxColor = new ColorProperty(new Color(0x34FFFFFF, true), false, new String[]{"BoxColor", "boxcol"});

    public BlockHighlight() {
        super("BlockHighlight", new String[]{"BlockHighlight", "BlockOutline", "Highlighter"}, "Highlights the block we are looking at.", Category.RENDER);
        this.offerProperties(this.renderMode, this.lineWidth, this.outlineColor, this.boxColor);
        this.offerListeners(new ListenerRender(this));
    }

    protected Color getBoxColor() {
        return this.boxColor.getColor();
    }

    protected Color getOutlineColor() {
        return this.outlineColor.getColor();
    }
}

