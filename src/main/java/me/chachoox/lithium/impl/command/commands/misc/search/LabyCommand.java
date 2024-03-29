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

public class LabyCommand
extends Command {
    public LabyCommand() {
        super(new String[]{"SearchLaby", "Laby"}, new Argument("player"));
    }

    @Override
    public String execute() {
        String player = this.getArgument("player").getValue();
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
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

