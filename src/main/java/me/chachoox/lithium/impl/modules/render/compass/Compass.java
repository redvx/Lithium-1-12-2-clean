/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.util.math.MathHelper
 */
package me.chachoox.lithium.impl.modules.render.compass;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.render.compass.ListenerRender;
import me.chachoox.lithium.impl.modules.render.compass.util.Direction;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.math.MathHelper;

public class Compass
extends Module {
    private final NumberProperty<Float> scale = new NumberProperty<Float>(Float.valueOf(3.0f), Float.valueOf(0.0f), Float.valueOf(10.0f), Float.valueOf(0.5f), new String[]{"Scale", "s"}, "Scale of the compass.");
    private final NumberProperty<Integer> posX = new NumberProperty<Integer>(0, -500, 500, new String[]{"PosX", "horizontalpos"}, "Horizontal position of the compass.");
    private final NumberProperty<Integer> posY = new NumberProperty<Integer>(0, -500, 500, new String[]{"PosY", "verticalpos"}, "Vertical position of the compass.");

    public Compass() {
        super("Compass", new String[]{"Compass", "Direction"}, "Draws a compass on your screen.", Category.RENDER);
        this.offerProperties(this.posX, this.posY, this.scale);
        this.offerListeners(new ListenerRender(this));
    }

    protected void onRender(ScaledResolution resolution) {
        int width = resolution.getScaledWidth() / 2;
        int height = resolution.getScaledHeight() / 2;
        double centerX = width + (Integer)this.posX.getValue();
        double centerY = height + (Integer)this.posY.getValue();
        for (Direction direction : Direction.values()) {
            double rad = this.getPosOnCompass(direction);
            Managers.FONT.drawString(direction.name(), (float)(centerX + this.getX(rad)), (float)(centerY + this.getY(rad)), direction == Direction.N ? -65536 : -1);
        }
    }

    private double getPosOnCompass(Direction dir) {
        double yaw = Math.toRadians(MathHelper.wrapDegrees((float)Compass.mc.player.rotationYaw));
        int index = dir.ordinal();
        return yaw + (double)index * 1.5707963267948966;
    }

    private double getX(double rad) {
        return Math.sin(rad) * (double)(((Float)this.scale.getValue()).floatValue() * 10.0f);
    }

    private double getY(double rad) {
        double pitch = MathHelper.clamp((float)(Compass.mc.player.rotationPitch + 30.0f), (float)-90.0f, (float)90.0f);
        double pitchRadians = Math.toRadians(pitch);
        return Math.cos(rad) * Math.sin(pitchRadians) * (double)(((Float)this.scale.getValue()).floatValue() * 10.0f);
    }
}

