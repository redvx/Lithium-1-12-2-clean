/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.WorldProvider
 */
package me.chachoox.lithium.asm.mixins.world;

import java.awt.Color;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.render.customsky.CustomSky;
import me.chachoox.lithium.impl.modules.render.customsky.mode.Mode;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={WorldProvider.class})
public class MixinWorldProvider {
    @Inject(method={"getFogColor"}, at={@At(value="RETURN")}, cancellable=true)
    public void getFog(float p_76562_1_, float p_76562_2_, CallbackInfoReturnable<Vec3d> cir) {
        CustomSky CUSTOM_SKY = Managers.MODULE.get(CustomSky.class);
        if (CUSTOM_SKY.isEnabled() && (CUSTOM_SKY.mode.getValue() == Mode.BOTH || CUSTOM_SKY.mode.getValue() == Mode.FOG)) {
            Color color = CUSTOM_SKY.getSkyColor();
            cir.setReturnValue(new Vec3d((double)((float)color.getRed() / 255.0f), (double)((float)color.getGreen() / 255.0f), (double)((float)color.getBlue() / 255.0f)));
        }
    }

    @Overwrite
    public double getVoidFogYFactor() {
        return 1.0;
    }
}

