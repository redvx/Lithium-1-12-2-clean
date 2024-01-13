/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.vecmath.Vector3d
 *  net.minecraft.network.Packet
 */
package me.chachoox.lithium.impl.modules.player.fakelag;

import java.awt.Color;
import java.util.ArrayList;
import java.util.function.Consumer;
import javax.vecmath.Vector3d;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.ColorProperty;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.api.util.network.PacketUtil;
import me.chachoox.lithium.impl.modules.player.fakelag.ListenerPacket;
import me.chachoox.lithium.impl.modules.player.fakelag.ListenerPosLook;
import me.chachoox.lithium.impl.modules.player.fakelag.ListenerRender;
import me.chachoox.lithium.impl.modules.player.fakeplayer.util.PlayerUtil;
import net.minecraft.network.Packet;

public class FakeLag
extends Module {
    protected final ArrayList<Packet<?>> cache = new ArrayList();
    protected final StopWatch suffixTimer = new StopWatch();
    protected final ArrayList<Vector3d> positons = new ArrayList();
    protected final ColorProperty lineColor = new ColorProperty(new Color(255, 255, 255, 125), true, new String[]{"LineColor", "color", "wirecolor"});

    public FakeLag() {
        super("FakeLag", new String[]{"FakeLag", "NetFreeze", "Blink", "291kinternet"}, "Cancels packets until we disable the module.", Category.PLAYER);
        this.offerProperties(this.lineColor);
        this.offerListeners(new ListenerPacket(this), new ListenerPosLook(this), new ListenerRender(this));
    }

    @Override
    public String getSuffix() {
        return this.suffixTimer.getTime() / 100L + "";
    }

    @Override
    public void onEnable() {
        if (this.check()) {
            this.disable();
            return;
        }
        PlayerUtil.addFakePlayerToWorld(FakeLag.mc.player.getName(), -FakeLag.mc.player.getEntityId());
        this.clear();
    }

    @Override
    public void onDisable() {
        this.cache.forEach((Consumer<Packet<?>>)((Consumer<Packet>)packet -> {
            if (packet != null) {
                PacketUtil.send(packet);
            }
        }));
        this.clear();
        PlayerUtil.removeFakePlayerFromWorld(-FakeLag.mc.player.getEntityId());
    }

    @Override
    public void onWorldLoad() {
        if (this.isEnabled()) {
            this.disable();
        }
    }

    protected boolean check() {
        return this.isNull() || mc.isSingleplayer() || mc.getConnection() == null;
    }

    protected void clear() {
        this.cache.clear();
        this.suffixTimer.reset();
        this.positons.clear();
    }
}

