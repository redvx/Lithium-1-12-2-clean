/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 *  net.minecraft.entity.Entity
 *  net.minecraft.world.World
 */
package me.chachoox.lithium.impl.modules.player.fakeplayer.util;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class PlayerUtil
implements Minecraftable {
    private static EntityOtherPlayerMP fake;

    public static void addFakePlayerToWorld(String name, int id) {
        if (PlayerUtil.mc.player != null) {
            GameProfile profile = new GameProfile(UUID.randomUUID(), name);
            fake = new EntityOtherPlayerMP((World)PlayerUtil.mc.world, profile);
            PlayerUtil.fake.inventory.copyInventory(PlayerUtil.mc.player.inventory);
            fake.copyLocationAndAnglesFrom((Entity)PlayerUtil.mc.player);
            fake.setHealth(PlayerUtil.mc.player.getHealth());
            fake.setAbsorptionAmount(PlayerUtil.mc.player.getAbsorptionAmount());
            PlayerUtil.fake.onGround = PlayerUtil.mc.player.onGround;
            PlayerUtil.mc.world.addEntityToWorld(id, (Entity)fake);
        }
    }

    public static void removeFakePlayerFromWorld(int id) {
        if (PlayerUtil.mc.player != null) {
            PlayerUtil.mc.world.removeEntityFromWorld(id);
            fake = null;
        }
    }

    public static EntityOtherPlayerMP getPlayer() {
        return fake;
    }
}

