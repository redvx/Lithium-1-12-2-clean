/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.modules.render.norender;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.impl.modules.render.norender.ListenerAnimation;
import me.chachoox.lithium.impl.modules.render.norender.ListenerGameLoop;
import me.chachoox.lithium.impl.modules.render.norender.ListenerRender;
import me.chachoox.lithium.impl.modules.render.norender.ListenerRenderEntity;
import me.chachoox.lithium.impl.modules.render.norender.ListenerSplash;

public class NoRender
extends Module {
    private final Property<Boolean> totemAnimation = new Property<Boolean>(true, new String[]{"Totem", "totemanimation", "totemanim"}, "");
    private final Property<Boolean> fire = new Property<Boolean>(true, new String[]{"Fire", "fir"}, "");
    private final Property<Boolean> portal = new Property<Boolean>(true, new String[]{"Portals", "portal"}, "");
    private final Property<Boolean> pumpkin = new Property<Boolean>(true, new String[]{"Pumpkins", "punpkin"}, "");
    private final Property<Boolean> hurtCam = new Property<Boolean>(true, new String[]{"Hurtcam", "hrtcam"}, "");
    private final Property<Boolean> toast = new Property<Boolean>(true, new String[]{"Toasts", "tutorial"}, "");
    private final Property<Boolean> explosions = new Property<Boolean>(true, new String[]{"Explosions", "explosive"}, "");
    private final Property<Boolean> suffocation = new Property<Boolean>(true, new String[]{"Suffocation", "georgefloyd"}, "");
    private final Property<Boolean> noBossOverlay = new Property<Boolean>(false, new String[]{"BossBar", "boss"}, "");
    private final Property<Boolean> noArmor = new Property<Boolean>(false, new String[]{"Armor", "aa"}, "");
    private final Property<Boolean> boxedVines = new Property<Boolean>(false, new String[]{"Vines", "vine"}, "");
    private final Property<Boolean> entityFire = new Property<Boolean>(false, new String[]{"EntityFire", "entityfir"}, "");
    private final Property<Boolean> loadingScreen = new Property<Boolean>(true, new String[]{"LoadingScreen", "loadinscreen"}, "");
    private final Property<Boolean> itemFrames = new Property<Boolean>(false, new String[]{"ItemFrames", "itemframe"}, "");
    private final Property<Boolean> limbSwing = new Property<Boolean>(false, new String[]{"LimbSwing", "dogshitanimations"}, "");
    private final Property<Boolean> noSpectators = new Property<Boolean>(false, new String[]{"Spectators", "spec"}, "");
    private final Property<Boolean> noParrots = new Property<Boolean>(true, new String[]{"Parrots", "parrot"}, "");
    private final Property<Boolean> weather = new Property<Boolean>(true, new String[]{"Weather", "weathr"}, "");
    private final Property<Boolean> viewBobbing = new Property<Boolean>(false, new String[]{"ViewBobbing", "nobob"}, "");
    private final Property<Boolean> tnt = new Property<Boolean>(false, new String[]{"TNT", "tn", "dynamite"}, "");
    private final Property<Boolean> dynamicFOV = new Property<Boolean>(false, new String[]{"DynamicFov", "dynfov"}, "");
    private final Property<Boolean> waterSplash = new Property<Boolean>(false, new String[]{"WaterSplash"}, "");
    private final Property<Boolean> antiResources = new Property<Boolean>(false, new String[]{"Resources", "resour"}, "");
    private final Property<Boolean> eatingParticles = new Property<Boolean>(true, new String[]{"EatingParticles", "eatparticles"}, "");
    private final Property<Boolean> criticalParticles = new Property<Boolean>(false, new String[]{"CriticalParticles", "critparticles"}, "");
    private final Property<Boolean> sprintingParticles = new Property<Boolean>(false, new String[]{"SprintingParticles", "sprintparticle"}, "");
    private final NumberProperty<Integer> time = new NumberProperty<Integer>(0, 0, 24000, new String[]{"TimeChanger", "time"}, "Changes the time client side.");

    public NoRender() {
        super("NoRender", new String[]{"NoRender", "lesslag", "polloshack"}, "Stops rendering certain entities or textures.", Category.RENDER);
        this.offerProperties(this.totemAnimation, this.fire, this.portal, this.pumpkin, this.toast, this.explosions, this.suffocation, this.noBossOverlay, this.noArmor, this.boxedVines, this.entityFire, this.loadingScreen, this.itemFrames, this.limbSwing, this.noSpectators, this.noParrots, this.weather, this.viewBobbing, this.tnt, this.dynamicFOV, this.waterSplash, this.antiResources, this.eatingParticles, this.criticalParticles, this.sprintingParticles, this.time);
        this.offerListeners(new ListenerRenderEntity(this), new ListenerGameLoop(this), new ListenerAnimation(this), new ListenerSplash(this), new ListenerRender(this));
        this.viewBobbing.addObserver(event -> {
            NoRender.mc.gameSettings.viewBobbing = this.viewBobbing.getValue();
        });
        for (Property<?> property : this.getProperties()) {
            if (property.getLabel().equals("TimeChanger") || property.getLabel().equals("CrystalRange") || property.getLabel().equals("Keybind")) continue;
            property.setDescription(String.format("Stops rendering %s.", property.getLabel().toLowerCase()));
        }
    }

    public boolean getTotemAnimation() {
        return this.isEnabled() && this.totemAnimation.getValue() != false;
    }

    public boolean getFire() {
        return this.isEnabled() && this.fire.getValue() != false;
    }

    public boolean getPortal() {
        return this.isEnabled() && this.portal.getValue() != false;
    }

    public boolean getPumpkin() {
        return this.isEnabled() && this.pumpkin.getValue() != false;
    }

    public boolean getHurtCam() {
        return this.isEnabled() && this.hurtCam.getValue() != false;
    }

    public boolean getToast() {
        return this.isEnabled() && this.toast.getValue() != false;
    }

    public boolean getExplosions() {
        return this.isEnabled() && this.explosions.getValue() != false;
    }

    public boolean getSuffocation() {
        return this.isEnabled() && this.suffocation.getValue() != false;
    }

    public boolean getNoBossOverlay() {
        return this.isEnabled() && this.noBossOverlay.getValue() != false;
    }

    public boolean getNoArmor() {
        return this.isEnabled() && this.noArmor.getValue() != false;
    }

    public boolean getBoxedVines() {
        return this.isEnabled() && this.boxedVines.getValue() != false;
    }

    public boolean getEntityFire() {
        return this.isEnabled() && this.entityFire.getValue() != false;
    }

    public boolean getLoadingScreen() {
        return this.isEnabled() && this.loadingScreen.getValue() != false;
    }

    public boolean getItemFrames() {
        return this.isEnabled() && this.itemFrames.getValue() != false;
    }

    public boolean getLimbSwing() {
        return this.isEnabled() && this.limbSwing.getValue() != false;
    }

    public boolean getNoParrots() {
        return this.isEnabled() && this.noParrots.getValue() != false;
    }

    public boolean getWeather() {
        return this.isEnabled() && this.weather.getValue() != false;
    }

    public boolean getTnt() {
        return this.isEnabled() && this.tnt.getValue() != false;
    }

    public boolean getDynamicFov() {
        return this.isEnabled() && this.dynamicFOV.getValue() != false;
    }

    public boolean getWaterSplash() {
        return this.isEnabled() && this.waterSplash.getValue() != false;
    }

    public boolean getAntiResources() {
        return this.isEnabled() && this.antiResources.getValue() != false;
    }

    public boolean getNoSpectators() {
        return this.isEnabled() && this.noSpectators.getValue() != false;
    }

    public boolean getCriticalParticles() {
        return this.isEnabled() && this.criticalParticles.getValue() != false;
    }

    public boolean getEatingParticles() {
        return this.isEnabled() && this.eatingParticles.getValue() != false;
    }

    public boolean getSprintParticles() {
        return this.isEnabled() && this.sprintingParticles.getValue() != false;
    }

    public int getTime() {
        return (Integer)this.time.getValue();
    }
}

