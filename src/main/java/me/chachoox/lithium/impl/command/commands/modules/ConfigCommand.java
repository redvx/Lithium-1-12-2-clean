/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.command.commands.modules;

import me.chachoox.lithium.impl.command.Argument;
import me.chachoox.lithium.impl.command.Command;
import me.chachoox.lithium.impl.managers.Managers;

public class ConfigCommand
extends Command {
    public ConfigCommand() {
        super(new String[]{"Config", "ThisDoesNotEvenWorkBecauseYouCantMakeDifferentConfigsRetard"}, new Argument("save/load"));
    }

    @Override
    public String execute() {
        switch (this.getArgument("save/load").getValue().toUpperCase()) {
            case "SAVE": {
                Managers.CONFIG.saveConfig();
                Managers.FRIEND.saveFriends();
                return "Config has been saved";
            }
            case "LOAD": {
                Managers.CONFIG.loadConfig();
                Managers.FRIEND.loadFriends();
                return "Config has been loaded";
            }
        }
        return this.getSyntax();
    }
}

