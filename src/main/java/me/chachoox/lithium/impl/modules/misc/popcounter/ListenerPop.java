/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 */
package me.chachoox.lithium.impl.modules.misc.popcounter;

import me.chachoox.lithium.impl.event.events.entity.TotemPopEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.misc.popcounter.PopCounter;
import net.minecraft.entity.player.EntityPlayer;

public class ListenerPop
extends ModuleListener<PopCounter, TotemPopEvent> {
    public ListenerPop(PopCounter module) {
        super(module, TotemPopEvent.class);
    }

    @Override
    public void call(TotemPopEvent event) {
        EntityPlayer entity = event.getEntity();
        String name = entity.getName();
        boolean isSelf = entity == ListenerPop.mc.player;
        boolean friend = isSelf || Managers.FRIEND.isFriend(name);
        int pops = Managers.TOTEM.getPopMap().get(name);
        if (((PopCounter)this.module).ordinalNumbers.getValue().booleanValue()) {
            ((PopCounter)this.module).sendMessage((friend ? ((PopCounter)this.module).getFriendColor() : ((PopCounter)this.module).getPlayerColor()) + (isSelf ? "You" : name) + ((PopCounter)this.module).getTotemColor() + (isSelf ? " popped your " : " popped their ") + ((PopCounter)this.module).getNumberColor() + pops + ((PopCounter)this.module).getNumberStringThing(pops) + ((PopCounter)this.module).getTotemColor() + " totem!", -entity.getEntityId());
        } else {
            ((PopCounter)this.module).sendMessage((friend ? ((PopCounter)this.module).getFriendColor() : ((PopCounter)this.module).getPlayerColor()) + (isSelf ? "You" : name) + ((PopCounter)this.module).getTotemColor() + (isSelf ? " popped " : " has popped ") + ((PopCounter)this.module).getNumberColor() + pops + ((PopCounter)this.module).getTotemColor() + (pops == 1 ? " time in total!" : " times in total!"), -entity.getEntityId());
        }
    }
}

