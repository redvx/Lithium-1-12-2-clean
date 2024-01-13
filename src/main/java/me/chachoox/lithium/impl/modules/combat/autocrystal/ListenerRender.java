/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.init.Items
 *  net.minecraft.item.EnumAction
 *  net.minecraft.item.ItemTool
 *  net.minecraft.util.math.AxisAlignedBB
 */
package me.chachoox.lithium.impl.modules.combat.autocrystal;

import me.chachoox.lithium.api.util.blocks.MineUtil;
import me.chachoox.lithium.api.util.inventory.ItemUtil;
import me.chachoox.lithium.api.util.render.Interpolation;
import me.chachoox.lithium.api.util.render.RenderUtil;
import me.chachoox.lithium.impl.event.events.render.main.Render3DEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.combat.autocrystal.AutoCrystal;
import me.chachoox.lithium.impl.modules.combat.autocrystal.mode.Swap;
import me.chachoox.lithium.impl.modules.other.colours.Colours;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemTool;
import net.minecraft.util.math.AxisAlignedBB;

public class ListenerRender
extends ModuleListener<AutoCrystal, Render3DEvent> {
    public ListenerRender(AutoCrystal module) {
        super(module, Render3DEvent.class);
    }

    @Override
    public void call(Render3DEvent event) {
        if (((AutoCrystal)this.module).multiTask.getValue().booleanValue() && (ListenerRender.mc.player.isHandActive() && ListenerRender.mc.player.getActiveItemStack().getItemUseAction().equals((Object)EnumAction.EAT) || ListenerRender.mc.player.getActiveItemStack().getItemUseAction().equals((Object)EnumAction.DRINK))) {
            return;
        }
        if (((AutoCrystal)this.module).whileMining.getValue().booleanValue() && ItemUtil.isHolding(ItemTool.class) && ListenerRender.mc.playerController.getIsHittingBlock() && MineUtil.canBreak(ListenerRender.mc.objectMouseOver.getBlockPos()) && !ListenerRender.mc.world.isAirBlock(ListenerRender.mc.objectMouseOver.getBlockPos())) {
            return;
        }
        int crystalSlot = ItemUtil.getItemFromHotbar(Items.END_CRYSTAL);
        if (crystalSlot == -1 && !((AutoCrystal)this.module).offhand) {
            return;
        }
        if (!(((AutoCrystal)this.module).swap.getValue() != Swap.NONE && ((AutoCrystal)this.module).swap.getValue() != Swap.NORMAL || ListenerRender.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL || ((AutoCrystal)this.module).offhand)) {
            return;
        }
        if (((AutoCrystal)this.module).renderPos != null) {
            RenderUtil.startRender();
            AxisAlignedBB bb = Interpolation.interpolatePos(((AutoCrystal)this.module).renderPos, 1.0f);
            RenderUtil.drawBox(bb, Colours.get().getColourCustomAlpha(30));
            RenderUtil.drawOutline(bb, 1.6f, Colours.get().getColour());
            RenderUtil.endRender();
        }
    }
}

