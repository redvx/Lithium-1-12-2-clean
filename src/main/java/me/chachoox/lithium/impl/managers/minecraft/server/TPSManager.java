/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketTimeUpdate
 */
package me.chachoox.lithium.impl.managers.minecraft.server;

import java.util.ArrayDeque;
import me.chachoox.lithium.api.event.bus.Listener;
import me.chachoox.lithium.api.event.bus.SubscriberImpl;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import net.minecraft.network.play.server.SPacketTimeUpdate;

public class TPSManager
extends SubscriberImpl
implements Minecraftable {
    private final ArrayDeque<Float> queue = new ArrayDeque(20);
    private float currentTps;
    private long time;
    private float tps;

    public TPSManager() {
        this.listeners.add(new Listener<PacketEvent.Receive<SPacketTimeUpdate>>(PacketEvent.Receive.class, SPacketTimeUpdate.class){

            @Override
            public void call(PacketEvent.Receive<SPacketTimeUpdate> event) {
                if (TPSManager.this.time != 0L) {
                    if (TPSManager.this.queue.size() > 20) {
                        TPSManager.this.queue.poll();
                    }
                    TPSManager.this.currentTps = Math.max(0.0f, Math.min(20.0f, 20.0f * (1000.0f / (float)(System.currentTimeMillis() - TPSManager.this.time))));
                    TPSManager.this.queue.add(Float.valueOf(TPSManager.this.currentTps));
                    float factor = 0.0f;
                    for (Float qTime : TPSManager.this.queue) {
                        factor += Math.max(0.0f, Math.min(20.0f, qTime.floatValue()));
                    }
                    if (TPSManager.this.queue.size() > 0) {
                        factor /= (float)TPSManager.this.queue.size();
                    }
                    TPSManager.this.tps = factor;
                }
                TPSManager.this.time = System.currentTimeMillis();
            }
        });
    }

    public float getCurrentTps() {
        return this.currentTps;
    }

    public float getTps() {
        return this.tps;
    }

    public float getFactor() {
        return this.tps / 20.0f;
    }
}

