/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiGameOver
 */
package me.chachoox.lithium.impl.modules.combat.autolog;

import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.impl.event.events.screen.GuiScreenEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.combat.autolog.AutoLog;
import net.minecraft.client.gui.GuiGameOver;

public class ListenerGui
extends ModuleListener<AutoLog, GuiScreenEvent<GuiGameOver>> {
    public ListenerGui(AutoLog module) {
        super(module, GuiScreenEvent.class, GuiGameOver.class);
    }

    @Override
    public void call(GuiScreenEvent<GuiGameOver> event) {
        Logger.getLogger().log("\u00a7c<AutoLog> Disabling, try setting your health higher!");
        ((AutoLog)this.module).disable();
    }
}

