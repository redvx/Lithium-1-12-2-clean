/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 */
package me.chachoox.lithium.impl.modules.movement.phase;

import me.chachoox.lithium.api.event.events.Stage;
import me.chachoox.lithium.api.util.movement.MovementUtil;
import me.chachoox.lithium.api.util.network.PacketUtil;
import me.chachoox.lithium.impl.event.events.movement.MotionUpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.movement.phase.Phase;
import me.chachoox.lithium.impl.modules.movement.phase.util.PhaseMode;
import me.chachoox.lithium.impl.modules.other.hud.Hud;
import net.minecraft.network.play.client.CPacketPlayer;

public class ListenerMotion
extends ModuleListener<Phase, MotionUpdateEvent> {
    public ListenerMotion(Phase module) {
        super(module, MotionUpdateEvent.class);
    }

    @Override
    public void call(MotionUpdateEvent event) {
        if (!((Phase)this.module).timer.passed((long)((Integer)((Phase)this.module).delay.getValue()).intValue() * 1000L)) {
            return;
        }
        block0 : switch ((PhaseMode)((Object)((Phase)this.module).mode.getValue())) {
            case PACKET: {
                if (event.getStage() != Stage.POST) break;
                double multiplier1 = 0.3;
                double mx1 = Math.cos(Math.toRadians(ListenerMotion.mc.player.rotationYaw + 90.0f));
                double mz1 = Math.sin(Math.toRadians(ListenerMotion.mc.player.rotationYaw + 90.0f));
                double xOff1 = (double)ListenerMotion.mc.player.movementInput.moveForward * multiplier1 * mx1 + (double)ListenerMotion.mc.player.movementInput.moveStrafe * multiplier1 * mz1;
                double zOff1 = (double)ListenerMotion.mc.player.movementInput.moveForward * multiplier1 * mz1 - (double)ListenerMotion.mc.player.movementInput.moveStrafe * multiplier1 * mx1;
                PacketUtil.send(new CPacketPlayer.Position(ListenerMotion.mc.player.posX + xOff1, ListenerMotion.mc.player.posY, ListenerMotion.mc.player.posZ + zOff1, false));
                for (int i = 1; i < 10; ++i) {
                    PacketUtil.send(new CPacketPlayer.Position(ListenerMotion.mc.player.posX, 8.988465674311579E307, ListenerMotion.mc.player.posZ, false));
                }
                ListenerMotion.mc.player.setPosition(ListenerMotion.mc.player.posX + xOff1, ListenerMotion.mc.player.posY, ListenerMotion.mc.player.posZ + zOff1);
                break;
            }
            case ZOOM: {
                ListenerMotion.mc.player.motionY = 0.0;
                double mx = Math.cos(Math.toRadians(ListenerMotion.mc.player.rotationYaw + 90.0f));
                double mz = Math.sin(Math.toRadians(ListenerMotion.mc.player.rotationYaw + 90.0f));
                double xOff = (double)ListenerMotion.mc.player.movementInput.moveForward * 0.152 * mx + (double)ListenerMotion.mc.player.movementInput.moveStrafe * 0.152 * mz;
                double zOff = (double)ListenerMotion.mc.player.movementInput.moveForward * 0.152 * mz - (double)ListenerMotion.mc.player.movementInput.moveStrafe * 0.152 * mx;
                PacketUtil.send(new CPacketPlayer.Position(ListenerMotion.mc.player.posX + ListenerMotion.mc.player.motionX * 11.0 + xOff, ListenerMotion.mc.player.posY + (ListenerMotion.mc.gameSettings.keyBindJump.isPressed() ? 0.0624 : (((Phase)this.module).zoomies ? 0.0625 : 1.0E-8)) - (ListenerMotion.mc.gameSettings.keyBindSneak.isPressed() ? 0.0624 : (((Phase)this.module).zoomies ? 0.0625 : 2.0E-8)), ListenerMotion.mc.player.posZ + ListenerMotion.mc.player.motionZ * 11.0 + zOff, false));
                PacketUtil.send(new CPacketPlayer.Position(ListenerMotion.mc.player.posX + ListenerMotion.mc.player.motionX * 11.0 + xOff, 1337.0 + ListenerMotion.mc.player.posY, ListenerMotion.mc.player.posZ + ListenerMotion.mc.player.motionZ * 11.0 + zOff, false));
                ListenerMotion.mc.player.setPositionAndUpdate(ListenerMotion.mc.player.posX + xOff, ListenerMotion.mc.player.posY, ListenerMotion.mc.player.posZ + zOff);
                ((Phase)this.module).zoomies = !((Phase)this.module).zoomies;
                break;
            }
            case INFINITE: {
                ListenerMotion.mc.player.motionY = 0.0;
                switch (Hud.getDirection4D()) {
                    case 0: {
                        PacketUtil.send(new CPacketPlayer.Position(ListenerMotion.mc.player.posX, ListenerMotion.mc.player.posY, ListenerMotion.mc.player.posZ + 0.5, ListenerMotion.mc.player.onGround));
                        ListenerMotion.mc.player.setPosition(ListenerMotion.mc.player.posX, ListenerMotion.mc.player.posY, ListenerMotion.mc.player.posZ + 1.0);
                        PacketUtil.send(new CPacketPlayer.Position(Double.POSITIVE_INFINITY, ListenerMotion.mc.player.posY, ListenerMotion.mc.player.posZ, ListenerMotion.mc.player.onGround));
                        break block0;
                    }
                    case 1: {
                        PacketUtil.send(new CPacketPlayer.Position(ListenerMotion.mc.player.posX - 0.5, ListenerMotion.mc.player.posY, ListenerMotion.mc.player.posZ, ListenerMotion.mc.player.onGround));
                        ListenerMotion.mc.player.setPosition(ListenerMotion.mc.player.posX - 1.0, ListenerMotion.mc.player.posY, ListenerMotion.mc.player.posZ);
                        PacketUtil.send(new CPacketPlayer.Position(Double.POSITIVE_INFINITY, ListenerMotion.mc.player.posY, ListenerMotion.mc.player.posZ, ListenerMotion.mc.player.onGround));
                        break block0;
                    }
                    case 2: {
                        PacketUtil.send(new CPacketPlayer.Position(ListenerMotion.mc.player.posX, ListenerMotion.mc.player.posY, ListenerMotion.mc.player.posZ - 0.5, ListenerMotion.mc.player.onGround));
                        ListenerMotion.mc.player.setPosition(ListenerMotion.mc.player.posX, ListenerMotion.mc.player.posY, ListenerMotion.mc.player.posZ - 1.0);
                        PacketUtil.send(new CPacketPlayer.Position(Double.POSITIVE_INFINITY, ListenerMotion.mc.player.posY, ListenerMotion.mc.player.posZ, ListenerMotion.mc.player.onGround));
                        break block0;
                    }
                }
                PacketUtil.send(new CPacketPlayer.Position(ListenerMotion.mc.player.posX + 0.5, ListenerMotion.mc.player.posY, ListenerMotion.mc.player.posZ, ListenerMotion.mc.player.onGround));
                ListenerMotion.mc.player.setPosition(ListenerMotion.mc.player.posX + 1.0, ListenerMotion.mc.player.posY, ListenerMotion.mc.player.posZ);
                PacketUtil.send(new CPacketPlayer.Position(Double.POSITIVE_INFINITY, ListenerMotion.mc.player.posY, ListenerMotion.mc.player.posZ, ListenerMotion.mc.player.onGround));
                break;
            }
            case SKIP: {
                double[] yOffsets = new double[]{0.025f, 0.028571428997176036, 0.033333333830038704, 0.04000000059604645, 0.05f, 0.06666666766007741, 0.1f, 0.2f, 0.04000000059604645, 0.033333333830038704, 0.028571428997176036, 0.025f};
                double[] dirSpeed = MovementUtil.strafe(0.031);
                for (int index = 0; index < yOffsets.length; ++index) {
                    PacketUtil.send(new CPacketPlayer.Position(ListenerMotion.mc.player.posX, ListenerMotion.mc.player.posY + yOffsets[index], ListenerMotion.mc.player.posZ, ListenerMotion.mc.player.onGround));
                    PacketUtil.send(new CPacketPlayer.Position(ListenerMotion.mc.player.posX + dirSpeed[0] * (double)index, ListenerMotion.mc.player.posY, ListenerMotion.mc.player.posZ + dirSpeed[1] * (double)index, ListenerMotion.mc.player.onGround));
                }
                break;
            }
        }
        ((Phase)this.module).timer.reset();
    }
}

