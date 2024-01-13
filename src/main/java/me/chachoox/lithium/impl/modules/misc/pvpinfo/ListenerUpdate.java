/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderPearl
 *  net.minecraft.entity.player.EntityPlayer
 */
package me.chachoox.lithium.impl.modules.misc.pvpinfo;

import java.util.UUID;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.impl.event.events.update.UpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.misc.pvpinfo.PvPInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;

public class ListenerUpdate
extends ModuleListener<PvPInfo, UpdateEvent> {
    public ListenerUpdate(PvPInfo module) {
        super(module, UpdateEvent.class);
    }

    @Override
    public void call(UpdateEvent event) {
        if (!((PvPInfo)this.module).pearls()) {
            return;
        }
        for (Entity entity : ListenerUpdate.mc.world.loadedEntityList) {
            if (!(entity instanceof EntityEnderPearl)) continue;
            EntityPlayer closest = null;
            for (EntityPlayer player : ListenerUpdate.mc.world.playerEntities) {
                if (closest != null && !(entity.getDistance((Entity)player) < entity.getDistance((Entity)closest))) continue;
                closest = player;
            }
            if (closest != null && closest == ListenerUpdate.mc.player) {
                if (!((PvPInfo)this.module).pearlThrown) {
                    ((PvPInfo)this.module).pearlTimer.reset();
                }
                ((PvPInfo)this.module).pearlThrown = true;
                return;
            }
            if (closest == null || !(closest.getDistance(entity) < 2.0f) || ((PvPInfo)this.module).pearlsUUIDs.containsKey(entity.getUniqueID())) continue;
            ((PvPInfo)this.module).pearlsUUIDs.put(entity.getUniqueID(), 200);
            Logger.getLogger().logNoMark(closest.getName() + " has just thrown a pearl!", false);
        }
        ((PvPInfo)this.module).pearlsUUIDs.forEach((name, timeout) -> {
            if (timeout <= 0) {
                ((PvPInfo)this.module).pearlsUUIDs.remove(name);
            } else {
                ((PvPInfo)this.module).pearlsUUIDs.put((UUID)name, timeout - 1);
            }
        });
        if (((PvPInfo)this.module).pearlTimer.passed(15000L)) {
            ((PvPInfo)this.module).pearlThrown = false;
        }
    }
}

