/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.misc.autoreply;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.impl.modules.misc.autoreply.ListenerMessage;
import me.chachoox.lithium.impl.modules.misc.autoreply.ReplyMessages;

public class AutoReply
extends Module {
    protected final EnumProperty<ReplyMessages> message = new EnumProperty<ReplyMessages>(ReplyMessages.LITHIUM, new String[]{"Message", "mode", "type", "response", "m"}, "How we want to reply to the message.");
    protected final NumberProperty<Integer> delay = new NumberProperty<Integer>(5, 1, 15, new String[]{"Delay", "del", "d"}, "How long we want to wait until responding to another message.");
    protected final StopWatch timer = new StopWatch();

    public AutoReply() {
        super("AutoReply", new String[]{"AutoReply", "reply", "replyback"}, "Automatically replies to whispers", Category.MISC);
        this.offerProperties(this.message, this.delay);
        this.offerListeners(new ListenerMessage(this));
    }

    @Override
    public void onEnable() {
        this.timer.reset();
    }

    @Override
    public String getSuffix() {
        return this.message.getFixedValue();
    }
}

