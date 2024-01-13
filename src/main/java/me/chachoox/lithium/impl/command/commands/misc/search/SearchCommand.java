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

public class SearchCommand
extends Command {
    public SearchCommand() {
        super(new String[]{"Search", "Searchington"}, new Argument("namemc/crafty/laby"), new Argument("player"));
    }

    @Override
    public String execute() {
        String arg = this.getArgument("namemc/crafty/laby").getValue();
        String player = this.getArgument("player").getValue();
        if (arg.equalsIgnoreCase("NameMC") && Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI("https://namemc.com/search?q=" + player));
                return "Opening " + player + " on site -> (NameMC.com)";
            }
            catch (IOException | URISyntaxException e) {
                return "Unknown error while trying to open site -> (NameMC.com)";
            }
        }
        if (arg.equalsIgnoreCase("Crafty") && Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI("https://crafty.gg/players/" + player));
                return "Opening " + player + " on site -> (Crafty.gg)";
            }
            catch (IOException | URISyntaxException e) {
                return "Unknown error while trying to open site -> (Crafty.gg)";
            }
        }
        if (arg.equalsIgnoreCase("Laby") && Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI("https://laby.net/@" + player));
                return "Opening " + player + " on site -> (Laby.net)";
            }
            catch (IOException | URISyntaxException e) {
                return "Unknown error while trying to open site -> (Laby.net)";
            }
        }
        return this.getSyntax();
    }
}

