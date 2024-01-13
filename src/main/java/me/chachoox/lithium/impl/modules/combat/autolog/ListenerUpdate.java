/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.init.Items
 */
package me.chachoox.lithium.impl.modules.combat.autolog;

import me.chachoox.lithium.api.util.inventory.ItemUtil;
import me.chachoox.lithium.api.util.math.MathUtil;
import me.chachoox.lithium.api.util.network.NetworkUtil;
import me.chachoox.lithium.impl.event.events.update.UpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.combat.autolog.AutoLog;
import net.minecraft.init.Items;

public class ListenerUpdate
extends ModuleListener<AutoLog, UpdateEvent> {
    public ListenerUpdate(AutoLog module) {
        super(module, UpdateEvent.class);
    }

    @Override
    public void call(UpdateEvent event) {
        int yLevel = (int)MathUtil.round(ListenerUpdate.mc.player.posY, 2);
        int math = (Integer)((AutoLog)this.module).threshold.getValue() - yLevel;
        String b = math == 1 ? "block" : "blocks";
        String a = yLevel > (Integer)((AutoLog)this.module).threshold.getValue() ? " below " : " above ";
        int totems = 0;
        if (ListenerUpdate.mc.player != null) {
            totems = ItemUtil.getItemCount(Items.TOTEM_OF_UNDYING);
        }
        if (mc.isSingleplayer()) {
            return;
        }
        String totemMessage = "Logged out with " + MathUtil.round(ListenerUpdate.mc.player.getHealth(), 2) + " hearts and " + totems + " totems remaining";
        if (totems <= (Integer)((AutoLog)this.module).totemCount.getValue() && ListenerUpdate.mc.player.getHealth() <= ((Float)((AutoLog)this.module).healthCount.getValue()).floatValue()) {
            NetworkUtil.disconnect(totemMessage);
            ((AutoLog)this.module).disable();
        }
        String fallDamageMessage = "Logged at Y level " + yLevel + " (" + math + ") " + b + a + "set Y level with " + totems + " totems remaining";
        if (ListenerUpdate.mc.player.posY <= (double)((Integer)((AutoLog)this.module).threshold.getValue()).intValue() && ((AutoLog)this.module).fallDamage.getValue().booleanValue() && totems <= (Integer)((AutoLog)this.module).totemCount.getValue()) {
            NetworkUtil.disconnect(fallDamageMessage);
            ((AutoLog)this.module).disable();
        }
    }
}

