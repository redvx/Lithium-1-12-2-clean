/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.vecmath.Vector3d
 *  org.lwjgl.opengl.GL11
 */
package me.chachoox.lithium.impl.modules.player.fakelag;

import java.awt.Color;
import javax.vecmath.Vector3d;
import me.chachoox.lithium.asm.mixins.render.IRenderManager;
import me.chachoox.lithium.impl.event.events.render.main.Render3DEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.player.fakelag.FakeLag;
import org.lwjgl.opengl.GL11;

public class ListenerRender
extends ModuleListener<FakeLag, Render3DEvent> {
    public ListenerRender(FakeLag module) {
        super(module, Render3DEvent.class);
    }

    @Override
    public void call(Render3DEvent event) {
        if (ListenerRender.mc.player.posX != ListenerRender.mc.player.prevPosX || ListenerRender.mc.player.posY != ListenerRender.mc.player.prevPosY || ListenerRender.mc.player.posZ != ListenerRender.mc.player.prevPosZ) {
            ((FakeLag)this.module).positons.add(new Vector3d(ListenerRender.mc.player.posX, ListenerRender.mc.player.posY, ListenerRender.mc.player.posZ));
        }
        GL11.glPushMatrix();
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)2929);
        GL11.glLineWidth((float)1.5f);
        GL11.glBegin((int)3);
        GL11.glColor4f((float)((float)((Color)((FakeLag)this.module).lineColor.getValue()).getRed() / 255.0f), (float)((float)((Color)((FakeLag)this.module).lineColor.getValue()).getGreen() / 255.0f), (float)((float)((Color)((FakeLag)this.module).lineColor.getValue()).getBlue() / 255.0f), (float)((float)((Color)((FakeLag)this.module).lineColor.getValue()).getAlpha() / 255.0f));
        ((FakeLag)this.module).positons.forEach(vector -> GL11.glVertex3d((double)(vector.x - ((IRenderManager)mc.getRenderManager()).getRenderPosX()), (double)(vector.y - ((IRenderManager)mc.getRenderManager()).getRenderPosY()), (double)(vector.z - ((IRenderManager)mc.getRenderManager()).getRenderPosZ())));
        GL11.glEnd();
        GL11.glEnable((int)2929);
        GL11.glDisable((int)2848);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
        GL11.glPopMatrix();
    }
}

