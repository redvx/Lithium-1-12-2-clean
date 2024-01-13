/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.item.ItemStack
 */
package me.chachoox.lithium.impl.modules.misc.announcer;

import java.util.LinkedHashMap;
import java.util.Random;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.math.MathUtil;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.impl.modules.misc.announcer.ListenerBreak;
import me.chachoox.lithium.impl.modules.misc.announcer.ListenerDeath;
import me.chachoox.lithium.impl.modules.misc.announcer.ListenerEat;
import me.chachoox.lithium.impl.modules.misc.announcer.ListenerJump;
import me.chachoox.lithium.impl.modules.misc.announcer.ListenerLogout;
import me.chachoox.lithium.impl.modules.misc.announcer.ListenerMotion;
import me.chachoox.lithium.impl.modules.misc.announcer.ListenerPlace;
import me.chachoox.lithium.impl.modules.misc.announcer.ListenerUpdate;
import me.chachoox.lithium.impl.modules.misc.announcer.util.Language;
import me.chachoox.lithium.impl.modules.misc.announcer.util.Type;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class Announcer
extends Module {
    protected final EnumProperty<Language> language = new EnumProperty<Language>(Language.ENGLISH, new String[]{"Language", "Mode", "Type"}, "The language we will announce stuff in.");
    protected final NumberProperty<Integer> delay = new NumberProperty<Integer>(15, 1, 60, new String[]{"Delay", "delayington"}, "Delay between sending different messages.");
    protected final Property<Boolean> move = new Property<Boolean>(true, new String[]{"Move", "movement", "walk", "distance"}, "Announces the distance we have walked.");
    protected final Property<Boolean> jump = new Property<Boolean>(true, new String[]{"Jump", "jumped"}, "Announces when we have jumped.");
    protected final Property<Boolean> mine = new Property<Boolean>(true, new String[]{"Break", "Destroyed", "broken", "destroylonely"}, "Announces the block that we destroyed.");
    protected final Property<Boolean> place = new Property<Boolean>(true, new String[]{"Place", "placements"}, "Announces the block that we placed.");
    protected final Property<Boolean> eat = new Property<Boolean>(true, new String[]{"Eat", "Eaten"}, "Announces the food that we have eaten.");
    protected final Property<Boolean> greenText = new Property<Boolean>(true, new String[]{"GreenText", ">", "Autogreentext"}, "Puts > in front of you messages to make it green on some servers.");
    protected final Property<Boolean> cycle = new Property<Boolean>(true, new String[]{"Cycle", "langaugecycle", "languageswitch"}, "Cycles through all the languages.");
    protected String walkMessage;
    protected String placeMessage;
    protected String jumpMessage;
    protected String breakMessage;
    protected String eatMessage;
    protected StopWatch timer = new StopWatch();
    protected StopWatch moveTimer = new StopWatch();
    protected StopWatch jumpTimer = new StopWatch();
    protected Random random = new Random();
    protected Block brokenBlock;
    protected ItemStack placeStack;
    protected ItemStack foodStack;
    protected double speed;
    protected LinkedHashMap<Type, Float> events = new LinkedHashMap();

    public Announcer() {
        super("Announcer", new String[]{"Announcer", "anounce", "greeter", "greet"}, "How to get muted fast.", Category.MISC);
        this.offerProperties(this.language, this.delay, this.move, this.jump, this.mine, this.place, this.eat, this.greenText, this.cycle);
        this.offerListeners(new ListenerBreak(this), new ListenerPlace(this), new ListenerMotion(this), new ListenerJump(this), new ListenerEat(this), new ListenerDeath(this), new ListenerLogout(this), new ListenerUpdate(this));
    }

    @Override
    public void onEnable() {
        this.reset();
    }

    @Override
    public void onDisable() {
        this.reset();
    }

    @Override
    public void onWorldLoad() {
        this.reset();
    }

    protected void setMessages() {
        switch ((Language)((Object)this.language.getValue())) {
            case ENGLISH: {
                this.walkMessage = "I just moved {blocks} blocks thanks to SexMaster.CC!";
                this.placeMessage = "I just placed {amount} {name} thanks to SexMaster.CC!";
                this.jumpMessage = "I just jumped thanks to SexMaster.CC!";
                this.breakMessage = "I just broke {amount} {name} thanks to SexMaster.CC!";
                this.eatMessage = "I just ate {amount} {name} thanks to SexMaster.CC!";
                break;
            }
            case SPANISH: {
                this.walkMessage = "Acabo de moverme {blocks} bloques gracias a SexMaster.CC!";
                this.placeMessage = "Acabo de colocar {amount} {name} gracias a SexMaster.CC!";
                this.jumpMessage = "Acabo de saltar gracias a SexMaster.CC!";
                this.breakMessage = "Acabo de romper {amount} {name} gracias a SexMaster.CC!";
                this.eatMessage = "Acabo de comer {amount} {name} gracias a SexMaster.CC!";
                break;
            }
            case RUSSIAN: {
                this.walkMessage = "\u042f \u0442\u043e\u043b\u044c\u043a\u043e \u0447\u0442\u043e \u043f\u0435\u0440\u0435\u0435\u0445\u0430\u043b {blocks} \u0431\u043b\u0430\u0433\u043e\u0434\u0430\u0440\u044f SexMaster.CC! (\u25e3_\u25e2)";
                this.placeMessage = "\u042f \u0442\u043e\u043b\u044c\u043a\u043e \u0447\u0442\u043e \u0440\u0430\u0437\u043c\u0435\u0441\u0442\u0438\u043b {amount} {name} \u0431\u043b\u0430\u0433\u043e\u0434\u0430\u0440\u044f SexMaster.CC! (\u25e3_\u25e2)";
                this.jumpMessage = "\u042f \u043f\u0440\u043e\u0441\u0442\u043e \u043f\u0440\u044b\u0433\u043d\u0443\u043b \u0431\u043b\u0430\u0433\u043e\u0434\u0430\u0440\u044f SexMaster.CC! (\u25e3_\u25e2)";
                this.breakMessage = "\u042f \u0442\u043e\u043b\u044c\u043a\u043e \u0447\u0442\u043e \u0440\u0430\u0437\u0431\u0438\u043b {amount} {name} \u0431\u043b\u0430\u0433\u043e\u0434\u0430\u0440\u044f SexMaster.CC! (\u25e3_\u25e2) ";
                this.eatMessage = "\u044f \u0442\u043e\u043b\u044c\u043a\u043e \u0447\u0442\u043e \u0441\u044a\u0435\u043b {amount} {name} \u0431\u043b\u0430\u0433\u043e\u0434\u0430\u0440\u044f SexMaster.CC! (\u25e3_\u25e2)";
                break;
            }
            case GERMAN: {
                this.walkMessage = "Ich habe gerade {blocks} Bl\u00c3\u00b6cke verschoben, dank SexMaster.CC!";
                this.placeMessage = "Ich habe gerade {amount} {name} dank SexMaster.CC!";
                this.jumpMessage = "Ich bin gerade dank SexMaster.CC!";
                this.breakMessage = "Ich habe gerade {amount} {name} dank SexMaster.CC!";
                this.eatMessage = "Ich habe gerade {amount} {name} dank SexMaster.CC!";
                break;
            }
            case ARABIC: {
                this.walkMessage = "\u0644\u0642\u062f \u0642\u0645\u062a \u0644\u0644\u062a\u0648 \u0628\u0646\u0642\u0644 {blocks} \u0645\u0646 \u0627\u0644\u0643\u062a\u0644 \u0628\u0641\u0636\u0644 SexMaster.CC!";
                this.placeMessage = "\u0644\u0642\u062f \u0642\u062f\u0645\u062a \u0644\u0644\u062a\u0648 {amount} {name} \u0628\u0641\u0636\u0644 SexMaster.CC!";
                this.jumpMessage = "\u0644\u0642\u062f \u0642\u0641\u0632\u062a \u0644\u0644\u062a\u0648 \u0628\u0641\u0636\u0644 SexMaster.CC!";
                this.breakMessage = "\u0644\u0642\u062f \u0643\u0633\u0631\u062a {amount} {name} \u0628\u0641\u0636\u0644 SexMaster.CC!";
                this.eatMessage = "\u0644\u0642\u062f \u0623\u0643\u0644\u062a \u0644\u0644\u062a\u0648 {amount} {name} \u0628\u0641\u0636\u0644 SexMaster.CC!";
                break;
            }
            case FRENCH: {
                this.walkMessage = "Je viens de bouger {blocks} pieds gr\u00c3\u00a2ce \u00c3\u00a0 SexMaster.CC!";
                this.placeMessage = "Je viens de placer {amount} {name} gr\u00c3\u00a2ce \u00c3\u00a0 SexMaster.CC!";
                this.jumpMessage = "Je viens de sauter gr\u00c3\u00a2ce \u00c3\u00a0 SexMaster.CC!";
                this.breakMessage = "Je viens de casser {amount} {name} gr\u00c3\u00a2ce \u00c3\u00a0 SexMaster.CC!";
                this.eatMessage = "Je viens de manger {amount} {name} gr\u00c3\u00a2ce \u00c3\u00a0 SexMaster.CC!";
                break;
            }
            case AMHARIC: {
                this.walkMessage = "\u12a0\u1201\u1295 \u1260SexMaster.CC \u121d\u12ad\u1295\u12eb\u1275 {blocks} \u1270\u122b\u1218\u12f5\u12a9";
                this.placeMessage = "\u1208 SexMaster.CC \u121d\u1235\u130b\u1293\u12ec\u1295 \u12a0\u1201\u1295 \u12a0\u1235\u1240\u121d\u132b\u1208\u1201 {amount} {name}!";
                this.jumpMessage = "\u12a5\u1294 \u1265\u127b SexMaster.CC \u121d\u1235\u130b\u1293 \u12d8\u120e!";
                this.breakMessage = "\u12a0\u1201\u1295 \u1208 SexMaster.CC \u121d\u1235\u130b\u1293\u12ec\u1295 \u12a0\u1245\u122d\u1264\u12eb\u1208\u1201 {amount} {name}!";
                this.eatMessage = "\u1208 SexMaster.CC \u121d\u1235\u130b\u1293 \u12ed\u130d\u1263\u12cd {amount} {name} \u1260\u120d\u127b\u1208\u1201!";
                break;
            }
            case SLOVAK: {
                this.walkMessage = "Pr\u00e1ve som presunul {blocks} blokov v\u010faka SexMaster.CC!";
                this.placeMessage = "Pr\u00e1ve som umiestnil {amount} {name} v\u010faka SexMaster.CC!";
                this.jumpMessage = "Pr\u00e1ve som sko\u010dil v\u010faka SexMaster.CC!";
                this.breakMessage = "Pr\u00e1ve som zlomil {amount} {name} v\u010faka SexMaster.CC!";
                this.eatMessage = "Pr\u00e1ve som zjedol {amount} {name} v\u010faka SexMaster.CC!";
                break;
            }
            case SLOVENIAN: {
                this.walkMessage = "Pravkar sem se premaknil {blocks} kock zahvaljujo\u010de SexMaster.CC!";
                this.placeMessage = "Pravkar sem postavil {amount} {name} zahvaljujo\u010de SexMaster.CC!";
                this.jumpMessage = "Pravkar sem sko\u010dil zahvaljujo\u010de SexMaster.CC!";
                this.breakMessage = "Pravkar sem zlomil {amount} {name} zahvaljujo\u010de SexMaster.CC!";
                this.eatMessage = "Pravkar sem pojedel {amount} {name} zahvaljujo\u010de SexMaster.CC!";
                break;
            }
            case SWEDISH: {
                this.walkMessage = "Jag har just flyttat {blocks} fot tack vare SexMaster.CC";
                this.placeMessage = "Jag har just placerat {amount} {name} tack vare SexMaster.CC";
                this.jumpMessage = "Jag hoppade just tack vare SexMaster.CC";
                this.breakMessage = "Jag f\u00c3\u00b6rst\u00c3\u00b6rde just {amount} {name} tack vare SexMaster.CC";
                this.eatMessage = "Jag \u00c3\u00a5t just {amount} {name} tack vare SexMaster.CC";
                break;
            }
            case INDONESIAN: {
                this.walkMessage = "Saya baru saja berjalan kaki {blocks} berkat SexMaster.CC!";
                this.placeMessage = "Saya baru saja menempatkan {amount} {name} berkat SexMaster.CC!";
                this.jumpMessage = "Saya baru saja melompat berkat SexMaster.CC!";
                this.breakMessage = "Saya baru saja menghancurkan {amount} {name} berkat SexMaster.CC!";
                this.eatMessage = "Saya baru saja makan {amount} {name} berkat SexMaster.CC!";
                break;
            }
            case FINNISH: {
                this.walkMessage = "K\u00c3\u00a4velin juuri {blocks} jalat ansiosta SexMaster.CC.";
                this.placeMessage = "Sijoitin juuri {amount} {name} kiitos SexMaster.CC.";
                this.jumpMessage = "Hypp\u00c3\u00a4sin juuri, kiitos SexMaster.CC";
                this.breakMessage = "Tuhosin juuri {amount} {name} kiitos SexMaster.CC.";
                this.eatMessage = "S\u00c3\u00b6in juuri {amount} {name} kiitos SexMaster.CC.";
                break;
            }
            case ESTONIAN: {
                this.walkMessage = "Ma lihtsalt k\u00c3\u00b5ndisin {blocks} jalad t\u00c3\u00a4nu SexMaster.CC!";
                this.placeMessage = "Ma just paigutasin {amount} {name} t\u00c3\u00a4nu SexMaster.CC!";
                this.jumpMessage = "Ma lihtsalt h\u00c3\u00bcppasin, t\u00c3\u00a4nu SexMaster.CC!";
                this.breakMessage = "Ma just murdsin {amount} {name} t\u00c3\u00a4nu SexMaster.CC!";
                this.eatMessage = "Ma just s\u00c3\u00b5in {amount} {name} t\u00c3\u00a4nu SexMaster.CC!";
                break;
            }
            case NORWEGIAN: {
                this.walkMessage = "Jeg gikk nettopp {blocks} f\u00c3\u00b8tter takket v\u00c3\u00a6re SexMaster.CC";
                this.placeMessage = "Jeg har nettopp plassert {amount} {name} Takk til SexMaster.CC";
                this.jumpMessage = "Jeg bare hoppet takket v\u00c3\u00a6re SexMaster.CC";
                this.breakMessage = "Jeg \u00c3\u00b8dela nettopp {amount} {name} Takket v\u00c3\u00a6re SexMaster.CC";
                this.eatMessage = "Jeg spiste nettopp {amount} {name} Takket v\u00c3\u00a6re SexMaster.CC";
                break;
            }
            case ROMAN: {
                this.walkMessage = "Tocmai am mers {blocks} picioare datorit\u00c4\u0192 SexMaster.CC";
                this.placeMessage = "Tocmai am plasat {amount} {name} Datorit\u00c4\u0192 SexMaster.CC";
                this.jumpMessage = "Tocmai am s\u00c4\u0192rit, mul\u00c8\u203aumit\u00c4\u0192 SexMaster.CC";
                this.breakMessage = "Tocmai am distrus {amount} {name} Mul\u00c8\u203aumit\u00c4\u0192 lui SexMaster.CC";
                this.eatMessage = "Tocmai am m\u00c3\u00a2ncat {amount} {name} Mul\u00c8\u203aumit\u00c4\u0192 lui SexMaster.CC";
                break;
            }
            case TURK: {
                this.walkMessage = "SexMaster.CC sayesinde {blocks} metre y\u00c3\u00bcr\u00c3\u00bcd\u00c3\u00bcm";
                this.placeMessage = "Az \u00c3\u00b6nce SexMaster.CC sayesinde {amount} {name} yerle\u00c5\u0178tirdim.";
                this.jumpMessage = "SexMaster.CC sayesinde atlad\u00c4\u00b1m.";
                this.breakMessage = "Az \u00c3\u00b6nce sexMaster.CC sayesinde {amount} {name}'i yok ettim";
                this.eatMessage = "Az \u00c3\u00b6nce SexMaster.CC sayesinde {amount} {name} yedim.";
                break;
            }
            case POLISH: {
                this.walkMessage = "W\u00c5\u201aa\u00c5\u203anie przeszed\u00c5\u201aem {blocks} metr\u00c3\u00b3w dzi\u00c4\u2122ki SexMaster.CC";
                this.placeMessage = "W\u00c5\u201aa\u00c5\u203anie umie\u00c5\u203aci\u00c5\u201aem {amount} {name} Dzi\u00c4\u2122ki SexMaster.CC";
                this.jumpMessage = "W\u00c5\u201aa\u00c5\u203anie skoczy\u00c5\u201aem, dzi\u00c4\u2122ki SexMaster.CC";
                this.breakMessage = "W\u00c5\u201aa\u00c5\u203anie zniszczy\u00c5\u201aem {amount} {name} Dzi\u00c4\u2122ki SexMaster.CC";
                this.eatMessage = "Zjad\u00c5\u201aem w\u00c5\u201aa\u00c5\u203anie {amount} {name} Dzi\u00c4\u2122ki SexMaster.CC";
                break;
            }
            case HUNGARIAN: {
                this.walkMessage = "\u00c3\u2030n csak s\u00c3\u00a9t\u00c3\u00a1ltam {blocks} l\u00c3\u00a1bak k\u00c3\u00b6sz\u00c3\u00b6nhet\u00c5\u2018en SexMaster.CC";
                this.placeMessage = "Most helyeztem el {amount} {name} K\u00c3\u00b6sz\u00c3\u00b6n\u00c3\u00b6m a SexMaster.CC.";
                this.jumpMessage = "\u00c3\u2030n csak ugrottam, K\u00c3\u00b6sz\u00c3\u00b6n\u00c3\u00b6m, hogy sexMaster.CC";
                this.breakMessage = "\u00c3\u2030pp most t\u00c3\u00b6rtem {amount} {name} K\u00c3\u00b6sz\u00c3\u00b6n\u00c3\u00b6m SexMaster.CC";
                this.eatMessage = "\u00c3\u2030pp most ettem {amount} {name} K\u00c3\u00b6sz\u00c3\u00b6n\u00c3\u00b6m SexMaster.CC";
                break;
            }
            case JAPANESE: {
                this.walkMessage = "SexMaster.CC \u306e\u304a\u304b\u3052\u3067\u3001{blocks} \u306e\u8db3\u3092\u52d5\u304b\u3057\u305f\u3060\u3051\u3067\u3059\u3002 \uff3c\uff08\uff3e\u25cb\uff3e\uff09\u4eba\uff08\uff3e\u25cb\uff3e\uff09\uff0f";
                this.placeMessage = "SexMaster.CC \u306e\u304a\u304b\u3052\u3067 {amount} {name} \u3092\u914d\u7f6e\u3057\u305f\u3068\u3053\u308d\u3067\u3059\u3002 \u30fd(\uff9f\uff70\uff9f*\u30fd)\u30fd(*\uff9f\uff70\uff9f*)\uff89(\uff89*\uff9f\uff70\uff9f)\uff89";
                this.jumpMessage = "SexMaster.CC \u306e\u304a\u304b\u3052\u3067\u30b8\u30e3\u30f3\u30d7\u3067\u304d\u307e\u3057\u305f\u3002 \u30fd(\u00c2\u00b4\u25bd\uff40)\u30ce";
                this.breakMessage = "SexMaster.CC \u306e\u304a\u304b\u3052\u3067 {amount} {name} \u3092\u7834\u58ca\u3057\u305f\u3068\u3053\u308d\u3067\u3059\u3002 (\uff61\u273f\u203f\u273f\uff61)";
                this.eatMessage = "SexMaster.CC \u306e\u304a\u304b\u3052\u3067 {amount} {name} \u3092\u98df\u3079\u305f\u3068\u3053\u308d\u3067\u3059\u3002 \uff08\u30df\uffe3\u30fc\uffe3\u30df\uff09";
                break;
            }
            case CHINESE: {
                this.walkMessage = "\u6211\u521a\u642c\u4e86 {blocks} \u8c22\u8c22 SexMaster.CC\uff01 (\u3065\uff61\u25d5\u203f\u203f\u25d5\uff61)\u3065";
                this.placeMessage = "\u6211\u521a\u521a\u653e\u7f6e\u4e86 {amount} {name} \u8c22\u8c22 SexMaster.CC\uff01 (\u25cf\u03c9\uff40\u25cf)";
                this.jumpMessage = "\u6211\u521a\u521a\u8df3\u4e86 \u8c22\u8c22 SexMaster.CC\uff01 \u2267\u25e1\u2266";
                this.breakMessage = "\u6211\u521a\u521a\u6253\u7834\u4e86 {amount} {name} \u8c22\u8c22 SexMaster.CC\uff01 (\u273f\u25e0\u203f\u25e0)";
                this.eatMessage = "\u6211\u521a\u5403\u4e86 {amount} {name} \u8c22\u8c22 SexMaster.CC\uff01 (\uff89\u25d5\u30ee\u25d5) \uff89*:\uff65\uff9f\u2727";
                break;
            }
            case LOLCAT: {
                this.walkMessage = "I JUS MOVD {blocks} BLOCKZ THX 2 SEKSMASTR.CC!";
                this.placeMessage = "I JUS PLACD {amount} {name} THX 2 SEKSMASTR.CC!";
                this.jumpMessage = "I JUS JUMPD THX 2 SEKSMASTR.CC!";
                this.breakMessage = "I JUS BROKE {amount} {name} THX 2 SEKSMASTR.CC!";
                this.eatMessage = "I JUS EATD {amount} {name} THX 2 SEKSMASTR.CC!";
                break;
            }
            case PRAYER: {
                this.walkMessage = "Inshallah my brothers I just walked {blocks} feet thanks to SexMaster.CC";
                this.placeMessage = "Alhamdulillah I just placed {amount} {name} thanks to SexMaster.CC";
                this.jumpMessage = "Bismillah I just jumped thanks to SexMaster.CC";
                this.eatMessage = "Mashallah I just ate {amount} {name} thanks to SexMaster.CC";
                this.breakMessage = "Subhanallah I just destroyed {amount} {name} thanks to SexMaster.CC";
                break;
            }
            case PIRATE: {
                this.walkMessage = "I jus' walked {blocks} feet thanks t' SexMaster.CC!";
                this.placeMessage = "I jus' placed {amount} {name} thanks t' SexMaster.CC!";
                this.jumpMessage = "I jus' jumped thanks t' SexMaster.CC";
                this.eatMessage = "I jus' ate {amount} {name} thanks t' SexMaster.CC";
                this.breakMessage = "I jus' destroyed {amount} {name} thanks t' SexMaster.CC";
                break;
            }
            case NFT: {
                this.walkMessage = "I just walked {blocks} feet follow me on rumble https://rumble.com/BitCrypto89";
                this.jumpMessage = "I just jumped follow me on rumble https://rumble.com/BitCrypto89";
                this.placeMessage = "I just placed {amount} {name} follow me on rumble https://rumble.com/BitCrypto89";
                this.eatMessage = "I just ate {amount} {name} follow me on rumble https://rumble.com/BitCrypto89";
                this.breakMessage = "I just destroyed {amount} {name} follow me on rumble https://rumble.com/BitCrypto89";
                break;
            }
            case HEBREW: {
                this.walkMessage = "\u05d6\u05d4 \u05e2\u05ea\u05d4 \u05d4\u05e2\u05d1\u05e8\u05ea\u05d9 {blocks} \u05d1\u05dc\u05d5\u05e7\u05d9\u05dd \u05d4\u05d5\u05d3\u05d5\u05ea \u05dc- SexMaster.CC!";
                this.placeMessage = "\u05d4\u05e8\u05d2\u05e2 \u05d4\u05e0\u05d7\u05ea\u05d9 {amount} {name} \u05d1\u05d6\u05db\u05d5\u05ea SexMaster.CC!";
                this.jumpMessage = "\u05e4\u05e9\u05d5\u05d8 \u05e7\u05e4\u05e6\u05ea\u05d9 \u05d1\u05d6\u05db\u05d5\u05ea SexMaster.CC!";
                this.breakMessage = "\u05e4\u05e9\u05d5\u05d8 \u05e9\u05d1\u05e8\u05ea\u05d9 \u05d0\u05ea {amount} {name} \u05d1\u05d6\u05db\u05d5\u05ea SexMaster.CC!";
                this.eatMessage = "\u05d4\u05e8\u05d2\u05e2 \u05d0\u05db\u05dc\u05ea\u05d9 {amount} {name} \u05d1\u05d6\u05db\u05d5\u05ea SexMaster.CC!";
                break;
            }
            case BANGLA: {
                this.walkMessage = "\u0986\u09ae\u09bf \u098f\u0987\u09ae\u09be\u09a4\u09cd\u09b0 SexMaster.CC \u0995\u09c7 \u09a7\u09a8\u09cd\u09af\u09ac\u09be\u09a6 {blocks} \u09ac\u09cd\u09b2\u0995\u0997\u09c1\u09b2\u09bf \u09b8\u09b0\u09bf\u09af\u09bc\u09c7\u099b\u09bf!";
                this.placeMessage = "\u0986\u09ae\u09bf \u098f\u0987\u09ae\u09be\u09a4\u09cd\u09b0 SexMaster.CC \u0995\u09c7 \u09a7\u09a8\u09cd\u09af\u09ac\u09be\u09a6 {amount} {name} \u09b0\u09be\u0996\u09b2\u09be\u09ae!";
                this.jumpMessage = "\u0986\u09ae\u09bf \u09b6\u09c1\u09a7\u09c1 SexMaster.CC \u09a7\u09a8\u09cd\u09af\u09ac\u09be\u09a6 \u09b2\u09be\u09ab!";
                this.breakMessage = "\u0986\u09ae\u09bf \u098f\u0987\u09ae\u09be\u09a4\u09cd\u09b0 SexMaster.CC \u0995\u09c7 \u09a7\u09a8\u09cd\u09af\u09ac\u09be\u09a6 {amount} {name} \u09ad\u09c7\u0999\u09c7\u099b\u09bf!";
                this.eatMessage = "\u0986\u09ae\u09bf \u098f\u0987\u09ae\u09be\u09a4\u09cd\u09b0 SexMaster.CC \u0995\u09c7 \u09a7\u09a8\u09cd\u09af\u09ac\u09be\u09a6 {amount} {name} \u0996\u09c7\u09af\u09bc\u09c7\u099b\u09bf!";
                break;
            }
            case KOREAN: {
                this.walkMessage = "SexMaster.CC \ub355\ubd84\uc5d0 \ubc29\uae08 {blocks} \ube14\ub85d\uc744 \uc62e\uacbc\uc2b5\ub2c8\ub2e4!";
                this.placeMessage = "SexMaster.CC \ub355\ubd84\uc5d0 \ubc29\uae08 {amount} {name}\uc744(\ub97c) \ubc30\uce58\ud588\uc2b5\ub2c8\ub2e4!";
                this.jumpMessage = "SexMaster.CC \ub355\ubd84\uc5d0 \ub6f0\uc5b4\ub0b4\ub838\uc2b5\ub2c8\ub2e4!";
                this.breakMessage = "SexMaster.CC \ub355\ubd84\uc5d0 \ubc29\uae08 {amount} {name}\uc744(\ub97c) \uae68\ub728\ub838\uc2b5\ub2c8\ub2e4!";
                this.eatMessage = "\ubc29\uae08 SexMaster.CC \ub355\ubd84\uc5d0 {amount} {name}\uc744(\ub97c) \uba39\uc5c8\uc2b5\ub2c8\ub2e4!";
                break;
            }
            case ALBANIA: {
                this.walkMessage = "Sapo zhvendosa {blloqe} blloqe fal\u00eb SexMaster.CC!";
                this.placeMessage = "Sapo vendosa {amount} {name} fal\u00eb SexMaster.CC!";
                this.jumpMessage = "Sapo u hodha fal\u00eb SexMaster.CC!";
                this.breakMessage = "Sapo kam thyer {samount} {name} fal\u00eb SexMaster.CC!";
                this.eatMessage = "Sapo h\u00ebngra {samount} {name} fal\u00eb SexMaster.CC";
                break;
            }
            case HAWAIIAN: {
                this.walkMessage = "Ua ho\u02bbone\u02bbe au i n\u0101 poloka {blocks} mahalo i\u0101 SexMaster.CC!";
                this.placeMessage = "Ua kau au i {ka nui} {name} mahalo i\u0101 SexMaster.CC!";
                this.jumpMessage = "Ua lele wale au i ka mahalo i\u0101 SexMaster.CC!";
                this.breakMessage = "Ua uhaki au i {ka nui} {name} mahalo i\u0101 SexMaster.CC!";
                this.eatMessage = "Ua \u02bbai wale au i {ka nui} {name} mahalo i\u0101 SexMaster.CC!";
                break;
            }
            case ITALY: {
                this.walkMessage = "Ho appena spostato {blocks} blocchi grazie a SexMaster.CC!";
                this.placeMessage = "Ho appena piazzato {amount} {name} grazie a SexMaster.CC!";
                this.jumpMessage = "Sono appena saltato grazie a SexMaster.CC!";
                this.breakMessage = "Ho appena rotto {amount} {name} grazie a SexMaster.CC!";
                this.eatMessage = "Ho appena mangiato {amount} {name} grazie a SexMaster.CC!";
                break;
            }
            case LATIN: {
                this.walkMessage = "Modo gratias ago SexMaster.CC ad caudices {blocks} commotus sum!";
                this.placeMessage = "Modo posui {amount} {name} gratias SexMaster.CC!";
                this.jumpMessage = "Modo gratias SexMaster.CC . laetabundus";
                this.breakMessage = "Modo fregi {amount} {name} gratias SexMaster.CC!";
                this.eatMessage = "Modo comedi {amount} {name} gratias SexMaster.CC!";
                break;
            }
            case GANDA: {
                this.walkMessage = "Naakasenguka {blocks} blocks olw'okuba SexMaster.CC!";
                this.placeMessage = "Naakateeka {amount} {name} olw'okusiima SexMaster.CC!";
                this.jumpMessage = "Nze naakabuuka nga nneebaza SexMaster.CC!";
                this.breakMessage = "Naakamenya {amount} {name} olw'okusiima SexMaster.CC!";
                this.eatMessage = "Naakalya {amount} {name} olw'okusiima SexMaster.CC!";
                break;
            }
            case IRISH: {
                this.walkMessage = "Bhog m\u00e9 {blocks} bloc a bhu\u00edochas le SexMaster.CC!";
                this.placeMessage = "Chuir m\u00e9 {amount} {name} a bhu\u00edochas le SexMaster.CC!";
                this.jumpMessage = "L\u00e9im m\u00e9 d\u00edreach tar \u00e9is bu\u00edochas le SexMaster.CC!";
                this.breakMessage = "Bhris m\u00e9 d\u00edreach {amount} {name} a bhu\u00edochas sin do SexMaster.CC!";
                this.eatMessage = "N\u00edor ith m\u00e9 ach {amount} {name} bu\u00edochas le SexMaster.CC!";
                break;
            }
            case ICELANDIC: {
                this.walkMessage = "\u00c9g flutti bara {blocks} blokkir \u00fe\u00f6kk s\u00e9 SexMaster.CC!";
                this.placeMessage = "\u00c9g setti bara {amount} {name} \u00fe\u00f6kk s\u00e9 SexMaster.CC!";
                this.jumpMessage = "\u00c9g hoppa\u00f0i bara \u00fe\u00f6kk s\u00e9 SexMaster.CC!";
                this.breakMessage = "\u00c9g braut bara {amount} {name} \u00fe\u00f6kk s\u00e9 SexMaster.CC!";
                this.eatMessage = "\u00c9g bor\u00f0a\u00f0i bara {amount} {name} \u00fe\u00f6kk s\u00e9 SexMaster.CC!";
                break;
            }
            case WELSH: {
                this.walkMessage = "Dwi newydd symud {blocks} blociau diolch i SexMaster.CC!";
                this.placeMessage = "Newydd osod {amount} {name} diolch i SexMaster.CC!";
                this.jumpMessage = "Fi jyst neidio diolch i SexMaster.CC!";
                this.breakMessage = "Newydd dorri {amount} {name} diolch i SexMaster.CC!";
                this.eatMessage = "Newydd fwyta {amount} {name} diolch i SexMaster.CC!";
                break;
            }
            case SOMALI: {
                this.walkMessage = "Hadda waxaan raray {blocks} baloogyada mahadnaqa SexMaster.CC!";
                this.placeMessage = "Hadda waxaan dhigay {amount} {name} mahadsanid SexMaster.CC!";
                this.jumpMessage = "Kaliya waxaan booday mahadsanid SexMaster.CC!";
                this.breakMessage = "Hadda waxaan jabiyay {amount} {name} mahadsanid SexMaster.CC!";
                this.eatMessage = "Hadda waxaan cunay {amount} {name} mahadsanid SexMaster.CC!";
                break;
            }
            case PERSIAN: {
                this.walkMessage = "\u0645\u0646 \u0641\u0642\u0637 \u0628\u0647 \u0644\u0637\u0641 SexMaster.CC \u0628\u0644\u0648\u06a9 \u0647\u0627\u06cc {blocks} \u0631\u0627 \u062c\u0627\u0628\u062c\u0627 \u06a9\u0631\u062f\u0645!";
                this.placeMessage = "\u0645\u0646 \u0641\u0642\u0637 \u0628\u0647 \u0644\u0637\u0641 SexMaster.CC {amount} {name} \u0631\u0627 \u0642\u0631\u0627\u0631 \u062f\u0627\u062f\u0645!";
                this.jumpMessage = "\u0645\u0646 \u0641\u0642\u0637 \u0628\u0647 \u0644\u0637\u0641 SexMaster.CC \u067e\u0631\u06cc\u062f\u0645!";
                this.breakMessage = "\u0645\u0646 \u0641\u0642\u0637 \u0628\u0647 \u0644\u0637\u0641 SexMaster.CC {amount} {name} \u0631\u0627 \u0634\u06a9\u0633\u062a\u0645!";
                this.eatMessage = "\u0645\u0646 \u0641\u0642\u0637 \u0628\u0647 \u0644\u0637\u0641 SexMaster.CC {amount} {name} \u062e\u0648\u0631\u062f\u0645!";
                break;
            }
            case SERBIAN: {
                this.walkMessage = "\u0423\u043f\u0440\u0430\u0432\u043e \u0441\u0430\u043c \u043f\u043e\u043c\u0435\u0440\u0438\u043e {blocks} \u0431\u043b\u043e\u043a\u043e\u0432\u0430 \u0437\u0430\u0445\u0432\u0430\u0459\u0443\u0458\u0443\u045b\u0438 SexMaster.CC!";
                this.placeMessage = "\u0423\u043f\u0440\u0430\u0432\u043e \u0441\u0430\u043c \u043f\u043e\u0441\u0442\u0430\u0432\u0438\u043e {amount} {name} \u0437\u0430\u0445\u0432\u0430\u0459\u0443\u0458\u0443\u045b\u0438 SexMaster.CC!";
                this.jumpMessage = "\u0423\u043f\u0440\u0430\u0432\u043e \u0441\u0430\u043c \u0441\u043a\u043e\u0447\u0438\u043e \u0437\u0430\u0445\u0432\u0430\u0459\u0443\u0458\u0443\u045b\u0438 SexMaster.CC!";
                this.breakMessage = "\u0423\u043f\u0440\u0430\u0432\u043e \u0441\u0430\u043c \u0441\u043b\u043e\u043c\u0438\u043e {amount} {name} \u0437\u0430\u0445\u0432\u0430\u0459\u0443\u0458\u0443\u045b\u0438 SexMaster.CC!";
                this.eatMessage = "\u0423\u043f\u0440\u0430\u0432\u043e \u0441\u0430\u043c \u043f\u043e\u0458\u0435\u043e {amount} {name} \u0437\u0430\u0445\u0432\u0430\u0459\u0443\u0458\u0443\u045b\u0438 SexMaster.CC!";
                break;
            }
            case UKRAINE: {
                this.walkMessage = "\u042f \u0449\u043e\u0439\u043d\u043e \u043f\u0435\u0440\u0435\u043c\u0456\u0441\u0442\u0438\u0432 {blocks} \u0431\u043b\u043e\u043a\u0456\u0432 \u0437\u0430\u0432\u0434\u044f\u043a\u0438 SexMaster.CC!";
                this.placeMessage = "\u042f \u0449\u043e\u0439\u043d\u043e \u0440\u043e\u0437\u043c\u0456\u0441\u0442\u0438\u0432 {amount} {name} \u0437\u0430\u0432\u0434\u044f\u043a\u0438 SexMaster.CC!";
                this.jumpMessage = "\u042f \u043f\u0440\u043e\u0441\u0442\u043e \u043f\u0456\u0434\u0441\u043a\u043e\u0447\u0438\u0432 \u0437\u0430\u0432\u0434\u044f\u043a\u0438 SexMaster.CC!";
                this.breakMessage = "\u042f \u0449\u043e\u0439\u043d\u043e \u0437\u0456\u0440\u0432\u0430\u0432 {amount} {name} \u0437\u0430\u0432\u0434\u044f\u043a\u0438 SexMaster.CC!";
                this.eatMessage = "\u042f \u0449\u043e\u0439\u043d\u043e \u0437\u2019\u0457\u0432 {amount} {name} \u0437\u0430\u0432\u0434\u044f\u043a\u0438 SexMaster.CC!";
                break;
            }
            case CROATIAN: {
                this.walkMessage = "Upravo sam premjestio {blocks} blokova zahvaljuju\u0107i SexMaster.CC!";
                this.placeMessage = "Upravo sam stavio {amount} {name} zahvaljuju\u0107i SexMaster.CC!";
                this.jumpMessage = "Upravo sam sko\u010dio zahvaljuju\u0107i SexMaster.CC!";
                this.breakMessage = "Upravo sam razbio {amount} {name} zahvaljuju\u0107i SexMaster.CC!";
                this.eatMessage = "Upravo sam pojeo {amount} {name} zahvaljuju\u0107i SexMaster.CC!";
                break;
            }
            case PORTUGUESE: {
                this.walkMessage = "Acabei de mover {blocks} blocos gra\u00e7as ao SexMaster.CC!";
                this.placeMessage = "Acabei de colocar {amount} {name} gra\u00e7as a SexMaster.CC!";
                this.jumpMessage = "Eu simplesmente pulei gra\u00e7as ao SexMaster.CC!";
                this.breakMessage = "Acabei de quebrar {amount} {name} gra\u00e7as a SexMaster.CC!";
                this.eatMessage = "Acabei de comer {amount} {name} gra\u00e7as ao SexMaster.CC!";
                break;
            }
            case POLLOSXD: {
                this.walkMessage = "kakakaka {blocks} blocks gwkk SexMaster.CC!";
                this.placeMessage = "Kkkkkkvkkk {amount} {name} gwkk SexMaster.CC!";
                this.jumpMessage = "kakakxcaxakxakxakxkaxkaxkakxkaxka jump gwkk SexMaster.CC!";
                this.breakMessage = "LKpMgiYjlrJf7n {amount} {name} gwkk SexMaster.CC!";
                this.eatMessage = "\u00a5^^\u00b0\u00b0\u00b0\u00b0\u00b0\u00b0 {amount} {name} gwkk SexMaster.CC!";
            }
        }
    }

    protected void reset() {
        this.speed = 0.0;
        this.foodStack = null;
        this.placeStack = null;
        this.brokenBlock = null;
        this.moveTimer.reset();
        this.jumpTimer.reset();
        this.timer.reset();
    }

    protected void addEvent(Type type) {
        if (this.events.containsKey((Object)type)) {
            this.events.put(type, Float.valueOf(this.events.get((Object)type).floatValue() + 1.0f));
        } else {
            this.events.put(type, Float.valueOf(1.0f));
        }
    }

    protected String getMessage(Type type, float count) {
        switch (type) {
            case BREAK: {
                return this.breakMessage.replace("{amount}", count + "").replace("{name}", this.brokenBlock.getLocalizedName()).replace(".0", "");
            }
            case PLACE: {
                return this.placeMessage.replace("{amount}", count + "").replace("{name}", this.placeStack.getDisplayName()).replace(".0", "");
            }
            case EAT: {
                return this.eatMessage.replace("{amount}", count + "").replace("{name}", this.foodStack.getDisplayName()).replace(".0", "");
            }
            case JUMP: {
                return this.jumpMessage;
            }
            case WALK: {
                return this.walkMessage.replace("{blocks}", MathUtil.round(count, 2) + "");
            }
        }
        return "hello nigga";
    }
}

