/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.item.EntityXPOrb
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.projectile.EntityArrow
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 */
package me.chachoox.lithium.impl.modules.combat.holefill;

import java.util.ArrayList;
import java.util.List;
import me.chachoox.lithium.api.module.BlockPlaceModule;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.blocks.BlockUtil;
import me.chachoox.lithium.api.util.blocks.HoleUtil;
import me.chachoox.lithium.api.util.math.MathUtil;
import me.chachoox.lithium.api.util.movement.PositionUtil;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.combat.holefill.ListenerMotion;
import me.chachoox.lithium.impl.modules.combat.holefill.ListenerRender;
import me.chachoox.lithium.impl.modules.combat.holefill.util.Priority;
import me.chachoox.lithium.impl.modules.render.holeesp.util.TwoBlockPos;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class HoleFill
extends BlockPlaceModule {
    protected final NumberProperty<Float> horizontal = new NumberProperty<Float>(Float.valueOf(4.5f), Float.valueOf(1.0f), Float.valueOf(6.0f), Float.valueOf(0.1f), new String[]{"HorizontalDist", "HorizontalDistance", "holeRangeXZ"}, "Horizontal distance for the hole range.");
    protected final NumberProperty<Float> vertical = new NumberProperty<Float>(Float.valueOf(4.0f), Float.valueOf(1.0f), Float.valueOf(6.0f), Float.valueOf(0.1f), new String[]{"VerticalDist", "VerticalDistance", "holerangeY"}, "Vertical distance for the hole range.");
    protected final EnumProperty<Priority> priority = new EnumProperty<Priority>(Priority.FARTHEST, new String[]{"Priority", "prio"}, "What holes should it fill first.");
    protected final Property<Boolean> smart = new Property<Boolean>(false, new String[]{"Smart", "Genius", "300Iq", "alberteinstein"}, "Avoids filling holes when a player isnt in the range but the module is enabled.");
    protected final Property<Boolean> noSelfFill = new Property<Boolean>(false, new String[]{"NoSelfFill", "NoHoleFillSelf", "AntiSelf"}, "Avoids filling holes that are close to us which might holefill ourself.");
    protected final NumberProperty<Float> noSelfFillRange = new NumberProperty<Float>(Float.valueOf(2.5f), Float.valueOf(1.0f), Float.valueOf(3.0f), Float.valueOf(0.1f), new String[]{"NoSelfFillRange", "NoSelfFillDistance"}, "How close a hole can be to us before it is considered holefilling ourself.");
    protected final NumberProperty<Float> smartRange = new NumberProperty<Float>(Float.valueOf(3.0f), Float.valueOf(1.0f), Float.valueOf(6.0f), Float.valueOf(0.1f), new String[]{"SmartRange", "GeniusRange", "300IqRange"}, "How close an enemy has to be to us before allowing us to holefill.");
    protected final NumberProperty<Float> enemyRange = new NumberProperty<Float>(Float.valueOf(8.0f), Float.valueOf(1.0f), Float.valueOf(10.0f), Float.valueOf(0.1f), new String[]{"EnemyRange", "TargetRange"}, "How close a player has to be to us before being selected as an enemy.");
    protected final Property<Boolean> twoByOne = new Property<Boolean>(true, new String[]{"2x1", "Long", "Doubles", "longHoles"}, "Fills holes that are 2 blocks long and 1 block wide.");
    protected final Property<Boolean> antiFriend = new Property<Boolean>(true, new String[]{"FriendProtect", "antifriend"}, "Wont place if a friend is nearby.");
    protected final Property<Boolean> autoDisable = new Property<Boolean>(true, new String[]{"AutoDisable", "Disable"}, "Disables holefill when there are no holes to be filled.");
    protected List<BlockPos> holes = new ArrayList<BlockPos>();
    protected EntityPlayer target;

    public HoleFill() {
        super("HoleFill", new String[]{"HoleFill", "antihole", "holefiller", "noholes"}, "Attempts to fill holes near your opponent.", Category.COMBAT);
        this.offerProperties(this.horizontal, this.vertical, this.priority, this.smart, this.antiFriend, this.noSelfFill, this.noSelfFillRange, this.smartRange, this.enemyRange, this.twoByOne, this.autoDisable);
        this.offerListeners(new ListenerMotion(this), new ListenerRender(this));
        this.smart.addObserver(event -> this.holes.clear());
    }

    @Override
    public void clear() {
        this.holes.clear();
        this.target = null;
    }

    protected List<BlockPos> calcHoles() {
        ArrayList<BlockPos> holes = new ArrayList<BlockPos>();
        List<BlockPos> positions = BlockUtil.getSphere(((Float)this.horizontal.getValue()).floatValue(), false);
        for (BlockPos pos : positions) {
            if (!this.isHole(pos)) continue;
            holes.add(pos);
        }
        return holes;
    }

    private boolean isHole(BlockPos pos) {
        for (Entity entity : HoleFill.mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos))) {
            if (entity instanceof EntityItem || entity instanceof EntityXPOrb || entity instanceof EntityArrow) continue;
            return false;
        }
        if (this.twoByOne.getValue().booleanValue()) {
            BlockPos twoPos = HoleUtil.isDoubleBedrock(pos);
            if (twoPos == null) {
                twoPos = HoleUtil.isDoubleObby(pos);
            }
            if (twoPos != null) {
                TwoBlockPos twoBlockPos = new TwoBlockPos(pos, pos.add(twoPos.getX(), twoPos.getY(), twoPos.getZ()));
                return this.target == null || !twoBlockPos.getOne().equals((Object)PositionUtil.getPosition((Entity)this.target)) && !twoBlockPos.getTwo().equals((Object)PositionUtil.getPosition((Entity)this.target));
            }
        }
        return HoleUtil.isHole(pos);
    }

    protected boolean isInDoubleHole(EntityPlayer player) {
        return this.isDoubleHole(player.getPosition());
    }

    protected boolean isDoubleHole(BlockPos pos) {
        BlockPos validTwoBlockObby = HoleUtil.isDoubleObby(pos);
        if (validTwoBlockObby != null) {
            return true;
        }
        BlockPos validTwoBlockBedrock = HoleUtil.isDoubleBedrock(pos);
        return validTwoBlockBedrock != null;
    }

    protected EntityPlayer calcFriend() {
        EntityPlayer friend = null;
        for (EntityPlayer player : HoleFill.mc.world.playerEntities) {
            if (player == HoleFill.mc.player || !Managers.FRIEND.isFriend(player) || this.target == null || !(this.target.getDistanceSq((Entity)player) < (double)MathUtil.square(2.5f))) continue;
            friend = player;
        }
        return friend;
    }

    protected boolean isTrapped(EntityPlayer player) {
        Vec3d vec = player.getPositionVector();
        BlockPos pos = new BlockPos(vec);
        return !HoleFill.mc.world.getBlockState(pos.up(2)).getMaterial().isReplaceable();
    }
}

