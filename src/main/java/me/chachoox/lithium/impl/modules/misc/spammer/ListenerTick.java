/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.misc.spammer;

import java.util.concurrent.ThreadLocalRandom;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.api.util.text.TextUtil;
import me.chachoox.lithium.impl.event.events.update.TickEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.misc.spammer.Spammer;
import org.apache.logging.log4j.Level;

public class ListenerTick
extends ModuleListener<Spammer, TickEvent> {
    public ListenerTick(Spammer module) {
        super(module, TickEvent.class);
    }

    @Override
    public void call(TickEvent event) {
        if (((Spammer)this.module).isNull()) {
            ((Spammer)this.module).disable();
            return;
        }
        if (((Spammer)this.module).currentFile == null) {
            Logger.getLogger().log("You have to load a file to use this module");
            ((Spammer)this.module).disable();
            return;
        }
        try {
            if (((Spammer)this.module).timer.passed(((Float)((Spammer)this.module).delay.getValue()).floatValue() * 1000.0f)) {
                int line = ((Spammer)this.module).randomize.getValue() != false ? ThreadLocalRandom.current().nextInt(((Spammer)this.module).strings.size()) : 0;
                String greenText = ((Spammer)this.module).greenText.getValue() != false ? "> " : "";
                String text = ((Spammer)this.module).strings.get(line);
                String antiKick = ((Spammer)this.module).antiKick.getValue() != false ? TextUtil.generateRandomString(5) : "";
                ListenerTick.mc.player.sendChatMessage(greenText + text + antiKick);
                if (!((Spammer)this.module).randomize.getValue().booleanValue()) {
                    ((Spammer)this.module).strings.remove(text);
                    ((Spammer)this.module).strings.add(text);
                }
                ((Spammer)this.module).timer.reset();
            }
        }
        catch (Exception e) {
            Logger.getLogger().log(Level.ERROR, "Couldn't send spammer message");
        }
    }
}

