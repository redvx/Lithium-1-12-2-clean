/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.init.MobEffects
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.inventory.Slot
 *  net.minecraft.item.EnumAction
 *  net.minecraft.item.ItemEndCrystal
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemTool
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketClickWindow
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 */
package me.chachoox.lithium.impl.modules.combat.autocrystal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.blocks.BlockUtil;
import me.chachoox.lithium.api.util.blocks.MineUtil;
import me.chachoox.lithium.api.util.entity.CombatUtil;
import me.chachoox.lithium.api.util.entity.DamageUtil;
import me.chachoox.lithium.api.util.entity.EntityUtil;
import me.chachoox.lithium.api.util.inventory.ItemUtil;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.api.util.math.MathUtil;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.api.util.network.PacketUtil;
import me.chachoox.lithium.api.util.rotation.RotationUtil;
import me.chachoox.lithium.api.util.rotation.raytrace.RaytraceUtil;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.combat.autocrystal.ListenerDestroy;
import me.chachoox.lithium.impl.modules.combat.autocrystal.ListenerExplosion;
import me.chachoox.lithium.impl.modules.combat.autocrystal.ListenerMotion;
import me.chachoox.lithium.impl.modules.combat.autocrystal.ListenerRender;
import me.chachoox.lithium.impl.modules.combat.autocrystal.ListenerSound;
import me.chachoox.lithium.impl.modules.combat.autocrystal.ListenerSpawnObject;
import me.chachoox.lithium.impl.modules.combat.autocrystal.mode.Swap;
import me.chachoox.lithium.impl.modules.combat.autocrystal.mode.YawStep;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class AutoCrystal
extends Module {
    protected final Property<Boolean> multiTask = new Property<Boolean>(false, new String[]{"MultiTask", "PauseEat"}, "Stops autocrystal if we are eating.");
    protected final Property<Boolean> whileMining = new Property<Boolean>(false, new String[]{"WhileMining", "MiningPause"}, "Stops autocrystal if we are mining.");
    protected final Property<Boolean> rotation = new Property<Boolean>(false, new String[]{"Rotate", "rots", "r", "LOOK"}, "Rotates to crystals when we are placing / breaking them.");
    protected final EnumProperty<YawStep> yawStep = new EnumProperty<YawStep>(YawStep.FULL, new String[]{"YawStep", "Yaw", "Strict"}, "Off: - Wont use YawStep / Semi: - Only uses YawStep if yaw is below the first angle / Full: - Always uses YawStep.");
    protected final NumberProperty<Integer> yawStepThreshold = new NumberProperty<Integer>(25, 0, 180, new String[]{"YawStepThreshold", "YawThreshold"}, "How far we have to look down before activating yawstep.");
    protected final Property<Boolean> rayTrace = new Property<Boolean>(false, new String[]{"RayTrace", "RayTracing", "ThroughWalls"}, "Wont break / place crystals through walls.");
    protected final Property<Boolean> attack = new Property<Boolean>(true, new String[]{"Break", "Attack", "Destroy", "RAPE"}, "Attacks crystals when we are able to.");
    protected final Property<Boolean> breakRangeEye = new Property<Boolean>(false, new String[]{"BreakRangeEye", "EyeBreak"}, "Uses crystal eye range instead of just the crystal range.");
    protected final NumberProperty<Float> breakRange = new NumberProperty<Float>(Float.valueOf(5.0f), Float.valueOf(1.0f), Float.valueOf(6.0f), Float.valueOf(0.1f), new String[]{"BreakRange", "AttackRange", "RapeRange"}, "How far we have to be from a crystal to attack it.");
    protected final NumberProperty<Float> breakWallRange = new NumberProperty<Float>(Float.valueOf(3.0f), Float.valueOf(1.0f), Float.valueOf(6.0f), Float.valueOf(0.1f), new String[]{"BreakWallRange", "AttackWallRange"}, "How far we have to be from a crystal to attack it.");
    protected final NumberProperty<Integer> existed = new NumberProperty<Integer>(3, 0, 10, new String[]{"TicksExisted", "Existed"}, "How long the crystal has to of existed for (in ticks) before we attack it.");
    protected final Property<Boolean> boost = new Property<Boolean>(true, new String[]{"Boost", "IdPredict", "Fast", "BreakBoost"}, "Predicts ids of crystals safely which boost the break speed.");
    protected final NumberProperty<Integer> breakDelay = new NumberProperty<Integer>(80, 0, 500, new String[]{"BreakDelay", "AttackDelay", "DestroyDelay", "RapeDelay"}, "Delay interval for attacking crystals.");
    protected final Property<Boolean> antiWeakness = new Property<Boolean>(true, new String[]{"AntiWeakness", "aw", "NoWeakness"}, "Switches to a sword using the same switching method as the \"Switch\" property values.");
    protected final Property<Boolean> switchBack = new Property<Boolean>(false, new String[]{"SwitchBack", "Swapback"}, "Switches back to the crystal after hitting the crystal with a sword.");
    protected final Property<Boolean> place = new Property<Boolean>(false, new String[]{"Place", "Placement"}, "Places crystals.");
    protected final Property<Boolean> secondCheck = new Property<Boolean>(true, new String[]{"SecondPlace", "Semi1.13", "CCPlace"}, "Tries to place on semi 1.13 placements.");
    protected final Property<Boolean> blockDestruction = new Property<Boolean>(true, new String[]{"BlockDestruction", "ThroughTerrain"}, "Ignores terrain that can be exploded and calculates through it.");
    protected final NumberProperty<Integer> placeDelay = new NumberProperty<Integer>(50, 0, 500, new String[]{"PlaceDelay"}, "Delay interval for placing crystals.");
    protected final NumberProperty<Float> placeRange = new NumberProperty<Float>(Float.valueOf(5.0f), Float.valueOf(1.0f), Float.valueOf(6.0f), Float.valueOf(0.1f), new String[]{"PlaceRange", "PlacingRange"}, "How far we have to be from a crystal to attack it.");
    protected final NumberProperty<Float> placeWallRange = new NumberProperty<Float>(Float.valueOf(3.0f), Float.valueOf(1.0f), Float.valueOf(6.0f), Float.valueOf(0.1f), new String[]{"PlaceWallRange", "PlacingWallRange"}, "How far we have to be from a crystal to attack it.");
    protected final NumberProperty<Integer> facePlaceHp = new NumberProperty<Integer>(8, 0, 36, new String[]{"FacePlace", "FacePlaceHp", "FacePlaceHealth"}, "How low an enemy has to be to faceplace.");
    protected final NumberProperty<Float> minDamage = new NumberProperty<Float>(Float.valueOf(4.0f), Float.valueOf(1.0f), Float.valueOf(20.0f), Float.valueOf(0.1f), new String[]{"MinDamage", "MinDmg"}, "Minimum damage allowed for a crystal to do to the target.");
    protected final NumberProperty<Integer> maxSelfDamage = new NumberProperty<Integer>(8, 1, 36, new String[]{"MaxSelfDamage", "MaxSelfDmg"}, "Maximum damage allowed to do to ourself.");
    protected final NumberProperty<Float> targetRange = new NumberProperty<Float>(Float.valueOf(9.0f), Float.valueOf(1.0f), Float.valueOf(15.0f), Float.valueOf(0.1f), new String[]{"TargetRange", "TargetDistance"}, "How close we have to be to someone for them to be a target.");
    protected final NumberProperty<Float> lethalMult = new NumberProperty<Float>(Float.valueOf(3.0f), Float.valueOf(0.0f), Float.valueOf(6.0f), Float.valueOf(0.1f), new String[]{"LethalMult", "LethalMultiplier"}, "Multipler for absoprtion hearts.");
    protected final Property<Boolean> armorBreaker = new Property<Boolean>(true, new String[]{"ArmorBreaker", "ArmorDestroy"}, "If we want to faceplace people based off their armor percentage or not.");
    protected final NumberProperty<Float> armorScale = new NumberProperty<Float>(Float.valueOf(15.0f), Float.valueOf(0.0f), Float.valueOf(100.0f), Float.valueOf(0.1f), new String[]{"ArmorScale", "ArmorLevel", "Armor%"}, "How low one of the targets armor pieces has to be to faceplace.");
    protected final EnumProperty<Swap> swap = new EnumProperty<Swap>(Swap.NONE, new String[]{"Switch", "AutoSwitch", "Swap"}, "How we are going to switch to the crystal.");
    protected final NumberProperty<Float> switchDelay = new NumberProperty<Float>(Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(10.0f), Float.valueOf(0.1f), new String[]{"SwitchDelay", "SwapDelay"}, "How long we have to wait before switching to the crystal.");
    protected final Property<Boolean> debugAttack = new Property<Boolean>(false, new String[]{"DebugAttack"}, "");
    protected final Property<Boolean> debugPlace = new Property<Boolean>(false, new String[]{"DebugPlace"}, "");
    protected final Set<BlockPos> placeSet = new HashSet<BlockPos>();
    protected final Map<Integer, Integer> attackMap = new HashMap<Integer, Integer>();
    protected final StopWatch clearTimer = new StopWatch();
    protected final StopWatch breakTimer = new StopWatch();
    protected final StopWatch placeTimer = new StopWatch();
    protected final StopWatch switchTimer = new StopWatch();
    protected BlockPos renderPos;
    protected EntityPlayer target;
    protected int predictedId = -1;
    protected int ticks;
    protected boolean offhand;
    protected float damage;
    protected float actualArmorScale;

    public AutoCrystal() {
        super("AutoCrystal", new String[]{"AutoCrystal", "CrystalNuker", "CrystalNuke", "CrystalAura", "AutoCrystaler", "CA"}, "Automatically breaks/places crystals.", Category.COMBAT);
        this.offerProperties(this.multiTask, this.whileMining, this.rotation, this.yawStep, this.yawStepThreshold, this.rayTrace, this.attack, this.breakRangeEye, this.breakRange, this.breakWallRange, this.existed, this.boost, this.breakDelay, this.antiWeakness, this.switchBack, this.place, this.secondCheck, this.blockDestruction, this.placeDelay, this.placeRange, this.placeWallRange, this.facePlaceHp, this.minDamage, this.maxSelfDamage, this.targetRange, this.lethalMult, this.armorBreaker, this.armorScale, this.swap, this.switchDelay, this.debugAttack, this.debugPlace);
        this.offerListeners(new ListenerMotion(this), new ListenerSound(this), new ListenerSpawnObject(this), new ListenerRender(this), new ListenerDestroy(this), new ListenerExplosion(this));
        this.armorScale.addObserver(event -> {
            this.actualArmorScale = ((Float)this.armorScale.getValue()).floatValue();
        });
    }

    @Override
    public String getSuffix() {
        if (this.target != null) {
            return MathUtil.round(this.damage, 2) + "";
        }
        return null;
    }

    @Override
    public void onEnable() {
        this.target = null;
        this.attackMap.clear();
        this.placeSet.clear();
        this.predictedId = -1;
        this.renderPos = null;
    }

    protected void doAutoCrystal() {
        if (this.isNull()) {
            return;
        }
        if (this.multiTask.getValue().booleanValue() && (AutoCrystal.mc.player.isHandActive() && AutoCrystal.mc.player.getActiveItemStack().getItemUseAction().equals((Object)EnumAction.EAT) || AutoCrystal.mc.player.getActiveItemStack().getItemUseAction().equals((Object)EnumAction.DRINK))) {
            return;
        }
        if (this.whileMining.getValue().booleanValue() && ItemUtil.isHolding(ItemTool.class) && AutoCrystal.mc.playerController.getIsHittingBlock() && MineUtil.canBreak(AutoCrystal.mc.objectMouseOver.getBlockPos()) && !AutoCrystal.mc.world.isAirBlock(AutoCrystal.mc.objectMouseOver.getBlockPos())) {
            return;
        }
        this.target = CombatUtil.getTarget(((Float)this.targetRange.getValue()).floatValue());
        if (this.ticks < 40) {
            ++this.ticks;
        } else {
            this.ticks = 0;
            this.attackMap.clear();
        }
        if (this.clearTimer.passed(500L)) {
            this.placeSet.clear();
            this.attackMap.clear();
            this.predictedId = -1;
            this.renderPos = null;
            this.clearTimer.reset();
        }
        this.offhand = AutoCrystal.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL;
        this.doBreak();
        this.doPlace();
    }

    protected void doBreak() {
        if (this.attack.getValue().booleanValue()) {
            if (this.target == null) {
                return;
            }
            float maxDamage = 0.0f;
            Entity maxCrystal = null;
            float minDmg = this.isArmorUnderPercent() ? (EntityUtil.getHealth(this.target) < (float)((Integer)this.facePlaceHp.getValue()).intValue() ? 2.0f : ((Float)this.minDamage.getValue()).floatValue()) : 2.5f;
            for (Entity crystal : AutoCrystal.mc.world.loadedEntityList) {
                float selfDamage;
                float targetDamage;
                if (!(crystal instanceof EntityEnderCrystal) || crystal.isDead || this.breakRangeEye.getValue().booleanValue() && AutoCrystal.mc.player.getDistance(crystal.posX, crystal.posY + (double)crystal.getEyeHeight(), crystal.posZ) > (double)(RaytraceUtil.canBeSeen(crystal, (EntityLivingBase)AutoCrystal.mc.player) ? (Float)this.breakRange.getValue() : (Float)this.breakWallRange.getValue()).floatValue() || !this.breakRangeEye.getValue().booleanValue() && AutoCrystal.mc.player.getDistance(crystal) > (RaytraceUtil.canBeSeen(crystal, (EntityLivingBase)AutoCrystal.mc.player) ? (Float)this.breakRange.getValue() : (Float)this.breakWallRange.getValue()).floatValue() || (Integer)this.existed.getValue() != 0 && crystal.ticksExisted < (Integer)this.existed.getValue() || this.boost.getValue().booleanValue() && (Integer)this.existed.getValue() != 0 && (double)crystal.ticksExisted < 0.2) continue;
                if (this.rayTrace.getValue().booleanValue() && !RaytraceUtil.canBlockBeSeen((Entity)AutoCrystal.mc.player, crystal.getPosition(), true)) {
                    return;
                }
                if (this.attackMap.containsKey(crystal.getEntityId()) && this.attackMap.get(crystal.getEntityId()) > 5 || !((targetDamage = EntityUtil.calculate(crystal.posX, crystal.posY, crystal.posZ, (EntityLivingBase)this.target, this.blockDestruction.getValue())) > minDmg) && !(targetDamage * ((Float)this.lethalMult.getValue()).floatValue() > this.target.getHealth() + this.target.getAbsorptionAmount()) || (selfDamage = !AutoCrystal.mc.player.capabilities.isCreativeMode ? EntityUtil.calculate(crystal.posX, crystal.posY, crystal.posZ, (EntityLivingBase)AutoCrystal.mc.player, this.blockDestruction.getValue()) : 0.0f) > (float)((Integer)this.maxSelfDamage.getValue()).intValue() || selfDamage + 0.5f >= EntityUtil.getHealth((EntityPlayer)AutoCrystal.mc.player) || !(targetDamage > maxDamage)) continue;
                this.damage = maxDamage = targetDamage;
                maxCrystal = crystal;
            }
            if (maxCrystal != null) {
                int lastSlot = -1;
                int swordSlot = DamageUtil.findAntiWeakness();
                if (this.antiWeakness.getValue().booleanValue() && AutoCrystal.mc.player.isPotionActive(MobEffects.WEAKNESS)) {
                    boolean shouldSwitch;
                    boolean bl = shouldSwitch = !AutoCrystal.mc.player.isPotionActive(MobEffects.STRENGTH) || Objects.requireNonNull(AutoCrystal.mc.player.getActivePotionEffect(MobEffects.STRENGTH)).getAmplifier() != 2;
                    if (shouldSwitch && swordSlot != -1) {
                        lastSlot = AutoCrystal.mc.player.inventory.currentItem;
                        if (this.swap.getValue() == Swap.ALTERNATIVE) {
                            ItemUtil.switchToAlt(swordSlot);
                        } else {
                            ItemUtil.switchTo(swordSlot);
                        }
                    }
                }
                if (this.rotation.getValue().booleanValue()) {
                    this.rotateToEntity(maxCrystal);
                }
                if (this.debugAttack.getValue().booleanValue()) {
                    Logger.getLogger().log(String.format("Attacking Crystal at %s [Damage = %s]", maxCrystal.getPosition(), Float.valueOf(EntityUtil.calculate(maxCrystal.posX, maxCrystal.posY, maxCrystal.posZ, (EntityLivingBase)this.target, this.blockDestruction.getValue()))), false);
                }
                PacketUtil.send(new CPacketUseEntity(maxCrystal));
                this.attackMap.put(maxCrystal.getEntityId(), this.attackMap.containsKey(maxCrystal.getEntityId()) ? this.attackMap.get(maxCrystal.getEntityId()) + 1 : 1);
                AutoCrystal.mc.player.swingArm(this.offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
                this.breakTimer.reset();
                if ((this.switchBack.getValue().booleanValue() || this.swap.getValue() == Swap.ALTERNATIVE) && swordSlot != -1) {
                    if (lastSlot == -1) {
                        return;
                    }
                    if (this.swap.getValue() == Swap.ALTERNATIVE) {
                        ItemUtil.switchToAlt(swordSlot);
                    } else {
                        ItemUtil.switchTo(lastSlot);
                    }
                }
            }
        }
    }

    protected void doPlace() {
        if (this.place.getValue().booleanValue()) {
            if (this.target == null) {
                return;
            }
            float maxDamage = 0.0f;
            float minDmg = this.isArmorUnderPercent() ? (EntityUtil.getHealth(this.target) < (float)((Integer)this.facePlaceHp.getValue()).intValue() ? 2.0f : ((Float)this.minDamage.getValue()).floatValue()) : 2.5f;
            BlockPos placePos = null;
            for (BlockPos pos : BlockUtil.getSphere(((Float)this.targetRange.getValue()).floatValue(), true)) {
                float selfDamage;
                if (!BlockUtil.canPlaceCrystal(pos, this.secondCheck.getValue()) || BlockUtil.getDistanceSq(pos) > (double)(RaytraceUtil.canBlockBeSeen((Entity)AutoCrystal.mc.player, pos, true) ? MathUtil.square(((Float)this.placeRange.getValue()).floatValue()) : MathUtil.square(((Float)this.placeWallRange.getValue()).floatValue()))) continue;
                if (this.rayTrace.getValue().booleanValue() && !RaytraceUtil.canBlockBeSeen((Entity)AutoCrystal.mc.player, pos, true)) {
                    return;
                }
                float targetDamage = !this.target.capabilities.isCreativeMode ? EntityUtil.calculate((float)pos.getX() + 0.5f, (float)pos.getY() + 1.0f, (float)pos.getZ() + 0.5f, (EntityLivingBase)this.target, this.blockDestruction.getValue()) : 0.0f;
                if (targetDamage < minDmg || targetDamage * ((Float)this.lethalMult.getValue()).floatValue() < this.target.getHealth() + this.target.getAbsorptionAmount() || (selfDamage = !AutoCrystal.mc.player.capabilities.isCreativeMode ? EntityUtil.calculate((float)pos.getX() + 0.5f, (float)pos.getY() + 1.0f, (float)pos.getZ() + 0.5f, (EntityLivingBase)AutoCrystal.mc.player, this.blockDestruction.getValue()) : 0.0f) > (float)((Integer)this.maxSelfDamage.getValue()).intValue() || selfDamage + 0.5f >= EntityUtil.getHealth((EntityPlayer)AutoCrystal.mc.player) || !(targetDamage > maxDamage)) continue;
                maxDamage = targetDamage;
                placePos = pos;
            }
            if (placePos != null) {
                if ((Integer)this.placeDelay.getValue() != 0 && !this.placeTimer.passed(((Integer)this.placeDelay.getValue()).intValue())) {
                    return;
                }
                int crystalSlot = ItemUtil.getItemFromHotbar(Items.END_CRYSTAL);
                int oldSlot = AutoCrystal.mc.player.inventory.currentItem;
                if (crystalSlot == -1 && !this.offhand) {
                    return;
                }
                ItemStack oldItem = AutoCrystal.mc.player.getHeldItemMainhand();
                boolean switched = false;
                if (this.swap.getValue() != Swap.NONE && !this.offhand && !ItemUtil.isHolding(ItemEndCrystal.class)) {
                    switched = true;
                    switch ((Swap)((Object)this.swap.getValue())) {
                        case NORMAL: {
                            if (this.switchTimer.passed(((Float)this.switchDelay.getValue()).floatValue() * 10.0f)) {
                                ItemUtil.switchTo(crystalSlot);
                            }
                            this.switchTimer.reset();
                            break;
                        }
                        case SILENT: {
                            ItemUtil.switchTo(crystalSlot);
                            break;
                        }
                        case ALTERNATIVE: {
                            ItemUtil.switchToAlt(crystalSlot);
                        }
                    }
                }
                if (!(AutoCrystal.mc.player.getHeldItemMainhand().getItem() instanceof ItemEndCrystal) && !this.offhand) {
                    return;
                }
                ItemStack newItem = AutoCrystal.mc.player.getHeldItemMainhand();
                if (this.rotation.getValue().booleanValue()) {
                    this.rotateToPos(placePos);
                }
                if (this.debugPlace.getValue().booleanValue()) {
                    Logger.getLogger().log(String.format("Placing Crystal at X (%s) Y (%s) Z (%s) [Damage = %s]", placePos.getX(), placePos.getY(), placePos.getZ(), Float.valueOf(EntityUtil.calculate(placePos.getX(), placePos.getY(), placePos.getZ(), (EntityLivingBase)this.target, this.blockDestruction.getValue()))), false);
                }
                AutoCrystal.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(placePos, EnumFacing.UP, this.offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.5f, 1.0f, 0.5f));
                this.placeSet.add(placePos);
                this.renderPos = placePos;
                this.placeTimer.reset();
                if (this.swap.getValue() != Swap.NONE && !this.offhand && switched) {
                    switch ((Swap)((Object)this.swap.getValue())) {
                        case SILENT: {
                            ItemUtil.switchTo(oldSlot);
                            break;
                        }
                        case ALTERNATIVE: {
                            short id = AutoCrystal.mc.player.openContainer.getNextTransactionID(AutoCrystal.mc.player.inventory);
                            ItemStack fakeStack = new ItemStack(Items.END_CRYSTAL, 64);
                            int slot = ItemUtil.hotbarToInventory(crystalSlot);
                            int altSlot = ItemUtil.hotbarToInventory(oldSlot);
                            Slot currentSlot = (Slot)AutoCrystal.mc.player.inventoryContainer.inventorySlots.get(altSlot);
                            Slot swapSlot = (Slot)AutoCrystal.mc.player.inventoryContainer.inventorySlots.get(slot);
                            PacketUtil.send(new CPacketClickWindow(0, slot, AutoCrystal.mc.player.inventory.currentItem, ClickType.SWAP, fakeStack, id));
                            currentSlot.putStack(oldItem);
                            swapSlot.putStack(newItem);
                            break;
                        }
                    }
                }
            }
        }
    }

    protected void rotateToPos(BlockPos pos) {
        float[] angle = RotationUtil.getRotations(pos, EnumFacing.UP);
        if (!((YawStep)((Object)this.yawStep.getValue())).equals((Object)YawStep.OFF) && this.rotation.getValue().booleanValue()) {
            float yaw = MathHelper.wrapDegrees((float)Managers.ROTATION.getYaw());
            float angleDifference = angle[0] - yaw;
            if (Math.abs(angleDifference) > 180.0f) {
                float adjust = angleDifference > 0.0f ? -360.0f : 360.0f;
                angleDifference += adjust;
            }
            if (Math.abs(angleDifference) > (float)((Integer)this.yawStepThreshold.getValue()).intValue() && (((YawStep)((Object)this.yawStep.getValue())).equals((Object)YawStep.FULL) || ((YawStep)((Object)this.yawStep.getValue())).equals((Object)YawStep.SEMI) && angle[0] > (float)((Integer)this.yawStepThreshold.getValue()).intValue())) {
                int rotationDirection = angleDifference > 0.0f ? 1 : -1;
                Managers.ROTATION.setRotations(yaw += (float)((Integer)this.yawStepThreshold.getValue() * rotationDirection), angle[1]);
            }
        }
        Managers.ROTATION.setRotations(angle[0], angle[1]);
    }

    protected void rotateToEntity(Entity entity) {
        float[] angle = RotationUtil.getRotations(entity.posX, entity.posY, entity.posZ);
        if (!((YawStep)((Object)this.yawStep.getValue())).equals((Object)YawStep.OFF) && this.rotation.getValue().booleanValue()) {
            float yaw = MathHelper.wrapDegrees((float)Managers.ROTATION.getYaw());
            float angleDifference = angle[0] - yaw;
            if (Math.abs(angleDifference) > 180.0f) {
                float adjust = angleDifference > 0.0f ? -360.0f : 360.0f;
                angleDifference += adjust;
            }
            if (Math.abs(angleDifference) > (float)((Integer)this.yawStepThreshold.getValue()).intValue() && (((YawStep)((Object)this.yawStep.getValue())).equals((Object)YawStep.FULL) || ((YawStep)((Object)this.yawStep.getValue())).equals((Object)YawStep.SEMI) && angle[0] > (float)((Integer)this.yawStepThreshold.getValue()).intValue())) {
                int rotationDirection = angleDifference > 0.0f ? 1 : -1;
                Managers.ROTATION.setRotations(yaw += (float)((Integer)this.yawStepThreshold.getValue() * rotationDirection), angle[1]);
            }
        }
        Managers.ROTATION.setRotations(angle[0], angle[1]);
    }

    private boolean isArmorUnderPercent() {
        if (this.armorBreaker.getValue().booleanValue() && this.target != null) {
            for (ItemStack armor : this.target.getArmorInventoryList()) {
                float armorDurability;
                if (armor == null || armor.getItem().equals(Items.AIR) || !((armorDurability = (float)(armor.getMaxDamage() - armor.getItemDamage()) / (float)armor.getMaxDamage() * 10.0f) < this.actualArmorScale)) continue;
                return true;
            }
        }
        return false;
    }
}

