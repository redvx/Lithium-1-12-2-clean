/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.command.commands.player.rotation;

import me.chachoox.lithium.impl.command.Argument;
import me.chachoox.lithium.impl.command.Command;

public class YawCommand
extends Command {
    public YawCommand() {
        super(new String[]{"Yaw", "y"}, new Argument("yaw"));
    }

    @Override
    public String execute() {
        float yaw;
        YawCommand.mc.player.rotationYaw = yaw = Float.parseFloat(this.getArgument("yaw").getValue());
        return "Set yaw to " + yaw;
    }
}

