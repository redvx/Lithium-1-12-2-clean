/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.command.commands.misc.list;

import java.util.StringJoiner;
import me.chachoox.lithium.impl.command.Argument;
import me.chachoox.lithium.impl.command.Command;
import me.chachoox.lithium.impl.managers.Managers;

public class ModuleCommand
extends Command {
    public ModuleCommand() {
        super(new String[]{"Modules", "mods", "ModuleList", "ModList"}, new Argument[0]);
    }

    @Override
    public String execute() {
        StringJoiner stringJoiner = new StringJoiner(", ");
        Managers.MODULE.getModules().forEach(module -> stringJoiner.add(module.getLabel()));
        String message = String.format(String.valueOf(stringJoiner), new Object[0]);
        return String.format("Modules (%s): %s", Managers.MODULE.getModules().size(), message);
    }
}

