/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.culling.Frustum
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 */
package me.chachoox.lithium.impl.modules.render.holeesp;

import me.chachoox.lithium.api.util.colors.ColorUtil;
import me.chachoox.lithium.api.util.render.Interpolation;
import me.chachoox.lithium.api.util.render.RenderUtil;
import me.chachoox.lithium.impl.event.events.render.main.Render3DEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.render.holeesp.HoleESP;
import me.chachoox.lithium.impl.modules.render.holeesp.mode.HolesMode;
import me.chachoox.lithium.impl.modules.render.holeesp.util.TwoBlockPos;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class ListenerRender
extends ModuleListener<HoleESP, Render3DEvent> {
    public ListenerRender(HoleESP module) {
        super(module, Render3DEvent.class);
    }

    @Override
    public void call(Render3DEvent event) {
        if (((HoleESP)this.module).holes.getValue() != HolesMode.VOID) {
            if (((HoleESP)this.module).holeTimer.passed(((Integer)((HoleESP)this.module).updates.getValue()).intValue())) {
                ((HoleESP)this.module).calcHoles();
                ((HoleESP)this.module).holeTimer.reset();
            }
            RenderUtil.startRender();
            this.drawBedrockHole();
            this.drawObbyHole();
            this.drawTwoByOneBedrockHole();
            this.drawTwoByOneObbyHole();
            RenderUtil.endRender();
        }
        if (((HoleESP)this.module).holes.getValue() != HolesMode.HOLE) {
            if (ListenerRender.mc.player.dimension == 1) {
                return;
            }
            if (ListenerRender.mc.player.getPosition().getY() > (Integer)((HoleESP)this.module).range.getValue() || ListenerRender.mc.player.getPosition().getY() < -10) {
                return;
            }
            if (((HoleESP)this.module).voidTimer.passed(((Integer)((HoleESP)this.module).updates.getValue()).intValue())) {
                ((HoleESP)this.module).voidHoles.clear();
                ((HoleESP)this.module).voidHoles = ((HoleESP)this.module).findVoidHoles();
                ((HoleESP)this.module).voidTimer.reset();
            }
            this.drawVoidHoles();
        }
    }

    private void drawObbyHole() {
        for (BlockPos pos : ((HoleESP)this.module).obbyHoles) {
            if (!this.isInFrustum(pos)) continue;
            AxisAlignedBB bb = Interpolation.interpolatePos(pos, this.getProperHeight());
            if (((HoleESP)this.module).fade.getValue().booleanValue()) {
                ((HoleESP)this.module).drawFade(bb, ((HoleESP)this.module).getObbyColor());
                continue;
            }
            RenderUtil.drawBox(bb, ((HoleESP)this.module).getObbyColor());
            ((HoleESP)this.module).drawOutline(bb, ((Float)((HoleESP)this.module).lineWidth.getValue()).floatValue(), ColorUtil.changeAlpha(((HoleESP)this.module).getObbyColor(), (Integer)((HoleESP)this.module).wireAlpha.getValue()));
        }
    }

    private void drawBedrockHole() {
        for (BlockPos pos : ((HoleESP)this.module).bedrockHoles) {
            if (!this.isInFrustum(pos)) continue;
            AxisAlignedBB bb = Interpolation.interpolatePos(pos, this.getProperHeight());
            if (((HoleESP)this.module).fade.getValue().booleanValue()) {
                ((HoleESP)this.module).drawFade(bb, ((HoleESP)this.module).getBedrockColor());
                continue;
            }
            RenderUtil.drawBox(bb, ((HoleESP)this.module).getBedrockColor());
            ((HoleESP)this.module).drawOutline(bb, ((Float)((HoleESP)this.module).lineWidth.getValue()).floatValue(), ColorUtil.changeAlpha(((HoleESP)this.module).getBedrockColor(), (Integer)((HoleESP)this.module).wireAlpha.getValue()));
        }
    }

    private void drawTwoByOneObbyHole() {
        for (TwoBlockPos pos : ((HoleESP)this.module).obbyHolesTwoBlock) {
            if (!this.isInFrustum(pos.getOne()) && !this.isInFrustum(pos.getTwo())) continue;
            AxisAlignedBB bb = new AxisAlignedBB((double)pos.getOne().getX() - ListenerRender.mc.getRenderManager().viewerPosX, (double)pos.getOne().getY() - ListenerRender.mc.getRenderManager().viewerPosY, (double)pos.getOne().getZ() - ListenerRender.mc.getRenderManager().viewerPosZ, (double)(pos.getTwo().getX() + 1) - ListenerRender.mc.getRenderManager().viewerPosX, (double)((float)pos.getTwo().getY() + this.getProperHeightTwoByOne()) - ListenerRender.mc.getRenderManager().viewerPosY, (double)(pos.getTwo().getZ() + 1) - ListenerRender.mc.getRenderManager().viewerPosZ);
            if (((HoleESP)this.module).fade.getValue().booleanValue()) {
                ((HoleESP)this.module).drawFade(bb, ((HoleESP)this.module).getObbyColor());
                continue;
            }
            RenderUtil.drawBox(bb, ((HoleESP)this.module).getObbyColor());
            ((HoleESP)this.module).drawOutline(bb, ((Float)((HoleESP)this.module).lineWidth.getValue()).floatValue(), ColorUtil.changeAlpha(((HoleESP)this.module).getObbyColor(), (Integer)((HoleESP)this.module).wireAlpha.getValue()));
        }
    }

    private void drawTwoByOneBedrockHole() {
        for (TwoBlockPos pos : ((HoleESP)this.module).bedrockHolesTwoBlock) {
            if (!this.isInFrustum(pos.getOne()) && !this.isInFrustum(pos.getTwo())) continue;
            AxisAlignedBB bb = new AxisAlignedBB((double)pos.getOne().getX() - ListenerRender.mc.getRenderManager().viewerPosX, (double)pos.getOne().getY() - ListenerRender.mc.getRenderManager().viewerPosY, (double)pos.getOne().getZ() - ListenerRender.mc.getRenderManager().viewerPosZ, (double)(pos.getTwo().getX() + 1) - ListenerRender.mc.getRenderManager().viewerPosX, (double)((float)pos.getTwo().getY() + this.getProperHeightTwoByOne()) - ListenerRender.mc.getRenderManager().viewerPosY, (double)(pos.getTwo().getZ() + 1) - ListenerRender.mc.getRenderManager().viewerPosZ);
            if (((HoleESP)this.module).fade.getValue().booleanValue()) {
                ((HoleESP)this.module).drawFade(bb, ((HoleESP)this.module).getBedrockColor());
                continue;
            }
            RenderUtil.drawBox(bb, ((HoleESP)this.module).getBedrockColor());
            ((HoleESP)this.module).drawOutline(bb, ((Float)((HoleESP)this.module).lineWidth.getValue()).floatValue(), ColorUtil.changeAlpha(((HoleESP)this.module).getBedrockColor(), (Integer)((HoleESP)this.module).wireAlpha.getValue()));
        }
    }

    private void drawVoidHoles() {
        for (BlockPos hole : ((HoleESP)this.module).voidHoles) {
            if (!this.isInFrustum(hole)) continue;
            AxisAlignedBB bb = Interpolation.interpolatePos(hole, 0.0f);
            RenderUtil.startRender();
            RenderUtil.drawBox(bb, ((HoleESP)this.module).getVoidColor());
            RenderUtil.drawOutline(bb, ((Float)((HoleESP)this.module).lineWidth.getValue()).floatValue(), ColorUtil.changeAlpha(((HoleESP)this.module).getVoidColor(), (Integer)((HoleESP)this.module).wireAlpha.getValue()));
            RenderUtil.endRender();
        }
    }

    private float getProperHeight() {
        return ((HoleESP)this.module).fade.getValue() != false ? ((Float)((HoleESP)this.module).fadeHeight.getValue()).floatValue() : ((Float)((HoleESP)this.module).height.getValue()).floatValue();
    }

    private float getProperHeightTwoByOne() {
        return ((HoleESP)this.module).fade.getValue() != false ? ((Float)((HoleESP)this.module).fadeHeight.getValue()).floatValue() : ((Float)((HoleESP)this.module).doubleHeight.getValue()).floatValue();
    }

    public boolean isInFrustum(BlockPos pos) {
        Entity renderEntity = RenderUtil.getEntity();
        if (renderEntity == null) {
            return false;
        }
        if (renderEntity.getDistanceSq(pos) < 1.5) {
            return true;
        }
        return this.isInFrustum((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5);
    }

    public boolean isInFrustum(double x, double y, double z) {
        Entity renderEntity = RenderUtil.getEntity();
        if (renderEntity == null) {
            return false;
        }
        Frustum frustum = Interpolation.createFrustum(renderEntity);
        return frustum.isBoundingBoxInFrustum(new AxisAlignedBB(x - 1.0, y - 1.0, x - 1.0, x + 1.0, y + 1.0, z + 1.0));
    }
}

