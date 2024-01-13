/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.RayTraceResult$Type
 *  net.minecraft.world.World
 */
package me.chachoox.lithium.impl.modules.render.blockhighlight;

import me.chachoox.lithium.api.util.render.Interpolation;
import me.chachoox.lithium.api.util.render.RenderUtil;
import me.chachoox.lithium.impl.event.events.render.main.Render3DEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.player.fastbreak.FastBreak;
import me.chachoox.lithium.impl.modules.render.blockhighlight.BlockHighlight;
import me.chachoox.lithium.impl.modules.render.blockhighlight.mode.RenderMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ListenerRender
extends ModuleListener<BlockHighlight, Render3DEvent> {
    public ListenerRender(BlockHighlight module) {
        super(module, Render3DEvent.class);
    }

    @Override
    public void call(Render3DEvent event) {
        if (ListenerRender.mc.objectMouseOver != null && ListenerRender.mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos pos = ListenerRender.mc.objectMouseOver.getBlockPos();
            Entity player = mc.getRenderViewEntity();
            if (player != null && !ListenerRender.mc.objectMouseOver.getBlockPos().equals((Object)Managers.MODULE.get(FastBreak.class).getPos())) {
                IBlockState state = ListenerRender.mc.world.getBlockState(pos);
                AxisAlignedBB bb = Interpolation.interpolateAxis(state.getSelectedBoundingBox((World)ListenerRender.mc.world, pos).grow((double)0.002f));
                RenderUtil.startRender();
                switch ((RenderMode)((Object)((BlockHighlight)this.module).renderMode.getValue())) {
                    case OUTLINE: {
                        RenderUtil.drawOutline(bb, ((Float)((BlockHighlight)this.module).lineWidth.getValue()).floatValue(), ((BlockHighlight)this.module).getOutlineColor());
                        break;
                    }
                    case BOX: {
                        RenderUtil.drawBox(bb, ((BlockHighlight)this.module).getBoxColor());
                        break;
                    }
                    case BOTH: {
                        RenderUtil.drawBox(bb, ((BlockHighlight)this.module).getBoxColor());
                        RenderUtil.drawOutline(bb, ((Float)((BlockHighlight)this.module).lineWidth.getValue()).floatValue(), ((BlockHighlight)this.module).getOutlineColor());
                    }
                }
                RenderUtil.endRender();
            }
        }
    }
}

