/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 */
package me.chachoox.lithium.impl.command.commands.player.clip;

import me.chachoox.lithium.impl.command.Argument;
import me.chachoox.lithium.impl.command.Command;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class HitboxDesyncCommand
extends Command {
    public HitboxDesyncCommand() {
        super(new String[]{"HitboxDesync", "hd"}, new Argument[0]);
    }

    @Override
    public String execute() {
        EnumFacing f = HitboxDesyncCommand.mc.player.getHorizontalFacing();
        AxisAlignedBB bb = HitboxDesyncCommand.mc.player.getEntityBoundingBox();
        Vec3d center = bb.getCenter();
        Vec3d offset = new Vec3d(f.getDirectionVec());
        double MAGIC_OFFSET = 0.20000996883537;
        Vec3d fin = this.merge(new Vec3d((Vec3i)new BlockPos(center)).add(0.5, 0.0, 0.5).add(offset.scale(MAGIC_OFFSET)), f);
        HitboxDesyncCommand.mc.player.setPositionAndUpdate(fin.x == 0.0 ? HitboxDesyncCommand.mc.player.posX : fin.x, HitboxDesyncCommand.mc.player.posY, fin.z == 0.0 ? HitboxDesyncCommand.mc.player.posZ : fin.z);
        return "Executed russian exploit";
    }

    private Vec3d merge(Vec3d a, EnumFacing facing) {
        return new Vec3d(a.x * (double)Math.abs(facing.getDirectionVec().getX()), a.y * (double)Math.abs(facing.getDirectionVec().getY()), a.z * (double)Math.abs(facing.getDirectionVec().getZ()));
    }
}

