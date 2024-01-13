/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockLiquid
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityAgeable
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.EnumCreatureType
 *  net.minecraft.entity.SharedMonsterAttributes
 *  net.minecraft.entity.monster.EntityEnderman
 *  net.minecraft.entity.monster.EntityIronGolem
 *  net.minecraft.entity.monster.EntityPigZombie
 *  net.minecraft.entity.monster.EntitySpider
 *  net.minecraft.entity.passive.EntityAmbientCreature
 *  net.minecraft.entity.passive.EntitySquid
 *  net.minecraft.entity.passive.EntityWolf
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.MobEffects
 *  net.minecraft.item.ItemStack
 *  net.minecraft.potion.Potion
 *  net.minecraft.util.CombatRules
 *  net.minecraft.util.DamageSource
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.Explosion
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.World
 */
package me.chachoox.lithium.api.util.entity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.api.util.blocks.BlockUtil;
import me.chachoox.lithium.api.util.blocks.HoleUtil;
import me.chachoox.lithium.api.util.movement.PositionUtil;
import me.chachoox.lithium.asm.ducks.IEntityLiving;
import me.chachoox.lithium.impl.managers.Managers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class EntityUtil
implements Minecraftable {
    private static final List<Block> burrowList = Arrays.asList(Blocks.BEDROCK, Blocks.OBSIDIAN, Blocks.ENDER_CHEST, Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.BEACON, Blocks.PISTON, Blocks.REDSTONE_BLOCK, Blocks.ENCHANTING_TABLE, Blocks.ANVIL);

    public static EntityPlayer getClosestEnemy() {
        EntityPlayer closest = null;
        double distance = 3.4028234663852886E38;
        for (EntityPlayer player : EntityUtil.mc.world.playerEntities) {
            if (player == null || EntityUtil.isDead((Entity)player) || player.equals((Object)EntityUtil.mc.player) || Managers.FRIEND.isFriend(player)) continue;
            Vec3d pos = EntityUtil.mc.player.getPositionVector();
            double dist = player.getDistanceSq(pos.x, pos.y, pos.z);
            if (!(dist < distance)) continue;
            closest = player;
            distance = dist;
        }
        return closest;
    }

    public static boolean isPlayerSafe(EntityPlayer player) {
        Vec3d playerVec = player.getPositionVector();
        BlockPos position = new BlockPos(playerVec);
        return HoleUtil.isHole(position) || EntityUtil.isOnBurrow(player);
    }

    public static double getDefaultMoveSpeed() {
        double baseSpeed = 0.2873;
        if (EntityUtil.mc.player != null && EntityUtil.mc.player.isPotionActive(Potion.getPotionById((int)1))) {
            int amplifier = EntityUtil.mc.player.getActivePotionEffect(Potion.getPotionById((int)1)).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }

    public static boolean isntValid(EntityPlayer entity, double range) {
        return (double)EntityUtil.mc.player.getDistance((Entity)entity) > range || entity == EntityUtil.mc.player || EntityUtil.isDead((Entity)entity) || Managers.FRIEND.isFriend(entity);
    }

    public static float getHealth(EntityPlayer player) {
        return player.getHealth() + player.getAbsorptionAmount();
    }

    public static float calculate(double posX, double posY, double posZ, EntityLivingBase entity, boolean breakBlocks) {
        double v = (1.0 - entity.getDistance(posX, posY, posZ) / 12.0) * EntityUtil.getBlockDensity(breakBlocks, new Vec3d(posX, posY, posZ), entity.getEntityBoundingBox());
        return EntityUtil.getBlastReduction(entity, EntityUtil.getDamageMultiplied((float)((v * v + v) / 2.0 * 85.0 + 1.0)), new Explosion((World)EntityUtil.mc.world, null, posX, posY, posZ, 6.0f, false, true));
    }

    public static float getBlastReduction(EntityLivingBase entity, float damageI, Explosion explosion) {
        float damage = damageI;
        DamageSource ds = DamageSource.causeExplosionDamage((Explosion)explosion);
        damage = CombatRules.getDamageAfterAbsorb((float)damage, (float)entity.getTotalArmorValue(), (float)((float)entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue()));
        int k = EnchantmentHelper.getEnchantmentModifierDamage((Iterable)entity.getArmorInventoryList(), (DamageSource)ds);
        damage *= 1.0f - MathHelper.clamp((float)k, (float)0.0f, (float)20.0f) / 25.0f;
        if (entity.isPotionActive(MobEffects.RESISTANCE)) {
            damage -= damage / 4.0f;
        }
        return damage;
    }

    public static float getDamageMultiplied(float damage) {
        int diff = EntityUtil.mc.world.getDifficulty().getId();
        return damage * (diff == 0 ? 0.0f : (diff == 2 ? 1.0f : (diff == 1 ? 0.5f : 1.5f)));
    }

    public static boolean isDead(Entity entity) {
        return EntityUtil.getHealth(entity) <= 0.0f || entity.isDead;
    }

    public static float getHealth(Entity entity) {
        if (entity instanceof EntityPlayer) {
            return ((EntityPlayer)entity).getHealth() + ((EntityPlayer)entity).getAbsorptionAmount();
        }
        return 0.0f;
    }

    public static double getBlockDensity(boolean blockDestruction, Vec3d vector, AxisAlignedBB bb) {
        double diffX = 1.0 / ((bb.maxX - bb.minX) * 2.0 + 1.0);
        double diffY = 1.0 / ((bb.maxY - bb.minY) * 2.0 + 1.0);
        double diffZ = 1.0 / ((bb.maxZ - bb.minZ) * 2.0 + 1.0);
        double diffHorizontal = (1.0 - Math.floor(1.0 / diffX) * diffX) / 2.0;
        double diffTranslational = (1.0 - Math.floor(1.0 / diffZ) * diffZ) / 2.0;
        if (diffX >= 0.0 && diffY >= 0.0 && diffZ >= 0.0) {
            float solid = 0.0f;
            float nonSolid = 0.0f;
            for (double x = 0.0; x <= 1.0; x += diffX) {
                for (double y = 0.0; y <= 1.0; y += diffY) {
                    for (double z = 0.0; z <= 1.0; z += diffZ) {
                        double scaledDiffX = bb.minX + (bb.maxX - bb.minX) * x;
                        double scaledDiffY = bb.minY + (bb.maxY - bb.minY) * y;
                        double scaledDiffZ = bb.minZ + (bb.maxZ - bb.minZ) * z;
                        if (!EntityUtil.isSolid(new Vec3d(scaledDiffX + diffHorizontal, scaledDiffY, scaledDiffZ + diffTranslational), vector, blockDestruction)) {
                            solid += 1.0f;
                        }
                        nonSolid += 1.0f;
                    }
                }
            }
            return solid / nonSolid;
        }
        return 0.0;
    }

    public static boolean isNaked(EntityPlayer player) {
        for (ItemStack stack : player.inventory.armorInventory) {
            if (stack == null || stack.isEmpty()) continue;
            return false;
        }
        return true;
    }

    public static boolean isSolid(Vec3d start, Vec3d end, boolean blockDestruction) {
        int currX = MathHelper.floor((double)start.x);
        int currY = MathHelper.floor((double)start.y);
        int currZ = MathHelper.floor((double)start.z);
        int endX = MathHelper.floor((double)end.x);
        int endY = MathHelper.floor((double)end.y);
        int endZ = MathHelper.floor((double)end.z);
        BlockPos blockPos = new BlockPos(currX, currY, currZ);
        IBlockState blockState = EntityUtil.mc.world.getBlockState(blockPos);
        Block block = blockState.getBlock();
        if (blockState.getCollisionBoundingBox((IBlockAccess)EntityUtil.mc.world, blockPos) != Block.NULL_AABB && block.canCollideCheck(blockState, false) && (BlockUtil.resistantBlocks.contains(block) || BlockUtil.unbreakableBlocks.contains(block)) | !blockDestruction) {
            return true;
        }
        double seDeltaX = end.x - start.x;
        double seDeltaY = end.y - start.y;
        double seDeltaZ = end.z - start.z;
        int steps = 200;
        while (steps-- >= 0) {
            EnumFacing facing;
            boolean unboundedX = true;
            boolean unboundedY = true;
            boolean unboundedZ = true;
            double stepX = 999.0;
            double stepY = 999.0;
            double stepZ = 999.0;
            double deltaX = 999.0;
            double deltaY = 999.0;
            double deltaZ = 999.0;
            if (endX > currX) {
                stepX = currX + 1;
            } else if (endX < currX) {
                stepX = currX;
            } else {
                unboundedX = false;
            }
            if (endY > currY) {
                stepY = (double)currY + 1.0;
            } else if (endY < currY) {
                stepY = currY;
            } else {
                unboundedY = false;
            }
            if (endZ > currZ) {
                stepZ = (double)currZ + 1.0;
            } else if (endZ < currZ) {
                stepZ = currZ;
            } else {
                unboundedZ = false;
            }
            if (unboundedX) {
                deltaX = (stepX - start.x) / seDeltaX;
            }
            if (unboundedY) {
                deltaY = (stepY - start.y) / seDeltaY;
            }
            if (unboundedZ) {
                deltaZ = (stepZ - start.z) / seDeltaZ;
            }
            if (deltaX == 0.0) {
                deltaX = -1.0E-4;
            }
            if (deltaY == 0.0) {
                deltaY = -1.0E-4;
            }
            if (deltaZ == 0.0) {
                deltaZ = -1.0E-4;
            }
            if (deltaX < deltaY && deltaX < deltaZ) {
                facing = endX > currX ? EnumFacing.WEST : EnumFacing.EAST;
                start = new Vec3d(stepX, start.y + seDeltaY * deltaX, start.z + seDeltaZ * deltaX);
            } else if (deltaY < deltaZ) {
                facing = endY > currY ? EnumFacing.DOWN : EnumFacing.UP;
                start = new Vec3d(start.x + seDeltaX * deltaY, stepY, start.z + seDeltaZ * deltaY);
            } else {
                facing = endZ > currZ ? EnumFacing.NORTH : EnumFacing.SOUTH;
                start = new Vec3d(start.x + seDeltaX * deltaZ, start.y + seDeltaY * deltaZ, stepZ);
            }
            if (!(block = (blockState = EntityUtil.mc.world.getBlockState(blockPos = new BlockPos(currX = MathHelper.floor((double)start.x) - (facing == EnumFacing.EAST ? 1 : 0), currY = MathHelper.floor((double)start.y) - (facing == EnumFacing.UP ? 1 : 0), currZ = MathHelper.floor((double)start.z) - (facing == EnumFacing.SOUTH ? 1 : 0)))).getBlock()).canCollideCheck(blockState, false) || !BlockUtil.resistantBlocks.contains(block) && !BlockUtil.unbreakableBlocks.contains(block) && blockDestruction) continue;
            return true;
        }
        return false;
    }

    public static Entity requirePositionEntity() {
        return Objects.requireNonNull(PositionUtil.getPositionEntity());
    }

    public static boolean inLiquid(boolean feet) {
        return EntityUtil.inLiquid(MathHelper.floor((double)(EntityUtil.requirePositionEntity().getEntityBoundingBox().minY - (feet ? 0.03 : 0.2))));
    }

    private static boolean inLiquid(int y) {
        return EntityUtil.findState(BlockLiquid.class, y) != null;
    }

    private static IBlockState findState(Class<? extends Block> block, int y) {
        Entity entity = EntityUtil.requirePositionEntity();
        int startX = MathHelper.floor((double)entity.getEntityBoundingBox().minX);
        int startZ = MathHelper.floor((double)entity.getEntityBoundingBox().minZ);
        int endX = MathHelper.ceil((double)entity.getEntityBoundingBox().maxX);
        int endZ = MathHelper.ceil((double)entity.getEntityBoundingBox().maxZ);
        for (int x = startX; x < endX; ++x) {
            for (int z = startZ; z < endZ; ++z) {
                IBlockState s = EntityUtil.mc.world.getBlockState(new BlockPos(x, y, z));
                if (!block.isInstance(s.getBlock())) continue;
                return s;
            }
        }
        return null;
    }

    public static EntityPlayer getClosestEnemy(BlockPos pos, List<EntityPlayer> list) {
        return EntityUtil.getClosestEnemy(pos.getX(), pos.getY(), pos.getZ(), list);
    }

    public static EntityPlayer getClosestEnemy(double x, double y, double z, List<EntityPlayer> players) {
        EntityPlayer closest = null;
        double distance = 3.4028234663852886E38;
        for (EntityPlayer player : players) {
            double dist;
            if (player == null || EntityUtil.isDead((Entity)player) || player.equals((Object)EntityUtil.mc.player) || Managers.FRIEND.isFriend(player) || !((dist = player.getDistanceSq(x, y, z)) < distance)) continue;
            closest = player;
            distance = dist;
        }
        return closest;
    }

    public static boolean isOnBurrow(EntityPlayer player) {
        BlockPos pos = PositionUtil.getPosition((Entity)player);
        return EntityUtil.isBurrow(pos, player) || EntityUtil.isBurrow(pos.up(), player);
    }

    public static boolean isBurrow(BlockPos pos, EntityPlayer player) {
        IBlockState state = EntityUtil.mc.world.getBlockState(pos);
        return burrowList.contains(state.getBlock()) && state.getBoundingBox((IBlockAccess)EntityUtil.mc.world, (BlockPos)pos).offset((BlockPos)pos).maxY > player.posY;
    }

    public static boolean isPassiveMob(Entity entity) {
        if (entity instanceof EntityWolf) {
            return !((EntityWolf)entity).isAngry();
        }
        if (entity instanceof EntityIronGolem) {
            return ((EntityIronGolem)entity).getRevengeTarget() == null;
        }
        return entity instanceof EntityAgeable || entity instanceof EntityAmbientCreature || entity instanceof EntitySquid;
    }

    public static boolean isHostileMob(Entity entity) {
        return entity.isCreatureType(EnumCreatureType.MONSTER, false) && !EntityUtil.isNeutralMob(entity) || entity instanceof EntitySpider;
    }

    public static boolean isNeutralMob(Entity entity) {
        return entity instanceof EntityPigZombie && !((EntityPigZombie)entity).isAngry() || entity instanceof EntityWolf && !((EntityWolf)entity).isAngry() || entity instanceof EntityEnderman && ((EntityEnderman)entity).isScreaming();
    }

    public static void swingClient() {
        if (!EntityUtil.mc.player.isSwingInProgress || EntityUtil.mc.player.swingProgressInt >= ((IEntityLiving)EntityUtil.mc.player).getArmSwingAnim() / 2 || EntityUtil.mc.player.swingProgressInt < 0) {
            EntityUtil.mc.player.swingProgressInt = -1;
            EntityUtil.mc.player.isSwingInProgress = true;
            EntityUtil.mc.player.swingingHand = EnumHand.MAIN_HAND;
        }
    }
}

