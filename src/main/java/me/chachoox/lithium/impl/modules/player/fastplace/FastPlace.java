/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.Item
 */
package me.chachoox.lithium.impl.modules.player.fastplace;

import java.util.Collection;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.property.list.ItemList;
import me.chachoox.lithium.api.util.list.ListEnum;
import me.chachoox.lithium.impl.modules.player.fastplace.ListenerPlaceBoat;
import me.chachoox.lithium.impl.modules.player.fastplace.ListenerTick;
import net.minecraft.item.Item;

public class FastPlace
extends Module {
    protected final EnumProperty<ListEnum> whitelist = new EnumProperty<ListEnum>(ListEnum.ANY, ListEnum.SELECTION_ALIAS, "Any: - Disregards all added items/blocks, always active / Whitelist: - Ignores added items/blocks / Blacklist: - Only uses added items/blocks");
    protected final NumberProperty<Integer> delay = new NumberProperty<Integer>(1, 0, 4, new String[]{"Delay", "del", "d"}, "What we set the rightclickdelay timer to.");
    protected final Property<Boolean> boatFix = new Property<Boolean>(false, new String[]{"BoatFix", "2b2tthing", "boats"}, "Fixes servers that try to patch placing boats.");
    protected final ItemList itemList = new ItemList(ListEnum.ITEM_LIST_ALIAS);

    public FastPlace() {
        super("FastPlace", new String[]{"FastPlace", "FastUse", "InstantUse", "InstantPlace"}, "Changes right click delay when holding a valid item.", Category.PLAYER);
        this.offerListeners(new ListenerPlaceBoat(this), new ListenerTick(this));
        this.offerProperties(this.whitelist, this.delay, this.boatFix, this.itemList);
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

