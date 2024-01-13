/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.init.MobEffects
 *  net.minecraft.util.MovementInput
 */
package me.chachoox.lithium.api.util.movement;

import java.util.Objects;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.impl.event.events.movement.actions.MoveEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.util.MovementInput;

public class MovementUtil
implements Minecraftable {
    public static double[] directionSpeed(double speed) {
        float forward = MovementUtil.mc.player.movementInput.moveForward;
        float side = MovementUtil.mc.player.movementInput.moveStrafe;
        float yaw = MovementUtil.mc.player.prevRotationYaw + (MovementUtil.mc.player.rotationYaw - MovementUtil.mc.player.prevRotationYaw) * mc.getRenderPartialTicks();
        if (forward != 0.0f) {
            if (side > 0.0f) {
                yaw += (float)(forward > 0.0f ? -45 : 45);
            } else if (side < 0.0f) {
                yaw += (float)(forward > 0.0f ? 45 : -45);
            }
            side = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        double posX = (double)forward * speed * cos + (double)side * speed * sin;
        double posZ = (double)forward * speed * sin - (double)side * speed * cos;
        return new double[]{posX, posZ};
    }

    public static double[] directionSpeedNoForward(double speed) {
        float forward = 1.0f;
        if (MovementUtil.mc.gameSettings.keyBindLeft.isPressed() || MovementUtil.mc.gameSettings.keyBindRight.isPressed() || MovementUtil.mc.gameSettings.keyBindBack.isPressed() || MovementUtil.mc.gameSettings.keyBindForward.isPressed()) {
            forward = MovementUtil.mc.player.movementInput.moveForward;
        }
        float side = MovementUtil.mc.player.movementInput.moveStrafe;
        float yaw = MovementUtil.mc.player.prevRotationYaw + (MovementUtil.mc.player.rotationYaw - MovementUtil.mc.player.prevRotationYaw) * mc.getRenderPartialTicks();
        if (forward != 0.0f) {
            if (side > 0.0f) {
                yaw += (float)(forward > 0.0f ? -45 : 45);
            } else if (side < 0.0f) {
                yaw += (float)(forward > 0.0f ? 45 : -45);
            }
            side = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        double posX = (double)forward * speed * cos + (double)side * speed * sin;
        double posZ = (double)forward * speed * sin - (double)side * speed * cos;
        return new double[]{posX, posZ};
    }

    public static boolean anyMovementKeys() {
        return MovementUtil.mc.player.movementInput.forwardKeyDown || MovementUtil.mc.player.movementInput.backKeyDown || MovementUtil.mc.player.movementInput.leftKeyDown || MovementUtil.mc.player.movementInput.rightKeyDown || MovementUtil.mc.player.movementInput.jump || MovementUtil.mc.player.movementInput.sneak;
    }

    public static boolean isMoving(EntityLivingBase entity) {
        return entity.moveForward != 0.0f || entity.moveStrafing != 0.0f;
    }

    public static double getDistance2D() {
        double xDist = MovementUtil.mc.player.posX - MovementUtil.mc.player.prevPosX;
        double zDist = MovementUtil.mc.player.posZ - MovementUtil.mc.player.prevPosZ;
        return Math.sqrt(xDist * xDist + zDist * zDist);
    }

    public static boolean isMoving() {
        return (double)MovementUtil.mc.player.moveForward != 0.0 || (double)MovementUtil.mc.player.moveStrafing != 0.0;
    }

    public static void strafe(MoveEvent event, double speed) {
        if (MovementUtil.isMoving()) {
            double[] strafe = MovementUtil.strafe(speed);
            event.setX(strafe[0]);
            event.setZ(strafe[1]);
        } else {
            event.setX(0.0);
            event.setZ(0.0);
        }
    }

    public static double[] strafe(double speed) {
        return MovementUtil.strafe((Entity)MovementUtil.mc.player, speed);
    }

    public static double[] strafe(Entity entity, double speed) {
        return MovementUtil.strafe(entity, MovementUtil.mc.player.movementInput, speed);
    }

    public static double[] strafe(Entity entity, MovementInput movementInput, double speed) {
        float moveForward = movementInput.moveForward;
        float moveStrafe = movementInput.moveStrafe;
        float rotationYaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * mc.getRenderPartialTicks();
        if (moveForward != 0.0f) {
            if (moveStrafe > 0.0f) {
                rotationYaw += (float)(moveForward > 0.0f ? -45 : 45);
            } else if (moveStrafe < 0.0f) {
                rotationYaw += (float)(moveForward > 0.0f ? 45 : -45);
            }
            moveStrafe = 0.0f;
            if (moveForward > 0.0f) {
                moveForward = 1.0f;
            } else if (moveForward < 0.0f) {
                moveForward = -1.0f;
            }
        }
        double posX = (double)moveForward * speed * -Math.sin(Math.toRadians(rotationYaw)) + (double)moveStrafe * speed * Math.cos(Math.toRadians(rotationYaw));
        double posZ = (double)moveForward * speed * Math.cos(Math.toRadians(rotationYaw)) - (double)moveStrafe * speed * -Math.sin(Math.toRadians(rotationYaw));
        return new double[]{posX, posZ};
    }

    public static double getSpeed() {
        return MovementUtil.getSpeed(false);
    }

    public static double getSpeed(boolean slowness) {
        int amplifier;
        double defaultSpeed = 0.2873;
        if (MovementUtil.mc.player.isPotionActive(MobEffects.SPEED)) {
            amplifier = Objects.requireNonNull(MovementUtil.mc.player.getActivePotionEffect(MobEffects.SPEED)).getAmplifier();
            defaultSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        if (slowness && MovementUtil.mc.player.isPotionActive(MobEffects.SLOWNESS)) {
            amplifier = Objects.requireNonNull(MovementUtil.mc.player.getActivePotionEffect(MobEffects.SLOWNESS)).getAmplifier();
            defaultSpeed /= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return defaultSpeed;
    }

    public static double getJumpSpeed() {
        double defaultSpeed = 0.0;
        if (MovementUtil.mc.player.isPotionActive(MobEffects.JUMP_BOOST)) {
            int amplifier = MovementUtil.mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier();
            defaultSpeed += (double)(amplifier + 1) * 0.1;
        }
        return defaultSpeed;
    }

    public static void setMotion(double x, double y, double z) {
        if (MovementUtil.mc.player != null && MovementUtil.mc.player.getRidingEntity() != null) {
            if (MovementUtil.mc.player.isRiding()) {
                MovementUtil.mc.player.getRidingEntity().motionX = x;
                MovementUtil.mc.player.getRidingEntity().motionY = y;
                MovementUtil.mc.player.getRidingEntity().motionZ = x;
            } else {
                MovementUtil.mc.player.motionX = x;
                MovementUtil.mc.player.motionY = y;
                MovementUtil.mc.player.motionZ = z;
            }
        }
    }

    public static double calcEffects(double speed) {
        int amplifier;
        if (MovementUtil.mc.player.isPotionActive(MobEffects.SPEED)) {
            amplifier = MovementUtil.mc.player.getActivePotionEffect(MobEffects.SPEED).getAmplifier();
            speed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        if (MovementUtil.mc.player.isPotionActive(MobEffects.SLOWNESS)) {
            amplifier = MovementUtil.mc.player.getActivePotionEffect(MobEffects.SLOWNESS).getAmplifier();
            speed /= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return speed;
    }
}

