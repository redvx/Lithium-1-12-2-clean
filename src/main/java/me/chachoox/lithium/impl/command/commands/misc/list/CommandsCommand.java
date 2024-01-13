/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.command.commands.misc.list;

import java.util.Arrays;
import java.util.StringJoiner;
import me.chachoox.lithium.impl.command.Argument;
import me.chachoox.lithium.impl.command.Command;
import me.chachoox.lithium.impl.managers.Managers;

public class CommandsCommand
extends Command {
    public CommandsCommand() {
        super(new String[]{"Commands", "cmds", "commands"}, new Argument[0]);
    }

    @Override
    public String execute() {
        StringJoiner stringJoiner = new StringJoiner(", ");
        Managers.COMMAND.getCommands().forEach(command -> stringJoiner.add(Arrays.stream(command.getAliases()).findFirst().get()));
        return String.format("Commands (%s) %s", Managers.COMMAND.getCommands().size(), stringJoiner);
    }
}

