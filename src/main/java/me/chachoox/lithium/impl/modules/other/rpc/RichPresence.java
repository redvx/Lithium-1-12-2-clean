/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.other.rpc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.property.StringProperty;
import me.chachoox.lithium.api.util.discord.DiscordPresence;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.impl.modules.other.rpc.ListenerTick;

public class RichPresence
extends Module {
    protected List<String> keys = new ArrayList<String>();
    public final StringProperty state = new StringProperty("gang shi", new String[]{"State", "stat"});
    public final Property<Boolean> server = new Property<Boolean>(false, new String[]{"ServerIP", "ip"}, "Displays the current server ip.");
    private final StopWatch timer = new StopWatch();
    private final StopWatch coolDown = new StopWatch();
    protected final StopWatch imageTimer = new StopWatch();
    public static DiscordPresence RPC;

    public RichPresence() {
        super("RPC", new String[]{"RPC", "discordpresence", "discord", "discordia"}, "Discord presence.", Category.OTHER);
        this.offerProperties(this.state, this.server);
        this.offerListeners(new ListenerTick(this));
        this.server.addObserver(event -> {
            if (!this.timer.passed(10000L) && this.isEnabled()) {
                Logger.getLogger().log("\u00a7cWait before you change this again");
                event.setCanceled(true);
                return;
            }
            this.timer.reset();
        });
        this.initializeKeys();
    }

    @Override
    public void onEnable() {
        if (!this.coolDown.passed(10000L)) {
            Logger.getLogger().log("\u00a7cWait before you enable this module again");
            this.disable();
            return;
        }
        RPC.enable();
        this.coolDown.reset();
        this.imageTimer.reset();
    }

    @Override
    public void onDisable() {
        if (RPC == null) {
            return;
        }
        RPC.disable();
    }

    private void initializeKeys() {
        this.keys.addAll(Arrays.asList("killa", "dead", "jorge", "mateo", "me", "yabujin", "spongenig", "gasnster", "xiuss"));
    }

    public boolean isServer() {
        return this.isEnabled() && this.server.getValue() != false;
    }
}

