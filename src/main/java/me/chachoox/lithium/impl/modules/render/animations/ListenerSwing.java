/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketAnimation
 *  net.minecraft.util.EnumHand
 */
package me.chachoox.lithium.impl.modules.render.animations;

import me.chachoox.lithium.asm.mixins.network.client.ICPacketAnimation;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.render.animations.Animations;
import me.chachoox.lithium.impl.modules.render.animations.SwingEnum;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumHand;

public class ListenerSwing
extends ModuleListener<Animations, PacketEvent.Send<CPacketAnimation>> {
    public ListenerSwing(Animations module) {
        super(module, PacketEvent.Send.class, CPacketAnimation.class);
    }

    @Override
    public void call(PacketEvent.Send<CPacketAnimation> event) {
        if (((Animations)this.module).swing.getValue() == SwingEnum.NONE) {
            return;
        }
        if (((Animations)this.module).swing.getValue() == SwingEnum.CANCEL) {
            event.setCanceled(true);
            return;
        }
        CPacketAnimation packet = (CPacketAnimation)event.getPacket();
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
        ((ICPacketAnimation)packet).setHand(hand);
    }
}

