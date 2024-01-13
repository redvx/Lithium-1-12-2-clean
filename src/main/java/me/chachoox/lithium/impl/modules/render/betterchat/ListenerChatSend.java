/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketChatMessage
 */
package me.chachoox.lithium.impl.modules.render.betterchat;

import java.util.stream.Stream;
import me.chachoox.lithium.api.util.text.TextUtil;
import me.chachoox.lithium.asm.mixins.network.client.ICPacketChatMessage;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.misc.chatappend.ChatAppend;
import me.chachoox.lithium.impl.modules.render.betterchat.BetterChat;
import me.chachoox.lithium.impl.modules.render.betterchat.util.ChatType;
import net.minecraft.network.play.client.CPacketChatMessage;

public class ListenerChatSend
extends ModuleListener<BetterChat, PacketEvent.Send<CPacketChatMessage>> {
    public ListenerChatSend(BetterChat module) {
        super(module, PacketEvent.Send.class, 500, CPacketChatMessage.class);
    }

    @Override
    public void call(PacketEvent.Send<CPacketChatMessage> event) {
        ICPacketChatMessage packet = (ICPacketChatMessage)event.getPacket();
        String chatMessage = ((CPacketChatMessage)event.getPacket()).getMessage();
        if (ChatAppend.allowMessage(chatMessage)) {
            StringBuilder stringBuilder;
            if (((BetterChat)this.module).angry.getValue().booleanValue()) {
                chatMessage = chatMessage.toUpperCase();
            }
            if (((BetterChat)this.module).chatType.getValue() == ChatType.L33T) {
                if (Stream.of("o", "l", "a", "e", "i", "s").anyMatch(chatMessage::contains)) {
                    chatMessage = chatMessage.replace("o", "0").replace("l", "1").replace("a", "4").replace("e", "3").replace("i", "!").replace("s", "$");
                }
            }
            if (((BetterChat)this.module).chatType.getValue() == ChatType.DERP) {
                char[] array = chatMessage.toCharArray();
                for (int i = 0; i < chatMessage.length(); i += 2) {
                    char character = Character.isUpperCase(array[i]) ? Character.toLowerCase(array[i]) : Character.toUpperCase(array[i]);
                    array[i] = character;
                }
                chatMessage = String.valueOf(array);
            }
            if (((BetterChat)this.module).chatType.getValue() == ChatType.REVERSE) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(chatMessage);
                chatMessage = stringBuilder.reverse().toString();
            }
            if (((BetterChat)this.module).period.getValue().booleanValue()) {
                chatMessage = chatMessage + ".";
            }
            if (((BetterChat)this.module).chatType.getValue() == ChatType.FANCY) {
                stringBuilder = new StringBuilder();
                for (char character : chatMessage.toCharArray()) {
                    if (character >= '!' && character <= '\u0080' && !"(){}[]|".contains(Character.toString(character))) {
                        stringBuilder.append(new String(Character.toChars(character + 65248)));
                        continue;
                    }
                    stringBuilder.append(character);
                }
                chatMessage = stringBuilder.toString();
            }
            if (((BetterChat)this.module).face.getValue().booleanValue()) {
                chatMessage = String.format("(\u3063\u25d4\u25e1\u25d4)\u3063 \u2665 %s \u2665", chatMessage);
            }
            if (((BetterChat)this.module).face.getValue().booleanValue()) {
                chatMessage = String.format("> %s", chatMessage);
            }
        }
        if (((BetterChat)this.module).antiKick.getValue().booleanValue() && ((BetterChat)this.module).allowMessage(chatMessage)) {
            String antiKickString = ((BetterChat)this.module).antiKick.getValue() != false ? TextUtil.generateRandomString(5) : "";
            chatMessage = chatMessage + antiKickString;
        }
        packet.setMessage(chatMessage);
    }
}

