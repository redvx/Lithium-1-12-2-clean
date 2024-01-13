/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 */
package me.chachoox.lithium.impl.modules.player.positionspoof;

import me.chachoox.lithium.api.util.network.PacketUtil;
import me.chachoox.lithium.impl.event.events.movement.MotionUpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.player.positionspoof.PositionSpoof;
import net.minecraft.network.play.client.CPacketPlayer;

public class ListenerMotion
extends ModuleListener<PositionSpoof, MotionUpdateEvent> {
    public ListenerMotion(PositionSpoof module) {
        super(module, MotionUpdateEvent.class);
    }

    @Override
    public void call(MotionUpdateEvent event) {
        switch (event.getStage()) {
            case PRE: {
                PacketUtil.sendPacketNoEvent(new CPacketPlayer.Position((double)((PositionSpoof)this.module).posX, (double)((PositionSpoof)this.module).posY, (double)((PositionSpoof)this.module).posZ, true));
                break;
            }
            case POST: {
                PacketUtil.sendPacketNoEvent(new CPacketPlayer.Position(ListenerMotion.mc.player.posX, ListenerMotion.mc.player.posY, ListenerMotion.mc.player.posZ, true));
            }
        }
    }
}

