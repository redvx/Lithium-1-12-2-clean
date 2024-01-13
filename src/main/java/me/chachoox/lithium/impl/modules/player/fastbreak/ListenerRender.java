/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.world.World
 */
package me.chachoox.lithium.impl.modules.player.fastbreak;

import java.awt.Color;
import me.chachoox.lithium.api.util.colors.ColorUtil;
import me.chachoox.lithium.api.util.render.Interpolation;
import me.chachoox.lithium.api.util.render.RenderUtil;
import me.chachoox.lithium.impl.event.events.render.main.Render3DEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.player.fastbreak.FastBreak;
import me.chachoox.lithium.impl.modules.player.fastbreak.mode.MineMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class ListenerRender
extends ModuleListener<FastBreak, Render3DEvent> {
    public ListenerRender(FastBreak module) {
        super(module, Render3DEvent.class);
    }

    @Override
    public void call(Render3DEvent event) {
        if (!ListenerRender.mc.player.capabilities.isCreativeMode && ((FastBreak)this.module).getPos() != null) {
            IBlockState state = ListenerRender.mc.world.getBlockState(((FastBreak)this.module).getPos());
            AxisAlignedBB bb = Interpolation.interpolateAxis(state.getSelectedBoundingBox((World)ListenerRender.mc.world, ((FastBreak)this.module).getPos()).grow((double)0.002f));
            if (((FastBreak)this.module).mode.getValue() != MineMode.INSTANT && ((FastBreak)this.module).getBlock() == Blocks.AIR) {
                return;
            }
            if (((FastBreak)this.module).isBlockValid(ListenerRender.mc.world.getBlockState(((FastBreak)this.module).getPos()).getBlock())) {
                RenderUtil.startRender();
                RenderUtil.drawBox(bb, this.getColorByDamage());
                RenderUtil.drawOutline(bb, ((Float)((FastBreak)this.module).lineWidth.getValue()).floatValue(), this.getWireColorByDamage());
                RenderUtil.endRender();
            }
        }
    }

    private Color getColorByDamage() {
        return ((FastBreak)this.module).maxDamage >= 0.9f ? ColorUtil.changeAlpha(Color.GREEN, (Integer)((FastBreak)this.module).alpha.getValue()) : ColorUtil.changeAlpha(Color.RED, (Integer)((FastBreak)this.module).alpha.getValue());
    }

    private Color getWireColorByDamage() {
        return ((FastBreak)this.module).maxDamage >= 0.9f ? ColorUtil.changeAlpha(Color.GREEN, (Integer)((FastBreak)this.module).lineAlpha.getValue()) : ColorUtil.changeAlpha(Color.RED, (Integer)((FastBreak)this.module).lineAlpha.getValue());
    }
}

