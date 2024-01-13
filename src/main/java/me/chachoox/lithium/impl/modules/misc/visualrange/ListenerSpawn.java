/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.monster.EntityGhast
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.SoundEvents
 */
package me.chachoox.lithium.impl.modules.misc.visualrange;

import me.chachoox.lithium.api.util.entity.EntityUtil;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.impl.event.events.entity.EntityWorldEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.misc.visualrange.VisualRange;
import me.chachoox.lithium.impl.modules.misc.visualrange.mode.VisualRangeMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;

public class ListenerSpawn
extends ModuleListener<VisualRange, EntityWorldEvent.Add> {
    public ListenerSpawn(VisualRange module) {
        super(module, EntityWorldEvent.Add.class);
    }

    @Override
    public void call(EntityWorldEvent.Add event) {
        if (ListenerSpawn.mc.player != null && event.getEntity() instanceof EntityGhast && ((VisualRange)this.module).ghasts.getValue().booleanValue()) {
            Entity ghast = event.getEntity();
            Logger.getLogger().log("\u00a76Ghast spotted at: " + (int)ghast.posX + ", " + (int)ghast.posY + ", " + (int)ghast.posZ + ".", ghast.getEntityId());
        }
        if (ListenerSpawn.mc.player != null && event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)event.getEntity();
            if (player != null && !player.getName().equalsIgnoreCase(ListenerSpawn.mc.player.getName())) {
                if (((VisualRange)this.module).onlyGeared.getValue().booleanValue() && EntityUtil.isNaked(player)) {
                    return;
                }
                boolean isFriend = Managers.FRIEND.isFriend(player.getName());
                ((VisualRange)this.module).sendMessage(String.format("%s%s%s%s", isFriend ? ((VisualRange)this.module).getFriendColor() : ((VisualRange)this.module).getNameColor(), player.getName(), ((VisualRange)this.module).getBridgeColor(), ((VisualRangeMessage)((Object)((VisualRange)this.module).message.getValue())).getJoin()), player.getName().hashCode());
            }
            if (((VisualRange)this.module).sounds.getValue().booleanValue()) {
                ListenerSpawn.mc.player.playSound(SoundEvents.ENTITY_ARROW_HIT_PLAYER, 1.0f, 1.0f);
            }
        }
    }
}

