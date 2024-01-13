/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package me.chachoox.lithium.impl.command.commands.modules;

import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.util.Bind;
import me.chachoox.lithium.impl.command.Argument;
import me.chachoox.lithium.impl.command.Command;
import me.chachoox.lithium.impl.managers.Managers;
import org.lwjgl.input.Keyboard;

public class BindCommand
extends Command {
    public BindCommand() {
        super(new String[]{"Bind", "KeyBind"}, new Argument("module"), new Argument("key"));
    }

    @Override
    public String execute() {
        Module module = Managers.MODULE.getModuleByLabel(this.getArgument("module").getValue());
        int key = Keyboard.getKeyIndex((String)this.getArgument("key").getValue().toUpperCase());
        if (module == null) {
            return "No such module exists";
        }
        module.bind.setValue(new Bind(key));
        return String.format("%s has been bound to %s", module.getLabel(), Keyboard.getKeyName((int)key).toUpperCase());
    }
}

