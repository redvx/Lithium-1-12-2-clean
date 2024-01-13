/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.init.SoundEvents
 */
package me.chachoox.lithium.impl.modules.render.pollosesp;

import java.util.concurrent.ThreadLocalRandom;
import me.chachoox.lithium.impl.event.events.update.UpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.render.pollosesp.PollosESP;
import net.minecraft.init.SoundEvents;

public class ListenerUpdate
extends ModuleListener<PollosESP, UpdateEvent> {
    public ListenerUpdate(PollosESP module) {
        super(module, UpdateEvent.class);
    }

    @Override
    public void call(UpdateEvent event) {
        if (((PollosESP)this.module).pollosNoise.getValue().booleanValue()) {
            ListenerUpdate.mc.player.playSound(SoundEvents.ENTITY_CHICKEN_AMBIENT, 1.0f, 1.0f);
        }
        if (((PollosESP)this.module).pollosFps.getValue().booleanValue()) {
            ListenerUpdate.mc.gameSettings.limitFramerate = ThreadLocalRandom.current().nextInt(90);
        }
    }
}

