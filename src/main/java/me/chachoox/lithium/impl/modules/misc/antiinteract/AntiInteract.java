/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 */
package me.chachoox.lithium.impl.modules.misc.antiinteract;

import java.util.Collection;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.property.list.BlockList;
import me.chachoox.lithium.api.util.list.ListEnum;
import me.chachoox.lithium.impl.modules.misc.antiinteract.ListenerInteract;
import net.minecraft.block.Block;

public class AntiInteract
extends Module {
    protected final EnumProperty<ListEnum> whitelist = new EnumProperty<ListEnum>(ListEnum.WHITELIST, ListEnum.SELECTION_ALIAS, "Any: - Disregards all added items/blocks, always active / Whitelist: - Ignores added items/blocks / Blacklist: - Only uses added items/blocks");
    protected final Property<Boolean> sneak = new Property<Boolean>(true, new String[]{"Sneak", "Nosneak", "snk"}, "Wont try to cancel interact event if we are sneaking.");
    protected final Property<Boolean> onlyFood = new Property<Boolean>(false, new String[]{"Food", "onlyOnFood", "noFood", "foodOnly"}, "Only activate anti interact when holding food.");
    protected final BlockList blockList = new BlockList(ListEnum.BLOCKS_LIST_ALIAS);

    public AntiInteract() {
        super("AntiInteract", new String[]{"AntiInteract", "NoInteract"}, "Cancels right clicking on blocks", Category.MISC);
        this.offerProperties(this.whitelist, this.sneak, this.onlyFood, this.blockList);
        this.offerListeners(new ListenerInteract(this));
    }

    protected boolean isValid(Block block) {
        if (block == null) {
            return false;
        }
        if (this.whitelist.getValue() == ListEnum.ANY) {
            return true;
        }
        if (this.whitelist.getValue() == ListEnum.WHITELIST) {
            return this.getList().contains(block);
        }
        return !this.getList().contains(block);
    }

    public Collection<Block> getList() {
        return (Collection)this.blockList.getValue();
    }
}

