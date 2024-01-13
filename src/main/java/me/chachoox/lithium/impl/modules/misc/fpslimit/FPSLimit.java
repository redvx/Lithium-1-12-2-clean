/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.misc.fpslimit;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.impl.event.events.update.TickEvent;
import me.chachoox.lithium.impl.event.listener.LambdaListener;

public class FPSLimit
extends Module {
    private final NumberProperty<Integer> tabbedFPS = new NumberProperty<Integer>(0, 0, 360, new String[]{"TabbedFps", "fps"}, "What we limit the fps to when tabbed in, set to 0 if you want unused.");
    private final NumberProperty<Integer> unfocusedFPS = new NumberProperty<Integer>(0, 0, 60, new String[]{"UnfocusedFps", "unfocusedcpu", "unfocusedfp"}, "What we limit the fps to when not tabbed in, set to 0 if you want unused.");
    private int frames;

    public FPSLimit() {
        super("FPSLimit", new String[]{"FPSLimit", "unfocusedfps", "betterframes"}, "Limits minecraft FPS.", Category.MISC);
        this.offerProperties(this.tabbedFPS, this.unfocusedFPS);
        this.tabbedFPS.addObserver(event -> {
            if ((Integer)this.tabbedFPS.getValue() != 0) {
                this.frames = (Integer)this.tabbedFPS.getValue();
            }
        });
        this.listeners.add(new LambdaListener<TickEvent>(TickEvent.class, event -> {
            if ((Integer)this.tabbedFPS.getValue() != 0) {
                FPSLimit.mc.gameSettings.limitFramerate = this.frames;
            }
        }));
    }

    @Override
    public void onEnable() {
        if ((Integer)this.tabbedFPS.getValue() != 0) {
            this.frames = (Integer)this.tabbedFPS.getValue();
        }
    }

    public int getUnfocusedFPS() {
        return (Integer)this.unfocusedFPS.getValue();
    }

    public int getFocusedFPS() {
        return (Integer)this.tabbedFPS.getValue();
    }
}

