/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.command.commands.misc.refresh;

import me.chachoox.lithium.asm.mixins.audio.ISoundHandler;
import me.chachoox.lithium.impl.command.Argument;
import me.chachoox.lithium.impl.command.Command;

public class SoundRefreshCommand
extends Command {
    public SoundRefreshCommand() {
        super(new String[]{"SoundRefresh", "SoundReload", "ReloadSounds", "StupidChachooxFixThisWouldNeverHappenToMe"}, new Argument[0]);
    }

    @Override
    public String execute() {
        ((ISoundHandler)mc.getSoundHandler()).getManager().reloadSoundSystem();
        return "Reloaded sound system";
    }
}

