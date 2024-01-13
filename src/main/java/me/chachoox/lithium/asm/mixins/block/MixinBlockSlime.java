/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockSlime
 */
package me.chachoox.lithium.asm.mixins.block;

import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.movement.noslow.NoSlow;
import net.minecraft.block.BlockSlime;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={BlockSlime.class})
public abstract class MixinBlockSlime {
    @Inject(method={"onEntityWalk"}, at={@At(value="HEAD")}, cancellable=true)
    public void onEntityCollisionHook(CallbackInfo info) {
        if (Managers.MODULE.get(NoSlow.class).noSlime().booleanValue()) {
            info.cancel();
        }
    }
}

