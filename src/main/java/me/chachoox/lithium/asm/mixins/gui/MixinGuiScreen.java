/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.GuiConfirmOpenLink
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.GuiYesNoCallback
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.client.renderer.RenderItem
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.inventory.ItemStackHelper
 *  net.minecraft.item.ItemShulkerBox
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.util.NonNullList
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.event.ClickEvent
 *  net.minecraft.util.text.event.ClickEvent$Action
 */
package me.chachoox.lithium.asm.mixins.gui;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.Set;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.api.util.thread.events.IClickEvent;
import me.chachoox.lithium.asm.ducks.IStyle;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.render.inventorypreview.InventoryPreview;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.event.ClickEvent;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={GuiScreen.class})
public abstract class MixinGuiScreen
extends Gui
implements GuiYesNoCallback {
    @Shadow
    @Final
    private static Set<String> PROTOCOLS;
    @Shadow
    public Minecraft mc;
    @Shadow
    private URI clickedLinkURI;
    @Shadow
    RenderItem itemRender;
    @Shadow
    protected FontRenderer fontRenderer;

    @Shadow
    protected abstract void openWebLink(URI var1);

    @Shadow
    protected abstract void setText(String var1, boolean var2);

    @Shadow
    public abstract void sendChatMessage(String var1, boolean var2);

    @Shadow
    public static boolean isShiftKeyDown() {
        throw new IllegalStateException("isShiftKeyDown was not shadowed!");
    }

    @Inject(method={"renderToolTip"}, at={@At(value="HEAD")}, cancellable=true)
    public void renderToolTip(ItemStack stack, int x, int y, CallbackInfo info) {
        NBTTagCompound blockEntityTag;
        NBTTagCompound tagCompound;
        if (Managers.MODULE.get(InventoryPreview.class).isShulker() && stack.getItem() instanceof ItemShulkerBox && (tagCompound = stack.getTagCompound()) != null && tagCompound.hasKey("BlockEntityTag", 10) && (blockEntityTag = tagCompound.getCompoundTag("BlockEntityTag")).hasKey("Items", 9)) {
            info.cancel();
            NonNullList nonnulllist = NonNullList.withSize((int)27, (Object)ItemStack.EMPTY);
            ItemStackHelper.loadAllItems((NBTTagCompound)blockEntityTag, (NonNullList)nonnulllist);
            GlStateManager.enableBlend();
            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            int width = Math.max(144, this.fontRenderer.getStringWidth(stack.getDisplayName()) + 3);
            int x1 = x + 12;
            int y1 = y - 12;
            int height = 57;
            this.itemRender.zLevel = 300.0f;
            this.drawGradientRectP(x1 - 3, y1 - 4, x1 + width + 3, y1 - 3, -267386864, -267386864);
            this.drawGradientRectP(x1 - 3, y1 + height + 3, x1 + width + 3, y1 + height + 4, -267386864, -267386864);
            this.drawGradientRectP(x1 - 3, y1 - 3, x1 + width + 3, y1 + height + 3, -267386864, -267386864);
            this.drawGradientRectP(x1 - 4, y1 - 3, x1 - 3, y1 + height + 3, -267386864, -267386864);
            this.drawGradientRectP(x1 + width + 3, y1 - 3, x1 + width + 4, y1 + height + 3, -267386864, -267386864);
            this.drawGradientRectP(x1 - 3, y1 - 3 + 1, x1 - 3 + 1, y1 + height + 3 - 1, 0x505000FF, 1344798847);
            this.drawGradientRectP(x1 + width + 2, y1 - 3 + 1, x1 + width + 3, y1 + height + 3 - 1, 0x505000FF, 1344798847);
            this.drawGradientRectP(x1 - 3, y1 - 3, x1 + width + 3, y1 - 3 + 1, 0x505000FF, 0x505000FF);
            this.drawGradientRectP(x1 - 3, y1 + height + 2, x1 + width + 3, y1 + height + 3, 1344798847, 1344798847);
            this.fontRenderer.drawString(stack.getDisplayName(), x + 12, y - 12, 0xFFFFFF);
            GlStateManager.enableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.enableTexture2D();
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableGUIStandardItemLighting();
            for (int i = 0; i < nonnulllist.size(); ++i) {
                int iX = x + i % 9 * 16 + 11;
                int iY = y + i / 9 * 16 - 11 + 8;
                ItemStack itemStack = (ItemStack)nonnulllist.get(i);
                this.itemRender.renderItemAndEffectIntoGUI(itemStack, iX, iY);
                this.itemRender.renderItemOverlayIntoGUI(this.fontRenderer, itemStack, iX, iY, null);
            }
            RenderHelper.disableStandardItemLighting();
            this.itemRender.zLevel = 0.0f;
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();
        }
    }

    private void drawGradientRectP(int left, int top, int right, int bottom, int startColor, int endColor) {
        float f = (float)(startColor >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(startColor >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(startColor >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(startColor & 0xFF) / 255.0f;
        float f4 = (float)(endColor >> 24 & 0xFF) / 255.0f;
        float f5 = (float)(endColor >> 16 & 0xFF) / 255.0f;
        float f6 = (float)(endColor >> 8 & 0xFF) / 255.0f;
        float f7 = (float)(endColor & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel((int)7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double)right, (double)top, 300.0).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos((double)left, (double)top, 300.0).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos((double)left, (double)bottom, 300.0).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos((double)right, (double)bottom, 300.0).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel((int)7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    @Inject(method={"handleComponentClick"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/gui/GuiScreen;sendChatMessage(Ljava/lang/String;Z)V", shift=At.Shift.BEFORE)}, cancellable=true)
    public void handleComponentClick(ITextComponent component, CallbackInfoReturnable<Boolean> info) {
        IClickEvent event = (IClickEvent)component.getStyle().getClickEvent();
        if (event != null && event.getRunnable() != null) {
            event.getRunnable().run();
            info.setReturnValue(true);
        }
    }

    protected boolean handleClick(ITextComponent component, int button) {
        if (component == null) {
            return false;
        }
        IStyle style = (IStyle)component.getStyle();
        ClickEvent event = null;
        if (button == 1) {
            event = style.getRightClickEvent();
        } else if (button == 2) {
            event = style.getMiddleClickEvent();
        }
        if (MixinGuiScreen.isShiftKeyDown()) {
            String insertion = null;
            if (button == 1) {
                insertion = style.getRightInsertion();
            } else if (button == 2) {
                insertion = style.getMiddleInsertion();
            }
            if (insertion != null) {
                this.setText(insertion, false);
            }
        } else if (event != null) {
            block26: {
                if (event.getAction() == ClickEvent.Action.OPEN_URL) {
                    if (!this.mc.gameSettings.chatLinks) {
                        return false;
                    }
                    try {
                        URI uri = new URI(event.getValue());
                        String s = uri.getScheme();
                        if (s == null) {
                            throw new URISyntaxException(event.getValue(), "Missing protocol");
                        }
                        if (!PROTOCOLS.contains(s.toLowerCase(Locale.ROOT))) {
                            throw new URISyntaxException(event.getValue(), "Unsupported protocol: " + s.toLowerCase(Locale.ROOT));
                        }
                        if (this.mc.gameSettings.chatLinksPrompt) {
                            this.clickedLinkURI = uri;
                            this.mc.displayGuiScreen((GuiScreen)new GuiConfirmOpenLink((GuiYesNoCallback)this, event.getValue(), 31102009, false));
                            break block26;
                        }
                        this.openWebLink(uri);
                    }
                    catch (URISyntaxException urisyntaxexception) {
                        Logger.getLogger().log(Level.ERROR, "Can't open url for " + event + " : " + urisyntaxexception);
                    }
                } else if (event.getAction() == ClickEvent.Action.OPEN_FILE) {
                    URI uri1 = new File(event.getValue()).toURI();
                    this.openWebLink(uri1);
                } else if (event.getAction() == ClickEvent.Action.SUGGEST_COMMAND) {
                    this.setText(event.getValue(), true);
                } else if (event.getAction() == ClickEvent.Action.RUN_COMMAND) {
                    if (((IClickEvent)event).getRunnable() != null) {
                        ((IClickEvent)event).getRunnable().run();
                        return true;
                    }
                    this.sendChatMessage(event.getValue(), false);
                } else {
                    Logger.getLogger().log(Level.ERROR, "Don't know how to handle " + event);
                }
            }
            return true;
        }
        return false;
    }
}

