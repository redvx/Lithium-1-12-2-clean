/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.MoreObjects
 *  com.google.common.collect.Maps
 *  com.mojang.authlib.GameProfile
 *  com.mojang.authlib.minecraft.MinecraftProfileTexture$Type
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.network.NetworkPlayerInfo
 *  net.minecraft.client.resources.DefaultPlayerSkin
 *  net.minecraft.util.ResourceLocation
 */
package me.chachoox.lithium.asm.mixins.network;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import java.util.Map;
import java.util.UUID;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.misc.nameprotect.NameProtect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value={NetworkPlayerInfo.class})
public abstract class MixinNetworkPlayerInfo {
    @Shadow
    public GameProfile gameProfile;
    @Shadow
    Map<MinecraftProfileTexture.Type, ResourceLocation> playerTextures = Maps.newEnumMap(MinecraftProfileTexture.Type.class);

    @Shadow
    public abstract void loadPlayerTextures();

    @Shadow
    public abstract String getSkinType();

    @Overwrite
    public ResourceLocation getLocationSkin() {
        this.loadPlayerTextures();
        NameProtect NAME_PROTECT = Managers.MODULE.get(NameProtect.class);
        if (NAME_PROTECT.isFakeSkin() && this.gameProfile.getName().equals(Minecraft.getMinecraft().player.getName())) {
            if (this.getSkinType().equalsIgnoreCase("slim")) {
                return new ResourceLocation("textures/entity/alex.png");
            }
            return new ResourceLocation("textures/entity/steve.png");
        }
        return (ResourceLocation)MoreObjects.firstNonNull((Object)this.playerTextures.get(MinecraftProfileTexture.Type.SKIN), (Object)DefaultPlayerSkin.getDefaultSkin((UUID)this.gameProfile.getId()));
    }
}

