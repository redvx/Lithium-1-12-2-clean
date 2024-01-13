/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.misc.visualrange;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.api.util.text.ColorEnum;
import me.chachoox.lithium.api.util.text.TextUtil;
import me.chachoox.lithium.impl.modules.misc.visualrange.ListenerDespawn;
import me.chachoox.lithium.impl.modules.misc.visualrange.ListenerSpawn;
import me.chachoox.lithium.impl.modules.misc.visualrange.mode.VisualRangeMessage;
import me.chachoox.lithium.impl.modules.misc.visualrange.mode.VisualRangeMode;

public class VisualRange
extends Module {
    protected final EnumProperty<VisualRangeMode> mode = new EnumProperty<VisualRangeMode>(VisualRangeMode.PRIVATE, new String[]{"Mode", "messagemode", "type"}, "Private: - Sends client side message / Public: Sends public message in chat.");
    protected final EnumProperty<VisualRangeMessage> message = new EnumProperty<VisualRangeMessage>(VisualRangeMessage.NORMAL, new String[]{"Messages", "msg"}, "The type of message.");
    protected final NumberProperty<Integer> delay = new NumberProperty<Integer>(3, 1, 10, new String[]{"Delay", "del", "d"}, "Delay for sending the public messages.");
    protected final Property<Boolean> onlyGeared = new Property<Boolean>(false, new String[]{"OnlyArmor", "OnlyGeared", "armor", "gear", "geared", "gearplayer"}, "Only sends a message if the player who entered our visual range has a full set of armour.");
    protected final Property<Boolean> left = new Property<Boolean>(false, new String[]{"Leaving", "leavingvisualrange", "left"}, "Sends a message whenever someone leaves the visual range.");
    protected final Property<Boolean> sounds = new Property<Boolean>(false, new String[]{"Sounds", "sound", "noise"}, "Plays a sound whenever someone leaves visual range or enters.");
    protected final Property<Boolean> watermark = new Property<Boolean>(false, new String[]{"Watermark", "wmark"}, "Puts [SexMaster.CC] in front of your message.");
    protected final EnumProperty<ColorEnum> nameCol = new EnumProperty<ColorEnum>(ColorEnum.RED, new String[]{"NameColor", "namecol"}, "Changes the color of the player who entered your visual range, -> (name) has left/entered)");
    protected final EnumProperty<ColorEnum> bridgeCol = new EnumProperty<ColorEnum>(ColorEnum.LIGHTPURPLE, new String[]{"BridgeColor", "bridgecol"}, "Changes the color of the bridge in visual range message, -> name (has left/entered)");
    protected final EnumProperty<ColorEnum> friendCol = new EnumProperty<ColorEnum>(ColorEnum.AQUA, new String[]{"FriendColor", "friendcol"}, "Changes the color of the player who entered your visual range if they're a friend, -> (name) has left/entered)");
    protected final Property<Boolean> ghasts = new Property<Boolean>(false, new String[]{"Ghasts", "ghas", "G"}, "Notifies you whenever a ghast enters your visual range.");
    private final StopWatch timer = new StopWatch();

    public VisualRange() {
        super("VisualRange", new String[]{"VisualRange", "visualrang", "helloworld"}, "Sends a client message when entity leaves/enters your visual range.", Category.MISC);
        this.offerProperties(this.mode, this.message, this.delay, this.left, this.onlyGeared, this.sounds, this.watermark, this.nameCol, this.bridgeCol, this.friendCol, this.ghasts);
        this.offerListeners(new ListenerSpawn(this), new ListenerDespawn(this));
        this.mode.addObserver(event -> {
            if (this.isEnabled()) {
                this.timer.reset();
            }
        });
    }

    protected void sendMessage(String message, int id) {
        switch ((VisualRangeMode)((Object)this.mode.getValue())) {
            case PRIVATE: {
                if (this.watermark.getValue().booleanValue()) {
                    Logger.getLogger().log(message, id);
                    break;
                }
                Logger.getLogger().logNoMark(message, id);
                break;
            }
            case PUBLIC: {
                if (!this.timer.passed((Integer)this.delay.getValue() * 1000)) break;
                VisualRange.mc.player.sendChatMessage(TextUtil.removeColor(message));
                this.timer.reset();
            }
        }
    }

    protected String getNameColor() {
        return ((ColorEnum)((Object)this.nameCol.getValue())).getColor();
    }

    protected String getFriendColor() {
        return ((ColorEnum)((Object)this.friendCol.getValue())).getColor();
    }

    protected String getBridgeColor() {
        return ((ColorEnum)((Object)this.bridgeCol.getValue())).getColor();
    }
}

