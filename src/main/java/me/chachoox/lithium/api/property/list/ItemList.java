/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.Item
 */
package me.chachoox.lithium.api.property.list;

import java.util.List;
import me.chachoox.lithium.api.property.list.ListProperty;
import net.minecraft.item.Item;

public class ItemList
extends ListProperty<Item> {
    public ItemList(List<Item> list, String[] aliases) {
        super(list, aliases);
    }

    public ItemList(String[] aliases) {
        super(aliases);
    }
}

