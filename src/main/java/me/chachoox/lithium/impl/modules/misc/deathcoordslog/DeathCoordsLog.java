/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiGameOver
 */
package me.chachoox.lithium.impl.modules.misc.deathcoordslog;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.impl.event.events.screen.GuiScreenEvent;
import me.chachoox.lithium.impl.event.listener.LambdaListener;
import net.minecraft.client.gui.GuiGameOver;

public class DeathCoordsLog
extends Module {
    private String deathPosition;

    public DeathCoordsLog() {
        super("DeathCoordsLog", new String[]{"DeathCoordsLog", "deathlog", "dcl"}, "Logs your coordinates of where you died.", Category.MISC);
        this.offerListeners(new LambdaListener<GuiScreenEvent>(GuiScreenEvent.class, GuiGameOver.class, event -> {
            this.deathPosition = String.format("X: %s, Y: %s, Z: %s", (int)DeathCoordsLog.mc.player.posX, (int)DeathCoordsLog.mc.player.posY, (int)DeathCoordsLog.mc.player.posZ);
            Logger.getLogger().logNoMark(String.format("%s<%s> You died at -> (%s)", "\u00a7c", this.getLabel(), this.deathPosition), false);
        }));
    }

    public String getDeathPosition() {
        return this.deathPosition;
    }

    public Boolean hasDied() {
        return this.deathPosition != null;
    }
}

