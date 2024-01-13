/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.inventory.GuiScreenHorseInventory
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.BlockPos
 */
package me.chachoox.lithium.impl.managers.minecraft;

import me.chachoox.lithium.Lithium;
import me.chachoox.lithium.api.event.bus.Listener;
import me.chachoox.lithium.api.event.bus.SubscriberImpl;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.asm.mixins.network.client.ICPacketPlayerTryUseItemOnBlock;
import me.chachoox.lithium.impl.event.events.network.DisconnectEvent;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.events.update.UpdateEvent;
import me.chachoox.lithium.impl.event.events.world.WorldClientEvent;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.render.displaytweaks.DisplayTweaks;
import net.minecraft.client.gui.inventory.GuiScreenHorseInventory;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.Level;

public class DummyManager
extends SubscriberImpl
implements Minecraftable {
    private boolean isValidScreen;
    private boolean sentTut;

    public DummyManager() {
        this.listeners.add(new Listener<DisconnectEvent>(DisconnectEvent.class, Integer.MIN_VALUE){

            @Override
            public void call(DisconnectEvent event) {
                if (Minecraftable.mc.player != null && Minecraftable.mc.getCurrentServerData() != null) {
                    BlockPos pos = Minecraftable.mc.player.getPosition();
                    Logger.getLogger().log(Level.INFO, "SelfLogout: (Server: " + Minecraftable.mc.getCurrentServerData().serverIP + ") (XYZ: " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ")");
                }
            }
        });
        this.listeners.add(new Listener<UpdateEvent>(UpdateEvent.class, Integer.MIN_VALUE){

            @Override
            public void call(UpdateEvent event) {
                DummyManager.this.isValidScreen = Minecraftable.mc.currentScreen instanceof GuiScreenHorseInventory;
            }
        });
        this.listeners.add(new Listener<PacketEvent.Send<CPacketPlayerTryUseItemOnBlock>>(PacketEvent.Send.class, CPacketPlayerTryUseItemOnBlock.class){

            @Override
            public void call(PacketEvent.Send<CPacketPlayerTryUseItemOnBlock> event) {
                CPacketPlayerTryUseItemOnBlock packet = (CPacketPlayerTryUseItemOnBlock)event.getPacket();
                if (packet.getPos().getY() >= 255 && packet.getDirection() == EnumFacing.UP) {
                    ((ICPacketPlayerTryUseItemOnBlock)packet).setFacing(EnumFacing.DOWN);
                }
            }
        });
        this.listeners.add(new Listener<WorldClientEvent.Load>(WorldClientEvent.Load.class){

            @Override
            public void call(WorldClientEvent.Load event) {
                if (Lithium.firstTimeLoaded && !DummyManager.this.sentTut) {
                    Logger.getLogger().log("Prefix is [,] string properties & color properties have to be handled with commands.");
                    DummyManager.this.sentTut = true;
                    Managers.MODULE.get(DisplayTweaks.class).setEnabled(true);
                }
            }
        });
    }

    public boolean isValid() {
        return this.isValidScreen;
    }
}

