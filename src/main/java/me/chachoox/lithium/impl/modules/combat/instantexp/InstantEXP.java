/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.init.Items
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItem
 *  net.minecraft.util.EnumHand
 */
package me.chachoox.lithium.impl.modules.combat.instantexp;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.util.inventory.ItemUtil;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.api.util.network.PacketUtil;
import me.chachoox.lithium.impl.modules.combat.instantexp.ListenerDeath;
import me.chachoox.lithium.impl.modules.combat.instantexp.ListenerLogout;
import me.chachoox.lithium.impl.modules.combat.instantexp.ListenerTick;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;

public class InstantEXP
extends Module {
    private final NumberProperty<Integer> delay = new NumberProperty<Integer>(1, 0, 10, new String[]{"Delay", "ThrowDelay", "ExpDelay"}, "Delay between throwing exp bottles.");
    private final StopWatch timer = new StopWatch();

    public InstantEXP() {
        super("InstantEXP", new String[]{"instantexp", "silentxp", "xp"}, "Automatically throws EXP.", Category.COMBAT);
        this.offerProperties(this.delay);
        this.offerListeners(new ListenerTick(this), new ListenerDeath(this), new ListenerLogout(this));
    }

    protected void doEXP() {
        int expSlot = ItemUtil.getSlotHotbar(Items.EXPERIENCE_BOTTLE);
        if (expSlot != -1 && this.timer.passed((Integer)this.delay.getValue() * 10)) {
            int oldSlot = InstantEXP.mc.player.inventory.currentItem;
            ItemUtil.switchTo(expSlot);
            PacketUtil.send(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            ItemUtil.switchTo(oldSlot);
            this.timer.reset();
        }
    }
}

