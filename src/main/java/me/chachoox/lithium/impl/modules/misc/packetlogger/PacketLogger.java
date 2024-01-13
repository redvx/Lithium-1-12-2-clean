/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.StringUtils
 */
package me.chachoox.lithium.impl.modules.misc.packetlogger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import me.chachoox.lithium.Lithium;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.impl.modules.misc.packetlogger.ListenerReceive;
import me.chachoox.lithium.impl.modules.misc.packetlogger.ListenerSend;
import me.chachoox.lithium.impl.modules.misc.packetlogger.mode.Logging;
import me.chachoox.lithium.impl.modules.misc.packetlogger.mode.Packets;
import net.minecraft.util.StringUtils;
import org.apache.logging.log4j.Level;

public class PacketLogger
extends Module {
    protected final EnumProperty<Packets> packets = new EnumProperty<Packets>(Packets.OUTGOING, new String[]{"Packets", "packet"}, "What type of packets log. (Incoming: Server side packets / Outgoing: Client side packets)");
    protected final EnumProperty<Logging> logging = new EnumProperty<Logging>(Logging.CHAT, new String[]{"Logging", "log"}, "Chat: packets will be print in chat / File: packets are stored in a file at (Lithium/Packets/packet_log.txt)");
    protected final Property<Boolean> showCanceled = new Property<Boolean>(false, new String[]{"Canceled", "cancel"}, "Writes if the packet was cancelled or not");
    protected final Property<Boolean> cPlayer = new Property<Boolean>(false, new String[]{"CPacketPlayer", "cplayer"});
    protected final Property<Boolean> cUseEntity = new Property<Boolean>(false, new String[]{"CPacketUseEntity", "cuseentity", "cattack"});
    protected final Property<Boolean> cTryUseItemOnBlock = new Property<Boolean>(false, new String[]{"CPacketPlayerTryUseItemOnBlock", "cplayertryuseitemonblock", "cplace"});
    protected final Property<Boolean> cDigging = new Property<Boolean>(false, new String[]{"CPacketPlayerDigging", "cplayerdig", "cdig"});
    protected final Property<Boolean> cHeldItem = new Property<Boolean>(false, new String[]{"CPacketHeldItemChange", "citemchamge"});
    protected final Property<Boolean> cCloseWindow = new Property<Boolean>(false, new String[]{"CPacketCloseWindow", "cclosewindow"});
    protected final Property<Boolean> cClickWindow = new Property<Boolean>(false, new String[]{"CPacketClickWindow", "cclickwindow"});
    protected final Property<Boolean> cEntityAction = new Property<Boolean>(false, new String[]{"CPacketEntityAction", "centityaction"});
    protected final Property<Boolean> cUseItem = new Property<Boolean>(false, new String[]{"CPacketPlayerTryUseItem", "cuseitem"});
    protected final Property<Boolean> cAnimation = new Property<Boolean>(false, new String[]{"CPacketAnimation", "canimation"});
    protected final Property<Boolean> cConfirmTransaction = new Property<Boolean>(false, new String[]{"CPacketConfirmTransaction", "cconfirmtransaction"});
    protected final Property<Boolean> cConfirmTeleport = new Property<Boolean>(false, new String[]{"CPacketConfirmTeleport", "cconfirmteleport"});
    protected final Property<Boolean> sPlayerPosLook = new Property<Boolean>(false, new String[]{"SPacketPlayerPosLook", "spacketposlook"});
    protected final Property<Boolean> sPlayerListItem = new Property<Boolean>(false, new String[]{"SPacketPlayerListItem", "spacketplayerlist"});
    protected final Property<Boolean> sOpenWindow = new Property<Boolean>(false, new String[]{"SPacketOpenWindow", "sopenwindow"});
    protected final Property<Boolean> sCloseWindow = new Property<Boolean>(false, new String[]{"SPacketCloseWindow", "sclosewindow"});
    protected final Property<Boolean> sSetSlot = new Property<Boolean>(false, new String[]{"SPacketSetSlot", "ssetslot"});
    protected final Property<Boolean> sEntityStatus = new Property<Boolean>(false, new String[]{"SPacketEntityStatus", "sentitystatus"});
    protected final Property<Boolean> sPacketResource = new Property<Boolean>(false, new String[]{"SPacketResourcePackSend", "spacketresource"});
    protected final Property<Boolean> sConfirmTransaction = new Property<Boolean>(false, new String[]{"SPacketConfirmTransaction", "sconfirmtransaction"});
    private final File PACKET_LOG = new File(Lithium.PACKETS, "packet_log.txt");
    private BufferedWriter writer;

    public PacketLogger() {
        super("PacketLogger", new String[]{"PacketLogger", "packetprinter", "logger"}, "Prints packets in chat.", Category.MISC);
        this.offerProperties(this.packets, this.logging, this.showCanceled, this.cPlayer, this.cUseEntity, this.cTryUseItemOnBlock, this.cDigging, this.cHeldItem, this.cCloseWindow, this.cClickWindow, this.cEntityAction, this.cUseItem, this.cAnimation, this.cConfirmTransaction, this.cConfirmTeleport, this.sPlayerPosLook, this.sPlayerListItem, this.sOpenWindow, this.sCloseWindow, this.sSetSlot, this.sEntityStatus, this.sPacketResource, this.sConfirmTransaction);
        this.offerListeners(new ListenerSend(this), new ListenerReceive(this));
        for (Property<?> property : this.getProperties()) {
            if (!StringUtils.isNullOrEmpty((String)property.getDescription())) continue;
            property.setDescription(String.format("Prints every %s in chat.", property.getLabel()));
        }
        this.initializeFile();
        this.logging.addObserver(event -> {
            if (!event.isCanceled()) {
                this.clear();
            }
        });
    }

    @Override
    public void onEnable() {
        this.initializeWriter();
    }

    @Override
    public void onDisable() {
        this.clear();
    }

    protected void log(String message) {
        if (this.logging.getValue() == Logging.FILE) {
            return;
        }
        Logger.getLogger().logNoMark(message, false);
    }

    private void clear() {
        try {
            this.PACKET_LOG.delete();
            this.PACKET_LOG.createNewFile();
            this.writer.flush();
            this.writer.close();
            this.writer = null;
        }
        catch (Exception ex) {
            Logger.getLogger().log(Level.ERROR, "Failed to clear packet log files");
        }
    }

    protected void initializeWriter() {
        if (this.writer == null) {
            try {
                this.writer = new BufferedWriter(new FileWriter(this.PACKET_LOG));
            }
            catch (Exception e) {
                Logger.getLogger().log(Level.ERROR, "Failed to create writer");
            }
        }
    }

    private void initializeFile() {
        try {
            if (!this.PACKET_LOG.exists()) {
                this.PACKET_LOG.createNewFile();
            } else {
                this.PACKET_LOG.delete();
            }
        }
        catch (IOException e) {
            Logger.getLogger().log(Level.ERROR, "Couldn't make packet log file");
        }
    }

    protected void write(String message) {
        if (this.logging.getValue() == Logging.CHAT) {
            return;
        }
        try {
            String s = message + "\n";
            this.writer.write(s);
            this.writer.flush();
        }
        catch (IOException e) {
            Logger.getLogger().log(Level.ERROR, "Error while writing packet log text");
        }
    }
}

