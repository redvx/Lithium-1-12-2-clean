/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.inventory.GuiInventory
 *  net.minecraft.enchantment.Enchantment
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.init.Enchantments
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.EntityEquipmentSlot
 *  net.minecraft.item.ItemArmor
 *  net.minecraft.item.ItemStack
 */
package me.chachoox.lithium.impl.modules.combat.autoarmour;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import me.chachoox.lithium.api.util.entity.DamageUtil;
import me.chachoox.lithium.api.util.inventory.InventoryUtil;
import me.chachoox.lithium.api.util.thread.MutableWrapper;
import me.chachoox.lithium.impl.event.events.update.TickEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.combat.autoarmour.AutoArmour;
import me.chachoox.lithium.impl.modules.combat.autoarmour.util.DamageStack;
import me.chachoox.lithium.impl.modules.combat.autoarmour.util.LevelStack;
import me.chachoox.lithium.impl.modules.combat.autoarmour.util.MendingStage;
import me.chachoox.lithium.impl.modules.combat.autoarmour.util.SingleMendingSlot;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ListenerTick
extends ModuleListener<AutoArmour, TickEvent> {
    public ListenerTick(AutoArmour module) {
        super(module, TickEvent.class);
    }

    @Override
    public void call(TickEvent event) {
        if (!event.isSafe()) {
            ((AutoArmour)this.module).stage = MendingStage.MENDING;
            ((AutoArmour)this.module).putBackClick = null;
            return;
        }
        ((AutoArmour)this.module).stackSet = false;
        ((AutoArmour)this.module).queuedSlots.clear();
        ((AutoArmour)this.module).windowClicks.clear();
        if (((AutoArmour)this.module).pauseInInv.getValue().booleanValue() && ListenerTick.mc.currentScreen instanceof GuiInventory) {
            return;
        }
        if (InventoryUtil.validScreen()) {
            if (((AutoArmour)this.module).canAutoMend()) {
                ((AutoArmour)this.module).queuedSlots.add(-2);
                ItemStack setStack = ((AutoArmour)this.module).setStack();
                boolean setStackIsNull = setStack == null;
                boolean singleMend = ((AutoArmour)this.module).singleMend.getValue();
                if (setStack == null) {
                    if (!singleMend) {
                        return;
                    }
                    setStack = ListenerTick.mc.player.inventory.getItemStack();
                    ((AutoArmour)this.module).queuedSlots.remove(-2);
                }
                int mendBlock = 25;
                if (((AutoArmour)this.module).stage != MendingStage.MENDING) {
                    if (setStackIsNull || this.isFull()) {
                        ((AutoArmour)this.module).stage = MendingStage.MENDING;
                        return;
                    }
                    if (((AutoArmour)this.module).stage == MendingStage.BLOCK) {
                        if (((AutoArmour)this.module).mendingTimer.passed(mendBlock)) {
                            ((AutoArmour)this.module).stage = MendingStage.TAKEOFF;
                            ((AutoArmour)this.module).mendingTimer.reset();
                        } else {
                            return;
                        }
                    }
                    if (((AutoArmour)this.module).stage == MendingStage.TAKEOFF && ((AutoArmour)this.module).mendingTimer.passed(50L)) {
                        ((AutoArmour)this.module).stage = MendingStage.MENDING;
                    }
                }
                if (singleMend) {
                    this.doSingleMend(setStack, mendBlock);
                } else {
                    this.doNormalMend(setStack, mendBlock);
                }
            } else {
                int slot;
                ((AutoArmour)this.module).stage = MendingStage.MENDING;
                ((AutoArmour)this.module).unblockMendingSlots();
                Map<EntityEquipmentSlot, Integer> map = this.setup(((AutoArmour)this.module).strict.getValue() == false);
                int last = -1;
                ItemStack drag = ListenerTick.mc.player.inventory.getItemStack();
                for (Map.Entry<EntityEquipmentSlot, Integer> entry : map.entrySet()) {
                    if (entry.getValue() != 8) continue;
                    slot = AutoArmour.fromEquipment(entry.getKey());
                    if (slot != -1 && slot != 45) {
                        ItemStack inSlot = InventoryUtil.get(slot);
                        ((AutoArmour)this.module).queueClick(slot, inSlot, drag);
                        drag = inSlot;
                        last = slot;
                    }
                    map.remove(entry.getKey());
                    break;
                }
                for (Map.Entry<EntityEquipmentSlot, Integer> entry : map.entrySet()) {
                    slot = AutoArmour.fromEquipment(entry.getKey());
                    if (slot == -1 || slot == 45 || entry.getValue() == null) continue;
                    int i = entry.getValue();
                    ItemStack inSlot = InventoryUtil.get(i);
                    ((AutoArmour)this.module).queueClick(i, inSlot, drag).setDoubleClick(((AutoArmour)this.module).doubleClick.getValue());
                    if (!drag.isEmpty()) {
                        ((AutoArmour)this.module).queuedSlots.add(i);
                    }
                    drag = inSlot;
                    inSlot = InventoryUtil.get(slot);
                    ((AutoArmour)this.module).queueClick(slot, inSlot, drag);
                    drag = inSlot;
                    last = slot;
                }
                if (((AutoArmour)this.module).putBack.getValue().booleanValue()) {
                    if (last != -1) {
                        ItemStack stack = InventoryUtil.get(last);
                        if (!stack.isEmpty()) {
                            ((AutoArmour)this.module).queuedSlots.add(-2);
                            int air = AutoArmour.findItem(Items.AIR, ((AutoArmour)this.module).strict.getValue() == false, ((AutoArmour)this.module).queuedSlots);
                            if (air != -1) {
                                ItemStack inSlot = InventoryUtil.get(air);
                                ((AutoArmour)this.module).putBackClick = ((AutoArmour)this.module).queueClick(air, inSlot, drag);
                                ((AutoArmour)this.module).putBackClick.addPost(() -> {
                                    ((AutoArmour)this.module).putBackClick = null;
                                });
                            }
                        }
                    } else if (((AutoArmour)this.module).putBackClick != null && ((AutoArmour)this.module).putBackClick.isValid()) {
                        ((AutoArmour)this.module).queueClick(((AutoArmour)this.module).putBackClick);
                    } else {
                        ((AutoArmour)this.module).putBackClick = null;
                    }
                }
            }
        } else {
            ((AutoArmour)this.module).stage = MendingStage.MENDING;
        }
        ((AutoArmour)this.module).runClick();
    }

    private boolean checkMendingStage(int mendBlock) {
        if (mendBlock > 0 && ((AutoArmour)this.module).stage == MendingStage.MENDING) {
            ((AutoArmour)this.module).stage = MendingStage.BLOCK;
            ((AutoArmour)this.module).mendingTimer.reset();
            return true;
        }
        return false;
    }

    private boolean isFull() {
        boolean result;
        boolean added = false;
        if (!((AutoArmour)this.module).drag.getValue().booleanValue()) {
            added = ((AutoArmour)this.module).queuedSlots.add(-2);
        }
        boolean bl = result = AutoArmour.findItem(Items.AIR, ((AutoArmour)this.module).strict.getValue() == false, ((AutoArmour)this.module).queuedSlots) == -1;
        if (added) {
            ((AutoArmour)this.module).queuedSlots.remove(-2);
        }
        return result;
    }

    private void doNormalMend(ItemStack dragIn, int mendBlock) {
        ArrayList<DamageStack> stacks = new ArrayList<DamageStack>(4);
        for (int i = 5; i < 9; ++i) {
            float percent;
            ItemStack stack = ListenerTick.mc.player.inventoryContainer.getSlot(i).getStack();
            if (EnchantmentHelper.getEnchantmentLevel((Enchantment)Enchantments.MENDING, (ItemStack)stack) == 0 || !((percent = DamageUtil.getPercent(stack)) > (float)((Integer)((AutoArmour)this.module).damages[i - 5].getValue()).intValue())) continue;
            stacks.add(new DamageStack(stack, percent, i));
        }
        stacks.sort(DamageStack::compareTo);
        MutableWrapper<ItemStack> drag = new MutableWrapper<ItemStack>(dragIn);
        for (DamageStack stack : stacks) {
            if (!this.checkDamageStack(stack, mendBlock, drag)) continue;
            return;
        }
    }

    private void doSingleMend(ItemStack dragIn, int mendBlock) {
        boolean allBlocked = true;
        for (SingleMendingSlot singleMendingSlot : ((AutoArmour)this.module).singleMendingSlots) {
            allBlocked = allBlocked && singleMendingSlot.isBlocked();
        }
        if (allBlocked) {
            ((AutoArmour)this.module).unblockMendingSlots();
        }
        ArrayList<DamageStack> stacks = new ArrayList<DamageStack>(4);
        for (int i = 5; i < 9; ++i) {
            ItemStack stack2 = ListenerTick.mc.player.inventoryContainer.getSlot(i).getStack();
            if (EnchantmentHelper.getEnchantmentLevel((Enchantment)Enchantments.MENDING, (ItemStack)stack2) == 0) continue;
            float percent = DamageUtil.getPercent(stack2);
            stacks.add(new DamageStack(stack2, percent, i));
        }
        stacks.sort(DamageStack::compareTo);
        if (stacks.size() <= 0) {
            int bestSlot = -1;
            MutableWrapper<Float> lowest = new MutableWrapper<Float>(Float.valueOf(Float.MAX_VALUE));
            MutableWrapper<ItemStack> bestStack = new MutableWrapper<ItemStack>(ItemStack.EMPTY);
            for (SingleMendingSlot singleMendingSlot : ((AutoArmour)this.module).singleMendingSlots) {
                if (singleMendingSlot.isBlocked()) continue;
                int slot = AutoArmour.iterateItems(((AutoArmour)this.module).strict.getValue() == false, ((AutoArmour)this.module).queuedSlots, stack -> {
                    float percent;
                    if (AutoArmour.getSlot(stack) == singleMendingSlot.getSlot() && (percent = DamageUtil.getPercent(stack)) < ((Float)lowest.get()).floatValue()) {
                        bestStack.set((ItemStack)stack);
                        lowest.set(Float.valueOf(percent));
                        return true;
                    }
                    return false;
                });
                bestSlot = slot == -1 ? bestSlot : slot;
            }
            if (bestSlot != -1 && lowest.get().floatValue() < 100.0f) {
                EntityEquipmentSlot equipmentSlot = AutoArmour.getSlot(bestStack.get());
                if (equipmentSlot != null) {
                    int slot = AutoArmour.fromEquipment(equipmentSlot);
                    if (bestSlot != -2) {
                        ((AutoArmour)this.module).queueClick(bestSlot, bestStack.get(), dragIn, slot).setDoubleClick(((AutoArmour)this.module).doubleClick.getValue());
                    }
                    ((AutoArmour)this.module).queueClick(slot, InventoryUtil.get(slot), bestStack.get());
                }
            } else if (!allBlocked) {
                ((AutoArmour)this.module).unblockMendingSlots();
            }
        } else if (stacks.size() == 1) {
            DamageStack stack3 = (DamageStack)stacks.get(0);
            SingleMendingSlot mendingSlot = Arrays.stream(((AutoArmour)this.module).singleMendingSlots).filter(s -> s.getSlot() == AutoArmour.getSlot(stack3.getStack())).findFirst().orElse(null);
            if (mendingSlot != null && stack3.getDamage() > (float)((Integer)((AutoArmour)this.module).damages[stack3.getSlot() - 5].getValue()).intValue()) {
                MutableWrapper<ItemStack> drag = new MutableWrapper<ItemStack>(dragIn);
                this.checkDamageStack(stack3, mendBlock, drag);
                mendingSlot.setBlocked(true);
            }
        } else {
            MutableWrapper<ItemStack> drag = new MutableWrapper<ItemStack>(dragIn);
            for (DamageStack stack4 : stacks) {
                if (!this.checkDamageStack(stack4, mendBlock, drag)) continue;
                return;
            }
            ((AutoArmour)this.module).stage = MendingStage.MENDING;
        }
    }

    private boolean checkDamageStack(DamageStack stack, int mendBlock, MutableWrapper<ItemStack> drag) {
        ItemStack sStack = stack.getStack();
        int slot = AutoArmour.findItem(Items.AIR, ((AutoArmour)this.module).strict.getValue() == false, ((AutoArmour)this.module).queuedSlots);
        if (slot == -1) {
            if (((AutoArmour)this.module).drag.getValue().booleanValue() && (((AutoArmour)this.module).stackSet || ListenerTick.mc.player.inventory.getItemStack().isEmpty())) {
                if (this.checkMendingStage(mendBlock)) {
                    return true;
                }
                ((AutoArmour)this.module).queueClick(stack.getSlot(), sStack, drag.get(), -1);
            }
            return true;
        }
        if (slot != -2 && ListenerTick.mc.player.inventory.getItemStack().isEmpty()) {
            if (this.checkMendingStage(mendBlock)) {
                return true;
            }
            ((AutoArmour)this.module).queueClick(stack.getSlot(), sStack, drag.get(), slot).setDoubleClick(((AutoArmour)this.module).doubleClick.getValue());
            drag.set(sStack);
            ItemStack inSlot = InventoryUtil.get(slot);
            ((AutoArmour)this.module).queueClick(slot, inSlot, drag.get());
            ((AutoArmour)this.module).queuedSlots.add(slot);
            drag.set(inSlot);
        }
        return false;
    }

    private Map<EntityEquipmentSlot, Integer> setup(boolean xCarry) {
        boolean wearingBlast = false;
        HashSet<EntityEquipmentSlot> cursed = new HashSet<EntityEquipmentSlot>(6);
        ArrayList<EntityEquipmentSlot> empty = new ArrayList<EntityEquipmentSlot>(4);
        for (int i = 5; i < 9; ++i) {
            ItemStack stack = InventoryUtil.get(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof ItemArmor) {
                    int lvl = EnchantmentHelper.getEnchantmentLevel((Enchantment)Enchantments.BLAST_PROTECTION, (ItemStack)stack);
                    if (lvl > 0) {
                        wearingBlast = true;
                    }
                } else {
                    empty.add(AutoArmour.fromSlot(i));
                }
                if (!EnchantmentHelper.hasBindingCurse((ItemStack)stack)) continue;
                cursed.add(AutoArmour.fromSlot(i));
                continue;
            }
            empty.add(AutoArmour.fromSlot(i));
        }
        if (wearingBlast && empty.isEmpty()) {
            return new HashMap<EntityEquipmentSlot, Integer>(1, 1.0f);
        }
        HashMap<EntityEquipmentSlot, LevelStack> map = new HashMap<EntityEquipmentSlot, LevelStack>(6);
        HashMap<EntityEquipmentSlot, LevelStack> blast = new HashMap<EntityEquipmentSlot, LevelStack>(6);
        for (int i = 8; i < 45; ++i) {
            Object stack;
            if (i == 5) {
                i = 9;
            }
            if (!(stack = this.getStack(i)).isEmpty() && stack.getItem() instanceof ItemArmor) {
                float d = DamageUtil.getDamage((ItemStack)stack);
                ItemArmor armor = (ItemArmor)stack.getItem();
                EntityEquipmentSlot type = armor.getEquipmentSlot();
                int blastLvL = EnchantmentHelper.getEnchantmentLevel((Enchantment)Enchantments.BLAST_PROTECTION, (ItemStack)stack);
                if (blastLvL != 0) {
                    this.compute((ItemStack)stack, blast, type, i, blastLvL, d);
                }
                int lvl = EnchantmentHelper.getEnchantmentLevel((Enchantment)Enchantments.PROTECTION, (ItemStack)stack);
                if (blastLvL != 0) {
                    if (lvl < 4) continue;
                    lvl += blastLvL;
                }
                this.compute((ItemStack)stack, map, type, i, lvl, d);
            }
            if (i != 8 || !xCarry) continue;
            i = 0;
        }
        HashMap<EntityEquipmentSlot, Integer> result = new HashMap<EntityEquipmentSlot, Integer>(6);
        if (wearingBlast) {
            for (EntityEquipmentSlot slot : empty) {
                Object e2;
                if (map.get(slot) != null || (e2 = (LevelStack)blast.get(slot)) == null) continue;
                map.put(slot, (LevelStack)e2);
            }
            map.keySet().retainAll(empty);
            map.forEach((key, value) -> result.put((EntityEquipmentSlot)key, value.getSlot()));
        } else {
            boolean foundBlast = false;
            ArrayList<EntityEquipmentSlot> both = new ArrayList<EntityEquipmentSlot>(4);
            for (EntityEquipmentSlot slot : empty) {
                LevelStack b = (LevelStack)blast.get(slot);
                LevelStack p = (LevelStack)map.get(slot);
                if (b == null && p != null) {
                    result.put(slot, p.getSlot());
                    continue;
                }
                if (b != null && p == null) {
                    foundBlast = true;
                    result.put(slot, b.getSlot());
                    continue;
                }
                if (b == null) continue;
                both.add(slot);
            }
            for (EntityEquipmentSlot b : both) {
                if (foundBlast) {
                    result.put(b, ((LevelStack)map.get(b)).getSlot());
                    continue;
                }
                foundBlast = true;
                result.put(b, ((LevelStack)blast.get(b)).getSlot());
            }
            if (!foundBlast && !blast.isEmpty()) {
                Optional<Map.Entry> first = blast.entrySet().stream().filter(e -> !cursed.contains(e.getKey())).findFirst();
                first.ifPresent(e -> result.put((EntityEquipmentSlot)e.getKey(), ((LevelStack)e.getValue()).getSlot()));
            }
        }
        return result;
    }

    private ItemStack getStack(int slot) {
        if (slot == 8) {
            return ListenerTick.mc.player.inventory.getItemStack();
        }
        return InventoryUtil.get(slot);
    }

    private void compute(ItemStack stack, Map<EntityEquipmentSlot, LevelStack> map, EntityEquipmentSlot type, int slot, int level, float damage) {
        map.compute(type, (k, v) -> {
            if (v == null || !v.isBetter(damage, 35.0f, level, true)) {
                return new LevelStack(stack, damage, slot, level);
            }
            return v;
        });
    }
}

