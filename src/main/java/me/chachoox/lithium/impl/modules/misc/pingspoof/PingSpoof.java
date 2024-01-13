/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 */
package me.chachoox.lithium.impl.modules.misc.pingspoof;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import me.chachoox.lithium.api.event.bus.Listener;
import me.chachoox.lithium.api.event.bus.instance.Bus;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.util.network.PacketUtil;
import me.chachoox.lithium.api.util.thread.ThreadUtil;
import me.chachoox.lithium.impl.event.events.misc.ShutDownEvent;
import me.chachoox.lithium.impl.event.events.network.DisconnectEvent;
import me.chachoox.lithium.impl.event.listener.LambdaListener;
import me.chachoox.lithium.impl.modules.misc.pingspoof.ListenerKeepAlive;
import net.minecraft.network.Packet;

public class PingSpoof
extends Module {
    private final NumberProperty<Integer> latency = new NumberProperty<Integer>(50, 0, 3000, new String[]{"Latency", "Delay", "ping", "spoof", "spoofer", "Spoofamount", "p", "SHOOTPOLLOSXD"}, "How much we want to add to our actual latency.");
    private final NumberProperty<Integer> jitter = new NumberProperty<Integer>(10, 0, 50, new String[]{"Jitter", "jit", "jitte", "j", "IFUCKINGHATEPOLLOSXD"}, "Selects a random number that isnt higher than this value and adds it to the ping.");
    private final Queue<Packet<?>> packets = new ConcurrentLinkedQueue();
    private final ScheduledExecutorService service;
    private final Random rand = new Random();

    public PingSpoof() {
        super("PingSpoof", new String[]{"PingSpoof", "PingDelay", "BeanerNet"}, "Spoofs your in-game ping", Category.MISC);
        this.offerProperties(this.latency, this.jitter);
        this.offerListeners(new ListenerKeepAlive(this), new LambdaListener<DisconnectEvent>(DisconnectEvent.class, event -> this.packets.clear()));
        this.service = ThreadUtil.newDaemonScheduledExecutor("PingSpoof");
        Bus.EVENT_BUS.register(new Listener<ShutDownEvent>(ShutDownEvent.class){

            @Override
            public void call(ShutDownEvent event) {
                PingSpoof.this.service.shutdown();
            }
        });
    }

    @Override
    public void onDisable() {
        if (!this.packets.isEmpty()) {
            this.packets.forEach(packet -> {
                if (packet != null) {
                    PingSpoof.mc.player.connection.sendPacket(packet);
                }
            });
            this.packets.clear();
        }
    }

    protected void onPacket(Packet<?> packet) {
        this.packets.add(packet);
        this.service.schedule(() -> {
            Packet<?> p;
            if (PingSpoof.mc.player != null && (p = this.packets.poll()) != null) {
                PacketUtil.sendPacketNoEvent(p);
            }
        }, (long)((Integer)this.latency.getValue() + this.getJitter()), TimeUnit.MILLISECONDS);
    }

    public int getJitter() {
        int jitter = 0;
        if ((Integer)this.jitter.getValue() != 0) {
            jitter = this.rand.nextInt((Integer)this.jitter.getValue());
        }
        return jitter;
    }

    public int getLatency() {
        return (Integer)this.latency.getValue();
    }
}

