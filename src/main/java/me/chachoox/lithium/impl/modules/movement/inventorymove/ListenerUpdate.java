/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiChat
 *  net.minecraft.client.settings.KeyBinding
 *  org.lwjgl.input.Keyboard
 */
package me.chachoox.lithium.impl.modules.movement.inventorymove;

import me.chachoox.lithium.impl.event.events.update.UpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.movement.inventorymove.InventoryMove;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class ListenerUpdate
extends ModuleListener<InventoryMove, UpdateEvent> {
    public ListenerUpdate(InventoryMove module) {
        super(module, UpdateEvent.class);
    }

    @Override
    public void call(UpdateEvent event) {
        if (ListenerUpdate.mc.currentScreen != null && !(ListenerUpdate.mc.currentScreen instanceof GuiChat)) {
            KeyBinding.setKeyBindState((int)ListenerUpdate.mc.gameSettings.keyBindForward.getKeyCode(), (boolean)Keyboard.isKeyDown((int)ListenerUpdate.mc.gameSettings.keyBindForward.getKeyCode()));
            KeyBinding.setKeyBindState((int)ListenerUpdate.mc.gameSettings.keyBindBack.getKeyCode(), (boolean)Keyboard.isKeyDown((int)ListenerUpdate.mc.gameSettings.keyBindBack.getKeyCode()));
            KeyBinding.setKeyBindState((int)ListenerUpdate.mc.gameSettings.keyBindLeft.getKeyCode(), (boolean)Keyboard.isKeyDown((int)ListenerUpdate.mc.gameSettings.keyBindLeft.getKeyCode()));
            KeyBinding.setKeyBindState((int)ListenerUpdate.mc.gameSettings.keyBindRight.getKeyCode(), (boolean)Keyboard.isKeyDown((int)ListenerUpdate.mc.gameSettings.keyBindRight.getKeyCode()));
            if (((InventoryMove)this.module).jumping.getValue().booleanValue()) {
                KeyBinding.setKeyBindState((int)ListenerUpdate.mc.gameSettings.keyBindJump.getKeyCode(), (boolean)Keyboard.isKeyDown((int)ListenerUpdate.mc.gameSettings.keyBindJump.getKeyCode()));
            }
            if (((InventoryMove)this.module).sprint.getValue().booleanValue()) {
                KeyBinding.setKeyBindState((int)ListenerUpdate.mc.gameSettings.keyBindSprint.getKeyCode(), (boolean)Keyboard.isKeyDown((int)ListenerUpdate.mc.gameSettings.keyBindSprint.getKeyCode()));
            }
            if (((InventoryMove)this.module).crouch.getValue().booleanValue()) {
                KeyBinding.setKeyBindState((int)ListenerUpdate.mc.gameSettings.keyBindSneak.getKeyCode(), (boolean)Keyboard.isKeyDown((int)ListenerUpdate.mc.gameSettings.keyBindSneak.getKeyCode()));
            }
        }
    }
}

