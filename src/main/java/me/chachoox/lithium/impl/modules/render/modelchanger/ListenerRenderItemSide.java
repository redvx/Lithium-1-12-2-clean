/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.render.modelchanger;

import me.chachoox.lithium.impl.event.events.render.item.RenderItemSideEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.render.modelchanger.ModelChanger;

public class ListenerRenderItemSide
extends ModuleListener<ModelChanger, RenderItemSideEvent> {
    public ListenerRenderItemSide(ModelChanger module) {
        super(module, RenderItemSideEvent.class);
    }

    @Override
    public void call(RenderItemSideEvent event) {
        event.setX(((Float)((ModelChanger)this.module).scaleX.getValue()).floatValue());
        event.setY(((Float)((ModelChanger)this.module).scaleY.getValue()).floatValue());
        event.setZ(((Float)((ModelChanger)this.module).scaleZ.getValue()).floatValue());
    }
}

