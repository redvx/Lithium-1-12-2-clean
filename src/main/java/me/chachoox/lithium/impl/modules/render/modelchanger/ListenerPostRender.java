/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.util.EnumHandSide
 */
package me.chachoox.lithium.impl.modules.render.modelchanger;

import me.chachoox.lithium.impl.event.events.render.item.RenderFirstPersonEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.render.modelchanger.ModelChanger;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;

public class ListenerPostRender
extends ModuleListener<ModelChanger, RenderFirstPersonEvent.Post> {
    public ListenerPostRender(ModelChanger module) {
        super(module, RenderFirstPersonEvent.Post.class);
    }

    @Override
    public void call(RenderFirstPersonEvent.Post event) {
        if (event.getHandSide() == EnumHandSide.RIGHT) {
            GlStateManager.rotate((float)(((Float)((ModelChanger)this.module).rotateY.getValue()).floatValue() * 2.0f), (float)0.0f, (float)1.0f, (float)0.0f);
            GlStateManager.rotate((float)(((Float)((ModelChanger)this.module).rotateX.getValue()).floatValue() * 2.0f), (float)1.0f, (float)0.0f, (float)0.0f);
            GlStateManager.rotate((float)(((Float)((ModelChanger)this.module).rotateZ.getValue()).floatValue() * 2.0f), (float)0.0f, (float)0.0f, (float)1.0f);
        } else if (event.getHandSide() == EnumHandSide.LEFT) {
            GlStateManager.rotate((float)(-(((Float)((ModelChanger)this.module).rotateY.getValue()).floatValue() * 2.0f)), (float)0.0f, (float)1.0f, (float)0.0f);
            GlStateManager.rotate((float)(((Float)((ModelChanger)this.module).rotateX.getValue()).floatValue() * 2.0f), (float)1.0f, (float)0.0f, (float)0.0f);
            GlStateManager.rotate((float)(-(((Float)((ModelChanger)this.module).rotateZ.getValue()).floatValue() * 2.0f)), (float)0.0f, (float)0.0f, (float)1.0f);
        }
    }
}

