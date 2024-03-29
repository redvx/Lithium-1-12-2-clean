/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 */
package me.chachoox.lithium.impl.modules.render.logoutspots;

import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.impl.event.events.network.ConnectionEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.render.logoutspots.LogoutSpots;
import me.chachoox.lithium.impl.modules.render.logoutspots.util.LogoutSpot;
import net.minecraft.entity.player.EntityPlayer;

public class ListenerLeave
extends ModuleListener<LogoutSpots, ConnectionEvent.Leave> {
    public ListenerLeave(LogoutSpots module) {
        super(module, ConnectionEvent.Leave.class);
    }

    @Override
    public void call(ConnectionEvent.Leave event) {
        EntityPlayer player = event.getPlayer();
        if (player != null && player.getName().equals(mc.getSession().getProfile().getName())) {
            return;
        }
        if (((LogoutSpots)this.module).message.getValue().booleanValue()) {
            String text = null;
            if (player != null) {
                text = String.format("\u00a7c" + player.getName() + " logged out at: %s, %s, %s.", (int)player.posX, (int)player.posY, (int)player.posZ);
            }
            if (text != null) {
                Logger.getLogger().log(text);
            }
        }
        if (player != null) {
            LogoutSpot spot = new LogoutSpot(player);
            ((LogoutSpots)this.module).spots.put(player.getUniqueID(), spot);
        }
    }
}

