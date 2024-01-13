/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.command.commands.misc.connect;

import me.chachoox.lithium.api.util.network.NetworkUtil;
import me.chachoox.lithium.impl.command.Argument;
import me.chachoox.lithium.impl.command.Command;

public class DisconnectCommand
extends Command {
    public DisconnectCommand() {
        super(new String[]{"Disconnect", "Leave", "Quit"}, new Argument[0]);
    }

    @Override
    public String execute() {
        if (mc.isSingleplayer()) {
            return "You are in single player";
        }
        if (mc.getConnection() == null) {
            DisconnectCommand.mc.world.sendQuittingDisconnectingPacket();
        } else {
            String disconnectMessage = "Left server due to disconnect command being executed";
            NetworkUtil.disconnect(disconnectMessage);
        }
        return "Disconnecting...";
    }
}

