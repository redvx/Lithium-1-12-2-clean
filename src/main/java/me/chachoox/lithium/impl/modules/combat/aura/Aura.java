/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.ItemAxe
 *  net.minecraft.item.ItemShield
 *  net.minecraft.item.ItemSword
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.World
 */
package me.chachoox.lithium.impl.modules.combat.aura;

import java.util.Comparator;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.entity.EntityUtil;
import me.chachoox.lithium.api.util.network.PacketUtil;
import me.chachoox.lithium.api.util.rotation.raytrace.RaytraceUtil;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.combat.aura.ListenerMotion;
import me.chachoox.lithium.impl.modules.combat.aura.ListenerRender;
import me.chachoox.lithium.impl.modules.combat.aura.modes.HitBone;
import me.chachoox.lithium.impl.modules.combat.aura.modes.SwordMode;
import me.chachoox.lithium.impl.modules.combat.aura.modes.Target;
import me.chachoox.lithium.impl.modules.combat.aura.util.TpsSync;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Aura
extends Module {
    protected final NumberProperty<Float> range = new NumberProperty<Float>(Float.valueOf(6.0f), Float.valueOf(1.0f), Float.valueOf(6.0f), Float.valueOf(0.1f), new String[]{"Range", "rang", "r"}, "How close you have to be to your enemy before attacking.");
    protected final NumberProperty<Float> wallRange = new NumberProperty<Float>(Float.valueOf(3.0f), Float.valueOf(1.0f), Float.valueOf(6.0f), Float.valueOf(0.1f), new String[]{"WallRange", "wallrang", "wr"}, "How close you have to be to your enemy before attacking through a wall.");
    protected final Property<Boolean> rotate = new Property<Boolean>(false, new String[]{"Rotation", "rotate", "rot"}, "Rotates to the target.");
    protected final EnumProperty<Target> sortMode = new EnumProperty<Target>(Target.DISTANCE, new String[]{"Sort", "s"}, "Distance: Targets the player farthest from you. / Health: Targets the player who has the lowest health.");
    protected final EnumProperty<SwordMode> sword = new EnumProperty<SwordMode>(SwordMode.REQUIRE, new String[]{"Sword", "swor"}, "Require: Only attacks when holding sword. / Switch: Switches to sword then attacks. / None: Will attack with any weapon.");
    protected final EnumProperty<TpsSync> tpsSync = new EnumProperty<TpsSync>(TpsSync.LATEST, new String[]{"TpsSync", "tps"}, "Tries to sync attacking with the current server TPS.");
    protected final EnumProperty<HitBone> bone = new EnumProperty<HitBone>(HitBone.DICK, new String[]{"Bone", "bon"}, "The bone of the target we want to rotate to.");
    protected final Property<Boolean> stopSneak = new Property<Boolean>(false, new String[]{"StopSneaking", "nosneak"}, "Unsneaks and resneaks before attacking if we are sneaking.");
    protected final Property<Boolean> stopSprint = new Property<Boolean>(false, new String[]{"StopSprinting", "nosprint"}, "Stops sprinting and sprints again if we are sprinting.");
    protected final Property<Boolean> stopShield = new Property<Boolean>(false, new String[]{"StopShield", "noshiled"}, "Stops shielding and shields again if we have one pulled.");
    protected final Property<Boolean> render = new Property<Boolean>(false, new String[]{"Render", "rend"}, "Renders a box around the target.");
    protected EntityLivingBase target;

    public Aura() {
        super("Aura", new String[]{"Aura", "killaura", "ka"}, "Attack people or something.", Category.COMBAT);
        this.offerProperties(this.range, this.wallRange, this.rotate, this.sortMode, this.sword, this.tpsSync, this.bone, this.stopSneak, this.stopSprint, this.stopShield, this.render);
        this.offerListeners(new ListenerMotion(this), new ListenerRender(this));
    }

    @Override
    public void onEnable() {
        this.target = null;
    }

    @Override
    public String getSuffix() {
        return this.target != null ? this.target.getName() : null;
    }

    public EntityLivingBase getTarget() {
        return Aura.mc.world.loadedEntityList.stream().filter(this::isValid).min(Comparator.comparingDouble(e -> this.sortMode.getValue() == Target.DISTANCE ? Aura.mc.player.getDistanceSq(e) : (double)EntityUtil.getHealth((EntityPlayer)e))).orElse(null);
    }

    private boolean isValid(Entity entity) {
        if (entity == Aura.mc.player || !(entity instanceof EntityPlayer)) {
            return false;
        }
        return !(Aura.mc.player.getDistance(entity) > this.range(entity)) && !entity.isDead && !(((EntityLivingBase)entity).getHealth() <= 0.0f) && !Managers.FRIEND.isFriend(entity.getName());
    }

    private float range(Entity entity) {
        return RaytraceUtil.canBeSeen(entity.getPositionVector(), (Entity)Aura.mc.player) ? ((Float)this.range.getValue()).floatValue() : ((Float)this.wallRange.getValue()).floatValue();
    }

    protected void releaseShield() {
        if (Aura.mc.player.getHeldItemOffhand().getItem() instanceof ItemShield) {
            PacketUtil.send(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos((Entity)Aura.mc.player), EnumFacing.getFacingFromVector((float)((float)Aura.mc.player.posX), (float)((float)Aura.mc.player.posY), (float)((float)Aura.mc.player.posZ))));
        }
    }

    protected void useShield() {
        if ((Aura.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword || Aura.mc.player.getHeldItemMainhand().getItem() instanceof ItemAxe) && Aura.mc.player.getHeldItemOffhand().getItem() instanceof ItemShield) {
            Aura.mc.playerController.processRightClick((EntityPlayer)Aura.mc.player, (World)Aura.mc.world, EnumHand.OFF_HAND);
        }
    }
}

