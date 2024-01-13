/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.misc.announcer;

import me.chachoox.lithium.impl.event.events.misc.EatEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.misc.announcer.Announcer;
import me.chachoox.lithium.impl.modules.misc.announcer.util.Type;

public class ListenerEat
extends ModuleListener<Announcer, EatEvent> {
    public ListenerEat(Announcer module) {
        super(module, EatEvent.class);
    }

    @Override
    public void call(EatEvent event) {
        if (event.getEntity() == ListenerEat.mc.player && ((Announcer)this.module).eat.getValue().booleanValue()) {
            ((Announcer)this.module).foodStack = event.getStack();
            ((Announcer)this.module).addEvent(Type.EAT);
        }
    }
}

