/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.BlockPos$MutableBlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 */
package me.chachoox.lithium.impl.managers.minecraft.safe;

import java.util.List;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.api.util.blocks.BlockUtil;
import me.chachoox.lithium.api.util.blocks.HoleUtil;
import me.chachoox.lithium.api.util.entity.DamageUtil;
import me.chachoox.lithium.api.util.entity.EntityUtil;
import me.chachoox.lithium.api.util.math.Sphere;
import me.chachoox.lithium.api.util.movement.PositionUtil;
import me.chachoox.lithium.api.util.thread.SafeRunnable;
import me.chachoox.lithium.impl.managers.minecraft.safe.SafeManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class SafetyRunnable
implements Minecraftable,
SafeRunnable {
    private final SafeManager manager;
    private final List<Entity> crystals;

    public SafetyRunnable(SafeManager manager, List<Entity> crystals) {
        this.manager = manager;
        this.crystals = crystals;
    }

    @Override
    public void runSafely() {
        float maxDamage = 4.0f;
        for (Entity entity : this.crystals) {
            float damage;
            if (!(entity instanceof EntityEnderCrystal) || entity.isDead || !((damage = DamageUtil.calculate(entity, (EntityPlayer)SafetyRunnable.mc.player)) > 4.0f) && !((double)damage > (double)EntityUtil.getHealth((EntityPlayer)SafetyRunnable.mc.player) - 1.0)) continue;
            this.manager.setSafe(false);
            return;
        }
        boolean fullArmor = true;
        for (ItemStack stack : SafetyRunnable.mc.player.inventory.armorInventory) {
            if (!stack.isEmpty()) continue;
            fullArmor = false;
            break;
        }
        Vec3d vec3d = SafetyRunnable.mc.player.getPositionVector();
        BlockPos position = new BlockPos(vec3d);
        if (fullArmor && (double)position.getY() == vec3d.y && HoleUtil.isHole(position)) {
            this.manager.setSafe(true);
            return;
        }
        if (this.manager.isSafe()) {
            return;
        }
        BlockPos playerPos = PositionUtil.getPosition();
        int x = playerPos.getX();
        int y = playerPos.getY();
        int z = playerPos.getZ();
        int maxRadius = Sphere.getRadius(6.0);
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int i = 1; i < maxRadius; ++i) {
            float damage;
            Vec3i v = Sphere.get(i);
            pos.setPos(x + v.getX(), y + v.getY(), z + v.getZ());
            if (!BlockUtil.canPlaceCrystal((BlockPos)pos, false) || !((damage = DamageUtil.calculate((float)pos.getX() + 0.5f, pos.getY() + 1, (float)pos.getZ() + 0.5f, (EntityPlayer)SafetyRunnable.mc.player, this.getBB())) > 4.0f) && !((double)damage > (double)EntityUtil.getHealth((EntityPlayer)SafetyRunnable.mc.player) - 1.0)) continue;
            this.manager.setSafe(false);
            return;
        }
        this.manager.setSafe(true);
    }

    public AxisAlignedBB getBB() {
        double x = SafetyRunnable.mc.player.posX;
        double y = SafetyRunnable.mc.player.posY;
        double z = SafetyRunnable.mc.player.posZ;
        float w = SafetyRunnable.mc.player.width / 2.0f;
        float h = SafetyRunnable.mc.player.height;
        return new AxisAlignedBB(x - (double)w, y, z - (double)w, x + (double)w, y + (double)h, z + (double)w);
    }
}

