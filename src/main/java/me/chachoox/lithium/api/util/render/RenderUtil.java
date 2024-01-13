/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.math.AxisAlignedBB
 *  org.lwjgl.opengl.GL11
 */
package me.chachoox.lithium.api.util.render;

import java.awt.Color;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.api.util.colors.ColorUtil;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

public class RenderUtil
implements Minecraftable {
    public static Entity getEntity() {
        return mc.getRenderViewEntity() == null ? RenderUtil.mc.player : mc.getRenderViewEntity();
    }

    public static void startRender() {
        GL11.glPushAttrib((int)1048575);
        GL11.glPushMatrix();
        GL11.glDisable((int)3008);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glEnable((int)2884);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4353);
        GL11.glDisable((int)2896);
    }

    public static void endRender() {
        GL11.glEnable((int)2896);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3008);
        GL11.glDepthMask((boolean)true);
        GL11.glCullFace((int)1029);
        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    public static void drawBox(AxisAlignedBB bb) {
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)2896);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        RenderUtil.fillBox(bb);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2896);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }

    public static void drawBox(AxisAlignedBB bb, Color color) {
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)2896);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        RenderUtil.color(color);
        RenderUtil.fillBox(bb);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2896);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }

    public static void drawOutline(AxisAlignedBB bb, float lineWidth) {
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)2896);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glLineWidth((float)lineWidth);
        RenderUtil.fillOutline(bb);
        GL11.glLineWidth((float)1.0f);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2896);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }

    public static void drawCross(AxisAlignedBB bb, float lineWidth, Color color) {
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)2896);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glLineWidth((float)lineWidth);
        RenderUtil.color(color);
        RenderUtil.fillCross(bb);
        GL11.glLineWidth((float)1.0f);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2896);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }

    public static void fillCross(AxisAlignedBB bb) {
        if (bb != null) {
            GL11.glBegin((int)1);
            GL11.glVertex3d((double)bb.maxX, (double)bb.maxY, (double)bb.maxZ);
            GL11.glVertex3d((double)bb.maxX, (double)bb.minY, (double)bb.minZ);
            GL11.glVertex3d((double)bb.minX, (double)bb.maxY, (double)bb.minZ);
            GL11.glVertex3d((double)bb.minX, (double)bb.minY, (double)bb.maxZ);
            GL11.glVertex3d((double)bb.minX, (double)bb.minY, (double)bb.minZ);
            GL11.glVertex3d((double)bb.minX, (double)bb.maxY, (double)bb.maxZ);
            GL11.glVertex3d((double)bb.minX, (double)bb.minY, (double)bb.minZ);
            GL11.glVertex3d((double)bb.maxX, (double)bb.maxY, (double)bb.minZ);
            GL11.glVertex3d((double)bb.minX, (double)bb.maxY, (double)bb.minZ);
            GL11.glVertex3d((double)bb.maxX, (double)bb.minY, (double)bb.minZ);
            GL11.glVertex3d((double)bb.maxX, (double)bb.maxY, (double)bb.minZ);
            GL11.glVertex3d((double)bb.maxX, (double)bb.minY, (double)bb.maxZ);
            GL11.glVertex3d((double)bb.maxX, (double)bb.maxY, (double)bb.maxZ);
            GL11.glVertex3d((double)bb.minX, (double)bb.minY, (double)bb.maxZ);
            GL11.glVertex3d((double)bb.maxX, (double)bb.minY, (double)bb.maxZ);
            GL11.glVertex3d((double)bb.minX, (double)bb.maxY, (double)bb.maxZ);
            GL11.glVertex3d((double)bb.minX, (double)bb.minY, (double)bb.minZ);
            GL11.glVertex3d((double)bb.maxX, (double)bb.minY, (double)bb.maxZ);
            GL11.glVertex3d((double)bb.maxX, (double)bb.minY, (double)bb.minZ);
            GL11.glVertex3d((double)bb.minX, (double)bb.minY, (double)bb.maxZ);
            GL11.glVertex3d((double)bb.maxX, (double)bb.maxY, (double)bb.minZ);
            GL11.glVertex3d((double)bb.minX, (double)bb.maxY, (double)bb.maxZ);
            GL11.glVertex3d((double)bb.minX, (double)bb.maxY, (double)bb.minZ);
            GL11.glVertex3d((double)bb.maxX, (double)bb.maxY, (double)bb.maxZ);
            GL11.glEnd();
        }
    }

    public static void drawOutline(AxisAlignedBB bb, float lineWidth, Color color) {
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)2896);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glLineWidth((float)lineWidth);
        RenderUtil.color(color);
        RenderUtil.fillOutline(bb);
        GL11.glLineWidth((float)1.0f);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2896);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }

    public static void fillOutline(AxisAlignedBB bb) {
        if (bb != null) {
            GL11.glBegin((int)1);
            GL11.glVertex3d((double)bb.minX, (double)bb.minY, (double)bb.minZ);
            GL11.glVertex3d((double)bb.maxX, (double)bb.minY, (double)bb.minZ);
            GL11.glVertex3d((double)bb.maxX, (double)bb.minY, (double)bb.minZ);
            GL11.glVertex3d((double)bb.maxX, (double)bb.minY, (double)bb.maxZ);
            GL11.glVertex3d((double)bb.maxX, (double)bb.minY, (double)bb.maxZ);
            GL11.glVertex3d((double)bb.minX, (double)bb.minY, (double)bb.maxZ);
            GL11.glVertex3d((double)bb.minX, (double)bb.minY, (double)bb.maxZ);
            GL11.glVertex3d((double)bb.minX, (double)bb.minY, (double)bb.minZ);
            GL11.glVertex3d((double)bb.minX, (double)bb.minY, (double)bb.minZ);
            GL11.glVertex3d((double)bb.minX, (double)bb.maxY, (double)bb.minZ);
            GL11.glVertex3d((double)bb.maxX, (double)bb.minY, (double)bb.minZ);
            GL11.glVertex3d((double)bb.maxX, (double)bb.maxY, (double)bb.minZ);
            GL11.glVertex3d((double)bb.maxX, (double)bb.minY, (double)bb.maxZ);
            GL11.glVertex3d((double)bb.maxX, (double)bb.maxY, (double)bb.maxZ);
            GL11.glVertex3d((double)bb.minX, (double)bb.minY, (double)bb.maxZ);
            GL11.glVertex3d((double)bb.minX, (double)bb.maxY, (double)bb.maxZ);
            GL11.glVertex3d((double)bb.minX, (double)bb.maxY, (double)bb.minZ);
            GL11.glVertex3d((double)bb.maxX, (double)bb.maxY, (double)bb.minZ);
            GL11.glVertex3d((double)bb.maxX, (double)bb.maxY, (double)bb.minZ);
            GL11.glVertex3d((double)bb.maxX, (double)bb.maxY, (double)bb.maxZ);
            GL11.glVertex3d((double)bb.maxX, (double)bb.maxY, (double)bb.maxZ);
            GL11.glVertex3d((double)bb.minX, (double)bb.maxY, (double)bb.maxZ);
            GL11.glVertex3d((double)bb.minX, (double)bb.maxY, (double)bb.maxZ);
            GL11.glVertex3d((double)bb.minX, (double)bb.maxY, (double)bb.minZ);
            GL11.glEnd();
        }
    }

    public static void fillBox(AxisAlignedBB boundingBox) {
        if (boundingBox != null) {
            GL11.glBegin((int)7);
            GL11.glVertex3d((double)((float)boundingBox.minX), (double)((float)boundingBox.minY), (double)((float)boundingBox.maxZ));
            GL11.glVertex3d((double)((float)boundingBox.maxX), (double)((float)boundingBox.minY), (double)((float)boundingBox.maxZ));
            GL11.glVertex3d((double)((float)boundingBox.maxX), (double)((float)boundingBox.maxY), (double)((float)boundingBox.maxZ));
            GL11.glVertex3d((double)((float)boundingBox.minX), (double)((float)boundingBox.maxY), (double)((float)boundingBox.maxZ));
            GL11.glEnd();
            GL11.glBegin((int)7);
            GL11.glVertex3d((double)((float)boundingBox.maxX), (double)((float)boundingBox.minY), (double)((float)boundingBox.maxZ));
            GL11.glVertex3d((double)((float)boundingBox.minX), (double)((float)boundingBox.minY), (double)((float)boundingBox.maxZ));
            GL11.glVertex3d((double)((float)boundingBox.minX), (double)((float)boundingBox.maxY), (double)((float)boundingBox.maxZ));
            GL11.glVertex3d((double)((float)boundingBox.maxX), (double)((float)boundingBox.maxY), (double)((float)boundingBox.maxZ));
            GL11.glEnd();
            GL11.glBegin((int)7);
            GL11.glVertex3d((double)((float)boundingBox.minX), (double)((float)boundingBox.minY), (double)((float)boundingBox.minZ));
            GL11.glVertex3d((double)((float)boundingBox.minX), (double)((float)boundingBox.minY), (double)((float)boundingBox.maxZ));
            GL11.glVertex3d((double)((float)boundingBox.minX), (double)((float)boundingBox.maxY), (double)((float)boundingBox.maxZ));
            GL11.glVertex3d((double)((float)boundingBox.minX), (double)((float)boundingBox.maxY), (double)((float)boundingBox.minZ));
            GL11.glEnd();
            GL11.glBegin((int)7);
            GL11.glVertex3d((double)((float)boundingBox.minX), (double)((float)boundingBox.minY), (double)((float)boundingBox.maxZ));
            GL11.glVertex3d((double)((float)boundingBox.minX), (double)((float)boundingBox.minY), (double)((float)boundingBox.minZ));
            GL11.glVertex3d((double)((float)boundingBox.minX), (double)((float)boundingBox.maxY), (double)((float)boundingBox.minZ));
            GL11.glVertex3d((double)((float)boundingBox.minX), (double)((float)boundingBox.maxY), (double)((float)boundingBox.maxZ));
            GL11.glEnd();
            GL11.glBegin((int)7);
            GL11.glVertex3d((double)((float)boundingBox.maxX), (double)((float)boundingBox.minY), (double)((float)boundingBox.maxZ));
            GL11.glVertex3d((double)((float)boundingBox.maxX), (double)((float)boundingBox.minY), (double)((float)boundingBox.minZ));
            GL11.glVertex3d((double)((float)boundingBox.maxX), (double)((float)boundingBox.maxY), (double)((float)boundingBox.minZ));
            GL11.glVertex3d((double)((float)boundingBox.maxX), (double)((float)boundingBox.maxY), (double)((float)boundingBox.maxZ));
            GL11.glEnd();
            GL11.glBegin((int)7);
            GL11.glVertex3d((double)((float)boundingBox.maxX), (double)((float)boundingBox.minY), (double)((float)boundingBox.minZ));
            GL11.glVertex3d((double)((float)boundingBox.maxX), (double)((float)boundingBox.minY), (double)((float)boundingBox.maxZ));
            GL11.glVertex3d((double)((float)boundingBox.maxX), (double)((float)boundingBox.maxY), (double)((float)boundingBox.maxZ));
            GL11.glVertex3d((double)((float)boundingBox.maxX), (double)((float)boundingBox.maxY), (double)((float)boundingBox.minZ));
            GL11.glEnd();
            GL11.glBegin((int)7);
            GL11.glVertex3d((double)((float)boundingBox.minX), (double)((float)boundingBox.minY), (double)((float)boundingBox.minZ));
            GL11.glVertex3d((double)((float)boundingBox.maxX), (double)((float)boundingBox.minY), (double)((float)boundingBox.minZ));
            GL11.glVertex3d((double)((float)boundingBox.maxX), (double)((float)boundingBox.maxY), (double)((float)boundingBox.minZ));
            GL11.glVertex3d((double)((float)boundingBox.minX), (double)((float)boundingBox.maxY), (double)((float)boundingBox.minZ));
            GL11.glEnd();
            GL11.glBegin((int)7);
            GL11.glVertex3d((double)((float)boundingBox.maxX), (double)((float)boundingBox.minY), (double)((float)boundingBox.minZ));
            GL11.glVertex3d((double)((float)boundingBox.minX), (double)((float)boundingBox.minY), (double)((float)boundingBox.minZ));
            GL11.glVertex3d((double)((float)boundingBox.minX), (double)((float)boundingBox.maxY), (double)((float)boundingBox.minZ));
            GL11.glVertex3d((double)((float)boundingBox.maxX), (double)((float)boundingBox.maxY), (double)((float)boundingBox.minZ));
            GL11.glEnd();
            GL11.glBegin((int)7);
            GL11.glVertex3d((double)((float)boundingBox.minX), (double)((float)boundingBox.maxY), (double)((float)boundingBox.minZ));
            GL11.glVertex3d((double)((float)boundingBox.maxX), (double)((float)boundingBox.maxY), (double)((float)boundingBox.minZ));
            GL11.glVertex3d((double)((float)boundingBox.maxX), (double)((float)boundingBox.maxY), (double)((float)boundingBox.maxZ));
            GL11.glVertex3d((double)((float)boundingBox.minX), (double)((float)boundingBox.maxY), (double)((float)boundingBox.maxZ));
            GL11.glEnd();
            GL11.glBegin((int)7);
            GL11.glVertex3d((double)((float)boundingBox.maxX), (double)((float)boundingBox.maxY), (double)((float)boundingBox.minZ));
            GL11.glVertex3d((double)((float)boundingBox.minX), (double)((float)boundingBox.maxY), (double)((float)boundingBox.minZ));
            GL11.glVertex3d((double)((float)boundingBox.minX), (double)((float)boundingBox.maxY), (double)((float)boundingBox.maxZ));
            GL11.glVertex3d((double)((float)boundingBox.maxX), (double)((float)boundingBox.maxY), (double)((float)boundingBox.maxZ));
            GL11.glEnd();
            GL11.glBegin((int)7);
            GL11.glVertex3d((double)((float)boundingBox.minX), (double)((float)boundingBox.minY), (double)((float)boundingBox.minZ));
            GL11.glVertex3d((double)((float)boundingBox.maxX), (double)((float)boundingBox.minY), (double)((float)boundingBox.minZ));
            GL11.glVertex3d((double)((float)boundingBox.maxX), (double)((float)boundingBox.minY), (double)((float)boundingBox.maxZ));
            GL11.glVertex3d((double)((float)boundingBox.minX), (double)((float)boundingBox.minY), (double)((float)boundingBox.maxZ));
            GL11.glEnd();
            GL11.glBegin((int)7);
            GL11.glVertex3d((double)((float)boundingBox.maxX), (double)((float)boundingBox.minY), (double)((float)boundingBox.minZ));
            GL11.glVertex3d((double)((float)boundingBox.minX), (double)((float)boundingBox.minY), (double)((float)boundingBox.minZ));
            GL11.glVertex3d((double)((float)boundingBox.minX), (double)((float)boundingBox.minY), (double)((float)boundingBox.maxZ));
            GL11.glVertex3d((double)((float)boundingBox.maxX), (double)((float)boundingBox.minY), (double)((float)boundingBox.maxZ));
            GL11.glEnd();
        }
    }

    public static void color(Color color) {
        GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
    }

    public static void color(int color) {
        float[] color4f = ColorUtil.toArray(color);
        GL11.glColor4f((float)color4f[0], (float)color4f[1], (float)color4f[2], (float)color4f[3]);
    }

    public static void color(float r, float g, float b, float a) {
        GL11.glColor4f((float)r, (float)g, (float)b, (float)a);
    }
}

