/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketChat
 */
package me.chachoox.lithium.impl.modules.misc.smartreply;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.misc.smartreply.SmartReply;
import net.minecraft.network.play.server.SPacketChat;

public class ListenerSChat
extends ModuleListener<SmartReply, PacketEvent.Receive<SPacketChat>> {
    public ListenerSChat(SmartReply module) {
        super(module, PacketEvent.Receive.class, SPacketChat.class);
    }

    @Override
    public void call(PacketEvent.Receive<SPacketChat> event) {
        String message = ((SPacketChat)event.getPacket()).getChatComponent().getUnformattedText();
        if (message.contains("whispers: ") || message.contains("Whispers: ")) {
            if (this.containsUnicode(message)) {
                return;
            }
            ((SmartReply)this.module).whisperSender = message.split(" ")[0];
            ((SmartReply)this.module).whispered = true;
        }
    }

    public boolean containsUnicode(String text) {
        for (char charset : text.toCharArray()) {
            if (this.isAscii(charset)) continue;
            return true;
        }
        return false;
    }

    private boolean isAscii(char ch) {
        return ch < '\u0100';
    }
}

