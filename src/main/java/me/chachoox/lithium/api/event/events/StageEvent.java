/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.api.event.events;

import me.chachoox.lithium.api.event.events.Event;
import me.chachoox.lithium.api.event.events.Stage;

public class StageEvent
extends Event {
    private final Stage stage;

    public StageEvent(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return this.stage;
    }
}

