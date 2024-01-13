/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemSpectralArrow
 *  net.minecraft.item.ItemStack
 *  net.minecraft.potion.PotionType
 *  net.minecraft.potion.PotionUtils
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 */
package me.chachoox.lithium.impl.modules.player.quiver;

import me.chachoox.lithium.api.event.events.Stage;
import me.chachoox.lithium.api.util.inventory.ItemUtil;
import me.chachoox.lithium.api.util.movement.PositionUtil;
import me.chachoox.lithium.api.util.rotation.RotationUtil;
import me.chachoox.lithium.impl.event.events.movement.MotionUpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.player.quiver.Quiver;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSpectralArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class ListenerMotion
extends ModuleListener<Quiver, MotionUpdateEvent> {
    private PotionType lastType;
    private long lastDown;

    public ListenerMotion(Quiver module) {
        super(module, MotionUpdateEvent.class);
    }

    @Override
    public void call(MotionUpdateEvent event) {
        ItemStack arrow;
        EnumHand hand = ItemUtil.getHand((Item)Items.BOW);
        if (ListenerMotion.mc.player.isCreative() || ListenerMotion.mc.currentScreen != null || hand == null || (arrow = ((Quiver)this.module).findArrow()).isEmpty() || this.blocked()) {
            return;
        }
        boolean cycle = true;
        if (((Quiver)this.module).badStack(arrow) || ((Quiver)this.module).fast) {
            if (!cycle) {
                return;
            }
            ((Quiver)this.module).cycle(false, true);
            ((Quiver)this.module).fast = false;
            arrow = ((Quiver)this.module).findArrow();
            if (((Quiver)this.module).badStack(arrow)) {
                return;
            }
        }
        if (event.getStage() == Stage.PRE) {
            if (ListenerMotion.mc.gameSettings.keyBindUseItem.isKeyDown()) {
                this.lastDown = System.currentTimeMillis();
            } else if (System.currentTimeMillis() - this.lastDown > 100L) {
                return;
            }
            EntityPlayerSP player = ListenerMotion.mc.player;
            if (player.motionX != 0.0 || player.motionZ != 0.0) {
                Vec3d vec3d = player.getPositionVector().add(player.motionX, player.motionY + (double)player.getEyeHeight(), player.motionZ);
                float[] rotations = RotationUtil.getRotations(vec3d);
                ListenerMotion.mc.player.rotationYaw = rotations[0];
                ListenerMotion.mc.player.rotationPitch = rotations[1];
            } else {
                ListenerMotion.mc.player.rotationPitch = -90.0f;
            }
        } else if (!ListenerMotion.mc.player.getActiveItemStack().isEmpty()) {
            PotionType type = PotionUtils.getPotionFromItem((ItemStack)arrow);
            if (arrow.getItem() instanceof ItemSpectralArrow) {
                type = Quiver.SPECTRAL;
            }
            if (this.lastType == type && !((Quiver)this.module).timer.passed(((Integer)((Quiver)this.module).shootDelay.getValue()).intValue())) {
                return;
            }
            this.lastType = type;
            float ticks = (float)(ListenerMotion.mc.player.getHeldItem(hand).getMaxItemUseDuration() - ListenerMotion.mc.player.getItemInUseCount()) - 0.0f;
            if (ticks >= (float)((Integer)((Quiver)this.module).releaseTicks.getValue()).intValue() && ticks <= (float)((Integer)((Quiver)this.module).maxTicks.getValue()).intValue()) {
                ListenerMotion.mc.playerController.onStoppedUsingItem((EntityPlayer)ListenerMotion.mc.player);
                ((Quiver)this.module).fast = cycle;
                ((Quiver)this.module).timer.reset();
            }
        }
    }

    private boolean blocked() {
        BlockPos pos = PositionUtil.getPosition();
        return ListenerMotion.mc.world.getBlockState(pos.up()).getMaterial().blocksMovement() || ListenerMotion.mc.world.getBlockState(pos.up(2)).getMaterial().blocksMovement();
    }
}

