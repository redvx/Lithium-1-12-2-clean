/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.client.gui.inventory.GuiScreenHorseInventory
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.passive.AbstractChestHorse
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.util.EnumHand
 */
package me.chachoox.lithium.asm.mixins.gui;

import java.io.IOException;
import java.util.Comparator;
import me.chachoox.lithium.impl.gui.entity.DupeButton;
import me.chachoox.lithium.impl.managers.Managers;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiScreenHorseInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractChestHorse;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GuiContainer.class})
public abstract class MixinGuiContainer
extends GuiScreen {
    private DupeButton salGuiDupeButton;
    @Shadow
    protected int guiLeft;
    @Shadow
    protected int guiTop;

    @Inject(method={"initGui"}, at={@At(value="HEAD")})
    public void initGui(CallbackInfo info) {
        this.buttonList.clear();
        this.salGuiDupeButton = new DupeButton(1338, this.width / 2 - 50, this.guiTop - 20, "Dupe");
        this.buttonList.add(this.salGuiDupeButton);
        this.salGuiDupeButton.setWidth(100);
        this.updateButton();
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 1338) {
            this.doDupe();
        } else {
            super.actionPerformed(button);
        }
    }

    @Inject(method={"updateScreen"}, at={@At(value="HEAD")})
    public void updateScreen(CallbackInfo ci) {
        this.updateButton();
    }

    private void updateButton() {
        if (Managers.DUMMY.isValid()) {
            this.salGuiDupeButton.visible = true;
            this.salGuiDupeButton.displayString = "Dupe";
        } else {
            this.salGuiDupeButton.visible = false;
        }
    }

    public void doDupe() {
        AbstractChestHorse abstractChestHorse;
        Entity daHorse = this.mc.world.loadedEntityList.stream().filter(this::isValidEntity).min(Comparator.comparing(p_Entity -> Float.valueOf(this.mc.player.getDistance(p_Entity)))).orElse(null);
        if (this.mc.currentScreen instanceof GuiScreenHorseInventory && daHorse instanceof AbstractChestHorse && this.mc.player.getRidingEntity() != null && (abstractChestHorse = (AbstractChestHorse)daHorse).hasChest()) {
            this.mc.player.connection.sendPacket((Packet)new CPacketUseEntity(daHorse, EnumHand.MAIN_HAND, daHorse.getPositionVector()));
        }
    }

    private boolean isValidEntity(Entity entity) {
        if (entity instanceof AbstractChestHorse) {
            AbstractChestHorse horse = (AbstractChestHorse)entity;
            return !horse.isChild() && horse.isTame();
        }
        return false;
    }
}

