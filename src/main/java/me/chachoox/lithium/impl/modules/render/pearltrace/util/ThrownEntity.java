/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.Vec3d
 */
package me.chachoox.lithium.impl.modules.render.pearltrace.util;

import java.util.ArrayList;
import net.minecraft.util.math.Vec3d;

public class ThrownEntity {
    private long time;
    private final ArrayList<Vec3d> vertices;

    public ThrownEntity(long time, ArrayList<Vec3d> vertices) {
        this.time = time;
        this.vertices = vertices;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public ArrayList<Vec3d> getVertices() {
        return this.vertices;
    }
}

