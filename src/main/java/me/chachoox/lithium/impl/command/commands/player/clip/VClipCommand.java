/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityPlayerSP
 */
package me.chachoox.lithium.impl.command.commands.player.clip;

import me.chachoox.lithium.impl.command.Argument;
import me.chachoox.lithium.impl.command.Command;
import net.minecraft.client.entity.EntityPlayerSP;

public class VClipCommand
extends Command {
    public VClipCommand() {
        super(new String[]{"VClip", "vc"}, new Argument("blocks"));
    }

    @Override
    public String execute() {
        double amount = Double.parseDouble(this.getArgument("blocks").getValue());
        EntityPlayerSP entity = VClipCommand.mc.player.getRidingEntity() != null ? VClipCommand.mc.player.getRidingEntity() : VClipCommand.mc.player;
        entity.setPosition(entity.posX, entity.posY + amount, entity.posZ);
        return "VClipped you " + amount + " blocks";
    }
}

