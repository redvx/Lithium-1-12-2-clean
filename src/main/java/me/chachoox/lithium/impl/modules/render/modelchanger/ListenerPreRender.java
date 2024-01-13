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

public class ListenerPreRender
extends ModuleListener<ModelChanger, RenderFirstPersonEvent.Pre> {
    public ListenerPreRender(ModelChanger module) {
        super(module, RenderFirstPersonEvent.Pre.class);
    }

    @Override
    public void call(RenderFirstPersonEvent.Pre event) {
        if (event.getHandSide() == EnumHandSide.RIGHT) {
            GlStateManager.translate((double)((double)((Float)((ModelChanger)this.module).translateX.getValue()).floatValue() * 0.5), (double)((double)((Float)((ModelChanger)this.module).translateY.getValue()).floatValue() * 0.5), (double)((double)((Float)((ModelChanger)this.module).translateZ.getValue()).floatValue() * 0.5));
        } else if (event.getHandSide() == EnumHandSide.LEFT) {
            GlStateManager.translate((double)(-((double)((Float)((ModelChanger)this.module).translateX.getValue()).floatValue() * 0.5)), (double)((double)((Float)((ModelChanger)this.module).translateY.getValue()).floatValue() * 0.5), (double)((double)((Float)((ModelChanger)this.module).translateZ.getValue()).floatValue() * 0.5));
        }
    }
}

