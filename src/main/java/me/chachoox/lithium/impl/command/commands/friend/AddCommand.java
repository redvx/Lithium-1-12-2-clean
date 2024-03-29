/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.command.commands.friend;

import me.chachoox.lithium.impl.command.Argument;
import me.chachoox.lithium.impl.command.Command;
import me.chachoox.lithium.impl.managers.Managers;

public class AddCommand
extends Command {
    public AddCommand() {
        super(new String[]{"Add", "a"}, new Argument("player"));
    }

    @Override
    public String execute() {
        String name = this.getArgument("player").getValue();
        if (Managers.FRIEND.isFriend(name)) {
            return String.format("%s is already a friend", name);
        }
        Managers.FRIEND.addFriend(name);
        return String.format("Added %s%s%s as a friend", "\u00a79", name, "\u00a7d");
    }
}

