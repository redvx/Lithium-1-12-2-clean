/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.misc.chattimestamps;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.text.ColorEnum;
import me.chachoox.lithium.impl.modules.misc.chattimestamps.ListenerChat;
import me.chachoox.lithium.impl.modules.misc.chattimestamps.TimeStampsBracket;

public class ChatTimeStamps
extends Module {
    private final EnumProperty<ColorEnum> bracketColor = new EnumProperty<ColorEnum>(ColorEnum.DARKPURPLE, new String[]{"BracketColor", "brackc", "colorbracket", "bc"}, "The color of the brackets.");
    private final EnumProperty<ColorEnum> timeColor = new EnumProperty<ColorEnum>(ColorEnum.LIGHTPURPLE, new String[]{"TimeColor", "timec", "Tc"}, "The color of the time.");
    private final EnumProperty<TimeStampsBracket> brackets = new EnumProperty<TimeStampsBracket>(TimeStampsBracket.BRACKET, new String[]{"Bracket", "b"}, "Caret: - < | > / Bracket: - [ | ].");
    protected final Property<Boolean> underline = new Property<Boolean>(false, new String[]{"Underline", "line", "Add a underline to the text."});

    public ChatTimeStamps() {
        super("ChatTimeStamps", new String[]{"ChatTimeStamps", "chattime", "timestamps"}, "Chat time stamps.", Category.MISC);
        this.offerProperties(this.bracketColor, this.timeColor, this.brackets, this.underline);
        this.offerListeners(new ListenerChat(this));
    }

    public String getTimeStamps(String time) {
        if (this.underline.getValue().booleanValue()) {
            time = "\u00a7n" + time;
        }
        switch ((TimeStampsBracket)((Object)this.brackets.getValue())) {
            case NONE: {
                return String.format("%s%s%s ", ((ColorEnum)((Object)this.timeColor.getValue())).getColor(), time, "\u00a7r");
            }
            case CARET: {
                return String.format("%s<%s%s%s>%s ", ((ColorEnum)((Object)this.bracketColor.getValue())).getColor(), ((ColorEnum)((Object)this.timeColor.getValue())).getColor(), time, ((ColorEnum)((Object)this.bracketColor.getValue())).getColor(), "\u00a7r");
            }
            case BRACKET: {
                return String.format("%s[%s%s%s]%s ", ((ColorEnum)((Object)this.bracketColor.getValue())).getColor(), ((ColorEnum)((Object)this.timeColor.getValue())).getColor(), time, ((ColorEnum)((Object)this.bracketColor.getValue())).getColor(), "\u00a7r");
            }
        }
        return "i count monye right";
    }
}

