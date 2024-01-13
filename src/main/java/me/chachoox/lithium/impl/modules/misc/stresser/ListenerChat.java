/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketChat
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.event.ClickEvent
 */
package me.chachoox.lithium.impl.modules.misc.stresser;

import java.util.concurrent.atomic.AtomicReference;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.api.util.text.TextUtil;
import me.chachoox.lithium.api.util.text.component.SuppliedComponent;
import me.chachoox.lithium.api.util.thread.events.RunnableClickEvent;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.misc.stresser.Stresser;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.event.ClickEvent;

public class ListenerChat
extends ModuleListener<Stresser, PacketEvent.Receive<SPacketChat>> {
    public ListenerChat(Stresser module) {
        super(module, PacketEvent.Receive.class, 100, SPacketChat.class);
    }

    @Override
    public void call(PacketEvent.Receive<SPacketChat> event) {
        String text = ((SPacketChat)event.getPacket()).getChatComponent().getUnformattedText();
        int count = 0;
        boolean isUnicode = false;
        for (char charset : text.toCharArray()) {
            if (!((Stresser)this.module).antiUnicode.getValue().booleanValue() || this.isAscii(charset)) continue;
            ++count;
        }
        if (count >= (Integer)((Stresser)this.module).maxUnicodes.getValue()) {
            isUnicode = true;
            event.setCanceled(true);
        }
        if (isUnicode) {
            Logger.getLogger().logNoMark(this.makeClickableMessage(TextUtil.removeColor(text), count), false);
        }
    }

    private ITextComponent makeClickableMessage(String text, int count) {
        AtomicReference<String> supplier = new AtomicReference<String>("\u00a7c" + String.format("<Stresser> Message with %s unicode characters detected.", count));
        SuppliedComponent component = new SuppliedComponent(supplier::get);
        component.getStyle().setClickEvent((ClickEvent)new RunnableClickEvent(() -> Logger.getLogger().log("\u00a7e<Stresser> " + text)));
        return component;
    }

    private boolean isAscii(char ch) {
        return ch < '\u0100';
    }
}

