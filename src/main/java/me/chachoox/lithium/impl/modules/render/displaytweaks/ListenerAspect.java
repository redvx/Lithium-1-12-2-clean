/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.render.displaytweaks;

import me.chachoox.lithium.impl.event.events.screen.AspectRatioEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.render.displaytweaks.DisplayTweaks;

public class ListenerAspect
extends ModuleListener<DisplayTweaks, AspectRatioEvent> {
    public ListenerAspect(DisplayTweaks module) {
        super(module, AspectRatioEvent.class);
    }

    @Override
    public void call(AspectRatioEvent event) {
        if (((DisplayTweaks)this.module).aspectRatio.getValue().booleanValue()) {
            event.setAspectRatio((float)((Integer)((DisplayTweaks)this.module).aspectRatioWidth.getValue()).intValue() / (float)((Integer)((DisplayTweaks)this.module).aspectRatioHeight.getValue()).intValue());
        }
    }
}

