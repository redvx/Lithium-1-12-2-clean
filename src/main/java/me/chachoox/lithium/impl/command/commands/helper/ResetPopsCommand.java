/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.command.commands.helper;

import me.chachoox.lithium.impl.command.Argument;
import me.chachoox.lithium.impl.command.Command;
import me.chachoox.lithium.impl.managers.Managers;

public class ResetPopsCommand
extends Command {
    public ResetPopsCommand() {
        super(new String[]{"ResetPops", "PopsClear", "Clear", "ClearPops"}, new Argument[0]);
    }

    @Override
    public String execute() {
        Managers.TOTEM.getPopMap().clear();
        return "Pops Cleared";
    }
}

