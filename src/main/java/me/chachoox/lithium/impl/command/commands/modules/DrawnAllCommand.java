/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.command.commands.modules;

import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.impl.command.Argument;
import me.chachoox.lithium.impl.command.Command;
import me.chachoox.lithium.impl.managers.Managers;

public class DrawnAllCommand
extends Command {
    public DrawnAllCommand() {
        super(new String[]{"DrawnAll", "DrawAll"}, new Argument[0]);
    }

    @Override
    public String execute() {
        for (Module module : Managers.MODULE.getModules()) {
            module.setDrawn(module.drawn.getValue() == false);
        }
        return String.format("Drawn %s modules", Managers.MODULE.getModules().size());
    }
}

