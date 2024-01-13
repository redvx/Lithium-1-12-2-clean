/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketChat
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.event.ClickEvent
 */
package me.chachoox.lithium.impl.managers.minecraft.chat;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import me.chachoox.lithium.api.event.bus.Listener;
import me.chachoox.lithium.api.event.bus.SubscriberImpl;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.api.util.text.TextUtil;
import me.chachoox.lithium.api.util.text.component.SuppliedComponent;
import me.chachoox.lithium.api.util.thread.events.RunnableClickEvent;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.event.ClickEvent;

public class ChatManager
extends SubscriberImpl
implements Minecraftable {
    private final Set<String> IGNORED_WORDS = new HashSet<String>();
    private final Set<String> IGNORED_PLAYERS = new HashSet<String>();

    public ChatManager() {
        this.listeners.add(new Listener<PacketEvent.Receive<SPacketChat>>(PacketEvent.Receive.class, SPacketChat.class){

            @Override
            public void call(PacketEvent.Receive<SPacketChat> event) {
                String msg = ((SPacketChat)event.getPacket()).getChatComponent().getUnformattedText();
                for (String string : ChatManager.this.IGNORED_PLAYERS) {
                    if (string == null || !msg.toUpperCase().contains(String.format("<%s>", string.toUpperCase()))) continue;
                    event.setCanceled(true);
                    Logger.getLogger().logNoMark(ChatManager.this.createClickablePlayerMessage(TextUtil.removeColor(string), msg), true);
                }
                for (String string : ChatManager.this.IGNORED_WORDS) {
                    if (string == null || !msg.toUpperCase().contains(string.toUpperCase())) continue;
                    event.setCanceled(true);
                    Logger.getLogger().logNoMark(ChatManager.this.createClickableWordMessage(TextUtil.removeColor(string), msg), true);
                }
            }
        });
    }

    private ITextComponent createClickableWordMessage(String word, String message) {
        AtomicReference<String> supplier = new AtomicReference<String>(String.format("%sClick to show hidden message containing word %s.", "\u00a7d", word));
        SuppliedComponent component = new SuppliedComponent(supplier::get);
        component.getStyle().setClickEvent((ClickEvent)new RunnableClickEvent(() -> Logger.getLogger().logNoMark(message, 2)));
        return component;
    }

    private ITextComponent createClickablePlayerMessage(String name, String message) {
        AtomicReference<String> supplier = new AtomicReference<String>(String.format("%sClick to show hidden message from %s.", "\u00a7d", name));
        SuppliedComponent component = new SuppliedComponent(supplier::get);
        component.getStyle().setClickEvent((ClickEvent)new RunnableClickEvent(() -> Logger.getLogger().logNoMark(String.format("[%s]%s", name, message.replace(String.format("<%s>", name), "")), 1)));
        return component;
    }

    public Collection<String> getIgnoredWords() {
        return this.IGNORED_WORDS;
    }

    public Collection<String> getIgnoredPlayers() {
        return this.IGNORED_PLAYERS;
    }
}

