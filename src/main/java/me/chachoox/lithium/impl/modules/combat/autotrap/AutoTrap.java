/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3i
 */
package me.chachoox.lithium.impl.modules.combat.autotrap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import me.chachoox.lithium.api.module.BlockPlaceModule;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.blocks.BlockUtil;
import me.chachoox.lithium.api.util.entity.CombatUtil;
import me.chachoox.lithium.api.util.entity.EntityUtil;
import me.chachoox.lithium.api.util.math.MathUtil;
import me.chachoox.lithium.api.util.movement.PositionUtil;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.combat.autotrap.ListenerMotion;
import me.chachoox.lithium.impl.modules.combat.autotrap.ListenerRender;
import me.chachoox.lithium.impl.modules.combat.autotrap.util.Trap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class AutoTrap
extends BlockPlaceModule {
    private static final EnumFacing[] TOP_FACINGS = new EnumFacing[]{EnumFacing.UP, EnumFacing.NORTH, EnumFacing.WEST, EnumFacing.SOUTH, EnumFacing.EAST};
    protected final NumberProperty<Float> targetRange = new NumberProperty<Float>(Float.valueOf(7.0f), Float.valueOf(1.0f), Float.valueOf(15.0f), Float.valueOf(0.1f), new String[]{"TargetRange", "TargetDistance"}, "How close we have to be to someone for them to be a target.");
    protected final NumberProperty<Float> range = new NumberProperty<Float>(Float.valueOf(4.0f), Float.valueOf(1.0f), Float.valueOf(6.0f), Float.valueOf(0.1f), new String[]{"PlaceRange", "Range", "PlaceDistance", "Distance"}, "How close we have to be to the target to trap them.");
    protected final NumberProperty<Integer> extend = new NumberProperty<Integer>(1, 1, 3, new String[]{"Extend", "Extension", "wrap"}, "How much we want to extend the trap if the player is in a 2x1 or something that isnt a 1x1.");
    protected final Property<Boolean> logoutSpots = new Property<Boolean>(false, new String[]{"LogoutSpots", "Logs", "logSpots"}, "Traps logout spots if there are no other players in range.");
    protected final Property<Boolean> noStep = new Property<Boolean>(false, new String[]{"AntiStep", "NoStep"}, "Places an extra block on top of the targets head to make it harder to step out.");
    protected final Property<Boolean> noScaffold = new Property<Boolean>(false, new String[]{"AntiScaffold", "NoScaffold"}, "Places a block on top of the targets head to make it harder to scaffold out.");
    protected final Property<Boolean> upperBody = new Property<Boolean>(false, new String[]{"Upperbody", "Chest"}, "Places blocks around the targets upper body.");
    protected final Property<Boolean> upperFace = new Property<Boolean>(false, new String[]{"UpperFace", "FaceBlock"}, "Places blocks around the targets face.");
    protected final Property<Boolean> jumpDisable = new Property<Boolean>(false, new String[]{"JumpDisable", "AutoDisable"}, "Disables autotrap if we are higher than when the module was enabled.");
    protected final Map<EntityPlayer, List<BlockPos>> cached = new HashMap<EntityPlayer, List<BlockPos>>();
    protected final Map<EntityPlayer, Double> speeds = new HashMap<EntityPlayer, Double>();
    protected List<BlockPos> placeList;
    public EntityPlayer target;

    public AutoTrap() {
        super("AutoTrap", new String[]{"Autotrap", "trap", "encase"}, "Traps enemies.", Category.COMBAT);
        this.offerProperties(this.targetRange, this.range, this.extend, this.noStep, this.noScaffold, this.upperBody, this.upperFace, this.jumpDisable);
        this.offerListeners(new ListenerMotion(this), new ListenerRender(this));
    }

    @Override
    public void clear() {
        this.cached.clear();
        this.speeds.clear();
        this.target = null;
    }

    @Override
    public String getSuffix() {
        return this.target != null ? this.target.getName() : null;
    }

    protected void getTargets() {
        EntityPlayer newTarget;
        this.cached.clear();
        this.updateSpeed();
        this.target = newTarget = this.calcTarget();
        if (newTarget == null) {
            return;
        }
        List<BlockPos> newTrapping = this.cached.get(newTarget);
        if (newTrapping == null) {
            newTrapping = this.getPositions(newTarget);
        }
        this.placeList = newTrapping;
    }

    private List<BlockPos> getPositions(EntityPlayer player) {
        ArrayList<BlockPos> blocked = new ArrayList<BlockPos>();
        BlockPos playerPos = new BlockPos((Entity)player);
        if (CombatUtil.isHole(playerPos, false)[0] || (Integer)this.extend.getValue() == 1) {
            blocked.add(playerPos.up());
        } else {
            List unfiltered = new ArrayList<BlockPos>(PositionUtil.getBlockedPositions((Entity)player)).stream().sorted(Comparator.comparingDouble(BlockUtil::getDistanceSq)).collect(Collectors.toList());
            List filtered = new ArrayList(unfiltered).stream().filter(pos -> AutoTrap.mc.world.getBlockState(pos).getMaterial().isReplaceable() && AutoTrap.mc.world.getBlockState(pos.up()).getMaterial().isReplaceable()).collect(Collectors.toList());
            if ((Integer)this.extend.getValue() == 3 && filtered.size() == 2 && unfiltered.size() == 4 && ((BlockPos)unfiltered.get(0)).equals(filtered.get(0)) && ((BlockPos)unfiltered.get(3)).equals(filtered.get(1))) {
                filtered.clear();
            }
            if ((Integer)this.extend.getValue() == 2 && filtered.size() > 2 || (Integer)this.extend.getValue() == 3 && filtered.size() == 3) {
                while (filtered.size() > 2) {
                    filtered.remove(filtered.size() - 1);
                }
            }
            for (BlockPos pos2 : filtered) {
                blocked.add(pos2.up());
            }
        }
        if (blocked.isEmpty()) {
            blocked.add(playerPos.up());
        }
        List<BlockPos> positions = this.positionsFromBlocked(blocked);
        positions.sort(Comparator.comparingDouble(pos -> -BlockUtil.getDistanceSq(pos)));
        positions.sort(Comparator.comparingInt(Vec3i::getY));
        return positions.stream().filter(pos -> BlockUtil.getDistanceSq(pos) <= (double)MathUtil.square(((Float)this.range.getValue()).floatValue())).collect(Collectors.toList());
    }

    private List<BlockPos> positionsFromBlocked(List<BlockPos> blockedIn) {
        ArrayList<BlockPos> positions = new ArrayList<BlockPos>();
        if (!this.noStep.getValue().booleanValue() && !blockedIn.isEmpty()) {
            BlockPos[] helping = this.findTopHelping(blockedIn, true);
            for (int i = 0; i < helping.length; ++i) {
                BlockPos pos2 = helping[i];
                if (pos2 == null) continue;
                if (!(i != 1 || this.upperBody.getValue().booleanValue() || blockedIn.contains(PositionUtil.getPosition().up()) && this.upperFace.getValue().booleanValue() || helping[5] == null)) {
                    positions.add(helping[5]);
                }
                positions.add(helping[i]);
                break;
            }
        }
        blockedIn.forEach(pos -> positions.addAll(this.applyOffsets((BlockPos)pos, Trap.TOP, (List<BlockPos>)positions)));
        if (this.upperBody.getValue().booleanValue() || this.upperFace.getValue().booleanValue() && blockedIn.contains(PositionUtil.getPosition().up())) {
            blockedIn.forEach(pos -> positions.addAll(this.applyOffsets((BlockPos)pos, Trap.OFFSETS, (List<BlockPos>)positions)));
        }
        if (blockedIn.size() == 1) {
            if (this.noScaffold.getValue().booleanValue()) {
                blockedIn.forEach(pos -> positions.addAll(this.applyOffsets((BlockPos)pos, Trap.NO_SCAFFOLD, (List<BlockPos>)positions)));
            }
            if (this.noStep.getValue().booleanValue()) {
                blockedIn.forEach(pos -> positions.addAll(this.applyOffsets((BlockPos)pos, Trap.NO_STEP, (List<BlockPos>)positions)));
            }
        }
        return positions;
    }

    private List<BlockPos> applyOffsets(BlockPos pos, Vec3i[] offsets, List<BlockPos> alreadyAdded) {
        ArrayList<BlockPos> positions = new ArrayList<BlockPos>();
        for (Vec3i vec3i : offsets) {
            BlockPos offset = pos.add(vec3i);
            if (alreadyAdded.contains(offset)) continue;
            positions.add(offset);
        }
        return positions;
    }

    private BlockPos[] findTopHelping(List<BlockPos> positions, boolean first) {
        BlockPos[] bestPos = new BlockPos[]{null, null, null, null, positions.get(0).up().north(), null};
        for (BlockPos pos : positions) {
            BlockPos up = pos.up();
            block9: for (EnumFacing facing : TOP_FACINGS) {
                BlockPos helping = up.offset(facing);
                if (!AutoTrap.mc.world.getBlockState(helping).getMaterial().isReplaceable()) {
                    bestPos[0] = helping;
                    return bestPos;
                }
                EnumFacing helpingFace = BlockUtil.getFacing(helping);
                byte blockingFactor = this.helpingEntityCheck(helping);
                if (helpingFace == null) {
                    switch (blockingFactor) {
                        case 0: {
                            if (!first || bestPos[5] != null) break;
                            ArrayList<BlockPos> hPositions = new ArrayList<BlockPos>();
                            for (BlockPos hPos : positions) {
                                hPositions.add(hPos.down());
                            }
                            bestPos[5] = this.findTopHelping(hPositions, false)[0];
                            bestPos[1] = helping;
                            break;
                        }
                        case 1: {
                            bestPos[3] = helping;
                            break;
                        }
                    }
                    continue;
                }
                switch (blockingFactor) {
                    case 0: {
                        bestPos[0] = helping;
                        continue block9;
                    }
                    case 1: {
                        bestPos[2] = helping;
                        continue block9;
                    }
                }
            }
        }
        return bestPos;
    }

    private byte helpingEntityCheck(BlockPos pos) {
        byte blocking = 0;
        for (Entity entity : AutoTrap.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos))) {
            if (entity == null || EntityUtil.isDead(entity) || !entity.preventEntitySpawning || entity instanceof EntityPlayer && !entity.getEntityBoundingBox().intersects(new AxisAlignedBB(pos))) continue;
            return 2;
        }
        return blocking;
    }

    protected EntityPlayer calcTarget() {
        EntityPlayer closest = null;
        double distance = Double.MAX_VALUE;
        for (EntityPlayer player : AutoTrap.mc.world.playerEntities) {
            double playerDist = AutoTrap.mc.player.getDistanceSq((Entity)player);
            if (!(playerDist < distance) || !this.isValid(player)) continue;
            closest = player;
        }
        return closest;
    }

    private boolean isValid(EntityPlayer player) {
        if (player != null && !EntityUtil.isDead((Entity)player) && !player.equals((Object)AutoTrap.mc.player) && !Managers.FRIEND.isFriend(player) && player.getDistanceSq((Entity)AutoTrap.mc.player) <= (double)MathUtil.square(((Float)this.targetRange.getValue()).floatValue())) {
            if (this.getSpeed(player) <= 22.0) {
                List<BlockPos> positions = this.getPositions(player);
                this.cached.put(player, positions);
                return positions.stream().anyMatch(pos -> AutoTrap.mc.world.getBlockState(pos).getMaterial().isReplaceable());
            }
            return true;
        }
        return false;
    }

    protected void updateSpeed() {
        for (EntityPlayer player : AutoTrap.mc.world.playerEntities) {
            double xDist = player.posX - player.prevPosX;
            double yDist = player.posY - player.prevPosY;
            double zDist = player.posZ - player.prevPosZ;
            double speed = xDist * xDist + yDist * yDist + zDist * zDist;
            this.speeds.put(player, speed);
        }
    }

    private double getSpeed(EntityPlayer player) {
        Double playerSpeed = this.speeds.get(player);
        if (playerSpeed != null) {
            return Math.sqrt(playerSpeed) * 20.0 * 3.6;
        }
        return 0.0;
    }

    public boolean trapLogs() {
        return this.isEnabled() && this.logoutSpots.getValue() != false;
    }
}

