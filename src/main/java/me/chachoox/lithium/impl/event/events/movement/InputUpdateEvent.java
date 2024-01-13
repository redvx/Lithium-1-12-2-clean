/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.MovementInput
 */
package me.chachoox.lithium.impl.event.events.movement;

import me.chachoox.lithium.api.event.events.Event;
import net.minecraft.util.MovementInput;

public class InputUpdateEvent
extends Event {
    private final MovementInput input;

    public InputUpdateEvent(MovementInput input) {
        this.input = input;
    }

    public MovementInput getMovementInput() {
        return this.input;
    }
}

