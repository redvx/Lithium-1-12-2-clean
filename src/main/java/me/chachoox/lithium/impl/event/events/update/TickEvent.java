/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.event.events.update;

import me.chachoox.lithium.api.interfaces.Minecraftable;

public class TickEvent
implements Minecraftable {
    public boolean isSafe() {
        return TickEvent.mc.player != null && TickEvent.mc.world != null;
    }

    public static final class Post
    extends TickEvent {
    }

    public static final class PostWorldTick
    extends TickEvent {
    }
}

