/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.command.commands.misc.search;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import me.chachoox.lithium.impl.command.Argument;
import me.chachoox.lithium.impl.command.Command;

public class CraftyCommand
extends Command {
    public CraftyCommand() {
        super(new String[]{"SearchCrafty", "Crafty"}, new Argument("player"));
    }

    @Override
    public String execute() {
        String player = this.getArgument("player").getValue();
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI("https://crafty.gg/players/" + player));
                return "Opening " + player + " on site -> (Crafty.gg)";
            }
            catch (IOException | URISyntaxException e) {
                return "Unknown error while trying to open site -> (Crafty.gg)";
            }
        }
        return this.getSyntax();
    }
}

