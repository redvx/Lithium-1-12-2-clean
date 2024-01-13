/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  org.lwjgl.opengl.GL11
 */
package me.chachoox.lithium.api.util.render;

import java.awt.Rectangle;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class Render2DUtil
implements Minecraftable {
    private static final Tessellator tessellator = Tessellator.getInstance();

    public static void drawNameTagRect(float left, float top, float right, float bottom, int color, int border, float width) {
        Render2DUtil.quickDrawRect(left, top, right, bottom, color, false);
        GL11.glLineWidth((float)width);
        Render2DUtil.quickDrawRect(left, top, right, bottom, border, true);
    }

    public static void quickDrawRect(float x, float y, float x2, float y2, int color, boolean line) {
        float a = (float)(color >> 24 & 0xFF) / 255.0f;
        float r = (float)(color >> 16 & 0xFF) / 255.0f;
        float g = (float)(color >> 8 & 0xFF) / 255.0f;
        float b = (float)(color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        bufferbuilder.begin(line ? 2 : 7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double)x, (double)y2, 0.0).color(r, g, b, a).endVertex();
        bufferbuilder.pos((double)x2, (double)y2, 0.0).color(r, g, b, a).endVertex();
        bufferbuilder.pos((double)x2, (double)y, 0.0).color(r, g, b, a).endVertex();
        bufferbuilder.pos((double)x, (double)y, 0.0).color(r, g, b, a).endVertex();
        tessellator.draw();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawRect(Rectangle rectangle, int color) {
        Render2DUtil.drawRect(rectangle.x, rectangle.y, rectangle.x + rectangle.width, rectangle.y + rectangle.height, color);
    }

    public static void drawRect(float x, float y, float x1, float y1, int color) {
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        builder.pos((double)x, (double)y1, 0.0).color(red, green, blue, alpha).endVertex();
        builder.pos((double)x1, (double)y1, 0.0).color(red, green, blue, alpha).endVertex();
        builder.pos((double)x1, (double)y, 0.0).color(red, green, blue, alpha).endVertex();
        builder.pos((double)x, (double)y, 0.0).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawRect(float x, float y, float x1, float y1) {
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(7, DefaultVertexFormats.POSITION);
        builder.pos((double)x, (double)y1, 0.0).endVertex();
        builder.pos((double)x1, (double)y1, 0.0).endVertex();
        builder.pos((double)x1, (double)y, 0.0).endVertex();
        builder.pos((double)x, (double)y, 0.0).endVertex();
        tessellator.draw();
    }

    public static void drawOutline(float x, float y, float width, float height, float lineWidth, int color) {
        Render2DUtil.drawRect(x + lineWidth, y, x - lineWidth, y + lineWidth, color);
        Render2DUtil.drawRect(x + lineWidth, y, width - lineWidth, y + lineWidth, color);
        Render2DUtil.drawRect(x, y, x + lineWidth, height, color);
        Render2DUtil.drawRect(width - lineWidth, y, width, height, color);
        Render2DUtil.drawRect(x + lineWidth, height - lineWidth, width - lineWidth, height, color);
    }

    public static void drawBorderedRect(float x, float y, float x1, float y1, int insideC, int borderC) {
        Render2DUtil.enableGL2D();
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        Render2DUtil.drawVLine(x *= 2.0f, y *= 2.0f, (y1 *= 2.0f) - 1.0f, borderC);
        Render2DUtil.drawVLine((x1 *= 2.0f) - 1.0f, y, y1, borderC);
        Render2DUtil.drawHLine(x, x1 - 1.0f, y, borderC);
        Render2DUtil.drawHLine(x, x1 - 2.0f, y1 - 1.0f, borderC);
        Render2DUtil.drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
        Render2DUtil.disableGL2D();
    }

    public static void disableGL2D(boolean ignored) {
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)2848);
        GL11.glHint((int)3154, (int)4352);
        GL11.glHint((int)3155, (int)4352);
    }

    public static void enableGL2D(boolean ignored) {
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDepthMask((boolean)true);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glHint((int)3155, (int)4354);
    }

    public static void drawRect(float x, float y, float x1, float y1, int color, int ignored) {
        Render2DUtil.enableGL2D(false);
        Render2DUtil.glColor(color);
        GL11.glBegin((int)7);
        GL11.glVertex2f((float)x, (float)y1);
        GL11.glVertex2f((float)x1, (float)y1);
        GL11.glVertex2f((float)x1, (float)y);
        GL11.glVertex2f((float)x, (float)y);
        GL11.glEnd();
        Render2DUtil.disableGL2D(false);
    }

    public static void enableGL2D() {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
    }

    public static void disableGL2D() {
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawGradientRect(float left, float top, float right, float bottom, boolean sideways, int topColor, int bottomColor) {
        float alpha = (float)(topColor >> 24 & 0xFF) / 255.0f;
        float red = (float)(topColor >> 16 & 0xFF) / 255.0f;
        float green = (float)(topColor >> 8 & 0xFF) / 255.0f;
        float blue = (float)(topColor & 0xFF) / 255.0f;
        float alpha2 = (float)(bottomColor >> 24 & 0xFF) / 255.0f;
        float red2 = (float)(bottomColor >> 16 & 0xFF) / 255.0f;
        float green2 = (float)(bottomColor >> 8 & 0xFF) / 255.0f;
        float blue2 = (float)(bottomColor & 0xFF) / 255.0f;
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel((int)7425);
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        if (sideways) {
            builder.pos((double)left, (double)top, 0.0).color(red, green, blue, alpha).endVertex();
            builder.pos((double)left, (double)bottom, 0.0).color(red, green, blue, alpha).endVertex();
            builder.pos((double)right, (double)bottom, 0.0).color(red2, green2, blue2, alpha2).endVertex();
            builder.pos((double)right, (double)top, 0.0).color(red2, green2, blue2, alpha2).endVertex();
        } else {
            builder.pos((double)right, (double)top, 0.0).color(red, green, blue, alpha).endVertex();
            builder.pos((double)left, (double)top, 0.0).color(red, green, blue, alpha).endVertex();
            builder.pos((double)left, (double)bottom, 0.0).color(red2, green2, blue2, alpha2).endVertex();
            builder.pos((double)right, (double)bottom, 0.0).color(red2, green2, blue2, alpha2).endVertex();
        }
        tessellator.draw();
        GlStateManager.shadeModel((int)7424);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawHLine(float x, float y, float x1, int y1) {
        if (y < x) {
            float var5 = x;
            x = y;
            y = var5;
        }
        Render2DUtil.drawRect(x, x1, y + 1.0f, x1 + 1.0f, y1);
    }

    public static void drawVLine(float x, float y, float x1, int y1) {
        if (x1 < y) {
            float var5 = y;
            y = x1;
            x1 = var5;
        }
        Render2DUtil.drawRect(x, y + 1.0f, x + 1.0f, x1, y1);
    }

    public static void glColor(int hex) {
        GL11.glColor4f((float)((float)(hex >> 24 & 0xFF) / 255.0f), (float)((float)(hex >> 8 & 0xFF) / 255.0f), (float)((float)(hex & 0xFF) / 255.0f), (float)((float)(hex >> 24 & 0xFF) / 255.0f));
    }
}

