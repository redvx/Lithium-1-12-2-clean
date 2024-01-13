/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.settings.KeyBinding
 */
package me.chachoox.lithium.impl.modules.player.sprint;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.util.movement.MovementUtil;
import me.chachoox.lithium.impl.event.events.update.UpdateEvent;
import me.chachoox.lithium.impl.event.listener.LambdaListener;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.player.sprint.SprintEnum;
import net.minecraft.client.settings.KeyBinding;

public class Sprint
extends Module {
    private static final int SPRINT_KEY = Sprint.mc.gameSettings.keyBindSprint.getKeyCode();
    private final EnumProperty<SprintEnum> mode = new EnumProperty<SprintEnum>(SprintEnum.RAGE, new String[]{"Mode", "type", "sprint", "method"}, "Legit - Stops sprinting when certain objects are in the way. / Rage - Always sprints if we are moving forward.");

    public Sprint() {
        super("Sprint", new String[]{"Sprint", "InstantSprint", "AutoSprint"}, "Automatically sprints depending on the situation.", Category.PLAYER);
        this.offerProperties(this.mode);
        this.offerListeners(new LambdaListener<UpdateEvent>(UpdateEvent.class, event -> {
            switch ((SprintEnum)((Object)((Object)this.mode.getValue()))) {
                case LEGIT: {
                    if (!MovementUtil.isMoving() && !((float)Sprint.mc.player.getFoodStats().getFoodLevel() > 6.0f) && Managers.ACTION.isSneaking() && Sprint.mc.player.collidedHorizontally) break;
                    KeyBinding.setKeyBindState((int)SPRINT_KEY, (boolean)true);
                    break;
                }
                case RAGE: {
                    if (!this.canRageSprint()) break;
                    Sprint.mc.player.setSprinting(true);
                }
            }
        }));
    }

    @Override
    public String getSuffix() {
        return this.mode.getFixedValue();
    }

    public boolean canRageSprint() {
        return !(!this.isEnabled() || this.mode.getValue() != SprintEnum.RAGE || !Sprint.mc.gameSettings.keyBindForward.isKeyDown() && !Sprint.mc.gameSettings.keyBindBack.isKeyDown() && !Sprint.mc.gameSettings.keyBindLeft.isKeyDown() && !Sprint.mc.gameSettings.keyBindRight.isKeyDown() || Sprint.mc.player == null || Sprint.mc.player.isSneaking() || Sprint.mc.player.collidedHorizontally || (float)Sprint.mc.player.getFoodStats().getFoodLevel() <= 6.0f);
    }
}

