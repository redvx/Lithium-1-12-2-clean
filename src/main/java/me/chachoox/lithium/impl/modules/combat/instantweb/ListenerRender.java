/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockAir
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 */
package me.chachoox.lithium.impl.modules.combat.instantweb;

import me.chachoox.lithium.api.util.render.RenderUtil;
import me.chachoox.lithium.impl.event.events.render.main.Render3DEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.combat.instantweb.InstantWeb;
import me.chachoox.lithium.impl.modules.other.blocks.BlocksManager;
import me.chachoox.lithium.impl.modules.other.colours.Colours;
import net.minecraft.block.BlockAir;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class ListenerRender
extends ModuleListener<InstantWeb, Render3DEvent> {
    public ListenerRender(InstantWeb module) {
        super(module, Render3DEvent.class);
    }

    @Override
    public void call(Render3DEvent event) {
        if (BlocksManager.get().debugWeb().booleanValue() && ((InstantWeb)this.module).target != null) {
            for (BlockPos pos : ((InstantWeb)this.module).getPlacements()) {
                if (!(ListenerRender.mc.world.getBlockState(pos).getBlock() instanceof BlockAir)) continue;
                AxisAlignedBB bb = new AxisAlignedBB((double)pos.getX() - ListenerRender.mc.getRenderManager().viewerPosX, (double)pos.getY() - ListenerRender.mc.getRenderManager().viewerPosY, (double)pos.getZ() - ListenerRender.mc.getRenderManager().viewerPosZ, (double)(pos.getX() + 1) - ListenerRender.mc.getRenderManager().viewerPosX, (double)(pos.getY() + 1) - ListenerRender.mc.getRenderManager().viewerPosY, (double)(pos.getZ() + 1) - ListenerRender.mc.getRenderManager().viewerPosZ);
                RenderUtil.startRender();
                RenderUtil.drawBox(bb, Colours.get().getColourCustomAlpha(40));
                RenderUtil.drawOutline(bb, 1.3f, Colours.get().getColour());
                RenderUtil.endRender();
            }
        }
    }
}

