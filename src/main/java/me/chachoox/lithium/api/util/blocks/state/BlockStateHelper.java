/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.tileentity.TileEntity
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.WorldType
 *  net.minecraft.world.biome.Biome
 *  net.minecraft.world.chunk.Chunk
 */
package me.chachoox.lithium.api.util.blocks.state;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.api.util.blocks.state.IBlockStateHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;

public class BlockStateHelper
implements Minecraftable,
IBlockStateHelper {
    private final Map<BlockPos, IBlockState> states;
    private final Supplier<IBlockAccess> world;

    public BlockStateHelper() {
        this(new HashMap<BlockPos, IBlockState>());
    }

    public BlockStateHelper(Supplier<IBlockAccess> world) {
        this(new HashMap<BlockPos, IBlockState>(), world);
    }

    public BlockStateHelper(Map<BlockPos, IBlockState> stateMap) {
        this(stateMap, () -> BlockStateHelper.mc.world);
    }

    public BlockStateHelper(Map<BlockPos, IBlockState> stateMap, Supplier<IBlockAccess> world) {
        this.states = stateMap;
        this.world = world;
    }

    public IBlockState getBlockState(BlockPos pos) {
        IBlockState state = this.states.get(pos);
        if (state == null) {
            return this.world.get().getBlockState(pos);
        }
        return state;
    }

    @Override
    public void addBlockState(BlockPos pos, IBlockState state) {
        this.states.putIfAbsent(pos.toImmutable(), state);
    }

    @Override
    public void delete(BlockPos pos) {
        this.states.remove(pos);
    }

    @Override
    public void clearAllStates() {
        this.states.clear();
    }

    public TileEntity getTileEntity(BlockPos pos) {
        return this.world.get().getTileEntity(pos);
    }

    public int getCombinedLight(BlockPos pos, int lightValue) {
        return this.world.get().getCombinedLight(pos, lightValue);
    }

    public boolean isAirBlock(BlockPos pos) {
        return this.getBlockState(pos).getBlock().isAir(this.getBlockState(pos), (IBlockAccess)this, pos);
    }

    public Biome getBiome(BlockPos pos) {
        return this.world.get().getBiome(pos);
    }

    public int getStrongPower(BlockPos pos, EnumFacing direction) {
        return this.getBlockState(pos).getStrongPower((IBlockAccess)this, pos, direction);
    }

    public WorldType getWorldType() {
        return this.world.get().getWorldType();
    }

    public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _default) {
        if (!BlockStateHelper.mc.world.isValid(pos)) {
            return _default;
        }
        Chunk chunk = BlockStateHelper.mc.world.getChunk(pos);
        if (chunk == null || chunk.isEmpty()) {
            return _default;
        }
        return this.getBlockState(pos).isSideSolid((IBlockAccess)this, pos, side);
    }
}

