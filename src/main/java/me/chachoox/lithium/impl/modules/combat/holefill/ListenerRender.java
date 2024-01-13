/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockAir
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 */
package me.chachoox.lithium.impl.modules.combat.holefill;

import java.util.stream.Collectors;
import me.chachoox.lithium.api.util.entity.EntityUtil;
import me.chachoox.lithium.api.util.math.MathUtil;
import me.chachoox.lithium.api.util.render.RenderUtil;
import me.chachoox.lithium.impl.event.events.render.main.Render3DEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.combat.holefill.HoleFill;
import me.chachoox.lithium.impl.modules.other.blocks.BlocksManager;
import me.chachoox.lithium.impl.modules.other.colours.Colours;
import net.minecraft.block.BlockAir;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class ListenerRender
extends ModuleListener<HoleFill, Render3DEvent> {
    public ListenerRender(HoleFill module) {
        super(module, Render3DEvent.class);
    }

    @Override
    public void call(Render3DEvent event) {
        if (BlocksManager.get().debugHoleFill().booleanValue() && !((HoleFill)this.module).holes.isEmpty()) {
            for (BlockPos pos : ((HoleFill)this.module).holes) {
                if (!(ListenerRender.mc.world.getBlockState(pos).getBlock() instanceof BlockAir)) continue;
                EntityPlayer nearest = EntityUtil.getClosestEnemy(pos, ListenerRender.mc.world.getLoadedEntityList().stream().filter(entity -> entity instanceof EntityPlayer).map(entity -> (EntityPlayer)entity).collect(Collectors.toList()));
                if (((HoleFill)this.module).smart.getValue().booleanValue()) {
                    if (nearest == null) {
                        return;
                    }
                    if (!(nearest.getDistanceSqToCenter(pos) <= (double)MathUtil.square(((Float)((HoleFill)this.module).enemyRange.getValue()).floatValue()))) {
                        return;
                    }
                    if (!(nearest.getDistanceSqToCenter(pos) <= (double)MathUtil.square(((Float)((HoleFill)this.module).horizontal.getValue()).floatValue()))) {
                        return;
                    }
                }
                AxisAlignedBB bb = new AxisAlignedBB((double)pos.getX() - ListenerRender.mc.getRenderManager().viewerPosX, (double)pos.getY() - ListenerRender.mc.getRenderManager().viewerPosY, (double)pos.getZ() - ListenerRender.mc.getRenderManager().viewerPosZ, (double)(pos.getX() + 1) - ListenerRender.mc.getRenderManager().viewerPosX, (double)(pos.getY() + 1) - ListenerRender.mc.getRenderManager().viewerPosY, (double)(pos.getZ() + 1) - ListenerRender.mc.getRenderManager().viewerPosZ);
                RenderUtil.startRender();
                RenderUtil.drawBox(bb, Colours.get().getColourCustomAlpha(40));
                RenderUtil.drawOutline(bb, 1.3f, Colours.get().getColour());
                RenderUtil.endRender();
            }
        }
    }
}

