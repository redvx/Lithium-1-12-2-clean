/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.misc.announcer;

import me.chachoox.lithium.impl.event.events.blocks.PlaceBlockEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.misc.announcer.Announcer;
import me.chachoox.lithium.impl.modules.misc.announcer.util.Type;

public class ListenerPlace
extends ModuleListener<Announcer, PlaceBlockEvent> {
    public ListenerPlace(Announcer module) {
        super(module, PlaceBlockEvent.class);
    }

    @Override
    public void call(PlaceBlockEvent event) {
        if (((Announcer)this.module).place.getValue().booleanValue()) {
            ((Announcer)this.module).placeStack = event.getStack();
            ((Announcer)this.module).addEvent(Type.PLACE);
        }
    }
}

