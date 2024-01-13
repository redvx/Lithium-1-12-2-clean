/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.player.quiver;

import me.chachoox.lithium.api.property.util.Bind;
import me.chachoox.lithium.impl.event.events.misc.KeyboardEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.player.quiver.Quiver;

public class ListenerKeyboard
extends ModuleListener<Quiver, KeyboardEvent> {
    public ListenerKeyboard(Quiver module) {
        super(module, KeyboardEvent.class);
    }

    @Override
    public void call(KeyboardEvent event) {
        if (((Bind)((Quiver)this.module).cycleButton.getValue()).getKey() == event.getKey() && event.getEventState()) {
            ((Quiver)this.module).cycle(false, false);
        }
    }
}

