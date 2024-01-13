/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.network.NetworkPlayerInfo
 *  net.minecraft.scoreboard.ScorePlayerTeam
 *  net.minecraft.scoreboard.Team
 */
package me.chachoox.lithium.impl.modules.misc.extratab;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.text.ColorEnum;
import me.chachoox.lithium.impl.managers.Managers;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;

public class ExtraTab
extends Module {
    public final NumberProperty<Integer> tabSize = new NumberProperty<Integer>(80, 1, 1000, new String[]{"TabSize", "size", "tablength"}, "Overrides the minecraft tab size and replaces it for this.");
    public final EnumProperty<ColorEnum> friendColor = new EnumProperty<ColorEnum>(ColorEnum.AQUA, new String[]{"FriendColor", "frdcolor", "friend"}, "If we have someone added we will change their name colour to this on tab.");
    public final Property<Boolean> ping = new Property<Boolean>(false, new String[]{"Ping", "pingington", "pin"}, "Displays ping in text instead of bars.");
    public final Property<Boolean> bars = new Property<Boolean>(true, new String[]{"Bars", "vanillabars", "bar"}, "Displays the vanilla ping bars.");

    public ExtraTab() {
        super("ExtraTab", new String[]{"ExtraTab", "tabtweaks", "tab"}, "Tweaks how tab works.", Category.MISC);
        this.offerProperties(this.tabSize, this.friendColor, this.ping, this.bars);
    }

    public String getName(NetworkPlayerInfo info) {
        String name;
        String string = name = info.getDisplayName() != null ? info.getDisplayName().getFormattedText() : ScorePlayerTeam.formatPlayerName((Team)info.getPlayerTeam(), (String)info.getGameProfile().getName());
        if (Managers.FRIEND.isFriend(name)) {
            return ((ColorEnum)((Object)this.friendColor.getValue())).getColor() + name;
        }
        return name;
    }
}

