/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.command.commands.friend;

import java.util.StringJoiner;
import me.chachoox.lithium.impl.command.Argument;
import me.chachoox.lithium.impl.command.Command;
import me.chachoox.lithium.impl.managers.Managers;

public class FriendsCommand
extends Command {
    public FriendsCommand() {
        super(new String[]{"Friends", "friendlist"}, new Argument[0]);
    }

    @Override
    public String execute() {
        StringJoiner stringJoiner = new StringJoiner(", ");
        Managers.FRIEND.getFriends().forEach(module -> stringJoiner.add(module.getLabel()));
        String message = String.format(String.valueOf(stringJoiner), new Object[0]);
        return String.format("Friends (%s): %s", Managers.FRIEND.getFriends().size(), message);
    }
}

