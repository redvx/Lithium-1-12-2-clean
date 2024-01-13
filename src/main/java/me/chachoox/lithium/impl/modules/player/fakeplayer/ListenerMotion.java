/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 */
package me.chachoox.lithium.impl.modules.player.fakeplayer;

import me.chachoox.lithium.api.event.events.Stage;
import me.chachoox.lithium.impl.event.events.movement.MotionUpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.player.fakeplayer.FakePlayer;
import me.chachoox.lithium.impl.modules.player.fakeplayer.position.Position;
import me.chachoox.lithium.impl.modules.player.fakeplayer.util.PlayerUtil;
import net.minecraft.entity.player.EntityPlayer;

public class ListenerMotion
extends ModuleListener<FakePlayer, MotionUpdateEvent> {
    private boolean wasRecording;
    private int ticks;
    private int i;

    public ListenerMotion(FakePlayer module) {
        super(module, MotionUpdateEvent.class);
    }

    @Override
    public void call(MotionUpdateEvent event) {
        if (PlayerUtil.getPlayer() != null) {
            if (event.getStage() == Stage.PRE && !((FakePlayer)this.module).record.getValue().booleanValue()) {
                if (((FakePlayer)this.module).playRecording.getValue().booleanValue()) {
                    if (((FakePlayer)this.module).playerPositions.isEmpty()) {
                        ((FakePlayer)this.module).playRecording.setValue(false);
                        return;
                    }
                    if (this.i >= ((FakePlayer)this.module).playerPositions.size()) {
                        this.i = 0;
                    }
                    if (this.ticks++ % 2 == 0) {
                        Position p = ((FakePlayer)this.module).playerPositions.get(this.i++);
                        PlayerUtil.getPlayer().rotationYaw = p.getYaw();
                        PlayerUtil.getPlayer().rotationPitch = p.getPitch();
                        PlayerUtil.getPlayer().rotationYawHead = p.getHead();
                        PlayerUtil.getPlayer().setPositionAndRotationDirect(p.getX(), p.getY(), p.getZ(), p.getYaw(), p.getPitch(), 3, false);
                        PlayerUtil.getPlayer().motionX = p.getMotionX();
                        PlayerUtil.getPlayer().motionY = p.getMotionY();
                        PlayerUtil.getPlayer().motionZ = p.getMotionZ();
                    }
                } else {
                    this.i = 0;
                    PlayerUtil.getPlayer().motionX = 0.0;
                    PlayerUtil.getPlayer().motionY = 0.0;
                    PlayerUtil.getPlayer().motionZ = 0.0;
                }
            } else if (event.getStage() == Stage.POST && ((FakePlayer)this.module).record.getValue().booleanValue()) {
                ((FakePlayer)this.module).playRecording.setValue(false);
                PlayerUtil.getPlayer().motionX = 0.0;
                PlayerUtil.getPlayer().motionY = 0.0;
                PlayerUtil.getPlayer().motionZ = 0.0;
                if (!this.wasRecording) {
                    ((FakePlayer)this.module).playerPositions.clear();
                    this.wasRecording = true;
                }
                if (this.ticks++ % 2 == 0) {
                    ((FakePlayer)this.module).playerPositions.add(new Position((EntityPlayer)ListenerMotion.mc.player));
                }
            }
        }
    }
}

