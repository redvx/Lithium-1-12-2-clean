/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.item.EntityXPOrb
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 */
package me.chachoox.lithium.impl.modules.player.selfblocker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import me.chachoox.lithium.api.module.BlockPlaceModule;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.blocks.BlockUtil;
import me.chachoox.lithium.impl.modules.player.selfblocker.ListenerMotion;
import me.chachoox.lithium.impl.modules.player.selfblocker.ListenerRender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class SelfBlocker
extends BlockPlaceModule {
    protected final Property<Boolean> coverHead = new Property<Boolean>(true, new String[]{"CoverHead", "HeadCover", "HeadTrap"}, "Covers our head with obsidian.");
    protected final Property<Boolean> jumpDisable = new Property<Boolean>(true, new String[]{"JumpDisable", "AutoDisable", "NoJump"}, "Disables whenever we jumped so we dont spam obsidian.");

    public SelfBlocker() {
        super("SelfBlocker", new String[]{"SelfBlocker", "SelfTrap", "SelfBox"}, "Traps you in obsidian.", Category.PLAYER);
        this.offerListeners(new ListenerMotion(this), new ListenerRender(this));
        this.offerProperties(this.coverHead, this.jumpDisable);
    }

    protected List<BlockPos> getPlacements() {
        return this.getPlacements(this.getPlayerPos());
    }

    private List<BlockPos> getPlacements(BlockPos pos) {
        ArrayList<BlockPos> placements = new ArrayList<BlockPos>();
        HashSet<EnumFacing> directions = new HashSet<EnumFacing>();
        for (EnumFacing facing : EnumFacing.VALUES) {
            BlockPos offset = pos.offset(facing);
            if (this.isEntityIntersecting(offset)) {
                for (EnumFacing direction : EnumFacing.VALUES) {
                    BlockPos extend = offset.offset(direction);
                    if (extend.equals((Object)pos) || !BlockUtil.isReplaceable(extend)) continue;
                    if (!this.isEntityIntersecting(extend)) {
                        placements.add(extend);
                        continue;
                    }
                    directions.add(direction);
                }
                if (directions.size() > 1) {
                    BlockPos supportPos = pos;
                    for (EnumFacing direction : directions) {
                        supportPos = supportPos.offset(direction);
                    }
                    for (EnumFacing direction : EnumFacing.VALUES) {
                        if (this.isEntityIntersecting(supportPos.offset(direction))) continue;
                        placements.add(supportPos.offset(direction));
                    }
                }
            } else {
                placements.add(offset);
            }
            if (!this.coverHead.getValue().booleanValue()) continue;
            EnumFacing direction = SelfBlocker.mc.player.getHorizontalFacing();
            placements.add(new BlockPos(SelfBlocker.mc.player.posX + (double)direction.getXOffset(), SelfBlocker.mc.player.posY + 2.0, SelfBlocker.mc.player.posZ + (double)direction.getZOffset()));
        }
        for (BlockPos position : new ArrayList(placements)) {
            boolean support = true;
            for (EnumFacing direction : EnumFacing.VALUES) {
                BlockPos offsetPosition = position.offset(direction);
                if (SelfBlocker.mc.world.isAirBlock(offsetPosition)) continue;
                support = false;
                break;
            }
            if (!support) continue;
            placements.add(position.down());
        }
        return placements;
    }

    private boolean isEntityIntersecting(BlockPos pos) {
        for (Entity entity : new ArrayList(SelfBlocker.mc.world.loadedEntityList)) {
            if (entity == null || entity instanceof EntityXPOrb || entity instanceof EntityItem || entity instanceof EntityEnderCrystal || !entity.getEntityBoundingBox().intersects(new AxisAlignedBB(pos))) continue;
            return true;
        }
        return false;
    }

    private BlockPos getPlayerPos() {
        double decimalPoint = SelfBlocker.mc.player.posY - Math.floor(SelfBlocker.mc.player.posY);
        return new BlockPos(SelfBlocker.mc.player.posX, decimalPoint > 0.8 ? Math.floor(SelfBlocker.mc.player.posY) + 1.0 : Math.floor(SelfBlocker.mc.player.posY), SelfBlocker.mc.player.posZ);
    }
}

