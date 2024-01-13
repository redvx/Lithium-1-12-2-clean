/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.BlockPos
 */
package me.chachoox.lithium.impl.modules.player.scaffold;

import java.util.Collection;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.property.list.BlockList;
import me.chachoox.lithium.api.util.list.ListEnum;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.api.util.rotation.RotationsEnum;
import me.chachoox.lithium.impl.modules.other.blocks.util.SwingEnum;
import me.chachoox.lithium.impl.modules.player.scaffold.ListenerMotion;
import me.chachoox.lithium.impl.modules.player.scaffold.ListenerMove;
import me.chachoox.lithium.impl.modules.player.scaffold.ListenerRender;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class Scaffold
extends Module {
    protected final EnumProperty<ListEnum> whitelist = new EnumProperty<ListEnum>(ListEnum.ANY, ListEnum.SELECTION_ALIAS, "Any: - Disregards all added items/blocks, always active / Whitelist: - Ignores added items/blocks / Blacklist: - Only uses added items/blocks");
    protected final EnumProperty<SwingEnum> swing = new EnumProperty<SwingEnum>(SwingEnum.NORMAL, SwingEnum.ALIASES, "None: - Wont swing / Packet: - Swings server side / Normal: Swings client side and server side.");
    protected final EnumProperty<RotationsEnum> rotation = new EnumProperty<RotationsEnum>(RotationsEnum.NONE, RotationsEnum.ALIASES, "None - Does not rotate / Normal - Rotates normally by changing yaw & pitch / Packet - Uses packets to rotate, could get you kicked for too many packets.");
    protected final Property<Boolean> tower = new Property<Boolean>(true, new String[]{"Tower", "tow", "t"}, "Gives you higher y motion so we go upwards faster.");
    protected final Property<Boolean> down = new Property<Boolean>(false, new String[]{"Downwards", "down", "d"}, "Creates a downwards staircase whenever we are sneaking.");
    protected final NumberProperty<Integer> delay = new NumberProperty<Integer>(80, 0, 500, new String[]{"PlaceDelay", "Delay", "del"}, "Delay between placing another block.");
    protected final BlockList blockList = new BlockList(ListEnum.BLOCKS_LIST_ALIAS);
    protected final StopWatch rotationTimer = new StopWatch();
    protected final StopWatch towerTimer = new StopWatch();
    protected final StopWatch clickTimer = new StopWatch();
    protected final StopWatch placeTimer = new StopWatch();
    protected float[] rotations;
    protected EnumFacing facing;
    protected BlockPos pos;
    protected BlockPos rot;
    protected boolean sneak;

    public Scaffold() {
        super("Scaffold", new String[]{"Scaffold", "Tower"}, "Automatically places blocks under you.", Category.PLAYER);
        this.offerListeners(new ListenerMotion(this), new ListenerMove(this), new ListenerRender(this));
        this.offerProperties(this.whitelist, this.swing, this.rotation, this.tower, this.down, this.delay, this.blockList);
    }

    @Override
    public void onEnable() {
        this.towerTimer.reset();
        this.pos = null;
        this.facing = null;
        this.rot = null;
    }

    protected BlockPos findNextPos() {
        BlockPos leftPos;
        BlockPos backPos;
        BlockPos underPos = new BlockPos((Entity)Scaffold.mc.player).down();
        boolean under = false;
        if (this.down.getValue().booleanValue() && !Scaffold.mc.gameSettings.keyBindJump.isKeyDown() && Scaffold.mc.gameSettings.keyBindSneak.isKeyDown()) {
            under = true;
            underPos = underPos.down();
        }
        if (Scaffold.mc.world.getBlockState(underPos).getMaterial().isReplaceable() && (!under || Scaffold.mc.world.getBlockState(underPos.up()).getMaterial().isReplaceable())) {
            return underPos;
        }
        if (Scaffold.mc.gameSettings.keyBindForward.isKeyDown() && !Scaffold.mc.gameSettings.keyBindBack.isKeyDown()) {
            BlockPos forwardPos = underPos.offset(Scaffold.mc.player.getHorizontalFacing());
            if (Scaffold.mc.world.getBlockState(forwardPos).getMaterial().isReplaceable()) {
                return forwardPos;
            }
        } else if (Scaffold.mc.gameSettings.keyBindBack.isKeyDown() && !Scaffold.mc.gameSettings.keyBindForward.isKeyDown() && Scaffold.mc.world.getBlockState(backPos = underPos.offset(Scaffold.mc.player.getHorizontalFacing().getOpposite())).getMaterial().isReplaceable()) {
            return backPos;
        }
        if (Scaffold.mc.gameSettings.keyBindRight.isKeyDown() && !Scaffold.mc.gameSettings.keyBindLeft.isKeyDown()) {
            BlockPos rightPos = underPos.offset(Scaffold.mc.player.getHorizontalFacing().rotateY());
            if (Scaffold.mc.world.getBlockState(rightPos).getMaterial().isReplaceable()) {
                return rightPos;
            }
        } else if (Scaffold.mc.gameSettings.keyBindLeft.isKeyDown() && !Scaffold.mc.gameSettings.keyBindRight.isKeyDown() && Scaffold.mc.world.getBlockState(leftPos = underPos.offset(Scaffold.mc.player.getHorizontalFacing().rotateYCCW())).getMaterial().isReplaceable()) {
            return leftPos;
        }
        return null;
    }

    protected boolean isValid(Block block) {
        if (block == null) {
            return false;
        }
        if (this.whitelist.getValue() == ListEnum.ANY) {
            return true;
        }
        if (this.whitelist.getValue() == ListEnum.WHITELIST) {
            return this.getList().contains(block);
        }
        return !this.getList().contains(block);
    }

    public Collection<Block> getList() {
        return (Collection)this.blockList.getValue();
    }
}

