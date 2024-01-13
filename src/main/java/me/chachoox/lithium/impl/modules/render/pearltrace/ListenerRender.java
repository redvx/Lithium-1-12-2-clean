/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.Vec3d
 *  org.lwjgl.opengl.GL11
 */
package me.chachoox.lithium.impl.modules.render.pearltrace;

import me.chachoox.lithium.api.util.render.Interpolation;
import me.chachoox.lithium.api.util.render.RenderUtil;
import me.chachoox.lithium.impl.event.events.render.main.Render3DEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.render.pearltrace.PearlTrace;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class ListenerRender
extends ModuleListener<PearlTrace, Render3DEvent> {
    public ListenerRender(PearlTrace module) {
        super(module, Render3DEvent.class);
    }

    @Override
    public void call(Render3DEvent event) {
        double x = Interpolation.getRenderPosX();
        double y = Interpolation.getRenderPosY();
        double z = Interpolation.getRenderPosZ();
        RenderUtil.startRender();
        GL11.glLineWidth((float)((Float)((PearlTrace)this.module).width.getValue()).floatValue());
        GL11.glColor4f((float)((float)((PearlTrace)this.module).getColor().getRed() / 255.0f), (float)((float)((PearlTrace)this.module).getColor().getGreen() / 255.0f), (float)((float)((PearlTrace)this.module).getColor().getBlue() / 255.0f), (float)((float)((PearlTrace)this.module).getColor().getAlpha() / 255.0f));
        ((PearlTrace)this.module).thrownEntities.forEach((id, thrownEntity) -> {
            GL11.glBegin((int)3);
            for (Vec3d vertex : thrownEntity.getVertices()) {
                Vec3d vec = vertex.subtract(x, y, z);
                GL11.glVertex3d((double)vec.x, (double)vec.y, (double)vec.z);
            }
            GL11.glEnd();
        });
        RenderUtil.endRender();
    }
}

