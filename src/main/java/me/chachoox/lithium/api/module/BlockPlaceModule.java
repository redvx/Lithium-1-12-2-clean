/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockAir
 *  net.minecraft.block.BlockDeadBush
 *  net.minecraft.block.BlockFire
 *  net.minecraft.block.BlockLiquid
 *  net.minecraft.block.BlockSnow
 *  net.minecraft.block.BlockTallGrass
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.item.EntityXPOrb
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.projectile.EntityArrow
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.inventory.Slot
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketAnimation
 *  net.minecraft.network.play.client.CPacketClickWindow
 *  net.minecraft.network.play.client.CPacketPlayer$Rotation
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.RayTraceResult$Type
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 */
package me.chachoox.lithium.api.module;

import java.util.ArrayList;
import java.util.List;
import me.chachoox.lithium.api.event.bus.Listener;
import me.chachoox.lithium.api.event.events.Stage;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.blocks.BlockUtil;
import me.chachoox.lithium.api.util.entity.DamageUtil;
import me.chachoox.lithium.api.util.entity.EntityUtil;
import me.chachoox.lithium.api.util.inventory.ItemUtil;
import me.chachoox.lithium.api.util.math.MathUtil;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.api.util.network.PacketUtil;
import me.chachoox.lithium.api.util.rotation.RotationUtil;
import me.chachoox.lithium.api.util.rotation.raytrace.RaytraceUtil;
import me.chachoox.lithium.impl.event.events.entity.DeathEvent;
import me.chachoox.lithium.impl.event.events.movement.MotionUpdateEvent;
import me.chachoox.lithium.impl.event.events.network.DisconnectEvent;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.combat.holefill.HoleFill;
import me.chachoox.lithium.impl.modules.combat.instantweb.InstantWeb;
import me.chachoox.lithium.impl.modules.other.blocks.BlocksManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockDeadBush;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class BlockPlaceModule
extends Module {
    protected final Property<Boolean> rotation = new Property<Boolean>(false, new String[]{"Rotation", "rotate", "rots"}, "Rotates to look at the block being placed.");
    protected final Property<Boolean> swing = new Property<Boolean>(false, new String[]{"Swing", "swingarm"}, "Swings your arm when placing blocks.");
    protected final NumberProperty<Integer> delay = new NumberProperty<Integer>(3, 0, 10, new String[]{"Delay", "Interval"}, "Delay between placing blocks.");
    protected final NumberProperty<Integer> blocks = new NumberProperty<Integer>(2, 1, 10, new String[]{"Blocks", "BlocksPerTick", "Blocks/Tick"}, "The amount of blocks we place per tick.");
    protected final Property<Boolean> strict = new Property<Boolean>(false, new String[]{"StrictDirection", "strict"}, "Wont place blocks through walls.");
    protected final NumberProperty<Float> strictRange = new NumberProperty<Float>(Float.valueOf(3.0f), Float.valueOf(1.0f), Float.valueOf(5.0f), Float.valueOf(0.1f), new String[]{"StrictRange", "wallrange"}, "How far will place blocks through walls.");
    protected final Property<Boolean> altSwap = new Property<Boolean>(false, new String[]{"AltSwap", "AlternativeSwap", "CooldownBypass"}, "Uses an alternative type of swap that bypass some anticheats (2b2tpvp main)");
    protected final Property<Boolean> preferWebs = new Property<Boolean>(false, new String[]{"OnlyWebs", "PreferWebs", "UseWebs"}, "Prefers webs when filling holes.");
    private final List<Packet<?>> packets = new ArrayList();
    private final StopWatch timer = new StopWatch();
    private float[] rotations;
    public double enablePosY;
    private int blocksPlaced = 0;
    private int slot = -1;

    public BlockPlaceModule(String label, String[] aliases, String description, Category category) {
        super(label, aliases, description, category);
        this.offerProperties(this.rotation, this.swing, this.delay, this.blocks, this.strict, this.strictRange);
        if (this instanceof HoleFill) {
            this.offerProperties(this.altSwap, this.preferWebs);
        }
        this.offerListeners(this.listenerDeath(this), this.listenerDisconnect(this));
    }

    @Override
    public String getSuffix() {
        return "" + this.blocksPlaced;
    }

    @Override
    public void onEnable() {
        this.packets.clear();
        this.blocksPlaced = 0;
        this.clear();
        if (this.isNull()) {
            this.disable();
            return;
        }
        this.enablePosY = BlockPlaceModule.mc.player.posY;
    }

    public void clear() {
    }

    public void onPreEvent(List<BlockPos> blocks, MotionUpdateEvent event) {
        if (event.getStage() == Stage.PRE) {
            this.blocksPlaced = 0;
            this.placeBlocks(blocks);
            if (this.rotations != null) {
                Managers.ROTATION.setRotations(this.rotations[0], this.rotations[1]);
                this.rotations = null;
            }
            this.execute();
        }
    }

    public void placeBlocks(List<BlockPos> blockList) {
        if (blockList == null || blockList.isEmpty()) {
            return;
        }
        for (BlockPos pos : blockList) {
            this.placeBlock(pos);
        }
    }

    public void placeBlock(BlockPos pos) {
        this.getSlot(BlocksManager.get().placeEnderChests(this));
        if (this.slot == -1) {
            return;
        }
        if (!this.timer.passed((Integer)this.delay.getValue() * 50)) {
            return;
        }
        EnumFacing facing = BlockUtil.getFacing(pos);
        if (facing == null) {
            return;
        }
        if (this.blocksPlaced >= (Integer)this.blocks.getValue()) {
            return;
        }
        if (BlockPlaceModule.mc.world.getBlockState(pos.offset(facing)).getMaterial().isReplaceable()) {
            return;
        }
        if (this.crystalCheck(pos) && !(this instanceof HoleFill) && !(this instanceof InstantWeb)) {
            this.timer.reset();
            return;
        }
        if (this.canPlaceBlock(pos, this.strict.getValue())) {
            this.placeBlock(pos.offset(facing), facing.getOpposite());
        }
    }

    private void placeBlock(BlockPos pos, EnumFacing facing) {
        float[] rots = RotationUtil.getRotations(pos, facing);
        RayTraceResult result = RaytraceUtil.getRayTraceResult(rots[0], rots[1]);
        this.placeBlock(pos, facing, rots, result.hitVec);
    }

    private void placeBlock(BlockPos on, EnumFacing facing, float[] helpingRotations, Vec3d hitVec) {
        if (this.rotation.getValue().booleanValue()) {
            if (this.rotations == null) {
                this.rotations = helpingRotations;
            }
            this.packets.add((Packet<?>)new CPacketPlayer.Rotation(helpingRotations[0], helpingRotations[1], BlockPlaceModule.mc.player.onGround));
        }
        float[] hitRots = RaytraceUtil.hitVecToPlaceVec(on, hitVec);
        this.packets.add((Packet<?>)new CPacketPlayerTryUseItemOnBlock(on, facing, EnumHand.MAIN_HAND, hitRots[0], hitRots[1], hitRots[2]));
        if (this.swing.getValue().booleanValue()) {
            this.packets.add((Packet<?>)new CPacketAnimation(EnumHand.MAIN_HAND));
        }
        ++this.blocksPlaced;
    }

    private void execute() {
        if (!this.packets.isEmpty()) {
            int lastSlot = BlockPlaceModule.mc.player.inventory.currentItem;
            ItemStack oldItem = BlockPlaceModule.mc.player.getHeldItemMainhand();
            if (this.altSwap.getValue().booleanValue() && this instanceof HoleFill) {
                ItemUtil.switchToAlt(this.slot);
            } else {
                ItemUtil.switchTo(this.slot);
            }
            ItemStack newItem = BlockPlaceModule.mc.player.getHeldItemMainhand();
            PacketUtil.sneak(true);
            if (this.swing.getValue().booleanValue()) {
                EntityUtil.swingClient();
            }
            this.packets.forEach(PacketUtil::send);
            this.packets.clear();
            this.timer.reset();
            PacketUtil.sneak(false);
            if (this.altSwap.getValue().booleanValue() && this instanceof HoleFill) {
                short id = BlockPlaceModule.mc.player.openContainer.getNextTransactionID(BlockPlaceModule.mc.player.inventory);
                ItemStack fakeStack = new ItemStack(Items.END_CRYSTAL, 64);
                int newSlot = ItemUtil.hotbarToInventory(this.slot);
                int altSlot = ItemUtil.hotbarToInventory(lastSlot);
                Slot currentSlot = (Slot)BlockPlaceModule.mc.player.inventoryContainer.inventorySlots.get(altSlot);
                Slot swapSlot = (Slot)BlockPlaceModule.mc.player.inventoryContainer.inventorySlots.get(newSlot);
                PacketUtil.send(new CPacketClickWindow(0, newSlot, BlockPlaceModule.mc.player.inventory.currentItem, ClickType.SWAP, fakeStack, id));
                currentSlot.putStack(oldItem);
                swapSlot.putStack(newItem);
            } else {
                ItemUtil.switchTo(lastSlot);
            }
        }
    }

    private boolean crystalCheck(BlockPos pos) {
        CPacketUseEntity attackPacket = null;
        float currentDmg = Float.MAX_VALUE;
        float[] angles = null;
        for (Entity entity : BlockPlaceModule.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos))) {
            float damage;
            if (entity == null || !EntityUtil.isDead(entity) || !(entity instanceof EntityEnderCrystal) || !((damage = DamageUtil.calculate(entity, (EntityPlayer)BlockPlaceModule.mc.player)) < currentDmg)) continue;
            currentDmg = damage;
            angles = RotationUtil.getRotations(entity.posX, entity.posY, entity.posZ);
            attackPacket = new CPacketUseEntity(entity);
        }
        if (attackPacket == null) {
            return false;
        }
        int weaknessSlot = -1;
        int oldSlot = BlockPlaceModule.mc.player.inventory.currentItem;
        if (!DamageUtil.canBreakWeakness(true) && (weaknessSlot = DamageUtil.findAntiWeakness()) == -1) {
            return true;
        }
        if (weaknessSlot != -1) {
            if (this.rotation.getValue().booleanValue()) {
                if (this.rotations == null) {
                    this.rotations = angles;
                }
                PacketUtil.send(new CPacketPlayer.Rotation((float)angles[0], angles[1], BlockPlaceModule.mc.player.onGround));
            }
            ItemUtil.switchTo(weaknessSlot);
            PacketUtil.send(attackPacket);
            if (this.swing.getValue().booleanValue()) {
                BlockPlaceModule.mc.player.swingArm(EnumHand.MAIN_HAND);
            }
            ItemUtil.switchTo(oldSlot);
            return false;
        }
        if (this.rotation.getValue().booleanValue()) {
            if (this.rotations == null) {
                this.rotations = angles;
            }
            this.packets.add((Packet<?>)new CPacketPlayer.Rotation(angles[0], angles[1], BlockPlaceModule.mc.player.onGround));
        }
        this.packets.add((Packet<?>)attackPacket);
        if (this.swing.getValue().booleanValue()) {
            this.packets.add((Packet<?>)new CPacketAnimation(EnumHand.MAIN_HAND));
        }
        return false;
    }

    private boolean canPlaceBlock(BlockPos pos, boolean strict) {
        Block block = BlockPlaceModule.mc.world.getBlockState(pos).getBlock();
        if (!(block instanceof BlockAir || block instanceof BlockLiquid || block instanceof BlockTallGrass || block instanceof BlockFire || block instanceof BlockDeadBush || block instanceof BlockSnow)) {
            return false;
        }
        if (!(this instanceof InstantWeb)) {
            for (Entity entity : BlockPlaceModule.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos))) {
                if (entity instanceof EntityItem || entity instanceof EntityXPOrb || entity instanceof EntityArrow || entity instanceof EntityEnderCrystal) continue;
                return false;
            }
        }
        for (EnumFacing side : this.getPlacableFacings(pos, strict)) {
            if (!this.canClick(pos.offset(side))) continue;
            return true;
        }
        return false;
    }

    private boolean canClick(BlockPos pos) {
        return BlockPlaceModule.mc.world.getBlockState(pos).getBlock().canCollideCheck(BlockPlaceModule.mc.world.getBlockState(pos), false);
    }

    private List<EnumFacing> getPlacableFacings(BlockPos pos, boolean strict) {
        ArrayList<EnumFacing> validFacings = new ArrayList<EnumFacing>();
        for (EnumFacing side : EnumFacing.values()) {
            BlockPos neighbour;
            IBlockState blockState;
            if (strict && BlockPlaceModule.mc.player.getDistanceSq(pos) > (double)MathUtil.square(((Float)this.strictRange.getValue()).floatValue())) {
                Vec3d testVec = new Vec3d((Vec3i)pos).add(0.5, 0.5, 0.5).add(new Vec3d(side.getDirectionVec()).scale(0.5));
                RayTraceResult result = BlockPlaceModule.mc.world.rayTraceBlocks(BlockPlaceModule.mc.player.getPositionEyes(1.0f), testVec);
                if (result != null && result.typeOfHit != RayTraceResult.Type.MISS) continue;
            }
            if (!(blockState = BlockPlaceModule.mc.world.getBlockState(neighbour = pos.offset(side))).getBlock().canCollideCheck(blockState, false) || blockState.getMaterial().isReplaceable()) continue;
            validFacings.add(side);
        }
        return validFacings;
    }

    private void getSlot(boolean echest) {
        if (this instanceof HoleFill && this.preferWebs.getValue().booleanValue() || this instanceof InstantWeb) {
            this.slot = ItemUtil.getBlockFromHotbar(Blocks.WEB);
        } else {
            this.slot = ItemUtil.getBlockFromHotbar(Blocks.OBSIDIAN);
            if (this.slot == -1 && echest) {
                this.slot = ItemUtil.getBlockFromHotbar(Blocks.ENDER_CHEST);
            }
        }
    }

    private Listener<?> listenerDeath(final Module module) {
        return new Listener<DeathEvent>(DeathEvent.class){

            @Override
            public void call(DeathEvent event) {
                if (event.getEntity() != null && event.getEntity().equals((Object)Minecraftable.mc.player) && BlocksManager.get().disableOnDeath().booleanValue()) {
                    Minecraftable.mc.addScheduledTask(module::disable);
                }
            }
        };
    }

    private Listener<?> listenerDisconnect(final Module module) {
        return new Listener<DisconnectEvent>(DisconnectEvent.class){

            @Override
            public void call(DisconnectEvent event) {
                if (BlocksManager.get().disableOnDisconnect().booleanValue()) {
                    Minecraftable.mc.addScheduledTask(module::disable);
                }
            }
        };
    }
}

