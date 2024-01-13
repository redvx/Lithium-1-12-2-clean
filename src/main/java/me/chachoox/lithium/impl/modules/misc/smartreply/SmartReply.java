/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.misc.smartreply;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.impl.modules.misc.smartreply.ListenerCChat;
import me.chachoox.lithium.impl.modules.misc.smartreply.ListenerSChat;

public class SmartReply
extends Module {
    protected String whisperSender;
    protected boolean whispered;

    public SmartReply() {
        super("SmartReply", new String[]{"SmartReply", "betterreply", "ignoreunicode"}, "Replies but smarter.", Category.MISC);
        this.offerListeners(new ListenerSChat(this), new ListenerCChat(this));
    }

    @Override
    public String getSuffix() {
        return this.whisperSender;
    }
}

