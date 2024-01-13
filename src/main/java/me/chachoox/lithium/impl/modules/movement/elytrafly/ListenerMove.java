/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.material.Material
 *  net.minecraft.entity.Entity
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.EntityEquipmentSlot
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.util.math.MathHelper
 */
package me.chachoox.lithium.impl.modules.movement.elytrafly;

import me.chachoox.lithium.api.util.movement.MovementUtil;
import me.chachoox.lithium.api.util.network.PacketUtil;
import me.chachoox.lithium.impl.event.events.movement.actions.MoveEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.movement.elytrafly.ElytraFly;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.math.MathHelper;

public class ListenerMove
extends ModuleListener<ElytraFly, MoveEvent> {
    public ListenerMove(ElytraFly module) {
        super(module, MoveEvent.class, 5000);
    }

    @Override
    public void call(MoveEvent event) {
        if (((ElytraFly)this.module).stopInWater.getValue().booleanValue() && ListenerMove.mc.player.isInsideOfMaterial(Material.WATER)) {
            return;
        }
        if (!ListenerMove.mc.player.onGround && !ListenerMove.mc.player.isElytraFlying() && ListenerMove.mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == Items.ELYTRA && ListenerMove.mc.gameSettings.keyBindJump.isKeyDown() && ((ElytraFly)this.module).autoStart.getValue().booleanValue()) {
            if (((ElytraFly)this.module).startTimer.passed(350L)) {
                Managers.TIMER.set(0.17f);
                PacketUtil.send(new CPacketEntityAction((Entity)ListenerMove.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                ((ElytraFly)this.module).startTimer.reset();
            }
        } else {
            Managers.TIMER.set(1.0f);
        }
        if (((ElytraFly)this.module).isElytra()) {
            if (!((ElytraFly)this.module).wasp.getValue().booleanValue() && !((ElytraFly)this.module).boost.getValue().booleanValue()) {
                if (!ListenerMove.mc.player.movementInput.forwardKeyDown && !ListenerMove.mc.player.movementInput.sneak) {
                    ListenerMove.mc.player.setVelocity(0.0, 0.0, 0.0);
                } else if (ListenerMove.mc.player.movementInput.forwardKeyDown && (((ElytraFly)this.module).vertical.getValue().booleanValue() || ListenerMove.mc.player.prevRotationPitch > 0.0f)) {
                    float yaw = (float)Math.toRadians(ListenerMove.mc.player.rotationYaw);
                    double speed = ((Float)((ElytraFly)this.module).hSpeed.getValue()).floatValue() / 5.0f;
                    ListenerMove.mc.player.motionX = (double)MathHelper.sin((float)yaw) * -speed;
                    ListenerMove.mc.player.motionZ = (double)MathHelper.cos((float)yaw) * speed;
                    return;
                }
            }
            if (((ElytraFly)this.module).wasp.getValue().booleanValue()) {
                double vSpeed = ListenerMove.mc.gameSettings.keyBindJump.isKeyDown() ? (double)((Float)((ElytraFly)this.module).vSpeed.getValue()).floatValue() : (ListenerMove.mc.gameSettings.keyBindSneak.isKeyDown() ? (double)(-((Float)((ElytraFly)this.module).vSpeed.getValue()).floatValue()) : 0.0);
                event.setY(vSpeed);
                ListenerMove.mc.player.setVelocity(0.0, 0.0, 0.0);
                ListenerMove.mc.player.motionY = vSpeed;
                ListenerMove.mc.player.moveVertical = (float)vSpeed;
                if (!(MovementUtil.isMoving() || ListenerMove.mc.gameSettings.keyBindJump.isKeyDown() || ListenerMove.mc.gameSettings.keyBindSneak.isKeyDown())) {
                    event.setX(0.0);
                    event.setY(0.0);
                    event.setY(0.0);
                    return;
                }
                MovementUtil.strafe(event, (double)(((Float)((ElytraFly)this.module).hSpeed.getValue()).floatValue() / 5.0f));
            }
            if (((ElytraFly)this.module).boost.getValue().booleanValue() && ListenerMove.mc.player.movementInput.jump) {
                float yaw = ListenerMove.mc.player.rotationYaw * ((float)Math.PI / 180);
                ListenerMove.mc.player.motionX -= (double)(MathHelper.sin((float)yaw) * 0.15f);
                ListenerMove.mc.player.motionZ += (double)(MathHelper.cos((float)yaw) * 0.15f);
            }
        }
    }
}

