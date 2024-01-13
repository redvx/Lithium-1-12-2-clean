/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketPlayerPosLook
 */
package me.chachoox.lithium.impl.modules.movement.speed;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.movement.speed.Speed;
import net.minecraft.network.play.server.SPacketPlayerPosLook;

public class ListenerPosLook
extends ModuleListener<Speed, PacketEvent.Receive<SPacketPlayerPosLook>> {
    public ListenerPosLook(Speed module) {
        super(module, PacketEvent.Receive.class, SPacketPlayerPosLook.class);
    }

    @Override
    public void call(PacketEvent.Receive<SPacketPlayerPosLook> event) {
        ((Speed)this.module).distance = 0.0;
        ((Speed)this.module).speed = 0.0;
        ((Speed)this.module).strafeStage = 4;
        ((Speed)this.module).onGroundStage = 2;
    }
}

