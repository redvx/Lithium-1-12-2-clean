/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.init.Items
 *  net.minecraft.item.Item
 */
package me.chachoox.lithium.impl.modules.combat.offhand;

import net.minecraft.init.Items;
import net.minecraft.item.Item;

public enum OffhandMode {
    TOTEMS(Items.TOTEM_OF_UNDYING),
    CRYSTALS(Items.END_CRYSTAL),
    GAPPLES(Items.GOLDEN_APPLE);

    public final Item item;

    private OffhandMode(Item item) {
        this.item = item;
    }
}

