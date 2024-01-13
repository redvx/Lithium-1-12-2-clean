/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.command.commands.misc.refresh;

import me.chachoox.lithium.impl.command.Argument;
import me.chachoox.lithium.impl.command.Command;

public class ResourceRefreshCommand
extends Command {
    public ResourceRefreshCommand() {
        super(new String[]{"ResourseRefresh", "RefreshResourses", "PackReload"}, new Argument[0]);
    }

    @Override
    public String execute() {
        mc.refreshResources();
        return "Textures reloaded";
    }
}

