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
package me.chachoox.lithium.impl.modules.player.autofeetplace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import me.chachoox.lithium.api.module.BlockPlaceModule;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.blocks.BlockUtil;
import me.chachoox.lithium.impl.modules.player.autofeetplace.ListenerBlockChange;
import me.chachoox.lithium.impl.modules.player.autofeetplace.ListenerMotion;
import me.chachoox.lithium.impl.modules.player.autofeetplace.ListenerMutliBlockChange;
import me.chachoox.lithium.impl.modules.player.autofeetplace.ListenerRender;
import me.chachoox.lithium.impl.modules.player.autofeetplace.ListenerSound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class AutoFeetPlace
extends BlockPlaceModule {
    protected final Property<Boolean> jumpDisable = new Property<Boolean>(true, new String[]{"JumpDisable", "AutoDisable", "NoJump"}, "Disables whenever we jumped so we dont spam obsidian.");

    public AutoFeetPlace() {
        super("AutoFeetPlace", new String[]{"AutoFeetPlace", "Surround", "AutoObsidian", "NoCrystals", "FeetTrap"}, "Traps your feet with obsidian.", Category.PLAYER);
        this.offerListeners(new ListenerMotion(this), new ListenerRender(this), new ListenerBlockChange(this), new ListenerMutliBlockChange(this), new ListenerSound(this));
        this.offerProperties(this.jumpDisable);
    }

    protected List<BlockPos> getPlacements() {
        return this.getPlacements(this.getPlayerPos());
    }

    private List<BlockPos> getPlacements(BlockPos pos) {
        ArrayList<BlockPos> placements = new ArrayList<BlockPos>();
        HashSet<EnumFacing> directions = new HashSet<EnumFacing>();
        for (EnumFacing facing : EnumFacing.VALUES) {
            if (facing.equals((Object)EnumFacing.UP)) continue;
            BlockPos offset = pos.offset(facing);
            if (this.isEntityIntersecting(offset)) {
                for (EnumFacing direction : EnumFacing.VALUES) {
                    BlockPos extend;
                    if (direction.equals((Object)EnumFacing.UP) || (extend = offset.offset(direction)).equals((Object)pos) || !BlockUtil.isReplaceable(extend)) continue;
                    if (!this.isEntityIntersecting(extend)) {
                        placements.add(extend);
                        continue;
                    }
                    directions.add(direction);
                }
                if (directions.size() <= 1) continue;
                BlockPos supportPos = pos;
                for (EnumFacing direction : directions) {
                    supportPos = supportPos.offset(direction);
                }
                for (EnumFacing direction : EnumFacing.VALUES) {
                    if (direction.equals((Object)EnumFacing.UP) || this.isEntityIntersecting(supportPos.offset(direction))) continue;
                    placements.add(supportPos.offset(direction));
                }
                continue;
            }
            placements.add(offset);
        }
        for (BlockPos position : new ArrayList(placements)) {
            boolean support = true;
            for (EnumFacing direction : EnumFacing.VALUES) {
                BlockPos offsetPosition = position.offset(direction);
                if (AutoFeetPlace.mc.world.isAirBlock(offsetPosition)) continue;
                support = false;
                break;
            }
            if (!support) continue;
            placements.add(position.down());
        }
        Collections.reverse(placements);
        return placements;
    }

    private boolean isEntityIntersecting(BlockPos pos) {
        for (Entity entity : new ArrayList(AutoFeetPlace.mc.world.loadedEntityList)) {
            if (entity == null || entity instanceof EntityXPOrb || entity instanceof EntityItem || entity instanceof EntityEnderCrystal || !entity.getEntityBoundingBox().intersects(new AxisAlignedBB(pos))) continue;
            return true;
        }
        return false;
    }

    private BlockPos getPlayerPos() {
        double decimalPoint = AutoFeetPlace.mc.player.posY - Math.floor(AutoFeetPlace.mc.player.posY);
        return new BlockPos(AutoFeetPlace.mc.player.posX, decimalPoint > 0.8 ? Math.floor(AutoFeetPlace.mc.player.posY) + 1.0 : Math.floor(AutoFeetPlace.mc.player.posY), AutoFeetPlace.mc.player.posZ);
    }
}

