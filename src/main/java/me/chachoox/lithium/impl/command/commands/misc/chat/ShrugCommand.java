/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.command.commands.misc.chat;

import me.chachoox.lithium.impl.command.Argument;
import me.chachoox.lithium.impl.command.Command;

public class ShrugCommand
extends Command {
    public ShrugCommand() {
        super(new String[]{"Shrug", "AnnoyingAssFace"}, new Argument[0]);
    }

    @Override
    public String execute() {
        ShrugCommand.mc.player.sendChatMessage("\u00af\\_(\u30c4)_/\u00af");
        return "Shrugging";
    }
}

