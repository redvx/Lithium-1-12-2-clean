/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.ItemFood
 *  net.minecraft.potion.PotionEffect
 */
package me.chachoox.lithium.asm.mixins.item;

import net.minecraft.item.ItemFood;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={ItemFood.class})
public interface IItemFood {
    @Accessor(value="potionId")
    public PotionEffect getPotionId();
}

