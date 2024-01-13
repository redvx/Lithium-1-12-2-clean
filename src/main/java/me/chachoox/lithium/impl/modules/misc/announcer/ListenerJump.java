/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.misc.announcer;

import me.chachoox.lithium.impl.event.events.movement.actions.JumpEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.misc.announcer.Announcer;
import me.chachoox.lithium.impl.modules.misc.announcer.util.Type;

public class ListenerJump
extends ModuleListener<Announcer, JumpEvent> {
    public ListenerJump(Announcer module) {
        super(module, JumpEvent.class);
    }

    @Override
    public void call(JumpEvent event) {
        if (((Announcer)this.module).jump.getValue().booleanValue() && ((Announcer)this.module).jumpTimer.passed((Integer)((Announcer)this.module).delay.getValue() * 2000)) {
            ((Announcer)this.module).addEvent(Type.JUMP);
        }
    }
}

