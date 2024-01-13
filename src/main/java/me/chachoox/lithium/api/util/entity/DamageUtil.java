/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.material.Material
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.SharedMonsterAttributes
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.MobEffects
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.item.ItemTool
 *  net.minecraft.potion.PotionEffect
 *  net.minecraft.util.CombatRules
 *  net.minecraft.util.DamageSource
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.RayTraceResult$Type
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.Explosion
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.World
 */
package me.chachoox.lithium.api.util.entity;

import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.asm.mixins.item.IItemTool;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class DamageUtil
implements Minecraftable {
    private static final DamageSource DAMAGE_SOURCE = DamageSource.causeExplosionDamage((Explosion)new Explosion((World)DamageUtil.mc.world, (Entity)DamageUtil.mc.player, 0.0, 0.0, 0.0, 6.0f, false, true));

    public static boolean canBreakWeakness(boolean checkStack) {
        if (!DamageUtil.mc.player.isPotionActive(MobEffects.WEAKNESS)) {
            return true;
        }
        int strengthAmp = 0;
        PotionEffect effect = DamageUtil.mc.player.getActivePotionEffect(MobEffects.STRENGTH);
        if (effect != null) {
            strengthAmp = effect.getAmplifier();
        }
        if (strengthAmp >= 1) {
            return true;
        }
        return checkStack && DamageUtil.canBreakWeakness(DamageUtil.mc.player.getHeldItemMainhand());
    }

    public static boolean canBreakWeakness(ItemStack stack) {
        if (stack.getItem() instanceof ItemSword) {
            return true;
        }
        if (stack.getItem() instanceof ItemTool) {
            IItemTool tool = (IItemTool)stack.getItem();
            return tool.getAttackDamage() > 4.0f;
        }
        return false;
    }

    public static int findAntiWeakness() {
        int slot = -1;
        for (int i = 8; i > -1; --i) {
            if (!DamageUtil.canBreakWeakness(DamageUtil.mc.player.inventory.getStackInSlot(i))) continue;
            slot = i;
            if (DamageUtil.mc.player.inventory.currentItem == i) break;
        }
        return slot;
    }

    public static float getDifficultyMultiplier(float distance) {
        switch (DamageUtil.mc.world.getDifficulty()) {
            case PEACEFUL: {
                return 0.0f;
            }
            case EASY: {
                return Math.min(distance / 2.0f + 1.0f, distance);
            }
            case HARD: {
                return distance * 3.0f / 2.0f;
            }
        }
        return distance;
    }

    public static float calculate(BlockPos pos) {
        return DamageUtil.calculate((float)pos.getX() + 0.5f, pos.getY() + 1, (float)pos.getZ() + 0.5f, (EntityPlayer)DamageUtil.mc.player);
    }

    public static float calculate(double x, double y, double z, EntityPlayer base) {
        return DamageUtil.calculate(x, y, z, base, base.getEntityBoundingBox());
    }

    public static float calculate(double x, double y, double z, EntityPlayer base, AxisAlignedBB boundingBox) {
        PotionEffect resistance;
        double distance = base.getDistance(x, y, z) / 12.0;
        if (distance > 1.0) {
            return 0.0f;
        }
        float density = DamageUtil.getBlockDensity(new Vec3d(x, y, z), boundingBox);
        double densityDistance = distance = (1.0 - distance) * (double)density;
        float damage = CombatRules.getDamageAfterAbsorb((float)DamageUtil.getDifficultyMultiplier((float)((densityDistance * densityDistance + distance) / 2.0 * 7.0 * 12.0 + 1.0)), (float)base.getTotalArmorValue(), (float)((float)base.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue()));
        int modifierDamage = EnchantmentHelper.getEnchantmentModifierDamage((Iterable)base.inventory.armorInventory, (DamageSource)DAMAGE_SOURCE);
        if (modifierDamage > 0) {
            damage = CombatRules.getDamageAfterMagicAbsorb((float)damage, (float)modifierDamage);
        }
        if ((resistance = base.getActivePotionEffect(MobEffects.RESISTANCE)) != null) {
            damage = damage * (float)(25 - (resistance.getAmplifier() + 1) * 5) / 25.0f;
        }
        return Math.max(damage, 0.0f);
    }

    public static float calculate(Entity crystal, EntityPlayer base) {
        return DamageUtil.calculate(crystal.posX, crystal.posY, crystal.posZ, base);
    }

    public static int getDamage(ItemStack stack) {
        return stack.getMaxDamage() - stack.getItemDamage();
    }

    public static float getPercent(ItemStack stack) {
        return (float)DamageUtil.getDamage(stack) / (float)stack.getMaxDamage() * 100.0f;
    }

    public static float getBlockDensity(Vec3d vec, AxisAlignedBB bb) {
        double x = 1.0 / ((bb.maxX - bb.minX) * 2.0 + 1.0);
        double y = 1.0 / ((bb.maxY - bb.minY) * 2.0 + 1.0);
        double z = 1.0 / ((bb.maxZ - bb.minZ) * 2.0 + 1.0);
        double xFloor = (1.0 - Math.floor(1.0 / x) * x) / 2.0;
        double zFloor = (1.0 - Math.floor(1.0 / z) * z) / 2.0;
        if (x >= 0.0 && y >= 0.0 && z >= 0.0) {
            int air = 0;
            int traced = 0;
            float a = 0.0f;
            while (a <= 1.0f) {
                float b = 0.0f;
                while (b <= 1.0f) {
                    float c = 0.0f;
                    while (c <= 1.0f) {
                        double xOff = bb.minX + (bb.maxX - bb.minX) * (double)a;
                        double yOff = bb.minY + (bb.maxY - bb.minY) * (double)b;
                        double zOff = bb.minZ + (bb.maxZ - bb.minZ) * (double)c;
                        RayTraceResult result = DamageUtil.rayTraceBlocks(new Vec3d(xOff + xFloor, yOff, zOff + zFloor), vec, false, true, true);
                        if (result == null || result.typeOfHit == RayTraceResult.Type.MISS) {
                            ++air;
                        }
                        ++traced;
                        c = (float)((double)c + z);
                    }
                    b = (float)((double)b + y);
                }
                a = (float)((double)a + x);
            }
            return (float)air / (float)traced;
        }
        return 0.0f;
    }

    public static RayTraceResult rayTraceBlocks(Vec3d vec31, Vec3d vec32, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock) {
        int i = MathHelper.floor((double)vec32.x);
        int j = MathHelper.floor((double)vec32.y);
        int k = MathHelper.floor((double)vec32.z);
        int l = MathHelper.floor((double)vec31.x);
        int i1 = MathHelper.floor((double)vec31.y);
        int j1 = MathHelper.floor((double)vec31.z);
        BlockPos blockpos = new BlockPos(l, i1, j1);
        IBlockState iblockstate = DamageUtil.mc.world.getBlockState(blockpos);
        Block block = iblockstate.getBlock();
        if ((!ignoreBlockWithoutBoundingBox || iblockstate.getCollisionBoundingBox((IBlockAccess)DamageUtil.mc.world, blockpos) != Block.NULL_AABB) && block.canCollideCheck(iblockstate, stopOnLiquid)) {
            return iblockstate.collisionRayTrace((World)DamageUtil.mc.world, blockpos, vec31, vec32);
        }
        RayTraceResult raytraceresult2 = null;
        int k1 = 200;
        while (k1-- >= 0) {
            EnumFacing enumfacing;
            if (Double.isNaN(vec31.x) || Double.isNaN(vec31.y) || Double.isNaN(vec31.z)) {
                return null;
            }
            if (l == i && i1 == j && j1 == k) {
                return returnLastUncollidableBlock ? raytraceresult2 : null;
            }
            boolean flag2 = true;
            boolean flag = true;
            boolean flag1 = true;
            double d0 = 999.0;
            double d1 = 999.0;
            double d2 = 999.0;
            if (i > l) {
                d0 = (double)l + 1.0;
            } else if (i < l) {
                d0 = (double)l + 0.0;
            } else {
                flag2 = false;
            }
            if (j > i1) {
                d1 = (double)i1 + 1.0;
            } else if (j < i1) {
                d1 = (double)i1 + 0.0;
            } else {
                flag = false;
            }
            if (k > j1) {
                d2 = (double)j1 + 1.0;
            } else if (k < j1) {
                d2 = (double)j1 + 0.0;
            } else {
                flag1 = false;
            }
            double d3 = 999.0;
            double d4 = 999.0;
            double d5 = 999.0;
            double d6 = vec32.x - vec31.x;
            double d7 = vec32.y - vec31.y;
            double d8 = vec32.z - vec31.z;
            if (flag2) {
                d3 = (d0 - vec31.x) / d6;
            }
            if (flag) {
                d4 = (d1 - vec31.y) / d7;
            }
            if (flag1) {
                d5 = (d2 - vec31.z) / d8;
            }
            if (d3 == -0.0) {
                d3 = -1.0E-4;
            }
            if (d4 == -0.0) {
                d4 = -1.0E-4;
            }
            if (d5 == -0.0) {
                d5 = -1.0E-4;
            }
            if (d3 < d4 && d3 < d5) {
                enumfacing = i > l ? EnumFacing.WEST : EnumFacing.EAST;
                vec31 = new Vec3d(d0, vec31.y + d7 * d3, vec31.z + d8 * d3);
            } else if (d4 < d5) {
                enumfacing = j > i1 ? EnumFacing.DOWN : EnumFacing.UP;
                vec31 = new Vec3d(vec31.x + d6 * d4, d1, vec31.z + d8 * d4);
            } else {
                enumfacing = k > j1 ? EnumFacing.NORTH : EnumFacing.SOUTH;
                vec31 = new Vec3d(vec31.x + d6 * d5, vec31.y + d7 * d5, d2);
            }
            l = MathHelper.floor((double)vec31.x) - (enumfacing == EnumFacing.EAST ? 1 : 0);
            i1 = MathHelper.floor((double)vec31.y) - (enumfacing == EnumFacing.UP ? 1 : 0);
            j1 = MathHelper.floor((double)vec31.z) - (enumfacing == EnumFacing.SOUTH ? 1 : 0);
            blockpos = new BlockPos(l, i1, j1);
            IBlockState iblockstate1 = DamageUtil.mc.world.getBlockState(blockpos);
            Block block1 = iblockstate1.getBlock();
            if (ignoreBlockWithoutBoundingBox && iblockstate1.getMaterial() != Material.PORTAL && iblockstate1.getCollisionBoundingBox((IBlockAccess)DamageUtil.mc.world, blockpos) == Block.NULL_AABB) continue;
            if (block1.canCollideCheck(iblockstate1, stopOnLiquid) && block1 != Blocks.WEB) {
                return iblockstate1.collisionRayTrace((World)DamageUtil.mc.world, blockpos, vec31, vec32);
            }
            raytraceresult2 = new RayTraceResult(RayTraceResult.Type.MISS, vec31, enumfacing, blockpos);
        }
        return returnLastUncollidableBlock ? raytraceresult2 : null;
    }
}

