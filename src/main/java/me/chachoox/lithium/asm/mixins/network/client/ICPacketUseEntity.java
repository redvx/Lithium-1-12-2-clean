/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.network.play.client.CPacketUseEntity$Action
 *  net.minecraft.util.EnumHand
 */
package me.chachoox.lithium.asm.mixins.network.client;

import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={CPacketUseEntity.class})
public interface ICPacketUseEntity {
    @Accessor(value="entityId")
    public void setEntityId(int var1);

    @Accessor(value="entityId")
    public int getEntityId();

    @Accessor(value="action")
    public void setAction(CPacketUseEntity.Action var1);

    @Accessor(value="hand")
    public void setHand(EnumHand var1);

    @Accessor(value="action")
    public CPacketUseEntity.Action getAction();
}

