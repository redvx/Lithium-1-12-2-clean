/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketParticles
 *  net.minecraft.util.EnumParticleTypes
 */
package me.chachoox.lithium.impl.modules.render.norender;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.render.norender.NoRender;
import net.minecraft.network.play.server.SPacketParticles;
import net.minecraft.util.EnumParticleTypes;

public class ListenerSplash
extends ModuleListener<NoRender, PacketEvent.Receive<SPacketParticles>> {
    public ListenerSplash(NoRender module) {
        super(module, PacketEvent.Receive.class, SPacketParticles.class);
    }

    @Override
    public void call(PacketEvent.Receive<SPacketParticles> event) {
        if (((SPacketParticles)event.getPacket()).getParticleType() == EnumParticleTypes.WATER_SPLASH && ((NoRender)this.module).getWaterSplash()) {
            event.setCanceled(true);
        }
    }
}

