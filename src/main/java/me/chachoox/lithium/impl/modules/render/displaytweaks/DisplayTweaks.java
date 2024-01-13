/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.Display
 */
package me.chachoox.lithium.impl.modules.render.displaytweaks;

import java.awt.Color;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.ColorProperty;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.property.StringProperty;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.api.util.text.ColorEnum;
import me.chachoox.lithium.impl.modules.render.displaytweaks.ListenerAspect;
import me.chachoox.lithium.impl.modules.render.displaytweaks.ListenerRender;
import me.chachoox.lithium.impl.modules.render.displaytweaks.util.GLSLSandboxShader;
import me.chachoox.lithium.impl.modules.render.displaytweaks.util.GLSLShaders;
import me.chachoox.lithium.impl.modules.render.displaytweaks.util.IconUtil;
import me.chachoox.lithium.impl.modules.render.displaytweaks.util.Icons;
import me.chachoox.lithium.impl.modules.render.displaytweaks.util.Images;
import me.chachoox.lithium.impl.modules.render.displaytweaks.util.TitleScreens;
import org.apache.logging.log4j.Level;
import org.lwjgl.opengl.Display;

public class DisplayTweaks
extends Module {
    protected final EnumProperty<TitleScreens> titleScreen = new EnumProperty<TitleScreens>(TitleScreens.SHADER, new String[]{"TitleScreen", "customtitlescreen"}, "Tweaks minecraft's title screen.");
    protected final EnumProperty<Images> image = new EnumProperty<Images>(Images.PIG, new String[]{"Images", "image"}, "The image that will be used.");
    protected final EnumProperty<GLSLShaders> shader = new EnumProperty<GLSLShaders>(GLSLShaders.WAIFU, new String[]{"Shader", "shad", "screen"}, "The shader that will be used.");
    protected final ColorProperty titleColor = new ColorProperty(new Color(-11184811), false, new String[]{"TitleColor", "titlescreencolor"});
    protected final ColorProperty titleColorTwo = new ColorProperty(new Color(986895), false, new String[]{"TitleColorTwo", "titlecolortwo"});
    protected final NumberProperty<Integer> fps = new NumberProperty<Integer>(60, 5, 60, new String[]{"Fps", "maxfps", "frames", "f"}, "Max amount of frames in the title screen.");
    protected final Property<Boolean> screenshotFix = new Property<Boolean>(false, new String[]{"ScreenShotFix", "ssfix", "pollosfix"}, "Removes lag when screenshotting.");
    protected final Property<Boolean> uploadToImgur = new Property<Boolean>(false, new String[]{"UploadToImgur", "imgur"}, "Uploads your screenshot to imgur.");
    protected final Property<Boolean> aspectRatio = new Property<Boolean>(false, new String[]{"AspectRatio", "aspect"}, "Changes your aspect ratio.");
    protected final NumberProperty<Integer> aspectRatioWidth;
    protected final NumberProperty<Integer> aspectRatioHeight;
    protected final Property<Boolean> hotbarKeys;
    protected final ColorProperty hotbarkeysColor;
    protected final NumberProperty<Integer> currentAccountPosY;
    protected final EnumProperty<ColorEnum> accountTextColor;
    protected final StringProperty window;
    protected final EnumProperty<Icons> icon;
    public GLSLSandboxShader GLSL_SHADER;

    public DisplayTweaks() {
        super("DisplayTweaks", new String[]{"DisplayTweaks", "TitleScreen", "CustomTitleScreen", "BetterScreenshots", "AspectRatioChanger"}, "Display tweaks that would look retarded as other modules.", Category.RENDER);
        this.aspectRatioWidth = new NumberProperty<Integer>(DisplayTweaks.mc.displayWidth, 0, DisplayTweaks.mc.displayWidth, new String[]{"Width", "w"}, "Width of the screen aspect");
        this.aspectRatioHeight = new NumberProperty<Integer>(DisplayTweaks.mc.displayHeight, 0, DisplayTweaks.mc.displayHeight, new String[]{"Height", "h"}, "Height of the screen aspect");
        this.hotbarKeys = new Property<Boolean>(false, new String[]{"HotbarKeys", "keys"}, "Draws keybinds in hotbar");
        this.hotbarkeysColor = new ColorProperty(Color.WHITE, false, new String[]{"KeyColor", "hotbarkeycolor"});
        this.currentAccountPosY = new NumberProperty<Integer>(42, 0, 200, new String[]{"AccountY", "accountposy"}, "Current height of the account message in the title screen.");
        this.accountTextColor = new EnumProperty<ColorEnum>(ColorEnum.RED, new String[]{"AccountText", "CurrentAccountText", "acctext"}, "Color of the text displayed on the title screen.");
        this.window = new StringProperty("Minecraft 1.12.2", new String[]{"Window", "screentitle", "title", "titlename"});
        this.icon = new EnumProperty<Icons>(Icons.CLASSIC, new String[]{"Icon", "icons", "ico"}, "The window icon.");
        this.GLSL_SHADER = null;
        this.offerProperties(this.titleScreen, this.image, this.shader, this.fps, this.titleColor, this.titleColorTwo, this.screenshotFix, this.uploadToImgur, this.aspectRatio, this.aspectRatioHeight, this.aspectRatioWidth, this.hotbarKeys, this.hotbarkeysColor, this.currentAccountPosY, this.accountTextColor, this.window, this.icon);
        this.offerListeners(new ListenerAspect(this), new ListenerRender(this));
        this.shader.addObserver(event -> this.setShader(((GLSLShaders)((Object)((Object)event.getValue()))).get()));
        this.window.addObserver(event -> Display.setTitle((String)((String)event.getValue())));
        this.icon.addObserver(event -> IconUtil.setWindowIcon(((Icons)((Object)((Object)event.getValue()))).get()));
    }

    public Color getColor() {
        return this.hotbarkeysColor.getColor();
    }

    public Color getTitleColorOne() {
        return this.titleColor.getColor();
    }

    public Color getTitleColorTwo() {
        return this.titleColorTwo.getColor();
    }

    public boolean isCustomTitle() {
        return this.isShader() || this.isImage() || this.isColor();
    }

    public boolean isShader() {
        return this.isEnabled() && this.titleScreen.getValue() == TitleScreens.SHADER;
    }

    public boolean isImage() {
        return this.isEnabled() && this.titleScreen.getValue() == TitleScreens.IMAGE;
    }

    public boolean isColor() {
        return this.isEnabled() && this.titleScreen.getValue() == TitleScreens.COLOR;
    }

    public String getImage() {
        return ((Images)((Object)this.image.getValue())).get();
    }

    public int getAccountMessagePosY() {
        return (Integer)this.currentAccountPosY.getValue();
    }

    public String getAccountTextColor() {
        return ((ColorEnum)((Object)this.accountTextColor.getValue())).getColor();
    }

    public boolean shouldUpload() {
        return this.isEnabled() && this.uploadToImgur.getValue() != false;
    }

    public boolean betterScreenShots() {
        return this.isEnabled() && this.screenshotFix.getValue() != false;
    }

    public void loadFromConfig(Module module) {
        if (module instanceof DisplayTweaks) {
            Display.setTitle((String)((String)((DisplayTweaks)module).window.getValue()));
            this.setShader(((GLSLShaders)((Object)((DisplayTweaks)module).shader.getValue())).get());
            IconUtil.setWindowIcon(((Icons)((Object)((DisplayTweaks)module).icon.getValue())).get());
        }
    }

    public void setShader(String shader) {
        try {
            Logger.getLogger().log(Level.INFO, "Trying to setup shader screen.");
            this.GLSL_SHADER = new GLSLSandboxShader(shader);
        }
        catch (Exception e) {
            Logger.getLogger().log("\u00a7cFatal errorjamin while trying to load shader (" + shader + ")");
            Logger.getLogger().log(Level.INFO, "Failed to setup shader screen.");
            this.GLSL_SHADER = null;
        }
    }

    public int getFps() {
        return (Integer)this.fps.getValue();
    }
}

