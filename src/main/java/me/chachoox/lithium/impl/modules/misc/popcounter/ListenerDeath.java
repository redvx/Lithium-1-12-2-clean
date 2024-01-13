/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 */
package me.chachoox.lithium.impl.modules.misc.popcounter;

import me.chachoox.lithium.impl.event.events.entity.DeathEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.misc.popcounter.PopCounter;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class ListenerDeath
extends ModuleListener<PopCounter, DeathEvent> {
    public ListenerDeath(PopCounter module) {
        super(module, DeathEvent.class);
    }

    @Override
    public void call(DeathEvent event) {
        EntityLivingBase player = event.getEntity();
        if (player instanceof EntityPlayer) {
            String name = player.getName();
            if (Managers.TOTEM.getPopMap().containsKey(name)) {
                boolean isSelf = player == ListenerDeath.mc.player;
                boolean friend = isSelf || Managers.FRIEND.isFriend(name);
                int pops = Managers.TOTEM.getPopMap().get(name);
                if (((PopCounter)this.module).ordinalNumbers.getValue().booleanValue()) {
                    ((PopCounter)this.module).sendMessage((friend ? ((PopCounter)this.module).getFriendColor() : ((PopCounter)this.module).getPlayerColor()) + (isSelf ? "You" : name) + ((PopCounter)this.module).getTotemColor() + " died after" + (isSelf ? " popping your " : " popping their ") + ((PopCounter)this.module).getFinalColor() + pops + ((PopCounter)this.module).getNumberStringThing(pops) + ((PopCounter)this.module).getTotemColor() + " totem!", -player.getEntityId());
                } else {
                    ((PopCounter)this.module).sendMessage((friend ? ((PopCounter)this.module).getFriendColor() : ((PopCounter)this.module).getPlayerColor()) + (isSelf ? "You" : name) + ((PopCounter)this.module).getTotemColor() + " died after popping " + ((PopCounter)this.module).getFinalColor() + pops + ((PopCounter)this.module).getTotemColor() + (pops == 1 ? " totem!" : " totems!"), -player.getEntityId());
                }
            }
        }
    }
}

