/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.settings.KeyBinding
 */
package me.chachoox.lithium.impl.modules.movement.inventorymove;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.impl.modules.movement.inventorymove.ListenerClick;
import me.chachoox.lithium.impl.modules.movement.inventorymove.ListenerUpdate;
import net.minecraft.client.settings.KeyBinding;

public class InventoryMove
extends Module {
    protected final Property<Boolean> crouch = new Property<Boolean>(false, new String[]{"Sneak", "Crouch"}, "Allows sneaking in guis.");
    protected final Property<Boolean> jumping = new Property<Boolean>(false, new String[]{"Jump", "aetra"}, "Allows jumping in guis.");
    protected final Property<Boolean> sprint = new Property<Boolean>(false, new String[]{"Sprint", "Run"}, "Allows sprinting in guis.");

    public InventoryMove() {
        super("InventoryMove", new String[]{"InventoryMove", "InvMove", "Im"}, "Allows you to move in inventories.", Category.MOVEMENT);
        this.offerProperties(this.sprint, this.crouch, this.jumping);
        this.offerListeners(new ListenerClick(this), new ListenerUpdate(this));
    }

    @Override
    public void onDisable() {
        if (InventoryMove.mc.currentScreen != null) {
            KeyBinding.setKeyBindState((int)InventoryMove.mc.gameSettings.keyBindForward.getKeyCode(), (boolean)false);
            KeyBinding.setKeyBindState((int)InventoryMove.mc.gameSettings.keyBindBack.getKeyCode(), (boolean)false);
            KeyBinding.setKeyBindState((int)InventoryMove.mc.gameSettings.keyBindLeft.getKeyCode(), (boolean)false);
            KeyBinding.setKeyBindState((int)InventoryMove.mc.gameSettings.keyBindRight.getKeyCode(), (boolean)false);
            KeyBinding.setKeyBindState((int)InventoryMove.mc.gameSettings.keyBindJump.getKeyCode(), (boolean)false);
            KeyBinding.setKeyBindState((int)InventoryMove.mc.gameSettings.keyBindSprint.getKeyCode(), (boolean)false);
            KeyBinding.setKeyBindState((int)InventoryMove.mc.gameSettings.keyBindSneak.getKeyCode(), (boolean)false);
        }
    }
}

