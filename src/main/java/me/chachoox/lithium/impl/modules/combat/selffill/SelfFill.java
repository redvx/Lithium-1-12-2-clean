/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.math.BlockPos
 */
package me.chachoox.lithium.impl.modules.combat.selffill;

import java.util.Collection;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.property.list.BlockList;
import me.chachoox.lithium.api.util.list.ListEnum;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.api.util.movement.PositionUtil;
import me.chachoox.lithium.api.util.rotation.RotationsEnum;
import me.chachoox.lithium.impl.modules.combat.selffill.ListenerMotion;
import me.chachoox.lithium.impl.modules.combat.selffill.ListenerRender;
import me.chachoox.lithium.impl.modules.other.blocks.util.SwingEnum;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class SelfFill
extends Module {
    protected final EnumProperty<ListEnum> whitelist = new EnumProperty<ListEnum>(ListEnum.ANY, ListEnum.SELECTION_ALIAS, "Any: - Disregards all added items/blocks, always active / Whitelist: - Ignores added items/blocks / Blacklist: - Only uses added items/blocks");
    protected final EnumProperty<SwingEnum> swing = new EnumProperty<SwingEnum>(SwingEnum.NORMAL, SwingEnum.ALIASES, "None: - Wont swing / Packet: - Swings server side / Normal: Swings client side and server side.");
    protected final Property<Boolean> rotate = new Property<Boolean>(false, RotationsEnum.ALIASES, "Looks at the floor when self filling.");
    protected final Property<Boolean> altSwap = new Property<Boolean>(false, new String[]{"AltSwap", "AlternativeSwap", "CooldownBypass"}, "Uses an alternative type of swap that bypass some anticheats (2b2tpvp main)");
    protected final Property<Boolean> strict = new Property<Boolean>(false, new String[]{"Strict", "betterplace", "strictplacement"}, "Stricter placements.");
    protected final Property<Boolean> wait = new Property<Boolean>(false, new String[]{"Wait", "NoDisable"}, "Waits to self fill again instead of disabling the module.");
    protected final Property<Boolean> noVoid = new Property<Boolean>(false, new String[]{"NoVoid", "AntiVoid"}, "Wont self fill if the pos is below 0.");
    protected final NumberProperty<Integer> delay = new NumberProperty<Integer>(100, 0, 500, new String[]{"Delay", "placeDelay", "Delay/place"}, "Delay between placing blocks.");
    protected final Property<Boolean> attack = new Property<Boolean>(false, new String[]{"Attack", "Destroy", "DestroyLonely"}, "Attacks crystals that are in the way.");
    protected final NumberProperty<Integer> attackDelay = new NumberProperty<Integer>(100, 0, 500, new String[]{"AttackDelay", "crystalAttackDelay", "ad"}, "Delay between attacking crystals.");
    protected final BlockList blockList = new BlockList(ListEnum.BLOCKS_LIST_ALIAS);
    protected final StopWatch breakTimer = new StopWatch();
    protected final StopWatch confirmTimer = new StopWatch();
    protected final StopWatch timer = new StopWatch();
    protected BlockPos startPos;

    public SelfFill() {
        super("SelfFill", new String[]{"Selffill", "burrow", "blocklag", "nonghostblockscaffold", "feetscaffold"}, "Places blocks inside you", Category.COMBAT);
        this.offerProperties(this.whitelist, this.swing, this.rotate, this.altSwap, this.strict, this.wait, this.noVoid, this.delay, this.attack, this.attackDelay, this.blockList);
        this.offerListeners(new ListenerMotion(this), new ListenerRender(this));
        this.initializeBlocks();
    }

    @Override
    public void onEnable() {
        this.timer.setTime(0L);
        this.breakTimer.setTime(0L);
        if (this.isNull()) {
            return;
        }
        this.startPos = PositionUtil.getPosition();
    }

    @Override
    public void onDisable() {
        this.startPos = null;
    }

    protected double getY(Entity entity) {
        double d = this.getY(entity, 2.1, 10.0, true);
        if (Double.isNaN(d) && Double.isNaN(d = this.getY(entity, -3.0, -10.0, false))) {
            return SelfFill.mc.player.posY + 1.242610501394747;
        }
        return d;
    }

    protected double getY(Entity entity, double min, double max, boolean add) {
        if (min > max && add || max > min && !add) {
            return Double.NaN;
        }
        double x = entity.posX;
        double y = entity.posY;
        double z = entity.posZ;
        boolean air = false;
        BlockPos last = null;
        double off = min;
        while (add ? off < max : off > max) {
            BlockPos pos = new BlockPos(x, y - off, z);
            if (!this.noVoid.getValue().booleanValue() || pos.getY() >= 0) {
                IBlockState state = SelfFill.mc.world.getBlockState(pos);
                if (!state.getMaterial().blocksMovement() || state.getBlock() == Blocks.AIR) {
                    if (air) {
                        if (add) {
                            return pos.getY();
                        }
                        return last.getY();
                    }
                    air = true;
                } else {
                    air = false;
                }
                last = pos;
            }
            off = add ? (off = off + 1.0) : (off = off - 1.0);
        }
        return Double.NaN;
    }

    protected boolean isInsideBlock() {
        double x = SelfFill.mc.player.posX;
        double y = SelfFill.mc.player.posY + 0.2;
        double z = SelfFill.mc.player.posZ;
        return SelfFill.mc.world.getBlockState(new BlockPos(x, y, z)).getMaterial().blocksMovement() || !SelfFill.mc.player.collidedVertically;
    }

    private void initializeBlocks() {
        if (!this.getList().contains(Blocks.OBSIDIAN)) {
            this.getList().add(Blocks.OBSIDIAN);
        }
        if (!this.getList().contains(Blocks.ENDER_CHEST)) {
            this.getList().add(Blocks.ENDER_CHEST);
        }
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

