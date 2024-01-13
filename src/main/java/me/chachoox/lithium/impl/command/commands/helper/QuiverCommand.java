/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.command.commands.helper;

import me.chachoox.lithium.api.util.text.TextUtil;
import me.chachoox.lithium.impl.command.Argument;
import me.chachoox.lithium.impl.command.Command;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.player.quiver.Quiver;

public class QuiverCommand
extends Command {
    public QuiverCommand() {
        super(new String[]{"Quiv", "Qv"}, new Argument("add/del/list/clear"), new Argument("arrow"));
    }

    @Override
    public String execute() {
        Quiver QUIVER = Managers.MODULE.get(Quiver.class);
        String arg = this.getArgument("add/del/list/clear").getValue();
        if (arg.equalsIgnoreCase("LIST")) {
            if (QUIVER.getList().isEmpty()) {
                return "There is no items added";
            }
            return QUIVER.getList().toString();
        }
        if (arg.equalsIgnoreCase("CLEAR")) {
            if (QUIVER.getList().isEmpty()) {
                return "There is no items added";
            }
            QUIVER.getList().clear();
            return "Cleared Quiver list";
        }
        String arrow = this.getArgument("arrow").getLabel();
        switch (arg.toUpperCase()) {
            case "ADD": {
                if (QUIVER.getList().contains(arrow)) {
                    return String.format("[%s] is already in the list", arrow);
                }
                QUIVER.getList().add(TextUtil.formatString(arrow));
                return "Added \u00a7b" + TextUtil.getFixedName(arrow) + "\u00a7d" + " to the list";
            }
            case "DEL": 
            case "DELETE": 
            case "REMOVE": {
                QUIVER.getList().remove(TextUtil.formatString(arrow));
                return "Removed \u00a7c" + TextUtil.getFixedName(arrow) + "\u00a7d" + " from the list";
            }
        }
        return this.getSyntax();
    }
}

