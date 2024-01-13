/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.event.events.keyboard;

import me.chachoox.lithium.api.event.events.Event;

public class MouseEvent
extends Event {
    private final int button;
    private final boolean state;

    public MouseEvent(int button, boolean state) {
        this.button = button;
        this.state = state;
    }

    public boolean getState() {
        return this.state;
    }

    public int getButton() {
        return this.button;
    }
}

