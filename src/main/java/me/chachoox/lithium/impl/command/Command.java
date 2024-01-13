/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.command;

import java.util.StringJoiner;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.impl.command.Argument;

public abstract class Command
implements Minecraftable {
    private final String[] aliases;
    private final Argument[] arguments;

    public Command(String[] aliases, Argument ... arguments) {
        this.aliases = aliases;
        this.arguments = arguments;
    }

    public String execute(String[] args) {
        Argument[] arguments = this.getArguments();
        boolean valid = false;
        if (args.length < arguments.length) {
            return String.format("%s %s", args[0], this.getSyntax());
        }
        if (args.length - 1 > arguments.length) {
            return String.format("Maximum number of arguments is %s%s", "\u00a7c", arguments.length);
        }
        if (arguments.length > 0) {
            for (int index = 0; index < arguments.length; ++index) {
                Argument argument = arguments[index];
                argument.setPresent(true);
                argument.setValue(args[index + 1]);
                valid = argument.isPresent();
            }
        } else {
            valid = true;
        }
        return valid ? this.execute() : "Invalid argument(s)";
    }

    public String[] getAliases() {
        return this.aliases;
    }

    public final Argument[] getArguments() {
        return this.arguments;
    }

    public Argument getArgument(String label) {
        for (Argument argument : this.arguments) {
            if (!label.equalsIgnoreCase(argument.getLabel())) continue;
            return argument;
        }
        return null;
    }

    public String getSyntax() {
        StringJoiner stringJoiner = new StringJoiner(" ");
        for (Argument argument : this.arguments) {
            stringJoiner.add(String.format("[%s]", argument.getLabel()));
        }
        return stringJoiner.toString();
    }

    public abstract String execute();
}

