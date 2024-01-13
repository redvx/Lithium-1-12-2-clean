/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 */
package me.chachoox.lithium.impl.modules.movement.holepull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.blocks.BlockUtil;
import me.chachoox.lithium.api.util.blocks.HoleUtil;
import me.chachoox.lithium.api.util.entity.EntityUtil;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.api.util.rotation.RotationUtil;
import me.chachoox.lithium.impl.event.events.movement.actions.MoveEvent;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.movement.holepull.ListenerMove;
import me.chachoox.lithium.impl.modules.movement.holepull.ListenerUpdate;
import me.chachoox.lithium.impl.modules.movement.holepull.mode.PullMode;
import me.chachoox.lithium.impl.modules.movement.step.Step;
import me.chachoox.lithium.impl.modules.render.holeesp.util.TwoBlockPos;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class HolePull
extends Module {
    protected final EnumProperty<PullMode> mode = new EnumProperty<PullMode>(PullMode.PULL, new String[]{"Mode", "Type", "method"}, "Pull: - Old HolePull, basically anchor / Snap: - Holesnap, like kami5/sn0w/cascade");
    public NumberProperty<Integer> pitch = new NumberProperty<Integer>(60, 0, 90, new String[]{"Pitch", "BITCH", "pitc"}, "How low we have to be looking to get pulled towards a hole. (Requires mode Pull)");
    public NumberProperty<Float> range = new NumberProperty<Float>(Float.valueOf(4.0f), Float.valueOf(1.0f), Float.valueOf(6.0f), Float.valueOf(0.5f), new String[]{"Range", "distance", "dist"}, "Maximum distance to hole. (Requires mode Snap)");
    protected final Property<Boolean> step = new Property<Boolean>(false, new String[]{"Step", "Up"}, "If you want to enable step with this (Requires mode Snap)");
    protected final Property<Boolean> timer = new Property<Boolean>(true, new String[]{"Timer", "tickshift", "tickspeed"}, "Makes you faster (Requires mode Snap)");
    public NumberProperty<Float> timerAmount = new NumberProperty<Float>(Float.valueOf(4.0f), Float.valueOf(1.0f), Float.valueOf(5.0f), Float.valueOf(0.1f), new String[]{"TimerAmount", "timerspeed"}, "Timer speed. (Requires mode Snap)");
    public NumberProperty<Integer> timerLength = new NumberProperty<Integer>(25, 10, 100, new String[]{"TimerLength", "tickshiftlength"}, "The amount of time for which timer is used. (Requires mode Snap)");
    protected boolean anchoring;
    protected int stuck;
    protected int boosted;
    protected BlockPos hole;
    protected Vec3d twoVec;

    public HolePull() {
        super("HolePull", new String[]{"HolePull", "HoleSnap", "HP", "HoleMove", "HoleWalk"}, "Moves you towards holes.", Category.MOVEMENT);
        this.offerProperties(this.mode, this.pitch, this.range, this.step, this.timer, this.timerAmount, this.timerLength);
        this.offerListeners(new ListenerMove(this), new ListenerUpdate(this));
        this.mode.addObserver(event -> this.reset());
    }

    @Override
    public void onEnable() {
        if (this.mode.getValue() == PullMode.SNAP) {
            if (this.step.getValue().booleanValue()) {
                Managers.MODULE.get(Step.class).enableNoMessage();
            }
            this.anchoring = true;
            this.hole = this.getTarget(((Float)this.range.getValue()).floatValue());
            if (this.hole == null) {
                Logger.getLogger().log("\u00a7c<HolePull> Couldn't find a hole.", 45088);
                this.disable();
            }
        }
    }

    @Override
    public void onDisable() {
        if (this.step.getValue().booleanValue()) {
            Managers.MODULE.get(Step.class).disableNoMessage();
        }
        Managers.TIMER.reset();
        this.reset();
    }

    public boolean isAnchoring() {
        return this.anchoring;
    }

    protected boolean isPitchDown() {
        return HolePull.mc.player.rotationPitch >= (float)((Integer)this.pitch.getValue()).intValue();
    }

    private void reset() {
        this.twoVec = null;
        this.anchoring = false;
        this.stuck = 0;
        this.boosted = 0;
    }

    protected void doPull(MoveEvent event, BlockPos pos) {
        double speed;
        Vec3d targetPos;
        Vec3d playerPos = HolePull.mc.player.getPositionVector();
        if (HoleUtil.isDoubleHole(pos)) {
            TwoBlockPos doubleHole = HoleUtil.getDouble(pos);
            this.twoVec = targetPos = this.getTwo(doubleHole);
        } else {
            targetPos = new Vec3d((double)pos.getX() + 0.5, HolePull.mc.player.posY, (double)pos.getZ() + 0.5);
        }
        double yawRad = Math.toRadians(RotationUtil.getRotationTo((Vec3d)playerPos, (Vec3d)targetPos).x);
        double dist = playerPos.distanceTo(targetPos);
        double d = speed = HolePull.mc.player.onGround ? -Math.min(0.2805, dist / 2.0) : -EntityUtil.getDefaultMoveSpeed() + 0.02;
        if (dist < 0.1) {
            event.setX(0.0);
            event.setZ(0.0);
            return;
        }
        event.setX(-Math.sin(yawRad) * speed);
        event.setZ(Math.cos(yawRad) * speed);
    }

    protected boolean isSafe(Vec3d vec) {
        if (vec == null) {
            return false;
        }
        Vec3d playerVec = HolePull.mc.player.getPositionVector();
        double dist = playerVec.distanceTo(vec);
        return dist < 0.1;
    }

    private Vec3d getTwo(TwoBlockPos twoPos) {
        BlockPos one = twoPos.getOne();
        BlockPos two = twoPos.getTwo();
        double x = ((double)one.getX() + 0.5 + ((double)two.getX() + 0.5)) / 2.0;
        double z = ((double)one.getZ() + 0.5 + ((double)two.getZ() + 0.5)) / 2.0;
        return new Vec3d(x, HolePull.mc.player.posY, z);
    }

    private List<BlockPos> getHoles(float range) {
        ArrayList<BlockPos> holes = new ArrayList<BlockPos>();
        List<BlockPos> circle = BlockUtil.getSphere(range, false);
        for (BlockPos pos : circle) {
            if (HolePull.mc.world.getBlockState(pos).getBlock() != Blocks.AIR || !HoleUtil.isObbyHole(pos) && !HoleUtil.isBedrockHole(pos) && !HoleUtil.isDoubleHole(pos)) continue;
            holes.add(pos);
        }
        return holes;
    }

    private BlockPos getTarget(float range) {
        return this.getHoles(range).stream().filter(hole -> HolePull.mc.player.getPositionVector().distanceTo(new Vec3d((double)hole.getX() + 0.5, HolePull.mc.player.posY, (double)hole.getZ() + 0.5)) <= (double)range).min(Comparator.comparingDouble(hole -> HolePull.mc.player.getPositionVector().distanceTo(new Vec3d((double)hole.getX() + 0.5, HolePull.mc.player.posY, (double)hole.getZ() + 0.5)))).orElse(null);
    }
}

