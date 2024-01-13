/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.crash.CrashReport
 */
package me.chachoox.lithium.impl.command.commands.misc;

import me.chachoox.lithium.impl.command.Argument;
import me.chachoox.lithium.impl.command.Command;
import net.minecraft.crash.CrashReport;

public class CrashCommand
extends Command {
    public CrashCommand() {
        super(new String[]{"Crash", "FuckJayMoney"}, new Argument[0]);
    }

    @Override
    public String execute() {
        mc.crashed(new CrashReport(">.<", new Throwable()));
        return "Crashing...";
    }
}

