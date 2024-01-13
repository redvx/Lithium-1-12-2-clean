/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 */
package me.chachoox.lithium.api.property.list;

import java.util.List;
import me.chachoox.lithium.api.property.list.ListProperty;
import net.minecraft.block.Block;

public class BlockList
extends ListProperty<Block> {
    public BlockList(List<Block> list, String[] aliases) {
        super(list, aliases);
    }

    public BlockList(String[] aliases) {
        super(aliases);
    }
}

