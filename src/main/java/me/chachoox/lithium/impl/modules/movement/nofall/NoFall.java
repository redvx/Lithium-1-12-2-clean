/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.network.play.client.CPacketPlayer$PositionRotation
 *  net.minecraft.network.play.client.CPacketPlayer$Rotation
 */
package me.chachoox.lithium.impl.modules.movement.nofall;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.asm.mixins.network.client.ICPacketPlayer;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.LambdaListener;
import me.chachoox.lithium.impl.modules.movement.nofall.ListenerMotion;
import me.chachoox.lithium.impl.modules.movement.nofall.util.NoFallMode;
import net.minecraft.network.play.client.CPacketPlayer;

public class NoFall
extends Module {
    protected final EnumProperty<NoFallMode> mode = new EnumProperty<NoFallMode>(NoFallMode.BUCKET, new String[]{"Mode", "type", "method"}, "Packet: - Sends a fake position packet / Bucket: - If we have a bucket we will switch to it and place it. / Anti: - Lags us back whenever we are gonna hit the ground.");
    protected final NumberProperty<Float> distance = new NumberProperty<Float>(Float.valueOf(5.0f), Float.valueOf(3.0f), Float.valueOf(10.0f), Float.valueOf(0.1f), new String[]{"Distance", "dist", "falldistance"}, "How much our fall distance has to be to attempt to avoid fall damage");
    protected StopWatch stopWatch = new StopWatch();

    public NoFall() {
        super("NoFall", new String[]{"NoFall", "NoFallDamage", "AntiFallDamage"}, "Multiple methods of avoiding dying of fall damage", Category.MOVEMENT);
        this.offerProperties(this.mode, this.distance);
        this.offerListeners(new ListenerMotion(this), new LambdaListener<PacketEvent.Send>(PacketEvent.Send.class, CPacketPlayer.Rotation.class, event -> this.onPacket((CPacketPlayer)event.getPacket())), new LambdaListener<PacketEvent.Send>(PacketEvent.Send.class, CPacketPlayer.PositionRotation.class, event -> this.onPacket((CPacketPlayer)event.getPacket())), new LambdaListener<PacketEvent.Send>(PacketEvent.Send.class, CPacketPlayer.Position.class, event -> this.onPacket((CPacketPlayer)event.getPacket())), new LambdaListener<PacketEvent.Send>(PacketEvent.Send.class, CPacketPlayer.class, event -> this.onPacket((CPacketPlayer)event.getPacket())), new LambdaListener<PacketEvent.Send>(PacketEvent.Send.class, CPacketPlayer.Rotation.class, event -> this.onPacket((CPacketPlayer)event.getPacket())));
    }

    @Override
    public String getSuffix() {
        return this.mode.getFixedValue();
    }

    @Override
    public void onEnable() {
        this.stopWatch.reset();
    }

    private void onPacket(CPacketPlayer packet) {
        if (this.mode.getValue() == NoFallMode.PACKET && this.check()) {
            ((ICPacketPlayer)packet).setOnGround(true);
        }
        if (this.mode.getValue() == NoFallMode.ANTI && this.check()) {
            ((ICPacketPlayer)packet).setY(NoFall.mc.player.posY + (double)0.1f);
        }
    }

    protected boolean check() {
        return NoFall.mc.player.fallDistance > ((Float)this.distance.getValue()).floatValue();
    }
}

