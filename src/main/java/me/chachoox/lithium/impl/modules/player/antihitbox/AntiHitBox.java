/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.Item
 */
package me.chachoox.lithium.impl.modules.player.antihitbox;

import java.util.Collection;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.list.ItemList;
import me.chachoox.lithium.api.util.list.ListEnum;
import me.chachoox.lithium.impl.modules.player.antihitbox.ListenerUpdate;
import net.minecraft.item.Item;

public class AntiHitBox
extends Module {
    protected final EnumProperty<ListEnum> whitelist = new EnumProperty<ListEnum>(ListEnum.WHITELIST, ListEnum.SELECTION_ALIAS, "Any: - Disregards all added items/blocks, always active / Whitelist: - Ignores added items/blocks / Blacklist: - Only uses added items/blocks");
    protected final ItemList itemList = new ItemList(ListEnum.ITEM_LIST_ALIAS);
    protected boolean noTrace;

    public AntiHitBox() {
        super("AntiHitbox", new String[]{"AntiHitbox", "NoHitBox", "NoHitBoxBlock"}, "Cancels hitbox if we are holding a valid item.", Category.PLAYER);
        this.offerProperties(this.whitelist, this.itemList);
        this.offerListeners(new ListenerUpdate(this));
    }

    public boolean canTrace() {
        return this.isEnabled() && this.noTrace;
    }

    protected boolean isValid(Item item) {
        if (item == null) {
            return false;
        }
        if (this.whitelist.getValue() == ListEnum.ANY) {
            return true;
        }
        if (this.whitelist.getValue() == ListEnum.WHITELIST) {
            return this.getList().contains(item);
        }
        return !this.getList().contains(item);
    }

    public Collection<Item> getList() {
        return (Collection)this.itemList.getValue();
    }
}

