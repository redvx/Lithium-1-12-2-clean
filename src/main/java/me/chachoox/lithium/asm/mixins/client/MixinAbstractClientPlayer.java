/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  net.minecraft.client.entity.AbstractClientPlayer
 *  net.minecraft.client.network.NetworkPlayerInfo
 *  net.minecraft.entity.SharedMonsterAttributes
 *  net.minecraft.entity.ai.attributes.IAttributeInstance
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.MobEffects
 *  net.minecraft.item.ItemBow
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.world.World
 *  net.minecraftforge.client.ForgeHooksClient
 */
package me.chachoox.lithium.asm.mixins.client;

import com.mojang.authlib.GameProfile;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.managers.minecraft.CapesManager;
import me.chachoox.lithium.impl.modules.render.fovmodifier.FovModifier;
import me.chachoox.lithium.impl.modules.render.norender.NoRender;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemBow;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={AbstractClientPlayer.class})
public abstract class MixinAbstractClientPlayer
extends EntityPlayer
implements Minecraftable {
    public MixinAbstractClientPlayer(World worldIn, GameProfile gameProfileIn) {
        super(worldIn, gameProfileIn);
    }

    @Shadow
    protected abstract NetworkPlayerInfo getPlayerInfo();

    @Inject(method={"getLocationCape"}, at={@At(value="HEAD")}, cancellable=true)
    public void getLocationCape(CallbackInfoReturnable<ResourceLocation> callbackInfoReturnable) {
        ResourceLocation location;
        NetworkPlayerInfo playerInfo = this.getPlayerInfo();
        if (playerInfo != null && Managers.CAPES.isCapesEnabled() && (location = CapesManager.getResourceLocation(playerInfo.getGameProfile().getId())) != null) {
            callbackInfoReturnable.setReturnValue(location);
        }
    }

    @Inject(method={"getFovModifier"}, at={@At(value="HEAD")}, cancellable=true)
    public void getFovModifierHook(CallbackInfoReturnable<Float> info) {
        if (Managers.MODULE.get(NoRender.class).getDynamicFov()) {
            info.setReturnValue(Float.valueOf(1.0f));
        } else if (Managers.MODULE.get(FovModifier.class).isEnabled()) {
            float f = 1.0f;
            float add = 0.0f;
            float ten = 10.0f;
            if (this.capabilities.isFlying) {
                f *= 1.0f + FovModifier.get().fly() / ten;
            }
            boolean sprint = this.isSprinting();
            boolean speed = this.isPotionActive(MobEffects.SPEED);
            boolean slow = this.isPotionActive(MobEffects.SLOWNESS);
            if (sprint) {
                add += FovModifier.get().sprint() / ten;
            }
            if (slow) {
                add += FovModifier.get().slow() / ten;
            }
            if (speed) {
                add += FovModifier.get().swiftness() / ten;
            }
            IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
            if (FovModifier.get().sprint() == 0.0f && sprint) {
                info.setReturnValue(Float.valueOf(1.0f));
                return;
            }
            if (FovModifier.get().slow() == 0.0f && slow) {
                info.setReturnValue(Float.valueOf(1.0f));
                return;
            }
            if (FovModifier.get().swiftness() == 0.0f && speed) {
                info.setReturnValue(Float.valueOf(1.0f));
                return;
            }
            f = (float)((double)f * ((iattributeinstance.getAttributeValue() / (double)this.capabilities.getWalkSpeed() + 1.0) / 2.0) + (double)add);
            if (this.capabilities.getWalkSpeed() == 0.0f || Float.isNaN(f) || Float.isInfinite(f)) {
                f = 1.0f;
            }
            if (this.isHandActive() && this.getActiveItemStack().getItem() instanceof ItemBow) {
                int i = this.getItemInUseMaxCount();
                float f1 = (float)i / 20.0f;
                f1 = f1 > 1.0f ? 1.0f : (f1 *= f1);
                if (FovModifier.get().aim() == 0.0f) {
                    info.setReturnValue(Float.valueOf(1.0f));
                    return;
                }
                f *= 1.0f - f1 * 0.15f + FovModifier.get().aim() / ten;
            }
            info.setReturnValue(Float.valueOf(ForgeHooksClient.getOffsetFOV((EntityPlayer)this, (float)f)));
        }
    }
}

