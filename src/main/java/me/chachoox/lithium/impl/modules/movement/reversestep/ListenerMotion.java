/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.material.Material
 *  net.minecraft.entity.item.EntityEnderPearl
 */
package me.chachoox.lithium.impl.modules.movement.reversestep;

import java.util.List;
import java.util.stream.Collectors;
import me.chachoox.lithium.api.event.events.Stage;
import me.chachoox.lithium.api.util.blocks.BlockUtil;
import me.chachoox.lithium.api.util.entity.EntityUtil;
import me.chachoox.lithium.impl.event.events.movement.MotionUpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.combat.selffill.SelfFill;
import me.chachoox.lithium.impl.modules.movement.noclip.NoClip;
import me.chachoox.lithium.impl.modules.movement.reversestep.ReverseStep;
import me.chachoox.lithium.impl.modules.movement.speed.Speed;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityEnderPearl;

public class ListenerMotion
extends ModuleListener<ReverseStep, MotionUpdateEvent> {
    private boolean reset = false;

    public ListenerMotion(ReverseStep module) {
        super(module, MotionUpdateEvent.class);
    }

    @Override
    public void call(MotionUpdateEvent event) {
        if (event.getStage() == Stage.POST) {
            if (EntityUtil.inLiquid(true) || EntityUtil.inLiquid(false) || Managers.MODULE.get(NoClip.class).isEnabled() || Managers.MODULE.get(SelfFill.class).isEnabled() || Managers.MODULE.get(Speed.class).isEnabled() || ListenerMotion.mc.player.noClip) {
                this.reset = true;
                return;
            }
            List pearls = ListenerMotion.mc.world.loadedEntityList.stream().filter(EntityEnderPearl.class::isInstance).map(EntityEnderPearl.class::cast).collect(Collectors.toList());
            if (!pearls.isEmpty()) {
                ((ReverseStep)this.module).waitForOnGround = true;
            }
            if (!ListenerMotion.mc.player.onGround) {
                if (ListenerMotion.mc.gameSettings.keyBindJump.isKeyDown()) {
                    ((ReverseStep)this.module).jumped = true;
                }
            } else {
                ((ReverseStep)this.module).jumped = false;
                this.reset = false;
                ((ReverseStep)this.module).waitForOnGround = false;
            }
            if (!((ReverseStep)this.module).jumped && (double)ListenerMotion.mc.player.fallDistance < 0.5 && ListenerMotion.mc.player.posY - BlockUtil.getNearestBlockBelow() > 0.625 && ListenerMotion.mc.player.posY - BlockUtil.getNearestBlockBelow() <= (Double)((ReverseStep)this.module).distance.getValue() && !this.reset && !((ReverseStep)this.module).waitForOnGround) {
                if (!ListenerMotion.mc.player.onGround) {
                    ++((ReverseStep)this.module).packets;
                }
                if (!(ListenerMotion.mc.player.onGround || !(ListenerMotion.mc.player.motionY < 0.0) || ListenerMotion.mc.player.isOnLadder() || ListenerMotion.mc.player.isEntityInsideOpaqueBlock() || ListenerMotion.mc.player.isInsideOfMaterial(Material.LAVA) || ListenerMotion.mc.player.isInsideOfMaterial(Material.WATER) || ListenerMotion.mc.gameSettings.keyBindJump.isKeyDown() || ((ReverseStep)this.module).packets <= 0)) {
                    ListenerMotion.mc.player.motionY = -((Double)((ReverseStep)this.module).speed.getValue()).doubleValue();
                    ((ReverseStep)this.module).packets = 0;
                }
            }
        }
    }
}

