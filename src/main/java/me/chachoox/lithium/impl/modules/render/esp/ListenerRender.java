/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.culling.Frustum
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.Vec3d
 *  org.lwjgl.opengl.GL11
 */
package me.chachoox.lithium.impl.modules.render.esp;

import java.util.Comparator;
import java.util.List;
import me.chachoox.lithium.api.util.render.Interpolation;
import me.chachoox.lithium.api.util.render.RenderUtil;
import me.chachoox.lithium.impl.event.events.render.main.Render3DEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.render.esp.ESP;
import me.chachoox.lithium.impl.modules.render.esp.util.RenderMode;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class ListenerRender
extends ModuleListener<ESP, Render3DEvent> {
    public ListenerRender(ESP module) {
        super(module, Render3DEvent.class);
    }

    @Override
    public void call(Render3DEvent event) {
        Entity renderEntity = RenderUtil.getEntity();
        Frustum frustum = Interpolation.createFrustum(renderEntity);
        List entityList = ListenerRender.mc.world.loadedEntityList;
        entityList.sort(Comparator.comparing(entity -> Float.valueOf(ListenerRender.mc.player.getDistance((Entity)entity))).reversed());
        for (Entity entity2 : entityList) {
            AxisAlignedBB bb = entity2.getEntityBoundingBox();
            Vec3d vec = Interpolation.interpolateEntity(entity2);
            if (!frustum.isBoundingBoxInFrustum(bb)) continue;
            if (((ESP)this.module).isValid(entity2) && ((ESP)this.module).mode.getValue() != RenderMode.NONE) {
                ((ESP)this.module).doRender(new AxisAlignedBB(0.0, 0.0, 0.0, (double)entity2.width, (double)entity2.height, (double)entity2.width).offset(vec.x - (double)(entity2.width / 2.0f), vec.y + 0.05, vec.z - (double)(entity2.width / 2.0f)).grow(0.05));
            }
            if (((ESP)this.module).itemNametags.getValue().booleanValue() && entity2 instanceof EntityItem) {
                GL11.glPushMatrix();
                ((ESP)this.module).renderNameTag(((EntityItem)entity2).getItem().getDisplayName() + " x" + ((EntityItem)entity2).getItem().getCount(), vec.x, vec.y, vec.z);
                RenderUtil.color(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glPopMatrix();
            }
            if (!((ESP)this.module).chorus.getValue().booleanValue() || ((ESP)this.module).teleportPos == null) continue;
            double x = (double)((ESP)this.module).teleportPos.getX() - Interpolation.getRenderPosX();
            double y = (double)((ESP)this.module).teleportPos.getY() - Interpolation.getRenderPosY();
            double z = (double)((ESP)this.module).teleportPos.getZ() - Interpolation.getRenderPosZ();
            if (((ESP)this.module).teleportTimer.passed(2500L)) {
                ((ESP)this.module).teleportPos = null;
                return;
            }
            GL11.glPushMatrix();
            ((ESP)this.module).renderNameTag("Player Teleports", x, y, z);
            RenderUtil.color(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glPopMatrix();
        }
    }
}

