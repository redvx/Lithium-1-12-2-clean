/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.material.Material
 *  net.minecraft.enchantment.Enchantment
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.init.Enchantments
 *  net.minecraft.inventory.EntityEquipmentSlot
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 */
package me.chachoox.lithium.impl.modules.movement.liquidspeed;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.api.util.movement.MovementUtil;
import me.chachoox.lithium.api.util.movement.PositionUtil;
import me.chachoox.lithium.impl.event.events.movement.actions.MoveEvent;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.LambdaListener;
import me.chachoox.lithium.impl.modules.movement.liquidspeed.LiquidDetection;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;

public class LiquidSpeed
extends Module {
    private final EnumProperty<LiquidDetection> detection = new EnumProperty<LiquidDetection>(LiquidDetection.STRICT, new String[]{"Detection", "detect", "mode", "type"}, "Strict: - Only changes speed if entire body is in liquid / Fast: - Only changes speed if any body part is in liquid.");
    private final NumberProperty<Float> waterSpeed = new NumberProperty<Float>(Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(15.0f), Float.valueOf(0.1f), new String[]{"WaterSpeed", "BaseSpeed", "WaterSped", "ws"}, "Speed for water.");
    private final NumberProperty<Float> lavaSpeed = new NumberProperty<Float>(Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(15.0f), Float.valueOf(0.1f), new String[]{"LavaSpeed", "LavaSped", "SpeedLava", "ls"}, "Speed for lava.");
    private final NumberProperty<Float> elytraSpeed = new NumberProperty<Float>(Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(15.0f), Float.valueOf(0.1f), new String[]{"ElytraSpeed", "Espeed", "ElySpeed", "es"}, "Speed for lava when we are elytra flying.");
    private final Property<Boolean> ySpeed = new Property<Boolean>(true, new String[]{"YSpeed", "YSped", "UpwardsSpeed", "VeritcalSpeed", "DownwardsSpeed"}, "Modifies upwards and downwards speed in both liquids.");
    private final Property<Boolean> strafe = new Property<Boolean>(true, new String[]{"Strafe", "straf", "fast"}, "Strafes in liquids, use a lower value for speed with this.");
    private final Property<Boolean> elytra = new Property<Boolean>(true, new String[]{"Elytra", "Elytras", "Ely"}, "Uses elytra fly in liquids using the elytra speed property.");
    private final Property<Boolean> depthStrider = new Property<Boolean>(true, new String[]{"DepthStrider", "Strider", "NoDepthStrider"}, "Wont use speed if we have depth strider boots.");
    private final Property<Boolean> cancelSneak = new Property<Boolean>(true, new String[]{"CancelSneak", "noSneak", "StopSneak"}, "Cancels the sneaking server side if we are going downwards in liquid.");
    private final StopWatch timer = new StopWatch();

    public LiquidSpeed() {
        super("LiquidSpeed", new String[]{"LiquidSpeed", "LavaSpeed", "WaterSpeed", "FastSwim"}, "Tweaks player speed while in liquids.", Category.MOVEMENT);
        this.offerProperties(this.detection, this.waterSpeed, this.lavaSpeed, this.elytra, this.strafe, this.ySpeed, this.depthStrider, this.cancelSneak);
        this.offerListeners(new LambdaListener<MoveEvent>(MoveEvent.class, event -> {
            switch ((LiquidDetection)((Object)((Object)this.detection.getValue()))) {
                case STRICT: {
                    if (!LiquidSpeed.mc.player.onGround) {
                        if (LiquidSpeed.mc.player.isInsideOfMaterial(Material.LAVA)) {
                            this.doSpeedLava((MoveEvent)event);
                            break;
                        }
                        if (!LiquidSpeed.mc.player.isInsideOfMaterial(Material.WATER)) break;
                        this.doSpeedWater((MoveEvent)event);
                        break;
                    }
                }
                case FAST: {
                    if (!PositionUtil.inLiquid()) {
                        this.timer.reset();
                    }
                    if (!this.timer.passed(250L)) break;
                    if (LiquidSpeed.mc.player.isInWater()) {
                        this.doSpeedWater((MoveEvent)event);
                    }
                    if (!LiquidSpeed.mc.player.isInLava()) break;
                    this.doSpeedLava((MoveEvent)event);
                }
            }
        }), new LambdaListener<PacketEvent.Send>(PacketEvent.Send.class, CPacketEntityAction.class, event -> {
            CPacketEntityAction packet = (CPacketEntityAction)event.getPacket();
            if (this.cancelSneak.getValue().booleanValue() && packet.getAction() == CPacketEntityAction.Action.START_SNEAKING) {
                switch ((LiquidDetection)((Object)((Object)this.detection.getValue()))) {
                    case STRICT: {
                        if (LiquidSpeed.mc.player.isInsideOfMaterial(Material.LAVA) || LiquidSpeed.mc.player.isInsideOfMaterial(Material.WATER)) {
                            event.setCanceled(true);
                        }
                    }
                    case FAST: {
                        if (!LiquidSpeed.mc.player.isInLava() && !LiquidSpeed.mc.player.isInWater()) break;
                        event.setCanceled(true);
                    }
                }
            }
        }));
    }

    private void doSpeedWater(MoveEvent event) {
        ItemStack stack = LiquidSpeed.mc.player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
        if (!this.depthStrider.getValue().booleanValue() && this.hasDepthStrider(stack)) {
            return;
        }
        if (this.strafe.getValue().booleanValue()) {
            double[] strafe = MovementUtil.strafe(((Float)this.waterSpeed.getValue()).floatValue() / 10.0f);
            event.setX(strafe[0]);
            this.doVerticalSpeed(event);
            event.setZ(strafe[1]);
        } else {
            event.setX(event.getX() * (double)((Float)this.waterSpeed.getValue()).floatValue());
            this.doVerticalSpeed(event);
            event.setZ(event.getZ() * (double)((Float)this.waterSpeed.getValue()).floatValue());
        }
    }

    private void doSpeedLava(MoveEvent event) {
        if (LiquidSpeed.mc.player.isElytraFlying() && this.elytra.getValue().booleanValue()) {
            event.setX(event.getX() * (double)((Float)this.elytraSpeed.getValue()).floatValue());
            this.doVerticalSpeed(event);
            event.setZ(event.getZ() * (double)((Float)this.elytraSpeed.getValue()).floatValue());
        }
        if (this.strafe.getValue().booleanValue()) {
            double[] strafe = MovementUtil.strafe(((Float)this.lavaSpeed.getValue()).floatValue() / 10.0f);
            event.setX(strafe[0]);
            this.doVerticalSpeed(event);
            event.setZ(strafe[1]);
        } else {
            event.setX(event.getX() * (double)((Float)this.lavaSpeed.getValue()).floatValue());
            this.doVerticalSpeed(event);
            event.setZ(event.getZ() * (double)((Float)this.lavaSpeed.getValue()).floatValue());
        }
    }

    private void doVerticalSpeed(MoveEvent event) {
        if (LiquidSpeed.mc.gameSettings.keyBindJump.isKeyDown() && this.ySpeed.getValue().booleanValue()) {
            event.setY(event.getY() + 0.16);
        } else if (LiquidSpeed.mc.gameSettings.keyBindSneak.isKeyDown() && this.ySpeed.getValue().booleanValue()) {
            event.setY(event.getY() - 0.12);
        }
    }

    private boolean hasDepthStrider(ItemStack stack) {
        return EnchantmentHelper.getEnchantmentLevel((Enchantment)Enchantments.DEPTH_STRIDER, (ItemStack)stack) > 0;
    }
}

