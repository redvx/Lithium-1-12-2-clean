/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.material.MapColor
 *  net.minecraft.block.material.Material
 *  net.minecraft.block.state.BlockStateContainer
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.World
 */
package me.chachoox.lithium.asm.mixins.block;

import java.util.Arrays;
import java.util.List;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.asm.ducks.IBlock;
import me.chachoox.lithium.impl.event.events.blocks.CollisionEvent;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.movement.jesus.Jesus;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Block.class})
public abstract class MixinBlock
implements IBlock,
Minecraftable {
    private final String[] harvestToolNonForge = new String[16];
    private final int[] harvestLevelNonForge = new int[16];
    @Shadow
    private float blockHardness;

    @Shadow
    public abstract BlockStateContainer getBlockState();

    @Shadow
    public abstract int getMetaFromState(IBlockState var1);

    @Shadow
    protected static void addCollisionBoxToList(BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> cBoxes, AxisAlignedBB blockBox) {
        throw new IllegalStateException("MixinBlock.addCollisionBoxToList has not been shadowed");
    }

    @Override
    public float getHardness() {
        return this.blockHardness;
    }

    @Override
    @Unique
    public void setHarvestLevelNonForge(String toolClass, int level) {
        for (IBlockState state : this.getBlockState().getValidStates()) {
            int idx = this.getMetaFromState(state);
            this.harvestToolNonForge[idx] = toolClass;
            this.harvestLevelNonForge[idx] = level;
        }
    }

    @Override
    @Unique
    public String getHarvestToolNonForge(IBlockState state) {
        return this.harvestToolNonForge[this.getMetaFromState(state)];
    }

    @Override
    @Unique
    public int getHarvestLevelNonForge(IBlockState state) {
        return this.harvestLevelNonForge[this.getMetaFromState(state)];
    }

    @Inject(method={"<init>(Lnet/minecraft/block/material/Material;Lnet/minecraft/block/material/MapColor;)V"}, at={@At(value="RETURN")})
    public void ctrHook(Material blockMaterialIn, MapColor blockMapColorIn, CallbackInfo ci) {
        Arrays.fill(this.harvestLevelNonForge, -1);
    }

    @Deprecated
    @Inject(method={"addCollisionBoxToList(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/AxisAlignedBB;Ljava/util/List;Lnet/minecraft/entity/Entity;Z)V"}, at={@At(value="HEAD")}, cancellable=true)
    public void addCollisionBoxToListHookPre(IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> cBoxes, Entity entity, boolean isActualState, CallbackInfo info) {
        Jesus JESUS = Managers.MODULE.get(Jesus.class);
        if (!JESUS.isEnabled()) {
            return;
        }
        Block block = (Block)Block.class.cast(this);
        AxisAlignedBB bb = block.getCollisionBoundingBox(state, (IBlockAccess)world, pos);
        CollisionEvent event = new CollisionEvent(pos, bb, entity, block);
        JESUS.onCollision(event);
        if (bb != event.getBB()) {
            bb = event.getBB();
        }
        if (bb != null && entityBox.intersects(bb)) {
            cBoxes.add(bb);
        }
        MixinBlock.addCollisionBoxToList(pos, entityBox, cBoxes, bb);
        info.cancel();
    }

    @Inject(method={"addCollisionBoxToList(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/AxisAlignedBB;Ljava/util/List;Lnet/minecraft/util/math/AxisAlignedBB;)V"}, at={@At(value="HEAD")}, cancellable=true)
    private static void addCollisionBoxToListHook(BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> cBoxes, AxisAlignedBB blockBox, CallbackInfo info) {
        Jesus JESUS = Managers.MODULE.get(Jesus.class);
        if (blockBox != Block.NULL_AABB && JESUS.isEnabled()) {
            AxisAlignedBB bb = blockBox.offset(pos);
            CollisionEvent event = new CollisionEvent(pos, bb, null, MixinBlock.mc.world != null ? MixinBlock.mc.world.getBlockState(pos).getBlock() : null);
            JESUS.onCollision(event);
            if (bb != event.getBB()) {
                bb = event.getBB();
            }
            if (bb != null && entityBox.intersects(bb)) {
                cBoxes.add(bb);
            }
            info.cancel();
        }
    }
}

