/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketChat
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.TextComponentString
 */
package me.chachoox.lithium.impl.modules.misc.chattimestamps;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import me.chachoox.lithium.asm.mixins.network.server.ISPacketChat;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.misc.chattimestamps.ChatTimeStamps;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class ListenerChat
extends ModuleListener<ChatTimeStamps, PacketEvent.Receive<SPacketChat>> {
    public ListenerChat(ChatTimeStamps module) {
        super(module, PacketEvent.Receive.class, -5000, SPacketChat.class);
    }

    @Override
    public void call(PacketEvent.Receive<SPacketChat> event) {
        SPacketChat packet = (SPacketChat)event.getPacket();
        ISPacketChat chatPacket = (ISPacketChat)event.getPacket();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("H:mm");
        String timeStamp = simpleDateFormat.format(calendar.getTime());
        String time = ((ChatTimeStamps)this.module).getTimeStamps(timeStamp);
        chatPacket.setChatComponent((ITextComponent)new TextComponentString(time + packet.getChatComponent().getFormattedText()));
    }
}

