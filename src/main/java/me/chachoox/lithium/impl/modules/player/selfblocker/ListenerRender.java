/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockAir
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 */
package me.chachoox.lithium.impl.modules.player.selfblocker;

import me.chachoox.lithium.api.util.render.RenderUtil;
import me.chachoox.lithium.impl.event.events.render.main.Render3DEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.other.blocks.BlocksManager;
import me.chachoox.lithium.impl.modules.other.colours.Colours;
import me.chachoox.lithium.impl.modules.player.selfblocker.SelfBlocker;
import net.minecraft.block.BlockAir;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class ListenerRender
extends ModuleListener<SelfBlocker, Render3DEvent> {
    public ListenerRender(SelfBlocker module) {
        super(module, Render3DEvent.class);
    }

    @Override
    public void call(Render3DEvent event) {
        if (BlocksManager.get().debugSelfBlocker().booleanValue()) {
            AxisAlignedBB bb;
            for (BlockPos pos : ((SelfBlocker)this.module).getPlacements()) {
                if (!(ListenerRender.mc.world.getBlockState(pos).getBlock() instanceof BlockAir)) continue;
                bb = new AxisAlignedBB((double)pos.getX() - ListenerRender.mc.getRenderManager().viewerPosX, (double)pos.getY() - ListenerRender.mc.getRenderManager().viewerPosY, (double)pos.getZ() - ListenerRender.mc.getRenderManager().viewerPosZ, (double)(pos.getX() + 1) - ListenerRender.mc.getRenderManager().viewerPosX, (double)(pos.getY() + 1) - ListenerRender.mc.getRenderManager().viewerPosY, (double)(pos.getZ() + 1) - ListenerRender.mc.getRenderManager().viewerPosZ);
                RenderUtil.startRender();
                RenderUtil.drawBox(bb, Colours.get().getColourCustomAlpha(40));
                RenderUtil.drawOutline(bb, 1.3f, Colours.get().getColour());
                RenderUtil.endRender();
            }
            for (BlockPos pos : ((SelfBlocker)this.module).getPlacements()) {
                if (!(ListenerRender.mc.world.getBlockState(pos).getBlock() instanceof BlockAir)) continue;
                bb = new AxisAlignedBB((double)pos.getX() - ListenerRender.mc.getRenderManager().viewerPosX, (double)pos.getY() - ListenerRender.mc.getRenderManager().viewerPosY, (double)pos.getZ() - ListenerRender.mc.getRenderManager().viewerPosZ, (double)(pos.getX() + 1) - ListenerRender.mc.getRenderManager().viewerPosX, (double)(pos.getY() + 1) - ListenerRender.mc.getRenderManager().viewerPosY, (double)(pos.getZ() + 1) - ListenerRender.mc.getRenderManager().viewerPosZ);
                RenderUtil.startRender();
                RenderUtil.drawBox(bb, Colours.get().getColourCustomAlpha(40));
                RenderUtil.drawOutline(bb, 1.3f, Colours.get().getColour());
                RenderUtil.endRender();
            }
        }
    }
}

