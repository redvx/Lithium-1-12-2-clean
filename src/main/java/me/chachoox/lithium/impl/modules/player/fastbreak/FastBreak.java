/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockAir
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.inventory.Slot
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketAnimation
 *  net.minecraft.network.play.client.CPacketClickWindow
 *  net.minecraft.network.play.client.CPacketPlayer$Rotation
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.RayTraceResult
 */
package me.chachoox.lithium.impl.modules.player.fastbreak;

import java.util.ArrayList;
import java.util.Collection;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.property.list.BlockList;
import me.chachoox.lithium.api.util.blocks.BlockUtil;
import me.chachoox.lithium.api.util.blocks.MineUtil;
import me.chachoox.lithium.api.util.entity.EntityUtil;
import me.chachoox.lithium.api.util.inventory.ItemUtil;
import me.chachoox.lithium.api.util.inventory.Swap;
import me.chachoox.lithium.api.util.inventory.SwitchUtil;
import me.chachoox.lithium.api.util.list.ListEnum;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.api.util.math.MathUtil;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.api.util.movement.PositionUtil;
import me.chachoox.lithium.api.util.network.NetworkUtil;
import me.chachoox.lithium.api.util.network.PacketUtil;
import me.chachoox.lithium.api.util.rotation.RotationUtil;
import me.chachoox.lithium.api.util.rotation.RotationsEnum;
import me.chachoox.lithium.api.util.rotation.raytrace.RaytraceUtil;
import me.chachoox.lithium.asm.ducks.IPlayerControllerMP;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.player.fastbreak.ListenerBlockChange;
import me.chachoox.lithium.impl.modules.player.fastbreak.ListenerClickBlock;
import me.chachoox.lithium.impl.modules.player.fastbreak.ListenerDamage;
import me.chachoox.lithium.impl.modules.player.fastbreak.ListenerDigging;
import me.chachoox.lithium.impl.modules.player.fastbreak.ListenerLogout;
import me.chachoox.lithium.impl.modules.player.fastbreak.ListenerMotion;
import me.chachoox.lithium.impl.modules.player.fastbreak.ListenerRender;
import me.chachoox.lithium.impl.modules.player.fastbreak.ListenerSpawnObject;
import me.chachoox.lithium.impl.modules.player.fastbreak.ListenerSwap;
import me.chachoox.lithium.impl.modules.player.fastbreak.ListenerUpdate;
import me.chachoox.lithium.impl.modules.player.fastbreak.mode.MineMode;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

public class FastBreak
extends Module {
    protected final EnumProperty<ListEnum> whitelist = new EnumProperty<ListEnum>(ListEnum.ANY, ListEnum.SELECTION_ALIAS, "Any: - Disregards all added items/blocks, always active / Whitelist: - Ignores added items/blocks / Blacklist: - Only uses added items/blocks");
    protected final EnumProperty<RotationsEnum> rotation = new EnumProperty<RotationsEnum>(RotationsEnum.NONE, RotationsEnum.ALIASES, "None - Does not rotate / Normal - Rotates normally by changing yaw & pitch / Packet - Uses packets to rotate, could get you kicked for too many packets.");
    protected final EnumProperty<MineMode> mode = new EnumProperty<MineMode>(MineMode.PACKET, new String[]{"Mode", "instantmine", "packetmine", "type", "method"}, "Packet: - Breaks blocks the same as normal mining / Instant: - Breaks the first block normally then instantly rebreaks that position until we select another.");
    protected final EnumProperty<Swap> swap = new EnumProperty<Swap>(Swap.NONE, new String[]{"Swap", "Switch", "swit"}, "Silent: - Uses just a packet to switch / Alternative: - Uses a different packet to switch using area 51 technology.");
    protected final Property<Boolean> fast = new Property<Boolean>(false, new String[]{"Fast", "fas", "fastrebreak"}, "Calculates the block damage even if its air so we instantly rebreak it without getting flagged.");
    protected final Property<Boolean> strict = new Property<Boolean>(false, new String[]{"Strict", "bypass"}, "Wont mine blocks unless we are on the ground, also a different block damage calculator.");
    protected final Property<Boolean> auto = new Property<Boolean>(true, new String[]{"AutoBreak", "Auto", "A"}, "Automatically breaks the block instead of whenever we click it.");
    protected final NumberProperty<Float> range = new NumberProperty<Float>(Float.valueOf(4.5f), Float.valueOf(0.1f), Float.valueOf(6.0f), Float.valueOf(0.1f), new String[]{"Range", "Distance", "rang"}, "How far we have to be from the block to cancel breaking it.");
    protected final NumberProperty<Float> lineWidth = new NumberProperty<Float>(Float.valueOf(1.4f), Float.valueOf(1.0f), Float.valueOf(4.0f), Float.valueOf(0.1f), new String[]{"LineWidth", "WireWidth", "ww", "lw"}, "Thickness of the outline.");
    protected final NumberProperty<Integer> alpha = new NumberProperty<Integer>(50, 0, 255, new String[]{"BoxAlpha", "boxa", "alpha"}, "Opacity of the box.");
    protected final NumberProperty<Integer> lineAlpha = new NumberProperty<Integer>(125, 0, 255, new String[]{"LineAlpha", "linea", "linalpha"}, "Opacity of the outline.");
    protected final Property<Boolean> debug = new Property<Boolean>(false, new String[]{"Debug", "debugger"}, "dgub.");
    protected final BlockList blockList = new BlockList(ListEnum.BLOCKS_LIST_ALIAS);
    protected final StopWatch timer = new StopWatch();
    protected final StopWatch rotationTimer = new StopWatch();
    protected final StopWatch retryTimer = new StopWatch();
    protected final StopWatch crystalTimer = new StopWatch();
    protected final StopWatch pingTimer = new StopWatch();
    protected final float[] damages = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
    protected float maxDamage;
    protected int retries;
    protected int clicks;
    protected boolean shouldAbort;
    protected boolean swapped;
    protected boolean canBreak;
    protected boolean executed;
    protected BlockPos pos;
    protected IBlockState state;
    protected EnumFacing direction;
    protected BlockPos crystalPos;
    protected boolean crystalSwapped;
    protected boolean crystalAttack;
    protected int crystalRetries;
    protected int crystalID;

    public FastBreak() {
        super("FastBreak", new String[]{"FastBreak", "SpeedyGonzales", "SpeedMine", "FastDestroy", "FastMine", "SpeedDestroy", "SpeedBreak"}, "Destroys blocks faster.", Category.PLAYER);
        this.offerProperties(this.whitelist, this.rotation, this.mode, this.swap, this.fast, this.strict, this.auto, this.range, this.lineAlpha, this.lineWidth, this.alpha, this.blockList, this.debug);
        this.offerListeners(new ListenerDamage(this), new ListenerRender(this), new ListenerSwap(this), new ListenerUpdate(this), new ListenerClickBlock(this), new ListenerMotion(this), new ListenerBlockChange(this), new ListenerLogout(this), new ListenerDigging(this), new ListenerSpawnObject(this));
    }

    @Override
    public void onEnable() {
        this.reset();
    }

    @Override
    public void onDisable() {
        this.reset();
    }

    @Override
    public String getSuffix() {
        return "" + MathUtil.round(this.maxDamage, 1);
    }

    public BlockPos getPos() {
        return this.pos;
    }

    protected Block getBlock() {
        return FastBreak.mc.world.getBlockState(this.pos).getBlock();
    }

    protected void abortCurrentPos() {
        PacketUtil.swing();
        PacketUtil.send(new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.pos, this.direction));
        PacketUtil.swing();
        ((IPlayerControllerMP)FastBreak.mc.playerController).setIsHittingBlock(false);
        ((IPlayerControllerMP)FastBreak.mc.playerController).setCurBlockDamageMP(0.0f);
        FastBreak.mc.world.sendBlockBreakProgress(FastBreak.mc.player.getEntityId(), this.pos, -1);
        this.reset();
    }

    public void sendPackets() {
        ArrayList<Object> packets = new ArrayList<Object>();
        CPacketAnimation animation = new CPacketAnimation(EnumHand.MAIN_HAND);
        packets.add(animation);
        packets.add(animation);
        packets.add(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.pos, this.direction));
        packets.add(animation);
        packets.add(animation);
        packets.add(new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.pos, this.direction));
        packets.add(animation);
        packets.add(animation);
        packets.add(animation);
        packets.add(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, this.pos, this.direction));
        packets.add(animation);
        if (this.fast.getValue().booleanValue()) {
            packets.add(animation);
            packets.add(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.pos, this.direction));
            packets.add(animation);
        }
        for (Packet packet : packets) {
            PacketUtil.send(packet);
        }
    }

    protected void reset() {
        this.pos = null;
        this.crystalPos = null;
        this.direction = null;
        this.maxDamage = 0.0f;
        this.retries = 0;
        this.clicks = 0;
        this.canBreak = false;
        this.executed = false;
        this.swapped = false;
        this.crystalSwapped = false;
        this.crystalAttack = false;
        this.crystalID = -1;
        this.crystalRetries = 0;
        for (int i = 0; i < 9; ++i) {
            this.damages[i] = 0.0f;
        }
    }

    protected void checkRetry() {
        switch ((MineMode)((Object)this.mode.getValue())) {
            case PACKET: {
                this.softReset(true);
                this.clicks = 0;
                break;
            }
            case INSTANT: {
                if (this.retries >= 3) {
                    this.softReset(true);
                    this.retries = 0;
                }
                this.retryTimer.reset();
                ++this.retries;
            }
        }
        this.executed = false;
        this.resetSwap();
    }

    protected void softReset(boolean full) {
        if (this.debug.getValue().booleanValue()) {
            Logger.getLogger().log("\u00a7bSoft reset", false);
        }
        if (full) {
            PacketUtil.send(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, this.pos, this.direction));
        }
        this.maxDamage = 0.0f;
        for (int i = 0; i < 9; ++i) {
            this.damages[i] = 0.0f;
        }
        this.retries = 0;
        this.executed = false;
    }

    protected void updateDamages() {
        this.maxDamage = 0.0f;
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = FastBreak.mc.player.inventory.getStackInSlot(i);
            float damage = this.strict.getValue() != false && this.getBlock() == Blocks.AIR && this.state != null ? 0.0f : (this.getBlock() == Blocks.AIR && this.state != null ? MineUtil.getDamage(this.state, stack, FastBreak.mc.player.onGround) : MineUtil.getDamage(stack, this.pos, FastBreak.mc.player.onGround));
            this.damages[i] = MathUtil.clamp(this.damages[i] + (damage *= Managers.TPS.getFactor()), 0.0f, 1.0f);
            if (!(this.damages[i] > this.maxDamage)) continue;
            this.maxDamage = this.damages[i];
        }
    }

    protected void tryBreak(int pickSlot) {
        BlockPos pos;
        int lastSlot = FastBreak.mc.player.inventory.currentItem;
        float[] rotations = RotationUtil.getRotations(this.pos);
        EntityPlayer target = this.getPlacePlayer(this.pos);
        if (target != null && (pos = this.getCrystalPos(this.pos)) != null && FastBreak.mc.player.getDistanceSq(pos) <= (double)MathUtil.square(((Float)this.range.getValue()).floatValue()) && BlockUtil.canPlaceCrystal(pos, false)) {
            RayTraceResult result = RaytraceUtil.getRayTraceResult(rotations[0], rotations[1]);
            if (FastBreak.mc.player.getHeldItemOffhand() != ItemStack.EMPTY && FastBreak.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL && !this.crystalSwapped) {
                this.crystalAttack = true;
                ItemUtil.syncItem();
                PacketUtil.send(new CPacketPlayerTryUseItemOnBlock(pos, result.sideHit, EnumHand.OFF_HAND, (float)result.hitVec.x, (float)result.hitVec.y, (float)result.hitVec.z));
                PacketUtil.send(new CPacketAnimation(EnumHand.OFF_HAND));
                this.crystalSwapped = true;
            } else {
                int crystalSlot = ItemUtil.findHotbarItem(Items.END_CRYSTAL);
                if (crystalSlot != -1 && !this.crystalSwapped) {
                    this.crystalAttack = true;
                    ItemStack oldItemC = FastBreak.mc.player.getHeldItemMainhand();
                    SwitchUtil.doSwitch((Swap)((Object)this.swap.getValue()), crystalSlot);
                    ItemStack newItemC = FastBreak.mc.player.getHeldItemMainhand();
                    PacketUtil.send(new CPacketPlayerTryUseItemOnBlock(pos, result.sideHit, EnumHand.MAIN_HAND, (float)result.hitVec.x, (float)result.hitVec.y, (float)result.hitVec.z));
                    PacketUtil.swing();
                    if (this.swap.getValue() == Swap.ALTERNATIVE) {
                        if (lastSlot != pickSlot) {
                            SwitchUtil.doSwitch((Swap)((Object)this.swap.getValue()), crystalSlot);
                        } else {
                            short id = FastBreak.mc.player.openContainer.getNextTransactionID(FastBreak.mc.player.inventory);
                            ItemStack fakeStackC = new ItemStack(Items.END_CRYSTAL, 64);
                            int slotC = ItemUtil.hotbarToInventory(crystalSlot);
                            int oldSlotC = ItemUtil.hotbarToInventory(lastSlot);
                            Slot currentSlotC = (Slot)FastBreak.mc.player.inventoryContainer.inventorySlots.get(oldSlotC);
                            Slot swapSlotC = (Slot)FastBreak.mc.player.inventoryContainer.inventorySlots.get(slotC);
                            PacketUtil.send(new CPacketClickWindow(0, slotC, FastBreak.mc.player.inventory.currentItem, ClickType.SWAP, fakeStackC, id));
                            currentSlotC.putStack(oldItemC);
                            swapSlotC.putStack(newItemC);
                        }
                    } else if (lastSlot == pickSlot) {
                        SwitchUtil.doSwitch((Swap)((Object)this.swap.getValue()), lastSlot);
                    }
                    this.crystalSwapped = true;
                }
            }
        }
        if (this.swap.getValue() != Swap.NONE && lastSlot != pickSlot && !this.swapped || lastSlot == pickSlot && !this.swapped) {
            if (this.getBlock() != Blocks.AIR) {
                if (this.mode.getValue() == MineMode.PACKET) {
                    this.rotationTimer.reset();
                }
                if (this.rotation.getValue() == RotationsEnum.PACKET) {
                    FastBreak.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(rotations[0], rotations[1], FastBreak.mc.player.onGround));
                }
                ItemStack oldItem = FastBreak.mc.player.getHeldItemMainhand();
                PacketUtil.swing();
                SwitchUtil.doSwitch((Swap)((Object)this.swap.getValue()), pickSlot);
                ItemStack newItem = FastBreak.mc.player.getHeldItemMainhand();
                PacketUtil.swing();
                PacketUtil.send(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.pos, this.direction));
                PacketUtil.swing();
                PacketUtil.swing();
                PacketUtil.send(new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.pos, this.direction));
                PacketUtil.swing();
                PacketUtil.swing();
                if (this.mode.getValue() == MineMode.PACKET) {
                    PacketUtil.swing();
                    PacketUtil.send(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, this.pos, this.direction));
                    PacketUtil.swing();
                    if (this.fast.getValue().booleanValue()) {
                        PacketUtil.swing();
                        PacketUtil.send(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.pos, this.direction));
                        PacketUtil.swing();
                    }
                }
                if (this.swap.getValue() != Swap.ALTERNATIVE) {
                    SwitchUtil.doSwitch((Swap)((Object)this.swap.getValue()), lastSlot);
                } else {
                    short id = FastBreak.mc.player.openContainer.getNextTransactionID(FastBreak.mc.player.inventory);
                    ItemStack fakeStack = new ItemStack(Items.END_CRYSTAL, 64);
                    int slot = ItemUtil.hotbarToInventory(pickSlot);
                    int oldSlot = ItemUtil.hotbarToInventory(lastSlot);
                    Slot currentSlot = (Slot)FastBreak.mc.player.inventoryContainer.inventorySlots.get(oldSlot);
                    Slot swapSlot = (Slot)FastBreak.mc.player.inventoryContainer.inventorySlots.get(slot);
                    PacketUtil.send(new CPacketClickWindow(0, slot, FastBreak.mc.player.inventory.currentItem, ClickType.SWAP, fakeStack, id));
                    currentSlot.putStack(oldItem);
                    swapSlot.putStack(newItem);
                }
                this.swapped = true;
            }
            if (this.mode.getValue() == MineMode.PACKET) {
                this.softReset(false);
            }
        }
    }

    private EntityPlayer getPlacePlayer(BlockPos pos) {
        for (EntityPlayer player : FastBreak.mc.world.playerEntities) {
            if (Managers.FRIEND.isFriend(player) || player == FastBreak.mc.player || EntityUtil.isOnBurrow(player) || EntityUtil.isDead((Entity)player)) continue;
            BlockPos playerPos = PositionUtil.getPosition((Entity)player);
            for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                if (!playerPos.offset(facing).equals((Object)pos) && !playerPos.up().offset(facing).equals((Object)pos)) continue;
                return player;
            }
            if (!playerPos.offset(EnumFacing.UP).offset(EnumFacing.UP).equals((Object)pos)) continue;
            return player;
        }
        return null;
    }

    private BlockPos getCrystalPos(BlockPos pos) {
        Block block = FastBreak.mc.world.getBlockState(pos).getBlock();
        if (block == Blocks.OBSIDIAN) {
            return pos;
        }
        return null;
    }

    protected void resetSwap() {
        this.swapped = false;
        this.crystalSwapped = false;
    }

    protected int getPingDelay() {
        if (FastBreak.mc.player != null) {
            if (NetworkUtil.getLatencyNoSpoof() < 25) {
                return 150;
            }
            if (NetworkUtil.getLatencyNoSpoof() < 50) {
                return 75;
            }
            if (NetworkUtil.getLatencyNoSpoof() < 100) {
                return 25;
            }
            if (NetworkUtil.getLatencyNoSpoof() < 200) {
                return 0;
            }
        }
        return 25;
    }

    public boolean isBlockValid(Block block) {
        if (block == null) {
            return false;
        }
        if (block instanceof BlockAir) {
            return true;
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

