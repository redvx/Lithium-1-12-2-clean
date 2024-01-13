/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.network.play.server.SPacketEntityStatus
 *  net.minecraft.world.World
 */
package me.chachoox.lithium.impl.managers.minecraft.combat;

import java.util.HashMap;
import java.util.Map;
import me.chachoox.lithium.api.event.bus.Listener;
import me.chachoox.lithium.api.event.bus.SubscriberImpl;
import me.chachoox.lithium.api.event.bus.instance.Bus;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.impl.event.events.entity.DeathEvent;
import me.chachoox.lithium.impl.event.events.entity.EntityWorldEvent;
import me.chachoox.lithium.impl.event.events.entity.TotemPopEvent;
import me.chachoox.lithium.impl.event.events.network.ConnectionEvent;
import me.chachoox.lithium.impl.event.events.network.DisconnectEvent;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.events.world.WorldClientEvent;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.misc.popcounter.PopCounter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.world.World;

public class PopManager
extends SubscriberImpl
implements Minecraftable {
    private final Map<String, Integer> popMap = new HashMap<String, Integer>();

    public PopManager() {
        this.listeners.add(new Listener<PacketEvent.Receive<SPacketEntityStatus>>(PacketEvent.Receive.class, Integer.MIN_VALUE, SPacketEntityStatus.class){

            @Override
            public void call(PacketEvent.Receive<SPacketEntityStatus> event) {
                if (Minecraftable.mc.player == null || Minecraftable.mc.world == null) {
                    return;
                }
                SPacketEntityStatus packet = (SPacketEntityStatus)event.getPacket();
                if (packet.getOpCode() == 35) {
                    Entity entity = packet.getEntity((World)Minecraftable.mc.world);
                    String name = entity.getName();
                    if (entity instanceof EntityPlayer) {
                        boolean contains = PopManager.this.popMap.containsKey(name);
                        PopManager.this.popMap.put(name, contains ? (Integer)PopManager.this.popMap.get(name) + 1 : 1);
                        TotemPopEvent totemPopEvent = new TotemPopEvent((EntityPlayer)entity);
                        Bus.EVENT_BUS.dispatch(totemPopEvent);
                    }
                }
            }
        });
        this.listeners.add(new Listener<DeathEvent>(DeathEvent.class, Integer.MIN_VALUE){

            @Override
            public void call(DeathEvent event) {
                EntityLivingBase player = event.getEntity();
                if (player instanceof EntityPlayer) {
                    String name = player.getName();
                    if (PopManager.this.popMap.containsKey(name)) {
                        PopManager.this.popMap.remove(name, PopManager.this.popMap.get(name));
                    }
                }
            }
        });
        this.listeners.add(new Listener<ConnectionEvent.Leave>(ConnectionEvent.Leave.class, Integer.MIN_VALUE){

            @Override
            public void call(ConnectionEvent.Leave event) {
                String name = event.getName();
                if (name == null) {
                    return;
                }
                PopCounter POP_COUNTER = Managers.MODULE.get(PopCounter.class);
                if (PopManager.this.popMap.containsKey(name) && POP_COUNTER.clearOnLog()) {
                    PopManager.this.popMap.remove(name, PopManager.this.popMap.get(name));
                }
            }
        });
        this.listeners.add(new Listener<EntityWorldEvent.Remove>(EntityWorldEvent.Remove.class, Integer.MIN_VALUE){

            @Override
            public void call(EntityWorldEvent.Remove event) {
                Entity player = event.getEntity();
                if (player == null) {
                    return;
                }
                if (player instanceof EntityPlayer) {
                    String name = player.getName();
                    boolean isSelf = player == Minecraftable.mc.player;
                    PopCounter POP_COUNTER = Managers.MODULE.get(PopCounter.class);
                    if (PopManager.this.popMap.containsKey(name) && !isSelf && POP_COUNTER.clearOnVisualRange()) {
                        PopManager.this.popMap.remove(name, PopManager.this.popMap.get(name));
                    }
                }
            }
        });
        this.listeners.add(new Listener<WorldClientEvent.Load>(WorldClientEvent.Load.class, Integer.MIN_VALUE){

            @Override
            public void call(WorldClientEvent.Load event) {
                PopManager.this.popMap.clear();
            }
        });
        this.listeners.add(new Listener<DisconnectEvent>(DisconnectEvent.class, Integer.MIN_VALUE){

            @Override
            public void call(DisconnectEvent event) {
                PopManager.this.popMap.clear();
            }
        });
    }

    public final Map<String, Integer> getPopMap() {
        return this.popMap;
    }
}

