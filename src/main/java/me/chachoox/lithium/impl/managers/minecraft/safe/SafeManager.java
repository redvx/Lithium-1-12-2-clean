/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.play.server.SPacketSpawnObject
 *  net.minecraft.util.math.BlockPos
 */
package me.chachoox.lithium.impl.managers.minecraft.safe;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import me.chachoox.lithium.api.event.bus.Listener;
import me.chachoox.lithium.api.event.bus.SubscriberImpl;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.api.util.entity.DamageUtil;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.impl.event.events.misc.GameLoopEvent;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.managers.minecraft.safe.SafetyRunnable;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.math.BlockPos;

public class SafeManager
extends SubscriberImpl
implements Minecraftable {
    private final AtomicBoolean safe = new AtomicBoolean(false);
    private final StopWatch timer = new StopWatch();

    public SafeManager() {
        this.listeners.add(new Listener<GameLoopEvent>(GameLoopEvent.class){

            @Override
            public void call(GameLoopEvent event) {
                if (SafeManager.this.timer.passed(25L)) {
                    SafeManager.this.runThread();
                    SafeManager.this.timer.reset();
                }
            }
        });
        this.listeners.add(new Listener<PacketEvent.Receive<SPacketSpawnObject>>(PacketEvent.Receive.class, SafeManager.class){

            @Override
            public void call(PacketEvent.Receive<SPacketSpawnObject> event) {
                SPacketSpawnObject packet = (SPacketSpawnObject)event.getPacket();
                if (packet.getType() == 51 && Minecraftable.mc.player != null) {
                    BlockPos blockPos = new BlockPos(packet.getX(), packet.getY(), packet.getZ());
                    if (DamageUtil.calculate(blockPos.down()) > 4.0f) {
                        SafeManager.this.setSafe(false);
                    }
                }
            }
        });
    }

    public boolean isSafe() {
        return this.safe.get();
    }

    public void setSafe(boolean safe) {
        this.safe.set(safe);
    }

    protected void runThread() {
        if (SafeManager.mc.player != null && SafeManager.mc.world != null) {
            ArrayList<Entity> crystals = new ArrayList<Entity>(SafeManager.mc.world.loadedEntityList);
            SafetyRunnable runnable = new SafetyRunnable(this, crystals);
            Managers.THREAD.submit(runnable);
        }
    }
}

