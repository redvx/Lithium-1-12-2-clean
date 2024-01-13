/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.shader.Framebuffer
 *  net.minecraft.util.ScreenShotHelper
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.TextComponentTranslation
 *  net.minecraft.util.text.event.ClickEvent
 *  org.lwjgl.BufferUtils
 */
package me.chachoox.lithium.asm.mixins.util;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.IntBuffer;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.Nullable;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.api.util.text.component.SuppliedComponent;
import me.chachoox.lithium.api.util.thread.ImgurScreenshotThread;
import me.chachoox.lithium.api.util.thread.ScreenShotRunnable;
import me.chachoox.lithium.api.util.thread.events.RunnableClickEvent;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.render.displaytweaks.DisplayTweaks;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ScreenShotHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.event.ClickEvent;
import org.apache.logging.log4j.Level;
import org.lwjgl.BufferUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={ScreenShotHelper.class})
public abstract class MixinScreenShotHelper {
    @Shadow
    private static IntBuffer pixelBuffer;
    @Shadow
    private static int[] pixelValues;

    @Redirect(method={"saveScreenshot(Ljava/io/File;IILnet/minecraft/client/shader/Framebuffer;)Lnet/minecraft/util/text/ITextComponent;"}, at=@At(value="INVOKE", target="Lnet/minecraft/util/ScreenShotHelper;saveScreenshot(Ljava/io/File;Ljava/lang/String;IILnet/minecraft/client/shader/Framebuffer;)Lnet/minecraft/util/text/ITextComponent;"))
    private static ITextComponent saveScreenshot(File gameDirectory, @Nullable String name, int width, int height, Framebuffer buffer) {
        if (Managers.MODULE.get(DisplayTweaks.class).shouldUpload()) {
            ImgurScreenshotThread eventClientScreenShot = new ImgurScreenshotThread(ScreenShotHelper.createScreenshot((int)width, (int)height, (Framebuffer)buffer));
            eventClientScreenShot.start();
        }
        if (Managers.MODULE.get(DisplayTweaks.class).betterScreenShots()) {
            try {
                return MixinScreenShotHelper.makeScreenShot(gameDirectory, name, width, height, buffer);
            }
            catch (IOException e) {
                e.printStackTrace();
                return new TextComponentTranslation("screenshot.failure", new Object[]{e.getMessage()});
            }
        }
        return ScreenShotHelper.saveScreenshot((File)gameDirectory, null, (int)width, (int)height, (Framebuffer)buffer);
    }

    private static ITextComponent makeScreenShot(File gameDirectory, @Nullable String name, int width, int height, Framebuffer buffer) throws IOException {
        if (OpenGlHelper.isFramebufferEnabled()) {
            width = buffer.framebufferTextureWidth;
            height = buffer.framebufferTextureHeight;
        }
        int i = width * height;
        if (pixelBuffer == null || pixelBuffer.capacity() < i) {
            pixelBuffer = BufferUtils.createIntBuffer((int)i);
            pixelValues = new int[i];
        }
        GlStateManager.glPixelStorei((int)3333, (int)1);
        GlStateManager.glPixelStorei((int)3317, (int)1);
        pixelBuffer.clear();
        if (OpenGlHelper.isFramebufferEnabled()) {
            GlStateManager.bindTexture((int)buffer.framebufferTexture);
            GlStateManager.glGetTexImage((int)3553, (int)0, (int)32993, (int)33639, (IntBuffer)pixelBuffer);
        } else {
            GlStateManager.glReadPixels((int)0, (int)0, (int)width, (int)height, (int)32993, (int)33639, (IntBuffer)pixelBuffer);
        }
        pixelBuffer.get(pixelValues);
        AtomicBoolean finished = new AtomicBoolean();
        AtomicReference<String> supplier = new AtomicReference<String>("Creating Screenshot...");
        AtomicReference<File> file = new AtomicReference<File>();
        Managers.THREAD.submit(new ScreenShotRunnable(supplier, file, finished, width, height, pixelValues, gameDirectory, name));
        SuppliedComponent component = new SuppliedComponent(supplier::get);
        component.getStyle().setClickEvent((ClickEvent)new RunnableClickEvent(() -> {
            File f = (File)file.get();
            if (f != null) {
                URI uri = new File(f.getAbsolutePath()).toURI();
                try {
                    MixinScreenShotHelper.openWebLink(uri);
                }
                catch (Throwable t) {
                    Throwable cause = t.getCause();
                    Logger.getLogger().log(Level.ERROR, "Couldn't open link: " + (cause == null ? "<UNKNOWN>" : cause.getMessage()));
                }
            }
        }));
        component.getStyle().setUnderlined(Boolean.valueOf(true));
        return component;
    }

    private static void openWebLink(URI url) throws Throwable {
        Class<?> clazz = Class.forName("java.awt.Desktop");
        Object object = clazz.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
        clazz.getMethod("browse", URI.class).invoke(object, url);
    }
}

