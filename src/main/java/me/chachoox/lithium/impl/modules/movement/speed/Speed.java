/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.movement.speed;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.movement.MovementUtil;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.managers.minecraft.movement.KnockbackManager;
import me.chachoox.lithium.impl.modules.movement.speed.ListenerMotion;
import me.chachoox.lithium.impl.modules.movement.speed.ListenerMove;
import me.chachoox.lithium.impl.modules.movement.speed.ListenerPosLook;
import me.chachoox.lithium.impl.modules.movement.speed.enums.JumpMode;
import me.chachoox.lithium.impl.modules.movement.speed.enums.SpeedMode;

public class Speed
extends Module {
    protected final EnumProperty<SpeedMode> mode = new EnumProperty<SpeedMode>(SpeedMode.STRAFE, new String[]{"Mode", "type", "method"}, "Strafe: - Better sprint jumping with more air control and speed (Max Speed - 28km / Strict - 27-25km) / OnGround: - Simulates jumping by modifying packets (Max Speed - 150km)");
    protected final EnumProperty<JumpMode> jump = new EnumProperty<JumpMode>(JumpMode.LOW, new String[]{"Jump", "AutoJump", "AutoJumpMode", "AETRA"}, "Low: - Jumps slightly lower but can cause you to get flagged / Vanilla: - Uses vanilla jump height, will slow down strafe.");
    protected final Property<Boolean> kbBoost = new Property<Boolean>(false, KnockbackManager.KB_BOOST_ALIAS, KnockbackManager.KB_BOOST_DESCRIPTION);
    protected final NumberProperty<Float> boostReduction = new NumberProperty<Float>(Float.valueOf(4.5f), Float.valueOf(1.0f), Float.valueOf(5.0f), Float.valueOf(0.1f), KnockbackManager.BOOST_REDUCTION_ALIAS, KnockbackManager.BOOST_REDUCTION_DESCRIPTION);
    protected final Property<Boolean> inLiquids = new Property<Boolean>(false, new String[]{"InLiquids", "SpeedinWater", "spedInWater", "SpeedINLava"}, "Uses speed in liquids.");
    protected final Property<Boolean> useTimer = new Property<Boolean>(false, new String[]{"UseTimer", "Timer"}, "Changes the tick speed a little bit to make you go faster.");
    protected final Property<Boolean> autoSprint = new Property<Boolean>(false, new String[]{"AutoSprint", "sprint", "auto", "sprin"}, "Automatically sprints when strafing.");
    protected double distance;
    protected double lastDist;
    protected boolean boost;
    protected double speed;
    protected double strictTicks;
    protected int strafeStage;
    protected int onGroundStage;

    public Speed() {
        super("Speed", new String[]{"Speed", "Sped", "FastRun", "FastMove", "PollosRunningFromHisParents"}, "Allows you to move faster.", Category.MOVEMENT);
        this.offerListeners(new ListenerMotion(this), new ListenerMove(this), new ListenerPosLook(this));
        this.offerProperties(this.mode, this.jump, this.kbBoost, this.boostReduction, this.inLiquids, this.useTimer, this.autoSprint);
        this.useTimer.addObserver(event -> Managers.TIMER.reset());
    }

    @Override
    public String getSuffix() {
        return this.mode.getFixedValue();
    }

    @Override
    public void onEnable() {
        if (Speed.mc.player != null) {
            this.speed = MovementUtil.getSpeed();
            this.distance = MovementUtil.getDistance2D();
        }
        this.strafeStage = 4;
        this.lastDist = 0.0;
        this.onGroundStage = 2;
    }

    @Override
    public void onDisable() {
        Managers.TIMER.reset();
    }

    protected boolean canSprint() {
        return !(!Speed.mc.gameSettings.keyBindForward.isKeyDown() && !Speed.mc.gameSettings.keyBindBack.isKeyDown() && !Speed.mc.gameSettings.keyBindLeft.isKeyDown() && !Speed.mc.gameSettings.keyBindRight.isKeyDown() || Speed.mc.player == null || Speed.mc.player.isSneaking() || Speed.mc.player.collidedHorizontally || (float)Speed.mc.player.getFoodStats().getFoodLevel() <= 6.0f);
    }
}

