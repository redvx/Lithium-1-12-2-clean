/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.ItemStack
 */
package me.chachoox.lithium.impl.modules.combat.autoarmour.util;

import net.minecraft.item.ItemStack;

public class DamageStack
implements Comparable<DamageStack> {
    private final ItemStack stack;
    private final float damage;
    private final int slot;

    public DamageStack(ItemStack stack, float damage, int slot) {
        this.stack = stack;
        this.damage = damage;
        this.slot = slot;
    }

    public int getSlot() {
        return this.slot;
    }

    public float getDamage() {
        return this.damage;
    }

    public ItemStack getStack() {
        return this.stack;
    }

    @Override
    public int compareTo(DamageStack o) {
        return Float.compare(o.damage, this.damage);
    }
}

