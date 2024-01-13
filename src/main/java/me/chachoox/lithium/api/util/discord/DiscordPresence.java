/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.StringUtils
 */
package me.chachoox.lithium.api.util.discord;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.other.rpc.RichPresence;
import net.minecraft.util.StringUtils;

public class DiscordPresence
implements Minecraftable {
    private final DiscordRPC rpc = DiscordRPC.INSTANCE;
    public final DiscordRichPresence presence = new DiscordRichPresence();
    private String details;
    private String state;
    public boolean isRunning = false;

    public void enable() {
        if (this.isRunning) {
            return;
        }
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        RichPresence RPC_MODULE = Managers.MODULE.get(RichPresence.class);
        handlers.disconnected = (var1, var2) -> System.out.println("Discord RPC disconnected, var1: " + var1 + ", var2: " + var2);
        this.rpc.Discord_Initialize("1077970844732629043", handlers, true, "");
        this.presence.startTimestamp = System.currentTimeMillis() / 1000L;
        this.presence.details = "v0.8.0";
        this.presence.state = "Main Menu";
        this.presence.largeImageKey = "me";
        this.presence.largeImageText = "SexMaster.CC - v0.8.0";
        this.rpc.Discord_UpdatePresence(this.presence);
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    this.rpc.Discord_RunCallbacks();
                    this.details = (String)RPC_MODULE.state.getValue();
                    this.state = "";
                    if (mc.getCurrentServerData() != null && RPC_MODULE.isServer()) {
                        if (!StringUtils.isNullOrEmpty((String)DiscordPresence.mc.getCurrentServerData().serverIP)) {
                            this.state = "Currently on " + DiscordPresence.mc.getCurrentServerData().serverIP;
                        }
                    } else {
                        this.state = "Counting Money";
                    }
                    if (!this.details.equals(this.presence.details) || !this.state.equals(this.presence.state)) {
                        this.presence.startTimestamp = System.currentTimeMillis() / 1000L;
                    }
                    this.presence.details = this.details;
                    this.presence.state = this.state;
                    this.rpc.Discord_UpdatePresence(this.presence);
                }
                catch (Exception e2) {
                    e2.printStackTrace();
                }
                try {
                    Thread.sleep(5000L);
                }
                catch (InterruptedException e3) {
                    e3.printStackTrace();
                }
            }
        }, "Discord-RPC-Callback-Handler").start();
        this.isRunning = true;
    }

    public void disable() {
        this.rpc.Discord_Shutdown();
        this.isRunning = false;
    }
}

