/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.impl.managers.client;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.impl.modules.combat.antiregear.AntiRegear;
import me.chachoox.lithium.impl.modules.combat.aura.Aura;
import me.chachoox.lithium.impl.modules.combat.autoarmour.AutoArmour;
import me.chachoox.lithium.impl.modules.combat.autocrystal.AutoCrystal;
import me.chachoox.lithium.impl.modules.combat.autolog.AutoLog;
import me.chachoox.lithium.impl.modules.combat.autotrap.AutoTrap;
import me.chachoox.lithium.impl.modules.combat.bowmanip.BowManip;
import me.chachoox.lithium.impl.modules.combat.criticals.Criticals;
import me.chachoox.lithium.impl.modules.combat.fastbow.FastBow;
import me.chachoox.lithium.impl.modules.combat.holefill.HoleFill;
import me.chachoox.lithium.impl.modules.combat.instantexp.InstantEXP;
import me.chachoox.lithium.impl.modules.combat.instantweb.InstantWeb;
import me.chachoox.lithium.impl.modules.combat.offhand.Offhand;
import me.chachoox.lithium.impl.modules.combat.selffill.SelfFill;
import me.chachoox.lithium.impl.modules.misc.announcer.Announcer;
import me.chachoox.lithium.impl.modules.misc.antiinteract.AntiInteract;
import me.chachoox.lithium.impl.modules.misc.autoreply.AutoReply;
import me.chachoox.lithium.impl.modules.misc.chatappend.ChatAppend;
import me.chachoox.lithium.impl.modules.misc.chattimestamps.ChatTimeStamps;
import me.chachoox.lithium.impl.modules.misc.coordexploit.CoordinatesExploit;
import me.chachoox.lithium.impl.modules.misc.deathannouncer.DeathAnnouncer;
import me.chachoox.lithium.impl.modules.misc.deathcoordslog.DeathCoordsLog;
import me.chachoox.lithium.impl.modules.misc.extratab.ExtraTab;
import me.chachoox.lithium.impl.modules.misc.fpslimit.FPSLimit;
import me.chachoox.lithium.impl.modules.misc.middleclick.MiddleClick;
import me.chachoox.lithium.impl.modules.misc.nameprotect.NameProtect;
import me.chachoox.lithium.impl.modules.misc.nobreakanim.NoBreakAnim;
import me.chachoox.lithium.impl.modules.misc.packetcanceller.PacketCanceller;
import me.chachoox.lithium.impl.modules.misc.packetlogger.PacketLogger;
import me.chachoox.lithium.impl.modules.misc.payloadspoof.PayloadSpoof;
import me.chachoox.lithium.impl.modules.misc.pingspoof.PingSpoof;
import me.chachoox.lithium.impl.modules.misc.popcounter.PopCounter;
import me.chachoox.lithium.impl.modules.misc.portalgodmode.PortalGodMode;
import me.chachoox.lithium.impl.modules.misc.pvpinfo.PvPInfo;
import me.chachoox.lithium.impl.modules.misc.smartreply.SmartReply;
import me.chachoox.lithium.impl.modules.misc.spammer.Spammer;
import me.chachoox.lithium.impl.modules.misc.stresser.Stresser;
import me.chachoox.lithium.impl.modules.misc.timer.Timer;
import me.chachoox.lithium.impl.modules.misc.visualrange.VisualRange;
import me.chachoox.lithium.impl.modules.movement.elytrafly.ElytraFly;
import me.chachoox.lithium.impl.modules.movement.fly.Fly;
import me.chachoox.lithium.impl.modules.movement.holepull.HolePull;
import me.chachoox.lithium.impl.modules.movement.icespeed.IceSpeed;
import me.chachoox.lithium.impl.modules.movement.inventorymove.InventoryMove;
import me.chachoox.lithium.impl.modules.movement.jesus.Jesus;
import me.chachoox.lithium.impl.modules.movement.liquidspeed.LiquidSpeed;
import me.chachoox.lithium.impl.modules.movement.noaccel.NoAccel;
import me.chachoox.lithium.impl.modules.movement.noclip.NoClip;
import me.chachoox.lithium.impl.modules.movement.nofall.NoFall;
import me.chachoox.lithium.impl.modules.movement.noslow.NoSlow;
import me.chachoox.lithium.impl.modules.movement.packetfly.PacketFly;
import me.chachoox.lithium.impl.modules.movement.phase.Phase;
import me.chachoox.lithium.impl.modules.movement.reversestep.ReverseStep;
import me.chachoox.lithium.impl.modules.movement.speed.Speed;
import me.chachoox.lithium.impl.modules.movement.step.Step;
import me.chachoox.lithium.impl.modules.movement.tunnelspeed.TunnelSpeed;
import me.chachoox.lithium.impl.modules.movement.velocity.Velocity;
import me.chachoox.lithium.impl.modules.other.blocks.BlocksManager;
import me.chachoox.lithium.impl.modules.other.clickgui.ClickGUI;
import me.chachoox.lithium.impl.modules.other.colours.Colours;
import me.chachoox.lithium.impl.modules.other.font.CustomFont;
import me.chachoox.lithium.impl.modules.other.hud.Hud;
import me.chachoox.lithium.impl.modules.other.rpc.RichPresence;
import me.chachoox.lithium.impl.modules.player.antihitbox.AntiHitBox;
import me.chachoox.lithium.impl.modules.player.antivoid.AntiVoid;
import me.chachoox.lithium.impl.modules.player.autofeetplace.AutoFeetPlace;
import me.chachoox.lithium.impl.modules.player.autorespawn.AutoRespawn;
import me.chachoox.lithium.impl.modules.player.autostackfill.AutoStackFill;
import me.chachoox.lithium.impl.modules.player.autotool.AutoTool;
import me.chachoox.lithium.impl.modules.player.fakelag.FakeLag;
import me.chachoox.lithium.impl.modules.player.fakeplayer.FakePlayer;
import me.chachoox.lithium.impl.modules.player.fakerotation.FakeRotation;
import me.chachoox.lithium.impl.modules.player.fastbreak.FastBreak;
import me.chachoox.lithium.impl.modules.player.fastdrop.FastDrop;
import me.chachoox.lithium.impl.modules.player.fastplace.FastPlace;
import me.chachoox.lithium.impl.modules.player.liquidinteract.LiquidInteract;
import me.chachoox.lithium.impl.modules.player.nobreakdelay.NoBreakDelay;
import me.chachoox.lithium.impl.modules.player.noeatdelay.NoEatDelay;
import me.chachoox.lithium.impl.modules.player.positionspoof.PositionSpoof;
import me.chachoox.lithium.impl.modules.player.quiver.Quiver;
import me.chachoox.lithium.impl.modules.player.scaffold.Scaffold;
import me.chachoox.lithium.impl.modules.player.selfblocker.SelfBlocker;
import me.chachoox.lithium.impl.modules.player.sprint.Sprint;
import me.chachoox.lithium.impl.modules.render.animations.Animations;
import me.chachoox.lithium.impl.modules.render.betterchat.BetterChat;
import me.chachoox.lithium.impl.modules.render.blockhighlight.BlockHighlight;
import me.chachoox.lithium.impl.modules.render.chams.Chams;
import me.chachoox.lithium.impl.modules.render.compass.Compass;
import me.chachoox.lithium.impl.modules.render.customsky.CustomSky;
import me.chachoox.lithium.impl.modules.render.displaytweaks.DisplayTweaks;
import me.chachoox.lithium.impl.modules.render.esp.ESP;
import me.chachoox.lithium.impl.modules.render.fovmodifier.FovModifier;
import me.chachoox.lithium.impl.modules.render.fullbright.Fullbright;
import me.chachoox.lithium.impl.modules.render.glintmodify.GlintModify;
import me.chachoox.lithium.impl.modules.render.holeesp.HoleESP;
import me.chachoox.lithium.impl.modules.render.inventorypreview.InventoryPreview;
import me.chachoox.lithium.impl.modules.render.killeffect.KillEffect;
import me.chachoox.lithium.impl.modules.render.logoutspots.LogoutSpots;
import me.chachoox.lithium.impl.modules.render.modelchanger.ModelChanger;
import me.chachoox.lithium.impl.modules.render.nametags.Nametags;
import me.chachoox.lithium.impl.modules.render.norender.NoRender;
import me.chachoox.lithium.impl.modules.render.pearltrace.PearlTrace;
import me.chachoox.lithium.impl.modules.render.pollosesp.PollosESP;
import me.chachoox.lithium.impl.modules.render.popcolours.PopColours;
import me.chachoox.lithium.impl.modules.render.skeleton.Skeleton;
import me.chachoox.lithium.impl.modules.render.storageesp.StorageESP;
import me.chachoox.lithium.impl.modules.render.tracers.Tracers;

public class ModuleManager {
    private final Map<Class<? extends Module>, Module> modules = new LinkedHashMap<Class<? extends Module>, Module>();

    public void init() {
        this.register(new ClickGUI());
        this.register(new Colours());
        this.register(new CustomFont());
        this.register(new Hud());
        this.register(new RichPresence());
        this.register(new BlocksManager());
        this.register(new AntiRegear());
        this.register(new Aura());
        this.register(new AutoCrystal());
        this.register(new AutoLog());
        this.register(new AutoTrap());
        this.register(new Criticals());
        this.register(new HoleFill());
        this.register(new Offhand());
        this.register(new SelfFill());
        this.register(new FastBow());
        this.register(new AutoArmour());
        this.register(new InstantEXP());
        this.register(new BowManip());
        this.register(new AutoFeetPlace());
        this.register(new SelfBlocker());
        this.register(new AutoStackFill());
        this.register(new InstantWeb());
        this.register(new AntiInteract());
        this.register(new PayloadSpoof());
        this.register(new Announcer());
        this.register(new Stresser());
        this.register(new AutoReply());
        this.register(new ExtraTab());
        this.register(new FPSLimit());
        this.register(new BetterChat());
        this.register(new DisplayTweaks());
        this.register(new PvPInfo());
        this.register(new ChatAppend());
        this.register(new DeathAnnouncer());
        this.register(new PacketCanceller());
        this.register(new PacketLogger());
        this.register(new AutoRespawn());
        this.register(new DeathCoordsLog());
        this.register(new PopCounter());
        this.register(new VisualRange());
        this.register(new CoordinatesExploit());
        this.register(new PortalGodMode());
        this.register(new NoBreakAnim());
        this.register(new Timer());
        this.register(new MiddleClick());
        this.register(new PingSpoof());
        this.register(new SmartReply());
        this.register(new Spammer());
        this.register(new HolePull());
        this.register(new Fly());
        this.register(new NoClip());
        this.register(new NoAccel());
        this.register(new Velocity());
        this.register(new InventoryMove());
        this.register(new LiquidSpeed());
        this.register(new ReverseStep());
        this.register(new TunnelSpeed());
        this.register(new IceSpeed());
        this.register(new NoSlow());
        this.register(new ElytraFly());
        this.register(new Step());
        this.register(new Jesus());
        this.register(new NoFall());
        this.register(new Speed());
        this.register(new PacketFly());
        this.register(new Phase());
        this.register(new FakeLag());
        this.register(new PositionSpoof());
        this.register(new FastBreak());
        this.register(new AntiHitBox());
        this.register(new NameProtect());
        this.register(new FakeRotation());
        this.register(new AutoTool());
        this.register(new LiquidInteract());
        this.register(new NoEatDelay());
        this.register(new FastDrop());
        this.register(new NoBreakDelay());
        this.register(new AntiVoid());
        this.register(new Scaffold());
        this.register(new FastPlace());
        this.register(new Sprint());
        this.register(new Quiver());
        this.register(new FakePlayer());
        this.register(new Animations());
        this.register(new BlockHighlight());
        this.register(new GlintModify());
        this.register(new Nametags());
        this.register(new ModelChanger());
        this.register(new NoRender());
        this.register(new HoleESP());
        this.register(new PearlTrace());
        this.register(new LogoutSpots());
        this.register(new Chams());
        this.register(new KillEffect());
        this.register(new ChatTimeStamps());
        this.register(new ESP());
        this.register(new StorageESP());
        this.register(new Skeleton());
        this.register(new CustomSky());
        this.register(new Compass());
        this.register(new FovModifier());
        this.register(new PopColours());
        this.register(new InventoryPreview());
        this.register(new Fullbright());
        this.register(new PollosESP());
        this.register(new Tracers());
        this.modules.values().forEach(Module::onLoad);
        this.modules.values().forEach(mod -> mod.displayLabel.setValue(mod.getLabel()));
    }

    private void register(Module module) {
        this.modules.put(module.getClass(), module);
    }

    public Collection<Module> getModules() {
        return this.modules.values();
    }

    public <T extends Module> T get(Class<T> clazz) {
        return (T)this.modules.get(clazz);
    }

    public Module getModuleByLabel(String label) {
        for (Module module : this.modules.values()) {
            if (!module.getLabel().equalsIgnoreCase(label)) continue;
            return module;
        }
        return null;
    }

    public Module getModuleByAlias(String alias) {
        for (Module module : this.modules.values()) {
            for (String aliases : module.getAliases()) {
                if (!aliases.equalsIgnoreCase(alias)) continue;
                return module;
            }
        }
        return null;
    }
}

