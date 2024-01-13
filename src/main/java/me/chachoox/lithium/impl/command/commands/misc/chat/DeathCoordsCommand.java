/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.command.commands.misc.chat;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import me.chachoox.lithium.impl.command.Argument;
import me.chachoox.lithium.impl.command.Command;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.misc.deathcoordslog.DeathCoordsLog;

public class DeathCoordsCommand
extends Command {
    public DeathCoordsCommand() {
        super(new String[]{"DeathCoords", "DeathSpot", "DeathLocation", "DeathPos", "WhereTfIDied"}, new Argument[0]);
    }

    @Override
    public String execute() {
        DeathCoordsLog deathCoordsLog = Managers.MODULE.get(DeathCoordsLog.class);
        if (deathCoordsLog.hasDied().booleanValue()) {
            StringSelection stringSelection = new StringSelection(deathCoordsLog.getDeathPosition());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
            return "Copied Death coordinates";
        }
        return "You haven't died yet";
    }
}

