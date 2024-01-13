/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.misc.nobreakanim;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.impl.modules.misc.nobreakanim.ListenerPacket;

public class NoBreakAnim
extends Module {
    public NoBreakAnim() {
        super("NoBreakAnim", new String[]{"NoBreakAnim", "nobreakanimation", "noblockbreak"}, "Spoofs block breaking animation.", Category.MISC);
        this.offerListeners(new ListenerPacket(this));
    }
}

