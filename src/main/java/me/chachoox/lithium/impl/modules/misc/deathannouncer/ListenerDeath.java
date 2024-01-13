/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 */
package me.chachoox.lithium.impl.modules.misc.deathannouncer;

import me.chachoox.lithium.impl.event.events.entity.DeathEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.misc.deathannouncer.DeathAnnouncer;
import me.chachoox.lithium.impl.modules.misc.deathannouncer.util.Announce;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class ListenerDeath
extends ModuleListener<DeathAnnouncer, DeathEvent> {
    public ListenerDeath(DeathAnnouncer module) {
        super(module, DeathEvent.class);
    }

    @Override
    public void call(DeathEvent event) {
        if (ListenerDeath.mc.player == null) {
            return;
        }
        if (event.getEntity() instanceof EntityPlayer) {
            EntityLivingBase player = event.getEntity();
            if (player == ListenerDeath.mc.player || Managers.FRIEND.isFriend((EntityPlayer)player)) {
                return;
            }
            if (ListenerDeath.mc.player.getPosition().getY() > (Integer)((DeathAnnouncer)this.module).yLevel.getValue()) {
                return;
            }
            if (!((DeathAnnouncer)this.module).timer.passed((Integer)((DeathAnnouncer)this.module).delay.getValue() * 1000)) {
                return;
            }
            ((DeathAnnouncer)this.module).name = player.getName();
            if (ListenerDeath.mc.player.getDistance((Entity)player) < (float)((Integer)((DeathAnnouncer)this.module).range.getValue()).intValue()) {
                switch ((Announce)((Object)((DeathAnnouncer)this.module).killSayPreset.getValue())) {
                    case TROLLGOD: {
                        ListenerDeath.mc.player.sendChatMessage((((DeathAnnouncer)this.module).greenText.getValue() != false ? "> " : "") + ((DeathAnnouncer)this.module).trollGodMessage());
                        break;
                    }
                    case POLLOSMOD: {
                        ListenerDeath.mc.player.sendChatMessage((((DeathAnnouncer)this.module).greenText.getValue() != false ? "> " : "") + ((DeathAnnouncer)this.module).pollosMessage());
                        break;
                    }
                    case TROLLHACK: {
                        ListenerDeath.mc.player.sendChatMessage((((DeathAnnouncer)this.module).greenText.getValue() != false ? "> " : "") + ((DeathAnnouncer)this.module).trollHackMessages());
                        break;
                    }
                    case ABYSS: {
                        ListenerDeath.mc.player.sendChatMessage((((DeathAnnouncer)this.module).greenText.getValue() != false ? "> " : "") + ((DeathAnnouncer)this.module).abyssMessage());
                        break;
                    }
                    case PHOBOS: {
                        ListenerDeath.mc.player.sendChatMessage((((DeathAnnouncer)this.module).greenText.getValue() != false ? "> " : "") + ((DeathAnnouncer)this.module).phobosMessage());
                        break;
                    }
                    case KONAS: {
                        ListenerDeath.mc.player.sendChatMessage((((DeathAnnouncer)this.module).greenText.getValue() != false ? "> " : "") + ((DeathAnnouncer)this.module).konasMessages());
                        break;
                    }
                    case WURSTPLUS: {
                        ListenerDeath.mc.player.sendChatMessage((((DeathAnnouncer)this.module).greenText.getValue() != false ? "> " : "") + ((DeathAnnouncer)this.module).wurstPlusMessages());
                        break;
                    }
                    case AURORA: {
                        ListenerDeath.mc.player.sendChatMessage((((DeathAnnouncer)this.module).greenText.getValue() != false ? "> " : "") + ((DeathAnnouncer)this.module).auroraMessage());
                        break;
                    }
                    case AUTISM: {
                        ListenerDeath.mc.player.sendChatMessage((((DeathAnnouncer)this.module).greenText.getValue() != false ? "> " : "") + ((DeathAnnouncer)this.module).autismMessage());
                        break;
                    }
                    case KAMI: {
                        ListenerDeath.mc.player.sendChatMessage((((DeathAnnouncer)this.module).greenText.getValue() != false ? "> " : "") + ((DeathAnnouncer)this.module).kamiMessage());
                        break;
                    }
                    case WELLPLAYED: {
                        ListenerDeath.mc.player.sendChatMessage((((DeathAnnouncer)this.module).greenText.getValue() != false ? "> " : "") + ((DeathAnnouncer)this.module).wellPlayedMessages());
                        break;
                    }
                    case PRAYER: {
                        ListenerDeath.mc.player.sendChatMessage((((DeathAnnouncer)this.module).greenText.getValue() != false ? "> " : "") + ((DeathAnnouncer)this.module).prayerMessage());
                        break;
                    }
                    case POLLOS: {
                        ListenerDeath.mc.player.sendChatMessage((((DeathAnnouncer)this.module).greenText.getValue() != false ? "> " : "") + "GG!");
                        break;
                    }
                    case NFT: {
                        ListenerDeath.mc.player.sendChatMessage((((DeathAnnouncer)this.module).greenText.getValue() != false ? "> " : "") + ((DeathAnnouncer)this.module).nftMessage());
                    }
                }
                ((DeathAnnouncer)this.module).timer.reset();
            }
        }
    }
}

