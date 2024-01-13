/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.render.chams;

import java.awt.Color;
import me.chachoox.lithium.impl.event.events.render.misc.DamageColorEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.render.chams.Chams;

public class ListenerDamageColour
extends ModuleListener<Chams, DamageColorEvent> {
    public ListenerDamageColour(Chams module) {
        super(module, DamageColorEvent.class);
    }

    @Override
    public void call(DamageColorEvent event) {
        if (((Chams)this.module).damage.getValue().booleanValue()) {
            Color color = ((Chams)this.module).getDamageColor();
            event.setRed((float)color.getRed() / 255.0f);
            event.setGreen((float)color.getGreen() / 255.0f);
            event.setBlue((float)color.getBlue() / 255.0f);
            event.setAlpha((float)((Color)((Chams)this.module).damageColor.getValue()).getAlpha() / 255.0f);
        }
    }
}

