/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 */
package me.chachoox.lithium.impl.modules.misc.visualrange;

import me.chachoox.lithium.api.util.entity.EntityUtil;
import me.chachoox.lithium.impl.event.events.entity.EntityWorldEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.misc.visualrange.VisualRange;
import me.chachoox.lithium.impl.modules.misc.visualrange.mode.VisualRangeMessage;
import net.minecraft.entity.player.EntityPlayer;

public class ListenerDespawn
extends ModuleListener<VisualRange, EntityWorldEvent.Remove> {
    public ListenerDespawn(VisualRange module) {
        super(module, EntityWorldEvent.Remove.class);
    }

    @Override
    public void call(EntityWorldEvent.Remove event) {
        EntityPlayer player;
        if (ListenerDespawn.mc.player != null && event.getEntity() instanceof EntityPlayer && ((VisualRange)this.module).left.getValue().booleanValue() && (player = (EntityPlayer)event.getEntity()) != null && !player.getName().equalsIgnoreCase(ListenerDespawn.mc.player.getName())) {
            if (((VisualRange)this.module).onlyGeared.getValue().booleanValue() && EntityUtil.isNaked(player)) {
                return;
            }
            boolean isFriend = Managers.FRIEND.isFriend(player.getName());
            ((VisualRange)this.module).sendMessage(String.format("%s%s%s%s", isFriend ? ((VisualRange)this.module).getFriendColor() : ((VisualRange)this.module).getNameColor(), player.getName(), ((VisualRange)this.module).getBridgeColor(), ((VisualRangeMessage)((Object)((VisualRange)this.module).message.getValue())).getLeave()), player.getName().hashCode());
        }
    }
}

