/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketChat
 */
package me.chachoox.lithium.impl.modules.misc.autoreply;

import java.lang.invoke.LambdaMetafactory;
import java.util.function.Predicate;
import java.util.stream.Stream;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.misc.autoreply.AutoReply;
import me.chachoox.lithium.impl.modules.misc.autoreply.ReplyMessages;
import net.minecraft.network.play.server.SPacketChat;

public class ListenerMessage
extends ModuleListener<AutoReply, PacketEvent.Receive<SPacketChat>> {
    public ListenerMessage(AutoReply module) {
        super(module, PacketEvent.Receive.class, SPacketChat.class);
    }

    @Override
    public void call(PacketEvent.Receive<SPacketChat> event) {
        if (!((AutoReply)this.module).timer.passed((Integer)((AutoReply)this.module).delay.getValue() * 1000)) {
            return;
        }
        SPacketChat packet = (SPacketChat)event.getPacket();
        String formattedText = packet.getChatComponent().getFormattedText();
        String message = packet.getChatComponent().getUnformattedText();
        String ign = message.split(" ")[0];
        if (Stream.of("whispers:", "whispers to you:", "says:").anyMatch(message::contains) && message.contains(ign) && Stream.of("\u00a7f").anyMatch(s -> !formattedText.contains((CharSequence)s)) && !ign.equals(ListenerMessage.mc.player.getName())) {
            this.sendReply(ign, message);
        }
    }

    /*
     * Unable to fully structure code
     */
    private void sendReply(String name, String message) {
        if (!Managers.FRIEND.isFriend(name) || !(ListenerMessage.mc.player.posX > 5000.0) || !(ListenerMessage.mc.player.posZ > 5000.0) || ((AutoReply)this.module).message.getValue() != ReplyMessages.COORDS) ** GOTO lbl-1000
        if (Stream.of(new String[]{"coords", "coord", "wya"}).anyMatch((Predicate<String>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Z, contains(java.lang.CharSequence ), (Ljava/lang/String;)Z)((String)message))) {
            reply = this.getCoords();
        } else lbl-1000:
        // 2 sources

        {
            reply = ((ReplyMessages)((AutoReply)this.module).message.getValue()).getReply();
        }
        if (reply.equalsIgnoreCase("<Coords>")) {
            return;
        }
        ListenerMessage.mc.player.sendChatMessage("/r " + reply);
        ((AutoReply)this.module).timer.reset();
    }

    private String getCoords() {
        return "XYZ: " + (int)ListenerMessage.mc.player.posX + ", " + (int)ListenerMessage.mc.player.posY + ", " + (int)ListenerMessage.mc.player.posZ;
    }
}

