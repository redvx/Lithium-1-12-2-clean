/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Util
 *  net.minecraft.util.Util$EnumOS
 *  org.apache.commons.io.IOUtils
 *  org.lwjgl.opengl.Display
 */
package me.chachoox.lithium.impl.modules.render.displaytweaks.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;
import me.chachoox.lithium.api.util.logger.Logger;
import net.minecraft.util.Util;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Level;
import org.lwjgl.opengl.Display;

public class IconUtil {
    private static final String RESOURCES_ROOT = "/assets/lithium/icons/";

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void setWindowIcon(String[] icons) {
        Util.EnumOS osType = Util.getOSType();
        InputStream inputstream = null;
        InputStream inputstream1 = null;
        if (osType != Util.EnumOS.OSX) {
            block5: {
                try {
                    inputstream = IconUtil.class.getResourceAsStream(RESOURCES_ROOT + icons[0]);
                    inputstream1 = IconUtil.class.getResourceAsStream(RESOURCES_ROOT + icons[1]);
                    if (inputstream == null || inputstream1 == null) break block5;
                    Display.setIcon((ByteBuffer[])new ByteBuffer[]{IconUtil.readImageToBuffer(inputstream), IconUtil.readImageToBuffer(inputstream1)});
                    Logger.getLogger().log(Level.INFO, "Icons set successfully");
                }
                catch (IOException e) {
                    try {
                        Logger.getLogger().log(Level.INFO, "Couldn't set icon: " + e.getMessage());
                    }
                    catch (Throwable throwable) {
                        IOUtils.closeQuietly(inputstream);
                        IOUtils.closeQuietly(inputstream1);
                        throw throwable;
                    }
                    IOUtils.closeQuietly((InputStream)inputstream);
                    IOUtils.closeQuietly(inputstream1);
                }
            }
            IOUtils.closeQuietly((InputStream)inputstream);
            IOUtils.closeQuietly((InputStream)inputstream1);
        }
    }

    private static ByteBuffer readImageToBuffer(InputStream imageStream) throws IOException {
        BufferedImage bufferedimage = ImageIO.read(imageStream);
        int[] aint = bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), null, 0, bufferedimage.getWidth());
        ByteBuffer bytebuffer = ByteBuffer.allocate(4 * aint.length);
        for (int i : aint) {
            bytebuffer.putInt(i << 8 | i >> 24 & 0xFF);
        }
        bytebuffer.flip();
        return bytebuffer;
    }
}

