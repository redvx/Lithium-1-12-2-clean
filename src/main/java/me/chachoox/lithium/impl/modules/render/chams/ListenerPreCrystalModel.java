/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.chachoox.lithium.impl.modules.render.chams;

import me.chachoox.lithium.impl.event.events.render.model.CrystalModelRenderEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.render.chams.Chams;
import org.lwjgl.opengl.GL11;

public class ListenerPreCrystalModel
extends ModuleListener<Chams, CrystalModelRenderEvent.Pre> {
    public ListenerPreCrystalModel(Chams module) {
        super(module, CrystalModelRenderEvent.Pre.class);
    }

    @Override
    public void call(CrystalModelRenderEvent.Pre event) {
        if (((Chams)this.module).crystalChams.getValue().booleanValue() && !((Chams)this.module).normal.getValue().booleanValue()) {
            GL11.glPushMatrix();
            if (((Chams)this.module).texture.getValue().booleanValue()) {
                event.getModel().render(event.getEntity(), event.getLimbSwing(), event.getLimbSwingAmount(), event.getAgeInTicks(), event.getNetHeadYaw(), event.getHeadPitch(), event.getScale());
            }
            GL11.glPushAttrib((int)1048575);
            GL11.glDisable((int)3008);
            GL11.glDisable((int)3553);
            GL11.glDisable((int)2896);
            GL11.glEnable((int)3042);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glEnable((int)2960);
            GL11.glEnable((int)10754);
            if (((Chams)this.module).xqz.getValue().booleanValue()) {
                GL11.glDepthMask((boolean)false);
                GL11.glDisable((int)2929);
                GL11.glColor4d((double)((float)((Chams)this.module).getInvisibleColor(event.getEntity()).getRed() / 255.0f), (double)((float)((Chams)this.module).getInvisibleColor(event.getEntity()).getGreen() / 255.0f), (double)((float)((Chams)this.module).getInvisibleColor(event.getEntity()).getBlue() / 255.0f), (double)((float)((Chams)this.module).getInvisibleColor(event.getEntity()).getAlpha() / 255.0f));
                event.getModel().render(event.getEntity(), event.getLimbSwing(), event.getLimbSwingAmount(), event.getAgeInTicks(), event.getNetHeadYaw(), event.getHeadPitch(), event.getScale());
                GL11.glDepthMask((boolean)true);
                GL11.glEnable((int)2929);
            }
            GL11.glColor4d((double)((float)((Chams)this.module).getVisibleColor(event.getEntity()).getRed() / 255.0f), (double)((float)((Chams)this.module).getVisibleColor(event.getEntity()).getGreen() / 255.0f), (double)((float)((Chams)this.module).getVisibleColor(event.getEntity()).getBlue() / 255.0f), (double)((float)((Chams)this.module).getVisibleColor(event.getEntity()).getAlpha() / 255.0f));
            event.getModel().render(event.getEntity(), event.getLimbSwing(), event.getLimbSwingAmount(), event.getAgeInTicks(), event.getNetHeadYaw(), event.getHeadPitch(), event.getScale());
            GL11.glEnable((int)3553);
            GL11.glEnable((int)3008);
            GL11.glPopAttrib();
            GL11.glPopMatrix();
            if (((Chams)this.module).glint.getValue().booleanValue()) {
                ((Chams)this.module).onGlintModel(event.getModel(), event.getEntity(), event.getLimbSwing(), event.getLimbSwingAmount(), event.getAgeInTicks(), event.getNetHeadYaw(), event.getHeadPitch(), event.getScale());
            }
            event.setCanceled(true);
        }
    }
}

