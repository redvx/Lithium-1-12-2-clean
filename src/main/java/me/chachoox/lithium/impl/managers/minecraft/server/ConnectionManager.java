/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.network.NetworkPlayerInfo
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.network.play.server.SPacketPlayerListItem
 *  net.minecraft.network.play.server.SPacketPlayerListItem$Action
 *  net.minecraft.network.play.server.SPacketPlayerListItem$AddPlayerData
 */
package me.chachoox.lithium.impl.managers.minecraft.server;

import java.util.UUID;
import me.chachoox.lithium.api.event.bus.Listener;
import me.chachoox.lithium.api.event.bus.SubscriberImpl;
import me.chachoox.lithium.api.event.bus.instance.Bus;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.impl.event.events.network.ConnectionEvent;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import org.apache.logging.log4j.Level;

public class ConnectionManager
extends SubscriberImpl
implements Minecraftable {
    public ConnectionManager() {
        this.listeners.add(new Listener<PacketEvent.Receive<SPacketPlayerListItem>>(PacketEvent.Receive.class, Integer.MAX_VALUE, SPacketPlayerListItem.class){

            @Override
            public void call(PacketEvent.Receive<SPacketPlayerListItem> event) {
                SPacketPlayerListItem packet = (SPacketPlayerListItem)event.getPacket();
                if (Minecraftable.mc.world == null || SPacketPlayerListItem.Action.ADD_PLAYER != packet.getAction() && SPacketPlayerListItem.Action.REMOVE_PLAYER != packet.getAction()) {
                    return;
                }
                try {
                    for (SPacketPlayerListItem.AddPlayerData data : packet.getEntries()) {
                        switch (packet.getAction()) {
                            case ADD_PLAYER: {
                                UUID joinUUID = data.getProfile().getId();
                                String joinName = data.getProfile().getName();
                                EntityPlayer joinPlayer = Minecraftable.mc.world.getPlayerEntityByUUID(joinUUID);
                                ConnectionManager.this.scheduleEvent(new ConnectionEvent.Join(joinName, joinUUID, joinPlayer));
                                break;
                            }
                            case REMOVE_PLAYER: {
                                NetworkPlayerInfo info = null;
                                for (NetworkPlayerInfo playerInfo : Minecraftable.mc.player.connection.getPlayerInfoMap()) {
                                    if (!playerInfo.getGameProfile().getId().equals(data.getProfile().getId())) continue;
                                    info = playerInfo;
                                    break;
                                }
                                if (info == null) break;
                                UUID leaveUUID = info.getGameProfile().getId();
                                String leaveName = info.getGameProfile().getName();
                                EntityPlayer leavePlayer = Minecraftable.mc.world.getPlayerEntityByUUID(leaveUUID);
                                ConnectionManager.this.scheduleEvent(new ConnectionEvent.Leave(leaveName, leaveUUID, leavePlayer));
                            }
                        }
                    }
                }
                catch (Exception e) {
                    Logger.getLogger().log(Level.ERROR, "Error while handling connection event");
                }
            }
        });
    }

    private void scheduleEvent(ConnectionEvent event) {
        mc.addScheduledTask(() -> Bus.EVENT_BUS.dispatch(event));
    }
}

