/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.misc.packetcanceller;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.impl.modules.misc.packetcanceller.ListenerReceive;
import me.chachoox.lithium.impl.modules.misc.packetcanceller.ListenerSend;

public class PacketCanceller
extends Module {
    protected final Property<Boolean> cTryUseItemOnBlock = new Property<Boolean>(false, new String[]{"CPacketPlayerTryUseItemOnBlock"});
    protected final Property<Boolean> cDigging = new Property<Boolean>(false, new String[]{"CPacketPlayerDigging"});
    protected final Property<Boolean> cInput = new Property<Boolean>(false, new String[]{"CPacketInput"});
    protected final Property<Boolean> cPlayer = new Property<Boolean>(false, new String[]{"CPacketPlayer"});
    protected final Property<Boolean> cEntityAction = new Property<Boolean>(false, new String[]{"CPacketEntityAction"});
    protected final Property<Boolean> cUseEntity = new Property<Boolean>(false, new String[]{"CPacketUseEntity"});
    protected final Property<Boolean> cVehicleMove = new Property<Boolean>(false, new String[]{"CPacketVehicleMove"});
    protected final Property<Boolean> sCloseWindow = new Property<Boolean>(false, new String[]{"SPacketCloseWindow"});
    protected int packets = 0;

    public PacketCanceller() {
        super("PacketCanceller", new String[]{"PacketCanceller", "antipackets", "antipacket"}, "Cancels outgoing packets.", Category.MISC);
        this.offerProperties(this.cTryUseItemOnBlock, this.cDigging, this.cInput, this.cPlayer, this.cEntityAction, this.cUseEntity, this.cVehicleMove, this.sCloseWindow);
        this.offerListeners(new ListenerSend(this), new ListenerReceive(this));
        for (Property<?> property : this.getProperties()) {
            property.setDescription(String.format("Cancels every %s being sent.", property.getLabel()));
        }
    }

    @Override
    public void onEnable() {
        this.packets = 0;
    }

    @Override
    public String getSuffix() {
        return "" + this.packets;
    }
}

