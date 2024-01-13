/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.render.betterchat;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.text.ColorEnum;
import me.chachoox.lithium.impl.modules.render.betterchat.ListenerChatRecieve;
import me.chachoox.lithium.impl.modules.render.betterchat.ListenerChatSend;
import me.chachoox.lithium.impl.modules.render.betterchat.util.ChatType;

public class BetterChat
extends Module {
    private final NumberProperty<Float> alphaFactor = new NumberProperty<Float>(Float.valueOf(0.5f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.01f), new String[]{"RectAlpha", "chatbackground"}, "Changes the alpha of the chat rectangle, default is the vanilla one.");
    private final EnumProperty<ColorEnum> selfColor = new EnumProperty<ColorEnum>(ColorEnum.NONE, new String[]{"Highlight", "playercolor"}, "Current chat formatting color");
    private final Property<Boolean> soundOnHighlight = new Property<Boolean>(false, new String[]{"SoundOnHighlight", "HighlightSound", "Notification"}, "Plays a sound whenever someone says your name in chat.");
    protected final Property<Boolean> lowercase = new Property<Boolean>(false, new String[]{"Lowercase", "Anticringe", "Anitannoying", "hiedunfunnyassniggas"}, "Makes all chat messages lowercase.");
    private final Property<Boolean> blur = new Property<Boolean>(false, new String[]{"Blur", "cum"}, "Adds cum when you open chat.");
    private final Property<Boolean> infinite = new Property<Boolean>(false, new String[]{"Infinite", "infi"}, "Removes chat limit.");
    protected final EnumProperty<ChatType> chatType = new EnumProperty<ChatType>(ChatType.NONE, new String[]{"ChatType", "fancychat"}, "Changes how your message will look.");
    protected final Property<Boolean> period = new Property<Boolean>(false, new String[]{"Period", "dot"}, "Ends all of your messages with a period, -> (cpvpnn is a gorilla.).");
    protected final Property<Boolean> greenText = new Property<Boolean>(false, new String[]{"Green", "Text"}, "Starts your messages with >, -> (> cpvpnn is a gorilla.).");
    protected final Property<Boolean> face = new Property<Boolean>(false, new String[]{"Face", "cute"}, "Starts your messages with a face and a heart and ends it with a heart, -> (\u3063\u25d4\u25e1\u25d4)\u3063 \u2665 cpvpnn is a gorilla \u2665).");
    protected final Property<Boolean> angry = new Property<Boolean>(false, new String[]{"Angry", "Screaming", "NidzyoWhenFriendlessAssNiggas"}, "MAKES YOUR TEXT ALL UPPERCASE.");
    protected final Property<Boolean> antiKick = new Property<Boolean>(false, new String[]{"AntiKick", "AntiCensor", "NoKick"}, "Puts a random string of characters at the end of your messages -> | f3j0f3.");
    protected final Property<Boolean> whispers = new Property<Boolean>(false, new String[]{"Whispers", "AntiKickWhisper", "NoKickWhisper"}, "If you want to use AntiKick when whispering.");

    public BetterChat() {
        super("BetterChat", new String[]{"BetterChat", "ChatTweaks", "ChatModify", "NoChatRect"}, "Tweaks minecraft's chat.", Category.RENDER);
        this.offerListeners(new ListenerChatSend(this), new ListenerChatRecieve(this));
        this.offerProperties(this.alphaFactor, this.selfColor, this.soundOnHighlight, this.lowercase, this.blur, this.infinite, this.chatType, this.period, this.greenText, this.face, this.angry, this.antiKick, this.whispers);
    }

    protected boolean allowMessage(String message) {
        String[] filters = new String[]{"/", ".", ",", "$", "#", "+", "@", "!", "*", "-"};
        boolean allow = true;
        for (String s : filters) {
            if (this.whispers.getValue().booleanValue() && s.equals("/") || !message.startsWith(s)) continue;
            allow = false;
            break;
        }
        return allow;
    }

    public boolean drawBlur() {
        return this.isEnabled() && this.blur.getValue() != false;
    }

    public boolean noAnnoyingPeople() {
        return this.isEnabled() && this.lowercase.getValue() != false;
    }

    public boolean isInfinite() {
        return this.isEnabled() && this.infinite.getValue() != false;
    }

    public boolean playSoundOnHighlight() {
        return this.isEnabled() && this.soundOnHighlight.getValue() != false;
    }

    public float getRectAlpha() {
        return ((Float)this.alphaFactor.getValue()).floatValue();
    }

    public String getPlayerColor() {
        return ((ColorEnum)((Object)this.selfColor.getValue())).getColor();
    }
}

