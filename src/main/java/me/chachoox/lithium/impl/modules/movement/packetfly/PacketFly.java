/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.util.internal.ConcurrentSet
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketConfirmTeleport
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.util.math.Vec3d
 */
package me.chachoox.lithium.impl.modules.movement.packetfly;

import io.netty.util.internal.ConcurrentSet;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.api.util.network.PacketUtil;
import me.chachoox.lithium.impl.modules.movement.packetfly.ListenerCPacketPlayer;
import me.chachoox.lithium.impl.modules.movement.packetfly.ListenerLogout;
import me.chachoox.lithium.impl.modules.movement.packetfly.ListenerMove;
import me.chachoox.lithium.impl.modules.movement.packetfly.ListenerPosLook;
import me.chachoox.lithium.impl.modules.movement.packetfly.ListenerUpdate;
import me.chachoox.lithium.impl.modules.movement.packetfly.mode.InvalidMode;
import me.chachoox.lithium.impl.modules.movement.packetfly.mode.LimitMode;
import me.chachoox.lithium.impl.modules.movement.packetfly.mode.PacketFlyMode;
import me.chachoox.lithium.impl.modules.movement.packetfly.mode.PhaseMode;
import me.chachoox.lithium.impl.modules.movement.packetfly.util.TeleportIDs;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.Vec3d;

public class PacketFly
extends Module {
    protected final EnumProperty<PacketFlyMode> mode = new EnumProperty<PacketFlyMode>(PacketFlyMode.FAST, new String[]{"Mode", "flightmode"}, "SetBack: - Slow packet fly, won't predict anything. / Factor: - Mode - \"Fast\" but you can change the speed with property \"Factor\", Fast: - Factor mode capped to 1.0 factor.  Limit: - Sets your motion instead of position also sends less fly packets.");
    protected final EnumProperty<PhaseMode> phase = new EnumProperty<PhaseMode>(PhaseMode.FULL, new String[]{"Phase", "PhaseMode"}, "Off: - Won't phase / Semi: - Clips halfway into blocks / Full: - Clips fully into blocks.");
    protected final EnumProperty<InvalidMode> type = new EnumProperty<InvalidMode>(InvalidMode.PRESERVE, new String[]{"Type", "InvalidMode"}, "Down: - Sends invalid packet downwards / Up: - Sends an invalid packet upwards / Preserve: - Sends randomly generated numbers for the packet. / Bounds: - Sends a packet to 255 or 0 / LimitJitter: - Sends a packet from 29-0.");
    protected final EnumProperty<LimitMode> limit = new EnumProperty<LimitMode>(LimitMode.BOTH, new String[]{"Limit", "LimitMode"}, "Tick: - Limits minecrafts ticks existed to let us know if we can packet fly. / Speed: - Limits lag time to prevent us from getting kicked, slows down if we are gonna get kicked. / Both: - Limits both ticks and speed. / None: - Limits nothing, unsafe.");
    protected final NumberProperty<Float> factor = new NumberProperty<Float>(Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(10.0f), Float.valueOf(0.1f), new String[]{"Factor", "fac", "speed", "Sped"}, "Speed / factor for mode - factor.");
    protected final Property<Boolean> conceal = new Property<Boolean>(false, new String[]{"Conceal", "conc", "bypass"}, "Forces 0.624 speed.");
    protected final Property<Boolean> antiKick = new Property<Boolean>(false, new String[]{"AntiKick", "noKick", "ak"}, "Bypasses minecraft anti fly.");
    protected final Property<Boolean> autoClip = new Property<Boolean>(false, new String[]{"AutoClip", "AutoSneak"}, "Spams sneak keybind to packet fly upwards into blocks.");
    protected ConcurrentHashMap<Integer, TeleportIDs> teleportMap = new ConcurrentHashMap();
    protected ConcurrentSet<CPacketPlayer> packets = new ConcurrentSet();
    protected Random random = new Random();
    protected double concealOffset = 0.0624;
    protected int lastTpID = -1;
    protected int flightCounter = 0;
    protected int lagTime;
    protected boolean limited = false;
    protected boolean isSneaking = false;

    public PacketFly() {
        super("PacketFly", new String[]{"PacketFly", "Pfly", "PacketFlight"}, "Makes you fly by using packets.", Category.MOVEMENT);
        this.offerProperties(this.mode, this.phase, this.type, this.limit, this.factor, this.conceal, this.antiKick, this.autoClip);
        this.offerListeners(new ListenerUpdate(this), new ListenerMove(this), new ListenerCPacketPlayer(this), new ListenerPosLook(this), new ListenerLogout(this));
    }

    @Override
    public String getSuffix() {
        return this.mode.getFixedValue();
    }

    @Override
    public void onEnable() {
        this.lastTpID = -1;
        this.reset();
        if (this.autoClip.getValue().booleanValue() && this.isSneaking) {
            PacketUtil.send(new CPacketEntityAction((Entity)PacketFly.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        }
        if (mc.isSingleplayer()) {
            Logger.getLogger().log("<PacketFly> You can't enable this in SinglePlayer.");
            this.disable();
        }
    }

    @Override
    public void onDisable() {
        PacketUtil.send(new CPacketEntityAction((Entity)PacketFly.mc.player, CPacketEntityAction.Action.START_SNEAKING));
        PacketUtil.send(new CPacketEntityAction((Entity)PacketFly.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        this.reset();
    }

    protected void reset() {
        this.flightCounter = 0;
        this.lastTpID = 0;
        this.lagTime = 0;
        this.packets.clear();
        this.teleportMap.clear();
    }

    protected boolean resetCounter(int counter) {
        ++this.flightCounter;
        if (this.flightCounter >= counter) {
            this.flightCounter = 0;
            return true;
        }
        return false;
    }

    protected void packetSender(CPacketPlayer packet) {
        this.packets.add((Object)packet);
        PacketUtil.send(packet);
    }

    private int getPreserveY() {
        int y = this.random.nextInt(29000000);
        if (this.random.nextBoolean()) {
            return y;
        }
        return -y;
    }

    private int getLimitJitterY() {
        int y = this.random.nextInt(22) + 7;
        if (this.random.nextBoolean()) {
            return y;
        }
        return -y;
    }

    protected Vec3d getTeleportPos(Vec3d vecOne, Vec3d vecTwo) {
        Vec3d vec = vecOne.add(vecTwo);
        switch ((InvalidMode)((Object)this.type.getValue())) {
            case PRESERVE: {
                vec = vec.add((double)this.getPreserveY(), 0.0, (double)this.getPreserveY());
                break;
            }
            case UP: {
                vec = vec.add(0.0, 1337.0, 0.0);
                break;
            }
            case DOWN: {
                vec = vec.add(0.0, -1337.0, 0.0);
                break;
            }
            case BOUNDS: {
                vec = new Vec3d(vec.x, PacketFly.mc.player.posY <= 10.0 ? 255.0 : 1.0, vec.z);
                break;
            }
            case LIMITJITTER: {
                vec = new Vec3d(0.0, (double)this.getLimitJitterY(), 0.0);
            }
        }
        return vec;
    }

    protected void sendPackets(Double x, Double y, Double z, Boolean teleport) {
        Vec3d vec3d = new Vec3d(x.doubleValue(), y.doubleValue(), z.doubleValue());
        Vec3d vec3d2 = PacketFly.mc.player.getPositionVector().add(vec3d);
        Vec3d vec3d3 = this.getTeleportPos(vec3d, vec3d2);
        this.packetSender((CPacketPlayer)new CPacketPlayer.Position(vec3d2.x, vec3d2.y, vec3d2.z, PacketFly.mc.player.onGround));
        this.packetSender((CPacketPlayer)new CPacketPlayer.Position(vec3d3.x, vec3d3.y, vec3d3.z, PacketFly.mc.player.onGround));
        if (teleport.booleanValue()) {
            PacketFly.mc.player.connection.sendPacket((Packet)new CPacketConfirmTeleport(++this.lastTpID));
            this.teleportMap.put(this.lastTpID, new TeleportIDs(vec3d2.x, vec3d2.y, vec3d2.z, System.currentTimeMillis()));
        }
    }

    protected boolean checkHitBox() {
        return !PacketFly.mc.world.getCollisionBoxes((Entity)PacketFly.mc.player, PacketFly.mc.player.getEntityBoundingBox().expand(-0.0625, -0.0625, -0.0625)).isEmpty();
    }

    protected boolean clearMap(Map.Entry<Integer, TeleportIDs> map) {
        return (double)System.currentTimeMillis() - map.getValue().getY() > (double)TimeUnit.SECONDS.toMillis(30L);
    }
}

