/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.movement.velocity;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.impl.modules.movement.velocity.ListenerBlockPush;
import me.chachoox.lithium.impl.modules.movement.velocity.ListenerExplosion;
import me.chachoox.lithium.impl.modules.movement.velocity.ListenerStatus;
import me.chachoox.lithium.impl.modules.movement.velocity.ListenerVelocity;

public class Velocity
extends Module {
    protected final NumberProperty<Integer> horizontal = new NumberProperty<Integer>(0, 0, 100, new String[]{"Horizontal", "horz"}, "How much kb we want to take horizontally.");
    protected final NumberProperty<Integer> vertical = new NumberProperty<Integer>(0, 0, 100, new String[]{"Vertical", "vert"}, "How much kb we want to take veritcally.");
    protected final Property<Boolean> noPush = new Property<Boolean>(true, new String[]{"NoPush", "AntiPush", "PushPrevent"}, "Stops us from getting pushed by players & blocks.");
    protected final Property<Boolean> fishingRod = new Property<Boolean>(true, new String[]{"FishingRod", "Rods", "Fishingrods", "noFishingRodPull"}, "Stops us from getting pulled by fishing rods.");

    public Velocity() {
        super("Velocity", new String[]{"Velocity", "Velo", "AntiKnockback", "AntiKB", "NoKnockback"}, "Modifies player knockback.", Category.MOVEMENT);
        this.offerProperties(this.horizontal, this.vertical, this.noPush, this.fishingRod);
        this.offerListeners(new ListenerVelocity(this), new ListenerExplosion(this), new ListenerBlockPush(this), new ListenerStatus(this));
    }

    @Override
    public String getSuffix() {
        return "H" + this.horizontal.getValue() + "%V" + this.vertical.getValue() + "%";
    }

    public Boolean doNoPush() {
        return this.isEnabled() && this.noPush.getValue() != false;
    }

    public void setVelocity(int h, int v) {
        this.horizontal.setValue(h);
        this.vertical.setValue(v);
    }
}

