/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 */
package me.chachoox.lithium.impl.modules.combat.holefill;

import java.util.Comparator;
import me.chachoox.lithium.api.util.blocks.BlockUtil;
import me.chachoox.lithium.api.util.blocks.HoleUtil;
import me.chachoox.lithium.api.util.entity.EntityUtil;
import me.chachoox.lithium.api.util.math.MathUtil;
import me.chachoox.lithium.api.util.movement.PositionUtil;
import me.chachoox.lithium.impl.event.events.movement.MotionUpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.combat.holefill.HoleFill;
import me.chachoox.lithium.impl.modules.combat.holefill.util.Priority;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class ListenerMotion
extends ModuleListener<HoleFill, MotionUpdateEvent> {
    public ListenerMotion(HoleFill module) {
        super(module, MotionUpdateEvent.class);
    }

    @Override
    public void call(MotionUpdateEvent event) {
        if (((HoleFill)this.module).isNull()) {
            ((HoleFill)this.module).disable();
            return;
        }
        ((HoleFill)this.module).holes = ((HoleFill)this.module).calcHoles();
        if (((HoleFill)this.module).holes.isEmpty() && ((HoleFill)this.module).autoDisable.getValue().booleanValue()) {
            ((HoleFill)this.module).disable();
            return;
        }
        BlockPos playerPos = PositionUtil.getPosition();
        if (!(HoleUtil.isHole(playerPos) || EntityUtil.isOnBurrow((EntityPlayer)ListenerMotion.mc.player) || ((HoleFill)this.module).isInDoubleHole((EntityPlayer)ListenerMotion.mc.player))) {
            if (((HoleFill)this.module).noSelfFill.getValue().booleanValue()) {
                Vec3d vec = ListenerMotion.mc.player.getPositionVector().add(ListenerMotion.mc.player.motionX, ListenerMotion.mc.player.motionY, ListenerMotion.mc.player.motionZ);
                ((HoleFill)this.module).holes.removeIf(pos -> pos.distanceSq(vec.x, vec.y, vec.z) < (double)MathUtil.square(((Float)((HoleFill)this.module).noSelfFillRange.getValue()).floatValue()) && !this.checkSelf());
            }
            boolean closest = ((HoleFill)this.module).priority.getValue() == Priority.CLOSEST;
            ((HoleFill)this.module).holes.sort(Comparator.comparingDouble(pos -> closest ? BlockUtil.getDistanceSq(pos) : -BlockUtil.getDistanceSq(pos)));
        }
        Vec3d eyePos = PositionUtil.getEyesPos((Entity)ListenerMotion.mc.player);
        ((HoleFill)this.module).holes.removeIf(pos -> MathUtil.getYDifferenceSq(pos.getY(), eyePos.y) > (double)MathUtil.square(((Float)((HoleFill)this.module).vertical.getValue()).floatValue()));
        ((HoleFill)this.module).target = EntityUtil.getClosestEnemy();
        if (((HoleFill)this.module).target != null) {
            ((HoleFill)this.module).holes.removeIf(p -> BlockUtil.getDistanceSq((Entity)((HoleFill)this.module).target, p) > (double)MathUtil.square(((Float)((HoleFill)this.module).smartRange.getValue()).floatValue()));
            ((HoleFill)this.module).holes.sort(Comparator.comparingDouble(p -> BlockUtil.getDistanceSq((Entity)((HoleFill)this.module).target, p)));
        }
        if (((HoleFill)this.module).smart.getValue().booleanValue()) {
            ((HoleFill)this.module).target = EntityUtil.getClosestEnemy();
            if (((HoleFill)this.module).target == null || ((HoleFill)this.module).target.getDistanceSq((Entity)ListenerMotion.mc.player) > (double)MathUtil.square(((Float)((HoleFill)this.module).enemyRange.getValue()).floatValue()) || EntityUtil.isPlayerSafe(((HoleFill)this.module).target) || ((HoleFill)this.module).isInDoubleHole(((HoleFill)this.module).target) || ((HoleFill)this.module).isTrapped(((HoleFill)this.module).target) || ((HoleFill)this.module).antiFriend.getValue().booleanValue() && ((HoleFill)this.module).calcFriend() != null) {
                return;
            }
        }
        ((HoleFill)this.module).onPreEvent(((HoleFill)this.module).holes, event);
    }

    private boolean checkSelf() {
        return EntityUtil.isOnBurrow((EntityPlayer)ListenerMotion.mc.player) || ((HoleFill)this.module).isDoubleHole(ListenerMotion.mc.player.getPosition()) || HoleUtil.isHole(PositionUtil.getPosition());
    }
}

