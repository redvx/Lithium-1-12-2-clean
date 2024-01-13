/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.player.fakeplayer;

import java.util.ArrayList;
import java.util.List;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.impl.modules.player.fakeplayer.ListenerDeath;
import me.chachoox.lithium.impl.modules.player.fakeplayer.ListenerMotion;
import me.chachoox.lithium.impl.modules.player.fakeplayer.position.Position;
import me.chachoox.lithium.impl.modules.player.fakeplayer.util.PlayerUtil;

public class FakePlayer
extends Module {
    protected final Property<Boolean> record = new Property<Boolean>(false, new String[]{"Record", "playrecord", "rec", "r"}, "Tracks all movement until this is disabled.");
    protected final Property<Boolean> playRecording = new Property<Boolean>(false, new String[]{"Play", "PlayRecord", "pla", "p"}, "Plays the tracked movement.");
    protected final List<Position> playerPositions = new ArrayList<Position>();

    public FakePlayer() {
        super("FakePlayer", new String[]{"FakePlayer", "FakeEntity", "TestPlayer", "PollosxD", "FakePlayerington"}, "Spawns a fake player into the world.", Category.PLAYER);
        this.offerProperties(this.record, this.playRecording);
        this.offerListeners(new ListenerMotion(this), new ListenerDeath(this));
    }

    @Override
    public String getSuffix() {
        return this.record.getValue() != false ? "Recording" : (this.playRecording.getValue() != false ? "Playing" : null);
    }

    @Override
    public void onEnable() {
        PlayerUtil.addFakePlayerToWorld("yung_lean_fan1", -2147483647);
    }

    @Override
    public void onDisable() {
        PlayerUtil.removeFakePlayerFromWorld(-2147483647);
        this.playerPositions.clear();
    }

    @Override
    public void onWorldLoad() {
        if (this.isEnabled()) {
            this.disable();
        }
    }

    public List<Position> getPlayerPositions() {
        return this.playerPositions;
    }
}

