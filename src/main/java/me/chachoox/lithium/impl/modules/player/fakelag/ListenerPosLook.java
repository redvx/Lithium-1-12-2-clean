/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketPlayerPosLook
 */
package me.chachoox.lithium.impl.modules.player.fakelag;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.player.fakelag.FakeLag;
import net.minecraft.network.play.server.SPacketPlayerPosLook;

public class ListenerPosLook
extends ModuleListener<FakeLag, PacketEvent.Receive<SPacketPlayerPosLook>> {
    public ListenerPosLook(FakeLag module) {
        super(module, PacketEvent.Receive.class, SPacketPlayerPosLook.class);
    }

    @Override
    public void call(PacketEvent.Receive<SPacketPlayerPosLook> event) {
        ((FakeLag)this.module).disable();
    }
}

