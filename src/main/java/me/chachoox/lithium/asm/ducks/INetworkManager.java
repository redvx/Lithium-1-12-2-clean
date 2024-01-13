/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 */
package me.chachoox.lithium.asm.ducks;

import net.minecraft.network.Packet;

public interface INetworkManager {
    public Packet<?> sendPacketNoEvent(Packet<?> var1);
}

