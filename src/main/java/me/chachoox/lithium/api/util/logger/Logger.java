/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.Style
 *  net.minecraft.util.text.TextComponentString
 *  net.minecraft.util.text.TextFormatting
 */
package me.chachoox.lithium.api.util.logger;

import me.chachoox.lithium.api.interfaces.Loggable;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.other.hud.Hud;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

public class Logger
implements Loggable,
Minecraftable {
    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger("Lithium");
    public static final int MESSAGE_ID = -2147442069;
    public static int CUSTOM_ID = -1;
    public static int PERMANENT_ID = -2147442070;
    private static Logger INSTANCE = new Logger();

    @Override
    public void log(String text) {
        this.log(text, true);
    }

    @Override
    public void log(String text, int id) {
        if (Logger.mc.ingameGUI != null) {
            ITextComponent component = new TextComponentString(text).setStyle(new Style().setColor(TextFormatting.LIGHT_PURPLE));
            String WATERMARK = Managers.MODULE.get(Hud.class).getWatermark();
            ITextComponent component1 = new TextComponentString(WATERMARK).appendSibling(component);
            CUSTOM_ID = id;
            Logger.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(component1, CUSTOM_ID);
        }
    }

    @Override
    public void log(String text, boolean delete) {
        ITextComponent component = new TextComponentString(text).setStyle(new Style().setColor(TextFormatting.LIGHT_PURPLE));
        this.log(component, delete);
    }

    @Override
    public void log(ITextComponent component, boolean delete) {
        if (Logger.mc.ingameGUI != null) {
            String WATERMARK = Managers.MODULE.get(Hud.class).getWatermark();
            ITextComponent markedComponent = new TextComponentString(WATERMARK).appendSibling(component);
            if (delete) {
                Logger.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(markedComponent, -2147442069);
            } else {
                Logger.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(markedComponent, PERMANENT_ID);
            }
        }
    }

    @Override
    public void log(Level level, String text) {
        LOGGER.log(level, text);
    }

    @Override
    public void logNoMark(String text) {
        this.logNoMark(text, true);
    }

    @Override
    public void logNoMark(String text, int id) {
        if (Logger.mc.ingameGUI != null) {
            ITextComponent component = new TextComponentString(text).setStyle(new Style().setColor(TextFormatting.LIGHT_PURPLE));
            CUSTOM_ID = id;
            Logger.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(component, CUSTOM_ID);
        }
    }

    @Override
    public void logNoMark(String text, boolean delete) {
        ITextComponent component = new TextComponentString(text).setStyle(new Style().setColor(TextFormatting.LIGHT_PURPLE));
        this.logNoMark(component, delete);
    }

    @Override
    public void logNoMark(ITextComponent component, boolean delete) {
        if (Logger.mc.ingameGUI != null) {
            if (delete) {
                Logger.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(component, -2147442069);
            } else {
                Logger.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(component, PERMANENT_ID);
            }
        }
    }

    public static Logger getLogger() {
        return INSTANCE == null ? (INSTANCE = new Logger()) : INSTANCE;
    }
}

