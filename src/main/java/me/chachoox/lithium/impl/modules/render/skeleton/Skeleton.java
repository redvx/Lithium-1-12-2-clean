/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.model.ModelBiped
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 */
package me.chachoox.lithium.impl.modules.render.skeleton;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.ColorProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.impl.modules.render.skeleton.ListenerRender;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class Skeleton
extends Module {
    private final ColorProperty color = new ColorProperty(new Color(-1), true, new String[]{"Color", "colour"});
    private final NumberProperty<Float> lineWidth = new NumberProperty<Float>(Float.valueOf(1.2f), Float.valueOf(1.0f), Float.valueOf(4.0f), Float.valueOf(0.1f), new String[]{"LineWidth", "width", "wirewidth"}, "Thickness of the line.");
    protected final Map<EntityPlayer, float[][]> rotationList = new HashMap<EntityPlayer, float[][]>();

    public Skeleton() {
        super("Skeleton", new String[]{"Skeleton", "spooky", "skelet"}, "Draws a skeleton on entities.", Category.RENDER);
        this.offerProperties(this.color, this.lineWidth);
        this.offerListeners(new ListenerRender(this));
    }

    protected Color getColor() {
        return this.color.getColor();
    }

    public void onRenderModel(ModelBase modelBase, Entity entity) {
        if (entity instanceof EntityPlayer && modelBase instanceof ModelBiped) {
            this.rotationList.put((EntityPlayer)entity, this.getBipedRotations((ModelBiped)modelBase));
        }
    }

    private float[][] getBipedRotations(ModelBiped biped) {
        float[][] rotations = new float[5][];
        float[] headRotation = new float[]{biped.bipedHead.rotateAngleX, biped.bipedHead.rotateAngleY, biped.bipedHead.rotateAngleZ};
        rotations[0] = headRotation;
        float[] rightArmRotation = new float[]{biped.bipedRightArm.rotateAngleX, biped.bipedRightArm.rotateAngleY, biped.bipedRightArm.rotateAngleZ};
        rotations[1] = rightArmRotation;
        float[] leftArmRotation = new float[]{biped.bipedLeftArm.rotateAngleX, biped.bipedLeftArm.rotateAngleY, biped.bipedLeftArm.rotateAngleZ};
        rotations[2] = leftArmRotation;
        float[] rightLegRotation = new float[]{biped.bipedRightLeg.rotateAngleX, biped.bipedRightLeg.rotateAngleY, biped.bipedRightLeg.rotateAngleZ};
        rotations[3] = rightLegRotation;
        float[] leftLegRotation = new float[]{biped.bipedLeftLeg.rotateAngleX, biped.bipedLeftLeg.rotateAngleY, biped.bipedLeftLeg.rotateAngleZ};
        rotations[4] = leftLegRotation;
        return rotations;
    }

    protected float getLineWidth() {
        return ((Float)this.lineWidth.getValue()).floatValue();
    }
}

