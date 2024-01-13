/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.misc.deathannouncer;

import java.util.Random;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.impl.modules.misc.deathannouncer.ListenerDeath;
import me.chachoox.lithium.impl.modules.misc.deathannouncer.util.Announce;

public class DeathAnnouncer
extends Module {
    protected final NumberProperty<Integer> range = new NumberProperty<Integer>(10, 5, 30, new String[]{"Range", "r", "rangejamin"}, "How close we have to be to the death to announce it.");
    protected final NumberProperty<Integer> yLevel = new NumberProperty<Integer>(50, 1, 255, new String[]{"YLevel", "height", "ylvl"}, "How far down we have to be to announce the death.");
    protected final NumberProperty<Integer> delay = new NumberProperty<Integer>(5, 0, 15, new String[]{"Delay", "del", "d"}, "Delay for sending message to make it look like we typed it.");
    protected final EnumProperty<Announce> killSayPreset = new EnumProperty<Announce>(Announce.TROLLGOD, new String[]{"Message", "preset", "mode", "type"}, "What we want the message to be.");
    protected final Property<Boolean> greenText = new Property<Boolean>(false, new String[]{"GreenText", "greentxt", "greentx"}, "Puts > in front of your message to make it green on some servers.");
    protected StopWatch timer = new StopWatch();
    protected String name;

    public DeathAnnouncer() {
        super("DeathAnnouncer", new String[]{"DeathAnnouncer", "autogg", "autoez"}, "Announces when a player dies.", Category.MISC);
        this.offerProperties(this.range, this.yLevel, this.delay, this.killSayPreset, this.greenText);
        this.offerListeners(new ListenerDeath(this));
    }

    @Override
    public void onDisable() {
        this.name = null;
    }

    @Override
    public String getSuffix() {
        return this.killSayPreset.getFixedValue();
    }

    protected String trollGodMessage() {
        return "You got boiled alhamdulillah " + this.name;
    }

    protected String prayerMessage() {
        return "Mashallah you just got sent to Jahannam " + this.name;
    }

    protected String auroraMessage() {
        return "GET FUCKED BY AURORA PUSSY " + this.name;
    }

    protected String autismMessage() {
        return "> EZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ " + this.name;
    }

    protected String pollosMessage() {
        return "i just raped " + this.name + " thanks to pollosmod.wtf";
    }

    protected String abyssMessage() {
        return "GG " + this.name + " Abyss on top!";
    }

    protected String phobosMessage() {
        return this.name + " is a noob hahaha phobos on tope";
    }

    protected String nftMessage() {
        return "Follow me on rumble https://rumble.com/BitCrypto89 " + this.name;
    }

    protected String trollHackMessages() {
        String[] konasMessages = new String[]{"Good fight" + this.name + "! Troll Hack owns me and all", "gg, " + this.name, "Troll Hack on top! ez " + this.name, "You just got ez'd " + this.name, "You just got naenae'd by Troll Hack, " + this.name};
        return konasMessages[new Random().nextInt(konasMessages.length)];
    }

    protected String kamiMessage() {
        return "KAMI BLUE on top! ez " + this.name;
    }

    protected String konasMessages() {
        String[] konasMessages = new String[]{"you just got nae nae'd by konas " + this.name, this.name + " tango down", this.name + " you just felt the wrath of konas client", "I guess konas ca is too fast for you " + this.name, this.name + " konas ca is too fast!", "you just got ez'd by konas client " + this.name};
        return konasMessages[new Random().nextInt(konasMessages.length)];
    }

    protected String wurstPlusMessages() {
        String[] wurstPlusMessages = new String[]{"you just got nae nae'd by wurst+2 ", "you just got nae nae'd by wurst+2 - discord.gg/wurst", "you just got nae nae'd by wurst+3", "you just got nae nae'd by wurst+3 | discord.gg/wurst"};
        return wurstPlusMessages[new Random().nextInt(wurstPlusMessages.length)];
    }

    protected String wellPlayedMessages() {
        String[] wellPlayedMessages = new String[]{"wp", this.name + " wp", "well played", this.name + " well played"};
        return wellPlayedMessages[new Random().nextInt(wellPlayedMessages.length)];
    }
}

