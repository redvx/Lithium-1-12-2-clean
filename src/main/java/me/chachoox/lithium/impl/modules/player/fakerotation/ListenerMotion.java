/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.ItemFood
 */
package me.chachoox.lithium.impl.modules.player.fakerotation;

import java.util.concurrent.ThreadLocalRandom;
import me.chachoox.lithium.api.event.events.Stage;
import me.chachoox.lithium.impl.event.events.movement.MotionUpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.player.fakerotation.FakeRotation;
import me.chachoox.lithium.impl.modules.player.fakerotation.enums.Pitch;
import net.minecraft.item.ItemFood;

public class ListenerMotion
extends ModuleListener<FakeRotation, MotionUpdateEvent> {
    public ListenerMotion(FakeRotation module) {
        super(module, MotionUpdateEvent.class);
    }

    @Override
    public void call(MotionUpdateEvent event) {
        if (ListenerMotion.mc.player != null && event.getStage() == Stage.PRE && !this.canRotate() && !Managers.ROTATION.isRotated()) {
            if (((FakeRotation)this.module).randomize.getValue().booleanValue()) {
                ListenerMotion.mc.player.rotationYaw = ThreadLocalRandom.current().nextInt(-90, 90);
                ListenerMotion.mc.player.rotationPitch = ThreadLocalRandom.current().nextInt(-180, 180);
            }
            float yaw = (event.getYaw() + 180.0f) % 360.0f;
            if (!((FakeRotation)this.module).randomize.getValue().booleanValue()) {
                if (((FakeRotation)this.module).jitter.getValue().booleanValue()) {
                    if (ListenerMotion.mc.player.ticksExisted % (Integer)((FakeRotation)this.module).speed.getValue() == 0) {
                        boolean bl = ((FakeRotation)this.module).check = !((FakeRotation)this.module).check;
                    }
                    yaw = ((FakeRotation)this.module).check ? (yaw += (float)((Integer)((FakeRotation)this.module).limit.getValue()).intValue()) : (yaw -= (float)((Integer)((FakeRotation)this.module).limit.getValue()).intValue());
                }
                ListenerMotion.mc.player.rotationYaw = yaw;
                ListenerMotion.mc.player.rotationYawHead = yaw;
                switch ((Pitch)((Object)((FakeRotation)this.module).pitch.getValue())) {
                    case DOWN: {
                        ListenerMotion.mc.player.rotationPitch = 90.0f;
                        break;
                    }
                    case UP: {
                        ListenerMotion.mc.player.rotationPitch = -90.0f;
                        break;
                    }
                    case ZERO: {
                        ListenerMotion.mc.player.rotationPitch = 0.0f;
                    }
                }
            }
        }
    }

    private boolean canRotate() {
        return !(((FakeRotation)this.module).strict.getValue() == false || ListenerMotion.mc.player.getActiveItemStack().getItem() instanceof ItemFood && !ListenerMotion.mc.gameSettings.keyBindAttack.isKeyDown() || !ListenerMotion.mc.gameSettings.keyBindAttack.isKeyDown() && !ListenerMotion.mc.gameSettings.keyBindUseItem.isKeyDown());
    }
}

