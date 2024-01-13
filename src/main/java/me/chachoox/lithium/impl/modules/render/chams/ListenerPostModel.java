/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 */
package me.chachoox.lithium.impl.modules.render.chams;

import me.chachoox.lithium.impl.event.events.render.model.ModelRenderEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.render.chams.Chams;
import net.minecraft.entity.Entity;

public class ListenerPostModel
extends ModuleListener<Chams, ModelRenderEvent.Post> {
    public ListenerPostModel(Chams module) {
        super(module, ModelRenderEvent.Post.class);
    }

    @Override
    public void call(ModelRenderEvent.Post event) {
        if (!((Chams)this.module).normal.getValue().booleanValue()) {
            Entity entity = event.getEntity();
            if (((Chams)this.module).playerWires.getValue().booleanValue()) {
                if (event.getEntity() == ListenerPostModel.mc.player && !((Chams)this.module).self.getValue().booleanValue()) {
                    return;
                }
                ((Chams)this.module).onWireframeModel(event.getModel(), entity, event.getLimbSwing(), event.getLimbSwingAmount(), event.getAgeInTicks(), event.getNetHeadYaw(), event.getHeadPitch(), event.getScale());
            }
        }
    }
}

