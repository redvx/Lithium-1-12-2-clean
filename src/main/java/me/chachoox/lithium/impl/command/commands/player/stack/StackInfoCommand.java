/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.NBTTagCompound
 */
package me.chachoox.lithium.impl.command.commands.player.stack;

import me.chachoox.lithium.impl.command.Argument;
import me.chachoox.lithium.impl.command.Command;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class StackInfoCommand
extends Command {
    public StackInfoCommand() {
        super(new String[]{"StackInfo", "SI", "DumpStack", "DS"}, new Argument[0]);
    }

    @Override
    public String execute() {
        ItemStack stack = StackInfoCommand.mc.player.inventory.getStackInSlot(StackInfoCommand.mc.player.inventory.currentItem);
        String info = stack.getDisplayName();
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        stack.writeToNBT(nbtTagCompound);
        info = info + ": " + nbtTagCompound;
        return info;
    }
}

