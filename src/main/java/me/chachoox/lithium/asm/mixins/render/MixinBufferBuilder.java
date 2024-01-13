/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.vertex.VertexFormat
 *  net.minecraft.client.renderer.vertex.VertexFormatElement
 *  net.minecraft.client.renderer.vertex.VertexFormatElement$EnumUsage
 */
package me.chachoox.lithium.asm.mixins.render;

import java.util.List;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value={BufferBuilder.class})
public class MixinBufferBuilder {
    @Shadow
    private int vertexFormatIndex;
    @Shadow
    private VertexFormatElement vertexFormatElement;
    @Shadow
    private VertexFormat vertexFormat;

    @Overwrite
    private void nextVertexFormatIndex() {
        List elementList = this.vertexFormat.getElements();
        int size = elementList.size();
        do {
            if (++this.vertexFormatIndex >= size) {
                this.vertexFormatIndex -= size;
            }
            this.vertexFormatElement = (VertexFormatElement)elementList.get(this.vertexFormatIndex);
        } while (this.vertexFormatElement.getUsage() == VertexFormatElement.EnumUsage.PADDING);
    }
}

