/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.command.commands.player.rotation;

import me.chachoox.lithium.impl.command.Argument;
import me.chachoox.lithium.impl.command.Command;

public class PitchCommand
extends Command {
    public PitchCommand() {
        super(new String[]{"Pitch", "p"}, new Argument("pitch"));
    }

    @Override
    public String execute() {
        float pitch;
        PitchCommand.mc.player.rotationPitch = pitch = Float.parseFloat(this.getArgument("pitch").getValue());
        return "Set pitch to " + pitch;
    }
}

