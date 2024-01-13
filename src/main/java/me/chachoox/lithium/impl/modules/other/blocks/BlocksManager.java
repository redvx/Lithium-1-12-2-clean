/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.StringUtils
 */
package me.chachoox.lithium.impl.modules.other.blocks;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.module.PersistentModule;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.impl.modules.other.blocks.util.Debugger;
import me.chachoox.lithium.impl.modules.player.autofeetplace.AutoFeetPlace;
import net.minecraft.util.StringUtils;
import org.apache.logging.log4j.Level;

public class BlocksManager
extends PersistentModule {
    private final Property<Boolean> enderChests = new Property<Boolean>(false, new String[]{"EnderChests", "chests", "echests"}, "Places enderchests with other modules that arent surround.");
    private final Property<Boolean> disableOnDeath = new Property<Boolean>(false, new String[]{"DisableOnDeath", "DeathDisable", "AutoDeathDisable"}, "Disables block place modules when you die.");
    private final Property<Boolean> disableOnDisconnect = new Property<Boolean>(false, new String[]{"DisableOnDisconnect", "DisconnectDisable", "LeaveDisable", "DisableOnLeave"}, "Disables block place modules when you disconnect.");
    private final Property<Boolean> debugSurround = new Property<Boolean>(false, new String[]{"DebugSurround"});
    private final Property<Boolean> debugSelfFill = new Property<Boolean>(false, new String[]{"DebugSelfFill"});
    private final Property<Boolean> debugSelfBlocker = new Property<Boolean>(false, new String[]{"DebugSelfBlocker"});
    private final Property<Boolean> debugScaffold = new Property<Boolean>(false, new String[]{"DebugScaffold"});
    private final Property<Boolean> debugTrap = new Property<Boolean>(false, new String[]{"DebugTrap"});
    private final Property<Boolean> debugHoleFill = new Property<Boolean>(false, new String[]{"DebugHoleFill"});
    private final Property<Boolean> debugWeb = new Property<Boolean>(false, new String[]{"DebugWeb"});
    private final EnumProperty<Debugger> debugger = new EnumProperty<Debugger>(Debugger.NONE, new String[]{"Debug"}, "Debugs block placements in chat or logs.");
    private static BlocksManager BLOCK_MANAGER;

    public BlocksManager() {
        super("Blocks", new String[]{"Blocks", "block", "blockmanage", "blocksmanager"}, "Manages obsidian & ender chest placing modules.", Category.OTHER);
        this.offerProperties(this.enderChests, this.disableOnDeath, this.disableOnDisconnect, this.debugSurround, this.debugSelfFill, this.debugSelfBlocker, this.debugScaffold, this.debugTrap, this.debugHoleFill, this.debugWeb, this.debugger);
        for (Property<?> property : this.getProperties()) {
            if (!StringUtils.isNullOrEmpty((String)property.getLabel())) continue;
            property.setDescription("Renders " + property.getLabel().replace("Debug", "") + ".");
        }
        BLOCK_MANAGER = this;
    }

    public static BlocksManager get() {
        return BLOCK_MANAGER == null ? (BLOCK_MANAGER = new BlocksManager()) : BLOCK_MANAGER;
    }

    public void log(String message) {
        switch ((Debugger)((Object)this.debugger.getValue())) {
            case LOG: {
                Logger.getLogger().log(Level.INFO, "<Blocks> " + message);
                break;
            }
            case CHAT: {
                Logger.getLogger().log("<Blocks> " + message, false);
            }
        }
    }

    public Boolean placeEnderChests(Module module) {
        return this.enderChests.getValue() != false || module instanceof AutoFeetPlace;
    }

    public Boolean disableOnDeath() {
        return this.disableOnDeath.getValue();
    }

    public Boolean disableOnDisconnect() {
        return this.disableOnDisconnect.getValue();
    }

    public Boolean debugTrap() {
        return this.debugTrap.getValue();
    }

    public Boolean debugHoleFill() {
        return this.debugHoleFill.getValue();
    }

    public Boolean debugScaffold() {
        return this.debugScaffold.getValue();
    }

    public Boolean debugSurround() {
        return this.debugSurround.getValue();
    }

    public Boolean debugSelfFill() {
        return this.debugSelfFill.getValue();
    }

    public Boolean debugSelfBlocker() {
        return this.debugSelfBlocker.getValue();
    }

    public Boolean debugWeb() {
        return this.debugWeb.getValue();
    }
}

