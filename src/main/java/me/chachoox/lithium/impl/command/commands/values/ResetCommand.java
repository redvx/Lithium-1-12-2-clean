/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.command.commands.values;

import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.impl.command.Argument;
import me.chachoox.lithium.impl.command.Command;
import me.chachoox.lithium.impl.managers.Managers;

public class ResetCommand
extends Command {
    public ResetCommand() {
        super(new String[]{"Reset", "r"}, new Argument("module"));
    }

    @Override
    public String execute() {
        Module module = Managers.MODULE.getModuleByLabel(this.getArgument("module").getValue());
        if (module == null) {
            return "No such module exists";
        }
        if (module.getProperties().isEmpty()) {
            return "Module has no properties";
        }
        for (Property<?> property : module.getProperties()) {
            if (module.isEnabled()) {
                property.reset();
                module.setEnabled(true);
                continue;
            }
            property.reset();
        }
        return "Reset the " + module.getLabel() + " values";
    }
}

