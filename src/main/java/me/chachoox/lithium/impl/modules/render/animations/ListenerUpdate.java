/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.EnumHand
 */
package me.chachoox.lithium.impl.modules.render.animations;

import me.chachoox.lithium.impl.event.events.update.UpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.render.animations.Animations;
import me.chachoox.lithium.impl.modules.render.animations.SwingEnum;
import net.minecraft.util.EnumHand;

public class ListenerUpdate
extends ModuleListener<Animations, UpdateEvent> {
    public ListenerUpdate(Animations module) {
        super(module, UpdateEvent.class);
    }

    @Override
    public void call(UpdateEvent event) {
        if (((Animations)this.module).swing.getValue() == SwingEnum.NONE) {
            return;
        }
        if (((Animations)this.module).swing.getValue() == SwingEnum.CANCEL) {
            ListenerUpdate.mc.player.isSwingInProgress = false;
            return;
        }
        EnumHand hand = null;
        switch ((SwingEnum)((Object)((Animations)this.module).swing.getValue())) {
            case OFFHAND: {
                hand = EnumHand.OFF_HAND;
                break;
            }
            case MAINHAND: {
                hand = EnumHand.MAIN_HAND;
            }
        }
        ListenerUpdate.mc.player.swingingHand = hand;
    }
}

