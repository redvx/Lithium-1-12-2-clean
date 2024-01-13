/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.api.util.list;

public enum ListEnum {
    WHITELIST,
    BLACKLIST,
    ANY;

    public static final String SELECTION_DESCRIPTION = "Any: - Disregards all added items/blocks, always active / Whitelist: - Ignores added items/blocks / Blacklist: - Only uses added items/blocks";
    public static final String[] SELECTION_ALIAS;
    public static final String[] BLOCKS_LIST_ALIAS;
    public static final String[] ITEM_LIST_ALIAS;

    static {
        SELECTION_ALIAS = new String[]{"Selection", "selec", "whitelist", "blacklist"};
        BLOCKS_LIST_ALIAS = new String[]{"Blocks", "blockslist", "blocklist", "block"};
        ITEM_LIST_ALIAS = new String[]{"Items", "itemslist", "itemlist", "item"};
    }
}

