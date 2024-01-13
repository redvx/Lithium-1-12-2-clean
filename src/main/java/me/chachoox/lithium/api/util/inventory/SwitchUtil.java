/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.api.util.inventory;

import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.api.util.inventory.ItemUtil;
import me.chachoox.lithium.api.util.inventory.Swap;

public class SwitchUtil
implements Minecraftable {
    public static void doSwitch(Swap swap, int slot) {
        switch (swap) {
            case SILENT: {
                ItemUtil.switchTo(slot);
                break;
            }
            case ALTERNATIVE: {
                ItemUtil.switchToAlt(slot);
            }
        }
    }
}

