/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.api.module;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import me.chachoox.lithium.api.event.bus.Listener;
import me.chachoox.lithium.api.event.bus.api.Subscriber;
import me.chachoox.lithium.api.event.bus.instance.Bus;
import me.chachoox.lithium.api.interfaces.Labeled;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.property.BindProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.property.StringProperty;
import me.chachoox.lithium.api.property.util.Bind;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.api.util.render.animation.Animation;
import me.chachoox.lithium.api.util.render.animation.DecelerateAnimation;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.other.clickgui.ClickGUI;
import me.chachoox.lithium.impl.modules.other.hud.Hud;

public class Module
implements Subscriber,
Minecraftable,
Labeled {
    public final List<Listener<?>> listeners = new ArrayList();
    public final List<Property<?>> properties = new ArrayList();
    public final StringProperty displayLabel = new StringProperty(null, new String[]{"DisplayLabel", "label"});
    public final Property<Boolean> enabled = new Property<Boolean>(false, new String[]{"Enabled", "enabl"}, "Current state of the module.");
    public final Property<Boolean> drawn = new Property<Boolean>(true, new String[]{"Drawn", "draw", "hide"}, "Display the module in the arraylist");
    public final BindProperty bind = new BindProperty(new Bind(-1), new String[]{"Keybind", "bind", "b"}, "Current bind of the module.");
    private final Animation animation = new DecelerateAnimation(250, 1.0);
    private final String label;
    private final String description;
    private final String[] aliases;
    private final Category category;

    public Module(String label, String[] aliases, String description, Category category) {
        this.label = label;
        this.category = category;
        this.aliases = aliases;
        this.description = description;
        this.offerProperties(this.displayLabel, this.enabled, this.drawn, this.bind);
    }

    public void offerProperties(Property<?> ... properties) {
        Collections.addAll(this.properties, properties);
    }

    public void offerListeners(Listener<?> ... listeners) {
        Collections.addAll(this.listeners, listeners);
    }

    public Collection<Property<?>> getProperties() {
        return this.properties;
    }

    public Property getProperty(String alias) {
        for (Property<?> property : this.properties) {
            for (String aliases : property.getAliases()) {
                if (!alias.equalsIgnoreCase(aliases)) continue;
                return property;
            }
        }
        return null;
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            this.enable();
        } else {
            this.disable();
        }
    }

    public void setDrawn(boolean drawn) {
        this.drawn.setValue(drawn);
    }

    public void toggle() {
        this.setEnabled(this.enabled.getValue() == false);
    }

    public void enable() {
        this.enabled.setValue(true);
        this.sendToggleMessage(this);
        if (!Bus.EVENT_BUS.isSubscribed(this)) {
            Bus.EVENT_BUS.subscribe(this);
        }
        this.onEnable();
    }

    public void enableNoMessage() {
        this.enabled.setValue(true);
        if (!Bus.EVENT_BUS.isSubscribed(this)) {
            Bus.EVENT_BUS.subscribe(this);
        }
        this.onEnable();
    }

    public void disable() {
        this.enabled.setValue(false);
        this.sendToggleMessage(this);
        this.onDisable();
        Bus.EVENT_BUS.unsubscribe(this);
    }

    public void disableNoMessage() {
        this.enabled.setValue(false);
        this.onDisable();
        Bus.EVENT_BUS.unsubscribe(this);
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void onWorldLoad() {
    }

    public void onLoad() {
    }

    public boolean isNull() {
        return Module.mc.player == null || Module.mc.world == null;
    }

    public boolean isEnabled() {
        return this.enabled.getValue();
    }

    public boolean isHidden() {
        return this.drawn.getValue() == false;
    }

    @Override
    public String getLabel() {
        return this.label;
    }

    public String[] getAliases() {
        return this.aliases;
    }

    public Category getCategory() {
        return this.category;
    }

    public int getKey() {
        return ((Bind)this.bind.getValue()).getKey();
    }

    public void setKey(int key) {
        ((Bind)this.bind.getValue()).setKey(key);
    }

    public String getSuffix() {
        return null;
    }

    public String getFullLabel() {
        return (String)this.displayLabel.getValue() + (this.getSuffix() != null ? this.brackets() + " [" + "\u00a7f" + this.getSuffix() + this.brackets() + "]" : "");
    }

    public String getDescription() {
        return this.description;
    }

    public Animation getAnimation() {
        return this.animation;
    }

    @Override
    public Collection<Listener<?>> getListeners() {
        return this.listeners;
    }

    private String brackets() {
        if (Managers.MODULE.get(Hud.class).whiteBrackets()) {
            return "\u00a7f";
        }
        return "\u00a77";
    }

    public void sendToggleMessage(Module module) {
        if (!(module instanceof ClickGUI) && Module.mc.ingameGUI != null && Module.mc.player != null && Managers.MODULE.get(Hud.class).isAnnouncingModules()) {
            Logger.getLogger().log("\u00a73" + (String)module.displayLabel.getValue() + "\u00a7d" + " was " + (this.enabled.getValue() != false ? "\u00a7a" : "\u00a7c") + (this.enabled.getValue() != false ? "enabled" : "disabled"), 4444);
        }
    }
}

