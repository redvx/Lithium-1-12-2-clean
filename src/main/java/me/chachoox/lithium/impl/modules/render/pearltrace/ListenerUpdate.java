/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.item.EntityEnderPearl
 *  net.minecraft.util.math.Vec3d
 */
package me.chachoox.lithium.impl.modules.render.pearltrace;

import java.util.ArrayList;
import me.chachoox.lithium.impl.event.events.update.UpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.render.pearltrace.PearlTrace;
import me.chachoox.lithium.impl.modules.render.pearltrace.util.ThrownEntity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.util.math.Vec3d;

public class ListenerUpdate
extends ModuleListener<PearlTrace, UpdateEvent> {
    public ListenerUpdate(PearlTrace module) {
        super(module, UpdateEvent.class);
    }

    @Override
    public void call(UpdateEvent event) {
        ListenerUpdate.mc.world.loadedEntityList.stream().filter(entity -> ListenerUpdate.mc.player != entity).forEach(entity -> {
            if (entity.ticksExisted > 1 && entity instanceof EntityEnderPearl) {
                if (!((PearlTrace)this.module).thrownEntities.containsKey(entity.getEntityId())) {
                    ArrayList<Vec3d> list = new ArrayList<Vec3d>();
                    list.add(new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ));
                    ((PearlTrace)this.module).thrownEntities.put(entity.getEntityId(), new ThrownEntity(System.currentTimeMillis(), list));
                } else {
                    ((PearlTrace)this.module).thrownEntities.get(entity.getEntityId()).getVertices().add(new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ));
                    ((PearlTrace)this.module).thrownEntities.get(entity.getEntityId()).setTime(System.currentTimeMillis());
                }
            }
        });
        ((PearlTrace)this.module).thrownEntities.forEach((id, thrownEntity) -> {
            if (System.currentTimeMillis() - thrownEntity.getTime() > (long)((Integer)((PearlTrace)this.module).timeout.getValue()).intValue()) {
                ((PearlTrace)this.module).thrownEntities.remove(id);
            }
        });
    }
}

