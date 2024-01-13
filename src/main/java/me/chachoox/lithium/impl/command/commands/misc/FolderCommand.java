/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.command.commands.misc;

import java.awt.Desktop;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import me.chachoox.lithium.impl.command.Argument;
import me.chachoox.lithium.impl.command.Command;

public class FolderCommand
extends Command {
    public FolderCommand() {
        super(new String[]{"Folder", "OpenFolder"}, new Argument[0]);
    }

    @Override
    public String execute() {
        try {
            Path lithium = Paths.get("Lithium", new String[0]);
            Desktop.getDesktop().open(lithium.toFile());
            return String.format("Opened %s folder", lithium.getFileName());
        }
        catch (IOException e) {
            return "Could not find (Lithium) folder";
        }
    }
}

