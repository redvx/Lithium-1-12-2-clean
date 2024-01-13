/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.math.BlockPos
 */
package me.chachoox.lithium.impl.modules.combat.instantweb;

import java.util.ArrayList;
import java.util.List;
import me.chachoox.lithium.api.module.BlockPlaceModule;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.math.MathUtil;
import me.chachoox.lithium.impl.modules.combat.instantweb.ListenerMotion;
import me.chachoox.lithium.impl.modules.combat.instantweb.ListenerRender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class InstantWeb
extends BlockPlaceModule {
    protected final Property<Boolean> face = new Property<Boolean>(true, new String[]{"Face", "head", "upper", "top"}, "Places webs on players face instead of just feet.");
    protected final NumberProperty<Float> range = new NumberProperty<Float>(Float.valueOf(4.0f), Float.valueOf(1.0f), Float.valueOf(6.0f), Float.valueOf(0.1f), new String[]{"PlaceRange", "PlaceDistance", "Range", "Distance"}, "How far we want to place webs.");
    protected final NumberProperty<Float> targetRange = new NumberProperty<Float>(Float.valueOf(9.0f), Float.valueOf(1.0f), Float.valueOf(12.0f), Float.valueOf(0.1f), new String[]{"TargetRange", "targetrang", "TargetDistance"}, "How far we the target can be.");
    protected final Property<Boolean> jumpDisable = new Property<Boolean>(true, new String[]{"JumpDisable", "AutoDisable", "NoJump"}, "Disables whenever we jumped.");
    protected EntityPlayer target;

    public InstantWeb() {
        super("InstantWeb", new String[]{"InstantWeb", "AutoWeb", "webber", "webaura", "instantweb"}, "Spams webs on players position to get them stuck.", Category.COMBAT);
        this.offerListeners(new ListenerMotion(this), new ListenerRender(this));
        this.offerProperties(this.face, this.range, this.targetRange, this.jumpDisable);
    }

    protected List<BlockPos> getPlacements() {
        BlockPos pos = new BlockPos(this.target.getPositionVector());
        ArrayList<BlockPos> placements = new ArrayList<BlockPos>();
        if (InstantWeb.mc.world.getBlockState(pos).getBlock() == Blocks.AIR || InstantWeb.mc.world.getBlockState(pos.up()).getBlock() == Blocks.AIR && InstantWeb.mc.player.getDistanceSq((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()) < (double)MathUtil.square(((Float)this.range.getValue()).floatValue())) {
            placements.add(pos);
            if (this.face.getValue().booleanValue()) {
                placements.add(pos.up());
            }
        }
        return placements;
    }
}

