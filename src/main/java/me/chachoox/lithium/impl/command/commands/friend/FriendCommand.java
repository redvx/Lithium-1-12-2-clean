/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.command.commands.friend;

import me.chachoox.lithium.impl.command.Argument;
import me.chachoox.lithium.impl.command.Command;
import me.chachoox.lithium.impl.managers.Managers;

public class FriendCommand
extends Command {
    public FriendCommand() {
        super(new String[]{"Friend", "f"}, new Argument("add/del"), new Argument("player"));
    }

    @Override
    public String execute() {
        String argument = this.getArgument("add/del").getValue();
        String name = this.getArgument("player").getValue();
        switch (argument.toUpperCase()) {
            case "ADD": {
                if (Managers.FRIEND.isFriend(name)) {
                    return String.format("%s is already a friend", name);
                }
                Managers.FRIEND.addFriend(name);
                return String.format("Added %s%s%s as a friend", "\u00a79", name, "\u00a7d");
            }
            case "DEL": 
            case "DELETE": 
            case "REM": 
            case "REMOVE": {
                if (!Managers.FRIEND.isFriend(name)) {
                    return "That user is not a friend";
                }
                Managers.FRIEND.removeFriend(name);
                return String.format("Removed %s%s%s as a friend", "\u00a7c", name, "\u00a7d");
            }
        }
        return this.getSyntax();
    }
}

