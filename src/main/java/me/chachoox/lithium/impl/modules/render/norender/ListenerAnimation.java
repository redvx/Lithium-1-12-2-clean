/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketAnimation
 */
package me.chachoox.lithium.impl.modules.render.norender;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.render.norender.NoRender;
import net.minecraft.network.play.server.SPacketAnimation;

public class ListenerAnimation
extends ModuleListener<NoRender, PacketEvent.Receive<SPacketAnimation>> {
    public ListenerAnimation(NoRender module) {
        super(module, PacketEvent.Receive.class, SPacketAnimation.class);
    }

    @Override
    public void call(PacketEvent.Receive<SPacketAnimation> event) {
        if (((NoRender)this.module).getCriticalParticles() && (((SPacketAnimation)event.getPacket()).getAnimationType() == 4 || ((SPacketAnimation)event.getPacket()).getAnimationType() == 5)) {
            event.setCanceled(true);
        }
    }
}

