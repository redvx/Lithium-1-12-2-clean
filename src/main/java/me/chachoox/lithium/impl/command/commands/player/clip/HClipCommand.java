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

public class HClipCommand
extends Command {
    public HClipCommand() {
        super(new String[]{"HClip", "hc"}, new Argument("blocks"));
    }

    @Override
    public String execute() {
        double h = Double.parseDouble(this.getArgument("blocks").getValue());
        EntityPlayerSP entity = HClipCommand.mc.player.getRidingEntity() != null ? HClipCommand.mc.player.getRidingEntity() : HClipCommand.mc.player;
        entity.setPosition(entity.posX + h * Math.cos(Math.toRadians(HClipCommand.mc.player.rotationYaw + 90.0f)), entity.posY, entity.posZ + h * Math.sin(Math.toRadians(HClipCommand.mc.player.rotationYaw + 90.0f)));
        return "HClipped you " + h + " blocks";
    }
}

