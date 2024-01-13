/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.settings.KeyBinding
 */
package me.chachoox.lithium.asm.mixins.settings;

import me.chachoox.lithium.asm.ducks.IKeyBinding;
import net.minecraft.client.settings.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={KeyBinding.class})
public abstract class MixinKeyBinding
implements IKeyBinding {
    @Shadow
    private boolean pressed;

    @Override
    @Accessor(value="pressed")
    public abstract void setPressed(boolean var1);

    @Inject(method={"isKeyDown"}, at={@At(value="RETURN")}, cancellable=true)
    public void isKeyDownHook(CallbackInfoReturnable<Boolean> info) {
        if (!info.getReturnValue().booleanValue()) {
            info.setReturnValue(this.pressed);
        }
    }
}

