/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.util.internal.ConcurrentSet
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.util.math.Vec3d
 */
package me.chachoox.lithium.impl.modules.movement.noclip;

import io.netty.util.internal.ConcurrentSet;
import java.util.Set;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.api.util.network.PacketUtil;
import me.chachoox.lithium.impl.modules.movement.noclip.ListenerCPlayer;
import me.chachoox.lithium.impl.modules.movement.noclip.ListenerMove;
import me.chachoox.lithium.impl.modules.movement.noclip.ListenerUpdate;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.Vec3d;

public class NoClip
extends Module {
    protected final Set<CPacketPlayer> packets = new ConcurrentSet();
    protected final NumberProperty<Float> phaseSpeed = new NumberProperty<Float>(Float.valueOf(0.42f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.05f), new String[]{"PhaseSpeed", "Speed", "Factor", "Sped"}, "How fast we want to clip.");
    protected final Property<Boolean> adjustMotion = new Property<Boolean>(true, new String[]{"MotionAdjust", "MotionAdj", "Motion"}, "Sets your position to the same as your motion.");
    protected final Property<Boolean> sendPackets = new Property<Boolean>(true, new String[]{"SendPacket", "SendPosition", "PositionSet"}, "Sets an extra position packet.");
    protected final Property<Boolean> cancelPackets = new Property<Boolean>(false, new String[]{"CancelPackets", "Cancel", "Canceller", "PacketCancel"}, "Limits the amount of packets we send (stops packet kicks).");
    protected final Property<Boolean> setPos = new Property<Boolean>(true, new String[]{"SetPosition", "PosSet", "Reposition"}, "Alternative way of setting position, uses minecraft method instead of packet method.");
    protected final Property<Boolean> shift = new Property<Boolean>(true, new String[]{"Shift", "Sneaked"}, "Uses sneaking offset instead of normal offset.");
    protected final Property<Boolean> removeHitbox = new Property<Boolean>(true, new String[]{"RemoveHitbox", "HitboxRemove", "NoHitbox"}, "Removes block hitbox when moving.");
    protected StopWatch timer = new StopWatch();

    public NoClip() {
        super("NoClip", new String[]{"NoClip", "Phase", "CornerClip", "Clip"}, "Phases you into half of a block to take less damage.", Category.MOVEMENT);
        this.offerListeners(new ListenerMove(this), new ListenerCPlayer(this), new ListenerUpdate(this));
        this.offerProperties(this.phaseSpeed, this.adjustMotion, this.sendPackets, this.cancelPackets, this.setPos, this.shift, this.removeHitbox);
    }

    @Override
    public void onEnable() {
        this.timer.reset();
    }

    protected double[] getMotion() {
        float moveForward = NoClip.mc.player.movementInput.moveForward;
        float moveStrafe = NoClip.mc.player.movementInput.moveStrafe;
        float rotationYaw = NoClip.mc.player.prevRotationYaw + (NoClip.mc.player.rotationYaw - NoClip.mc.player.prevRotationYaw) * mc.getRenderPartialTicks();
        if (moveForward != 0.0f) {
            if (moveStrafe > 0.0f) {
                rotationYaw += (float)(moveForward > 0.0f ? -45 : 45);
            } else if (moveStrafe < 0.0f) {
                rotationYaw += (float)(moveForward > 0.0f ? 45 : -45);
            }
            moveStrafe = 0.0f;
            if (moveForward > 0.0f) {
                moveForward = 1.0f;
            } else if (moveForward < 0.0f) {
                moveForward = -1.0f;
            }
        }
        double posX = (double)moveForward * 0.031 * -Math.sin(Math.toRadians(rotationYaw)) + (double)moveStrafe * 0.031 * Math.cos(Math.toRadians(rotationYaw));
        double posZ = (double)moveForward * 0.031 * Math.cos(Math.toRadians(rotationYaw)) - (double)moveStrafe * 0.031 * -Math.sin(Math.toRadians(rotationYaw));
        return new double[]{posX, posZ};
    }

    protected void sendPackets(double x, double y, double z) {
        Vec3d vec = new Vec3d(x, y, z);
        Vec3d position = NoClip.mc.player.getPositionVector().add(vec);
        if (this.sendPackets.getValue().booleanValue()) {
            this.packetSender((CPacketPlayer)new CPacketPlayer.Position(position.x, position.y, position.z, NoClip.mc.player.onGround));
        }
        if (this.setPos.getValue().booleanValue()) {
            NoClip.mc.player.setPosition(position.x, position.y, position.z);
        }
    }

    private void packetSender(CPacketPlayer packet) {
        this.packets.add(packet);
        PacketUtil.send(packet);
    }

    protected boolean checkHitBoxes() {
        return !NoClip.mc.world.getCollisionBoxes((Entity)NoClip.mc.player, NoClip.mc.player.getEntityBoundingBox().expand(-0.0625, -0.0625, -0.0625)).isEmpty();
    }
}

