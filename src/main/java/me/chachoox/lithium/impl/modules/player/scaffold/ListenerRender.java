/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.util.math.AxisAlignedBB
 */
package me.chachoox.lithium.impl.modules.player.scaffold;

import me.chachoox.lithium.api.util.render.RenderUtil;
import me.chachoox.lithium.impl.event.events.render.main.Render3DEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.other.blocks.BlocksManager;
import me.chachoox.lithium.impl.modules.other.colours.Colours;
import me.chachoox.lithium.impl.modules.player.scaffold.Scaffold;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.AxisAlignedBB;

public class ListenerRender
extends ModuleListener<Scaffold, Render3DEvent> {
    public ListenerRender(Scaffold module) {
        super(module, Render3DEvent.class);
    }

    @Override
    public void call(Render3DEvent event) {
        for (int i = 9; i >= 0; --i) {
            if (!(ListenerRender.mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemBlock) || !((Scaffold)this.module).isValid(((ItemBlock)ListenerRender.mc.player.inventory.getStackInSlot(i).getItem()).getBlock()) || !BlocksManager.get().debugScaffold().booleanValue() || ((Scaffold)this.module).pos == null) continue;
            AxisAlignedBB bb = new AxisAlignedBB((double)((Scaffold)this.module).pos.getX() - ListenerRender.mc.getRenderManager().viewerPosX, (double)((Scaffold)this.module).pos.getY() - ListenerRender.mc.getRenderManager().viewerPosY, (double)((Scaffold)this.module).pos.getZ() - ListenerRender.mc.getRenderManager().viewerPosZ, (double)(((Scaffold)this.module).pos.getX() + 1) - ListenerRender.mc.getRenderManager().viewerPosX, (double)(((Scaffold)this.module).pos.getY() + 1) - ListenerRender.mc.getRenderManager().viewerPosY, (double)(((Scaffold)this.module).pos.getZ() + 1) - ListenerRender.mc.getRenderManager().viewerPosZ);
            RenderUtil.startRender();
            RenderUtil.drawBox(bb, Colours.get().getColourCustomAlpha(40));
            RenderUtil.drawOutline(bb, 1.3f, Colours.get().getColour());
            RenderUtil.endRender();
        }
    }
}

