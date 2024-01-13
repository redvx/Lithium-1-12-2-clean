/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.movement.speed;

import me.chachoox.lithium.api.util.movement.MovementUtil;
import me.chachoox.lithium.api.util.movement.PositionUtil;
import me.chachoox.lithium.asm.ducks.IEntity;
import me.chachoox.lithium.impl.event.events.movement.actions.MoveEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.movement.speed.Speed;
import me.chachoox.lithium.impl.modules.movement.speed.enums.JumpMode;
import me.chachoox.lithium.impl.modules.movement.speed.enums.SpeedMode;

public class ListenerMove
extends ModuleListener<Speed, MoveEvent> {
    public ListenerMove(Speed module) {
        super(module, MoveEvent.class);
    }

    @Override
    public void call(MoveEvent event) {
        if (!MovementUtil.isMoving() && ListenerMove.mc.player.isElytraFlying() || ListenerMove.mc.player.isOnLadder() || ListenerMove.mc.player.isSneaking() && !ListenerMove.mc.player.noClip) {
            return;
        }
        if (!((Speed)this.module).inLiquids.getValue().booleanValue() && PositionUtil.inLiquid()) {
            return;
        }
        if (((Speed)this.module).autoSprint.getValue().booleanValue() && ((Speed)this.module).canSprint()) {
            ListenerMove.mc.player.setSprinting(true);
        }
        if (((Speed)this.module).useTimer.getValue().booleanValue()) {
            Managers.TIMER.set(1.0888f);
        }
        float defaultSpeed = 0.2873f;
        switch ((SpeedMode)((Object)((Speed)this.module).mode.getValue())) {
            case STRAFE: {
                if (((Speed)this.module).strafeStage == 1 && MovementUtil.isMoving()) {
                    ((Speed)this.module).speed = 1.35 * MovementUtil.calcEffects(defaultSpeed) - 0.01;
                } else if (((Speed)this.module).strafeStage == 2 && MovementUtil.isMoving()) {
                    if (!PositionUtil.inLiquid() && !((IEntity)ListenerMove.mc.player).getIsInWeb()) {
                        double yMotion;
                        ListenerMove.mc.player.motionY = yMotion = (double)((JumpMode)((Object)((Speed)this.module).jump.getValue())).getHeight() + MovementUtil.getJumpSpeed();
                        event.setY(yMotion);
                    }
                    ((Speed)this.module).speed = Managers.KNOCKBACK.shouldBoost(((Speed)this.module).kbBoost.getValue()) ? (double)((Float)((Speed)this.module).boostReduction.getValue()).floatValue() / 10.0 : ((Speed)this.module).speed * (((Speed)this.module).boost ? 1.6835 : 1.395);
                } else if (((Speed)this.module).strafeStage == 3) {
                    ((Speed)this.module).speed = Managers.KNOCKBACK.shouldBoost(((Speed)this.module).kbBoost.getValue()) ? (double)((Float)((Speed)this.module).boostReduction.getValue()).floatValue() / 10.0 : ((Speed)this.module).distance - 0.66 * (((Speed)this.module).distance - MovementUtil.calcEffects(0.2873));
                    ((Speed)this.module).boost = !((Speed)this.module).boost;
                } else {
                    if ((ListenerMove.mc.world.getCollisionBoxes(null, ListenerMove.mc.player.getEntityBoundingBox().offset(0.0, ListenerMove.mc.player.motionY, 0.0)).size() > 0 || ListenerMove.mc.player.collidedVertically) && ((Speed)this.module).strafeStage > 0) {
                        ((Speed)this.module).strafeStage = MovementUtil.isMoving() ? 1 : 0;
                    }
                    ((Speed)this.module).speed = ((Speed)this.module).distance - ((Speed)this.module).distance / 159.0;
                }
                ((Speed)this.module).speed = Math.min(((Speed)this.module).speed, MovementUtil.calcEffects(10.0));
                ((Speed)this.module).speed = Math.max(((Speed)this.module).speed, MovementUtil.calcEffects(defaultSpeed));
                MovementUtil.strafe(event, ((Speed)this.module).speed);
                if (!MovementUtil.isMoving()) break;
                ++((Speed)this.module).strafeStage;
                break;
            }
            case STRAFESTRICT: {
                if (((Speed)this.module).strafeStage == 1 && MovementUtil.isMoving()) {
                    ((Speed)this.module).speed = 1.35 * MovementUtil.calcEffects(defaultSpeed) - 0.01;
                } else if (((Speed)this.module).strafeStage == 2 && MovementUtil.isMoving()) {
                    if (!PositionUtil.inLiquid() && !((IEntity)ListenerMove.mc.player).getIsInWeb()) {
                        double yMotion;
                        ListenerMove.mc.player.motionY = yMotion = (double)((JumpMode)((Object)((Speed)this.module).jump.getValue())).getHeight() + MovementUtil.getJumpSpeed();
                        event.setY(yMotion);
                    }
                    ((Speed)this.module).speed = Managers.KNOCKBACK.shouldBoost(((Speed)this.module).kbBoost.getValue()) ? (double)((Float)((Speed)this.module).boostReduction.getValue()).floatValue() : ((Speed)this.module).speed * (((Speed)this.module).boost ? 1.6835 : 1.395);
                } else if (((Speed)this.module).strafeStage == 3) {
                    ((Speed)this.module).speed = Managers.KNOCKBACK.shouldBoost(((Speed)this.module).kbBoost.getValue()) ? (double)((Float)((Speed)this.module).boostReduction.getValue()).floatValue() : ((Speed)this.module).distance - 0.66 * (((Speed)this.module).distance - MovementUtil.calcEffects(0.2873));
                    ((Speed)this.module).boost = !((Speed)this.module).boost;
                } else {
                    if ((ListenerMove.mc.world.getCollisionBoxes(null, ListenerMove.mc.player.getEntityBoundingBox().offset(0.0, ListenerMove.mc.player.motionY, 0.0)).size() > 0 || ListenerMove.mc.player.collidedVertically) && ((Speed)this.module).strafeStage > 0) {
                        ((Speed)this.module).strafeStage = MovementUtil.isMoving() ? 1 : 0;
                    }
                    ((Speed)this.module).speed = ((Speed)this.module).distance - ((Speed)this.module).distance / 159.0;
                }
                double speed = 0.465;
                double restrictedSpeed = 0.44;
                ((Speed)this.module).speed = Math.min(((Speed)this.module).speed, MovementUtil.calcEffects(10.0));
                ((Speed)this.module).speed = Math.max(((Speed)this.module).speed, MovementUtil.calcEffects(defaultSpeed));
                ((Speed)this.module).speed = Math.min(((Speed)this.module).speed, ((Speed)this.module).strictTicks > 25.0 ? speed : restrictedSpeed);
                MovementUtil.strafe(event, ((Speed)this.module).speed);
                ((Speed)this.module).strictTicks += 1.0;
                if (((Speed)this.module).strictTicks > 50.0) {
                    ((Speed)this.module).strictTicks = 0.0;
                }
                if (!MovementUtil.isMoving()) break;
                ++((Speed)this.module).strafeStage;
                break;
            }
            case ONGROUND: {
                if (!ListenerMove.mc.player.onGround && ((Speed)this.module).onGroundStage != 3) break;
                if (!ListenerMove.mc.player.collidedHorizontally && ListenerMove.mc.player.moveForward != 0.0f || ListenerMove.mc.player.moveStrafing != 0.0f) {
                    if (((Speed)this.module).onGroundStage == 2) {
                        ((Speed)this.module).speed *= 2.149;
                        ((Speed)this.module).onGroundStage = 3;
                    } else if (((Speed)this.module).onGroundStage == 3) {
                        ((Speed)this.module).onGroundStage = 2;
                        ((Speed)this.module).speed = ((Speed)this.module).distance - 0.66 * (((Speed)this.module).distance - MovementUtil.getSpeed());
                    } else if (PositionUtil.isBoxColliding() || ListenerMove.mc.player.collidedVertically) {
                        ((Speed)this.module).onGroundStage = 1;
                    }
                }
                ((Speed)this.module).speed = Math.min(((Speed)this.module).speed, MovementUtil.calcEffects(10.0));
                ((Speed)this.module).speed = Math.max(((Speed)this.module).speed, MovementUtil.calcEffects(defaultSpeed));
                MovementUtil.strafe(event, ((Speed)this.module).speed);
            }
        }
    }
}

