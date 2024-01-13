/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.inventory.Slot
 *  org.lwjgl.input.Keyboard
 */
package me.chachoox.lithium.impl.managers.minecraft.key;

import me.chachoox.lithium.api.event.bus.Listener;
import me.chachoox.lithium.api.event.bus.SubscriberImpl;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.api.util.text.TextUtil;
import me.chachoox.lithium.impl.event.events.update.TickEvent;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.inventory.Slot;
import org.lwjgl.input.Keyboard;

public class KeyManager
extends SubscriberImpl
implements Minecraftable {
    private boolean keyDown;
    public static KeyBinding KIT_DELETE_BIND;

    public KeyManager() {
        this.listeners.add(new Listener<TickEvent>(TickEvent.class){

            @Override
            public void call(TickEvent event) {
                if (Minecraftable.mc.currentScreen instanceof GuiContainer && Keyboard.isKeyDown((int)KIT_DELETE_BIND.getKeyCode())) {
                    Slot slot = ((GuiContainer)Minecraftable.mc.currentScreen).getSlotUnderMouse();
                    if (slot == null || KeyManager.this.keyDown) {
                        return;
                    }
                    Minecraftable.mc.player.sendChatMessage("/deleteukit " + TextUtil.removeColor(slot.getStack().getDisplayName()));
                    KeyManager.this.keyDown = true;
                } else if (KeyManager.this.keyDown) {
                    KeyManager.this.keyDown = false;
                }
            }
        });
    }
}

