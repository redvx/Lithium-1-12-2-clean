/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.command.commands.misc.chat;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import me.chachoox.lithium.impl.command.Argument;
import me.chachoox.lithium.impl.command.Command;

public class CoordsCommand
extends Command {
    public CoordsCommand() {
        super(new String[]{"Coords", "Coord", "WhereTfAmIAt"}, new Argument[0]);
    }

    @Override
    public String execute() {
        String coordinates = "X: " + (int)CoordsCommand.mc.player.posX + ", Y: " + (int)CoordsCommand.mc.player.posY + ", Z: " + (int)CoordsCommand.mc.player.posZ;
        StringSelection stringSelection = new StringSelection(coordinates);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        return "Copied current position to clipboard";
    }
}

