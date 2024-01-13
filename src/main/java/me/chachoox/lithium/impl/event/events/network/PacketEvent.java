/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.INetHandler
 *  net.minecraft.network.Packet
 */
package me.chachoox.lithium.impl.event.events.network;

import java.util.ArrayDeque;
import java.util.Deque;
import me.chachoox.lithium.api.event.events.Event;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.api.util.thread.SafeRunnable;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;

public class PacketEvent<T extends Packet<? extends INetHandler>>
extends Event
implements Minecraftable {
    private final T packet;

    private PacketEvent(T packet) {
        this.packet = packet;
    }

    public T getPacket() {
        return this.packet;
    }

    /* synthetic */ PacketEvent(Packet x0, 1 x1) {
        this(x0);
    }

    public static class Post<T extends Packet<? extends INetHandler>>
    extends PacketEvent<T> {
        public Post(T packet) {
            super((Packet)packet, null);
        }
    }

    public static class Receive<T extends Packet<? extends INetHandler>>
    extends PacketEvent<T> {
        private final Deque<Runnable> postEvents = new ArrayDeque<Runnable>();

        public Receive(T packet) {
            super((Packet)packet, null);
        }

        public void addPostEvent(SafeRunnable runnable) {
            this.postEvents.add(runnable);
        }

        public Deque<Runnable> getPostEvents() {
            return this.postEvents;
        }
    }

    public static class NoEvent<T extends Packet<? extends INetHandler>>
    extends PacketEvent<T> {
        public NoEvent(T packet) {
            super((Packet)packet, null);
        }
    }

    public static class Send<T extends Packet<? extends INetHandler>>
    extends PacketEvent<T> {
        public Send(T packet) {
            super((Packet)packet, null);
        }
    }
}

