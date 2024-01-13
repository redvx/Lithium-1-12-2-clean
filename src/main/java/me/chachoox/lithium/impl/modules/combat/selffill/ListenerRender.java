/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockAir
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.util.math.AxisAlignedBB
 */
package me.chachoox.lithium.impl.modules.combat.selffill;

import me.chachoox.lithium.api.util.movement.PositionUtil;
import me.chachoox.lithium.api.util.render.RenderUtil;
import me.chachoox.lithium.impl.event.events.render.main.Render3DEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.combat.selffill.SelfFill;
import me.chachoox.lithium.impl.modules.other.blocks.BlocksManager;
import me.chachoox.lithium.impl.modules.other.colours.Colours;
import net.minecraft.block.BlockAir;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.AxisAlignedBB;

public class ListenerRender
extends ModuleListener<SelfFill, Render3DEvent> {
    public ListenerRender(SelfFill module) {
        super(module, Render3DEvent.class);
    }

    @Override
    public void call(Render3DEvent event) {
        for (int i = 9; i >= 0; --i) {
            if (!(ListenerRender.mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemBlock) || !((SelfFill)this.module).isValid(((ItemBlock)ListenerRender.mc.player.inventory.getStackInSlot(i).getItem()).getBlock()) || !BlocksManager.get().debugSelfFill().booleanValue()) continue;
            if (!(ListenerRender.mc.world.getBlockState(PositionUtil.getPosition()).getBlock() instanceof BlockAir)) {
                return;
            }
            AxisAlignedBB bb = new AxisAlignedBB((double)PositionUtil.getPosition().getX() - ListenerRender.mc.getRenderManager().viewerPosX, (double)PositionUtil.getPosition().getY() - ListenerRender.mc.getRenderManager().viewerPosY, (double)PositionUtil.getPosition().getZ() - ListenerRender.mc.getRenderManager().viewerPosZ, (double)(PositionUtil.getPosition().getX() + 1) - ListenerRender.mc.getRenderManager().viewerPosX, (double)(PositionUtil.getPosition().getY() + 1) - ListenerRender.mc.getRenderManager().viewerPosY, (double)(PositionUtil.getPosition().getZ() + 1) - ListenerRender.mc.getRenderManager().viewerPosZ);
            RenderUtil.startRender();
            RenderUtil.drawBox(bb, Colours.get().getColourCustomAlpha(40));
            RenderUtil.drawOutline(bb, 1.3f, Colours.get().getColour());
            RenderUtil.endRender();
        }
    }
}

