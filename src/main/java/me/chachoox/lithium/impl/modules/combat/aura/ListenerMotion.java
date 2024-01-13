/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.ItemSword
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.util.EnumHand
 */
package me.chachoox.lithium.impl.modules.combat.aura;

import me.chachoox.lithium.api.event.events.Stage;
import me.chachoox.lithium.api.util.inventory.ItemUtil;
import me.chachoox.lithium.api.util.network.PacketUtil;
import me.chachoox.lithium.api.util.rotation.RotationUtil;
import me.chachoox.lithium.impl.event.events.movement.MotionUpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.combat.aura.Aura;
import me.chachoox.lithium.impl.modules.combat.aura.modes.HitBone;
import me.chachoox.lithium.impl.modules.combat.aura.modes.SwordMode;
import me.chachoox.lithium.impl.modules.combat.aura.util.TpsSync;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.EnumHand;

public class ListenerMotion
extends ModuleListener<Aura, MotionUpdateEvent> {
    public ListenerMotion(Aura module) {
        super(module, MotionUpdateEvent.class);
    }

    @Override
    public void call(MotionUpdateEvent event) {
        ((Aura)this.module).target = null;
        ((Aura)this.module).target = ((Aura)this.module).getTarget();
        if (event.getStage() == Stage.PRE && ((Aura)this.module).target != null) {
            boolean stopShield;
            switch ((SwordMode)((Object)((Aura)this.module).sword.getValue())) {
                case SWITCH: {
                    int swordSlot = ItemUtil.getItemSlot(ItemSword.class);
                    if (swordSlot == -1) {
                        return;
                    }
                    ItemUtil.switchTo(swordSlot);
                    break;
                }
                case REQUIRE: {
                    if (ListenerMotion.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword) break;
                    return;
                }
            }
            if (((Aura)this.module).rotate.getValue().booleanValue()) {
                float[] rotations = RotationUtil.getRotations((Entity)ListenerMotion.mc.player, (Entity)((Aura)this.module).target, (double)((HitBone)((Object)((Aura)this.module).bone.getValue())).getHeight(), 180.0);
                ListenerMotion.mc.player.rotationYaw = rotations[0];
                ListenerMotion.mc.player.rotationPitch = rotations[1];
            }
            float factor = 0.0f;
            switch ((TpsSync)((Object)((Aura)this.module).tpsSync.getValue())) {
                case AVERAGE: {
                    factor = 20.0f - Managers.TPS.getCurrentTps();
                    break;
                }
                case LATEST: {
                    factor = 20.0f - Managers.TPS.getTps();
                }
            }
            boolean stopSneak = ((Aura)this.module).stopSneak.getValue() != false && Managers.ACTION.isSneaking();
            boolean stopSprint = ((Aura)this.module).stopSprint.getValue() != false && ListenerMotion.mc.player.isSprinting();
            boolean bl = stopShield = ((Aura)this.module).stopShield.getValue() != false && ListenerMotion.mc.player.isActiveItemStackBlocking();
            if (stopSneak) {
                PacketUtil.send(new CPacketEntityAction((Entity)ListenerMotion.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            }
            if (stopSprint) {
                PacketUtil.send(new CPacketEntityAction((Entity)ListenerMotion.mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
            }
            if (stopShield) {
                ((Aura)this.module).releaseShield();
            }
            if (ListenerMotion.mc.player.getCooledAttackStrength(factor) >= 1.0f) {
                ListenerMotion.mc.playerController.attackEntity((EntityPlayer)ListenerMotion.mc.player, (Entity)((Aura)this.module).target);
                ListenerMotion.mc.player.swingArm(EnumHand.MAIN_HAND);
            }
            if (stopSneak) {
                PacketUtil.send(new CPacketEntityAction((Entity)ListenerMotion.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            }
            if (stopSprint) {
                PacketUtil.send(new CPacketEntityAction((Entity)ListenerMotion.mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
            }
            if (stopShield) {
                ((Aura)this.module).useShield();
            }
        }
    }
}

