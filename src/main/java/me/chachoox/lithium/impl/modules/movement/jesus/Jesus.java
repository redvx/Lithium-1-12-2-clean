/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.movement.jesus;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.impl.event.events.blocks.CollisionEvent;
import me.chachoox.lithium.impl.modules.movement.jesus.JesusMode;
import me.chachoox.lithium.impl.modules.movement.jesus.ListenerCollision;
import me.chachoox.lithium.impl.modules.movement.jesus.ListenerLiquidJump;
import me.chachoox.lithium.impl.modules.movement.jesus.ListenerMotion;
import me.chachoox.lithium.impl.modules.movement.jesus.ListenerTick;

public class Jesus
extends Module
implements CollisionEvent.Listener {
    protected final EnumProperty<JesusMode> mode = new EnumProperty<JesusMode>(JesusMode.TRAMPOLINE, new String[]{"Mode", "Type", "method"}, "Solid: - Sets the current liquid you are standing on to act like a solid block / Trampoline: - Jumps on the liquid like a trampoline.");
    protected boolean jumped;
    protected double offset;
    protected StopWatch timer = new StopWatch();
    protected final ListenerCollision listenerCollision = new ListenerCollision(this);

    public Jesus() {
        super("Jesus", new String[]{"Jesus", "WaterWalk", "LavaWalk"}, "Walk on liquids.", Category.MOVEMENT);
        this.offerProperties(this.mode);
        this.offerListeners(new ListenerCollision(this), new ListenerLiquidJump(this), new ListenerMotion(this), new ListenerTick(this));
    }

    @Override
    public String getSuffix() {
        return this.mode.getFixedValue();
    }

    @Override
    public void onDisable() {
        this.offset = 0.0;
    }

    @Override
    public void onWorldLoad() {
        this.timer.reset();
    }

    @Override
    public void onCollision(CollisionEvent event) {
        if (this.isEnabled()) {
            this.listenerCollision.call(event);
        }
    }
}

