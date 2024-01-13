/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.util.EnumHand
 *  net.minecraft.world.World
 */
package me.chachoox.lithium.impl.modules.movement.nofall;

import me.chachoox.lithium.api.event.events.Stage;
import me.chachoox.lithium.api.util.blocks.BlockUtil;
import me.chachoox.lithium.api.util.inventory.InventoryUtil;
import me.chachoox.lithium.api.util.inventory.ItemUtil;
import me.chachoox.lithium.api.util.movement.PositionUtil;
import me.chachoox.lithium.impl.event.events.movement.MotionUpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.movement.nofall.NoFall;
import me.chachoox.lithium.impl.modules.movement.nofall.util.NoFallMode;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ListenerMotion
extends ModuleListener<NoFall, MotionUpdateEvent> {
    public ListenerMotion(NoFall module) {
        super(module, MotionUpdateEvent.class);
    }

    @Override
    public void call(MotionUpdateEvent event) {
        if (ListenerMotion.mc.player.onGround && (double)ListenerMotion.mc.player.fallDistance < 0.5 && ListenerMotion.mc.player.posY - BlockUtil.getNearestBlockBelow() > 0.625 && ListenerMotion.mc.gameSettings.keyBindJump.isKeyDown()) {
            return;
        }
        if (((NoFall)this.module).mode.getValue() == NoFallMode.BUCKET && ((NoFall)this.module).check()) {
            int slot = ItemUtil.findHotbarItem(Items.WATER_BUCKET);
            if (event.getStage() == Stage.PRE && InventoryUtil.isHolding(Items.WATER_BUCKET)) {
                ListenerMotion.mc.player.rotationPitch = 90.0f;
            }
            if (!PositionUtil.inLiquid() && ((NoFall)this.module).stopWatch.passed(1000L)) {
                ItemUtil.switchTo(slot);
                if (slot == -1) {
                    return;
                }
                ListenerMotion.mc.playerController.processRightClick((EntityPlayer)ListenerMotion.mc.player, (World)ListenerMotion.mc.world, slot == -2 ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
                ((NoFall)this.module).stopWatch.reset();
            }
        }
    }
}

