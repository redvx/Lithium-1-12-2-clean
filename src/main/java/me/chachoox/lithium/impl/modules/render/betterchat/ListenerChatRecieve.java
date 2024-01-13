/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.init.SoundEvents
 *  net.minecraft.network.play.server.SPacketChat
 */
package me.chachoox.lithium.impl.modules.render.betterchat;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.render.betterchat.BetterChat;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketChat;

public class ListenerChatRecieve
extends ModuleListener<BetterChat, PacketEvent.Receive<SPacketChat>> {
    public ListenerChatRecieve(BetterChat module) {
        super(module, PacketEvent.Receive.class, SPacketChat.class);
    }

    @Override
    public void call(PacketEvent.Receive<SPacketChat> event) {
        SPacketChat packet = (SPacketChat)event.getPacket();
        if (ListenerChatRecieve.mc.player != null && ((BetterChat)this.module).playSoundOnHighlight() && packet.getChatComponent().getUnformattedText().contains(ListenerChatRecieve.mc.player.getName())) {
            ListenerChatRecieve.mc.player.playSound(SoundEvents.ENTITY_ARROW_HIT_PLAYER, 1.0f, 1.0f);
        }
    }
}

