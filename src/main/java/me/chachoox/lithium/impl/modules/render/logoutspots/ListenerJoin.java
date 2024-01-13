/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.math.Vec3d
 */
package me.chachoox.lithium.impl.modules.render.logoutspots;

import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.impl.event.events.network.ConnectionEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.render.logoutspots.LogoutSpots;
import me.chachoox.lithium.impl.modules.render.logoutspots.util.LogoutSpot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

public class ListenerJoin
extends ModuleListener<LogoutSpots, ConnectionEvent.Join> {
    public ListenerJoin(LogoutSpots module) {
        super(module, ConnectionEvent.Join.class);
    }

    @Override
    public void call(ConnectionEvent.Join event) {
        if (event.getName().equals(mc.getSession().getProfile().getName())) {
            return;
        }
        LogoutSpot spot = ((LogoutSpots)this.module).spots.remove(event.getUuid());
        if (((LogoutSpots)this.module).message.getValue().booleanValue()) {
            String text;
            if (spot != null) {
                Vec3d pos = spot.rounded();
                text = "\u00a7c" + event.getName() + " is back at: " + pos.x + ", " + pos.y + ", " + pos.z + ".";
            } else {
                EntityPlayer player = event.getPlayer();
                if (player != null) {
                    text = "\u00a7a" + player.getName() + " joined at: %s, %s, %s.";
                    text = String.format(text, (int)player.posX, (int)player.posY, (int)player.posZ);
                } else {
                    return;
                }
            }
            Logger.getLogger().log(text);
        }
    }
}

