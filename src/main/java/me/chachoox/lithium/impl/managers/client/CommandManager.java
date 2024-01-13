/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.item.Item
 *  net.minecraft.network.play.client.CPacketChatMessage
 *  net.minecraft.util.ResourceLocation
 */
package me.chachoox.lithium.impl.managers.client;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import me.chachoox.lithium.api.event.bus.Listener;
import me.chachoox.lithium.api.event.bus.SubscriberImpl;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.impl.command.Command;
import me.chachoox.lithium.impl.command.commands.friend.AddCommand;
import me.chachoox.lithium.impl.command.commands.friend.FriendCommand;
import me.chachoox.lithium.impl.command.commands.friend.FriendsCommand;
import me.chachoox.lithium.impl.command.commands.friend.RemoveCommand;
import me.chachoox.lithium.impl.command.commands.helper.FakePlayerCommand;
import me.chachoox.lithium.impl.command.commands.helper.LogoutSpotsCommand;
import me.chachoox.lithium.impl.command.commands.helper.QuiverCommand;
import me.chachoox.lithium.impl.command.commands.helper.ResetPopsCommand;
import me.chachoox.lithium.impl.command.commands.helper.SpammerFileCommand;
import me.chachoox.lithium.impl.command.commands.helper.VelocityPercentageCommand;
import me.chachoox.lithium.impl.command.commands.misc.CrashCommand;
import me.chachoox.lithium.impl.command.commands.misc.FolderCommand;
import me.chachoox.lithium.impl.command.commands.misc.SessionCommand;
import me.chachoox.lithium.impl.command.commands.misc.TutorialCommand;
import me.chachoox.lithium.impl.command.commands.misc.chat.ChatFilterCommand;
import me.chachoox.lithium.impl.command.commands.misc.chat.CoordsCommand;
import me.chachoox.lithium.impl.command.commands.misc.chat.DeathCoordsCommand;
import me.chachoox.lithium.impl.command.commands.misc.chat.SexCommand;
import me.chachoox.lithium.impl.command.commands.misc.chat.ShrugCommand;
import me.chachoox.lithium.impl.command.commands.misc.chat.ignore.IgnoreCommand;
import me.chachoox.lithium.impl.command.commands.misc.chat.ignore.UnIgnoreCommand;
import me.chachoox.lithium.impl.command.commands.misc.connect.ConnectCommand;
import me.chachoox.lithium.impl.command.commands.misc.connect.DisconnectCommand;
import me.chachoox.lithium.impl.command.commands.misc.list.CommandsCommand;
import me.chachoox.lithium.impl.command.commands.misc.list.ModuleCommand;
import me.chachoox.lithium.impl.command.commands.misc.refresh.ResourceRefreshCommand;
import me.chachoox.lithium.impl.command.commands.misc.refresh.SoundRefreshCommand;
import me.chachoox.lithium.impl.command.commands.misc.search.CraftyCommand;
import me.chachoox.lithium.impl.command.commands.misc.search.LabyCommand;
import me.chachoox.lithium.impl.command.commands.misc.search.NameMCCommand;
import me.chachoox.lithium.impl.command.commands.misc.search.SearchCommand;
import me.chachoox.lithium.impl.command.commands.modules.BindCommand;
import me.chachoox.lithium.impl.command.commands.modules.ConfigCommand;
import me.chachoox.lithium.impl.command.commands.modules.DrawnAllCommand;
import me.chachoox.lithium.impl.command.commands.modules.DrawnCommand;
import me.chachoox.lithium.impl.command.commands.modules.PrefixCommand;
import me.chachoox.lithium.impl.command.commands.modules.ToggleCommand;
import me.chachoox.lithium.impl.command.commands.player.clip.HClipCommand;
import me.chachoox.lithium.impl.command.commands.player.clip.HitboxDesyncCommand;
import me.chachoox.lithium.impl.command.commands.player.clip.VClipCommand;
import me.chachoox.lithium.impl.command.commands.player.rotation.PitchCommand;
import me.chachoox.lithium.impl.command.commands.player.rotation.YawCommand;
import me.chachoox.lithium.impl.command.commands.player.stack.EnchantCommand;
import me.chachoox.lithium.impl.command.commands.player.stack.StackInfoCommand;
import me.chachoox.lithium.impl.command.commands.values.ResetCommand;
import me.chachoox.lithium.impl.command.commands.values.SubscriberCommand;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.util.ResourceLocation;

public class CommandManager
extends SubscriberImpl {
    private final List<Command> commands = new ArrayList<Command>();
    private String prefix = ",";

    public CommandManager() {
        this.listeners.add(new Listener<PacketEvent.Send<CPacketChatMessage>>(PacketEvent.Send.class, Integer.MIN_VALUE, CPacketChatMessage.class){

            /*
             * Exception decompiling
             */
            @Override
            public void call(PacketEvent.Send<CPacketChatMessage> event) {
                /*
                 * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
                 * 
                 * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [1[TRYBLOCK], 73[CATCHBLOCK]], but top level block is 4[TRYBLOCK]
                 *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
                 *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
                 *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
                 *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
                 *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
                 *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
                 *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
                 *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
                 *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
                 *     at org.benf.cfr.reader.entities.ClassFile.analyseInnerClassesPass1(ClassFile.java:923)
                 *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1035)
                 *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
                 *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
                 *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
                 *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
                 *     at org.benf.cfr.reader.Main.main(Main.java:54)
                 *     at com.github.wcaleniekubaa.fmd.Decompiler$1.invoke(Decompiler.kt:10)
                 *     at com.github.wcaleniekubaa.fmd.Decompiler$1.invoke(Decompiler.kt:9)
                 *     at com.github.wcaleniekubaa.fmd.FMDMain.process(FMDMain.kt:111)
                 *     at com.github.wcaleniekubaa.fmd.ui.FMDUI.lambda-1$lambda-0(FMDUI.kt:37)
                 *     at java.base/java.lang.Thread.run(Thread.java:1583)
                 */
                throw new IllegalStateException("Decompilation failed");
            }

            private static /* synthetic */ void lambda$call$3(StringJoiner joiner, Block block) {
                joiner.add(((ResourceLocation)Block.REGISTRY.getNameForObject((Object)block)).toString().replace("minecraft:", ""));
            }

            private static /* synthetic */ void lambda$call$2(StringJoiner joiner, Item item) {
                joiner.add(((ResourceLocation)Objects.requireNonNull(Item.REGISTRY.getNameForObject((Object)item))).toString().replace("minecraft:", ""));
            }

            private static /* synthetic */ void lambda$call$1(StringJoiner joiner, Block block) {
                joiner.add(((ResourceLocation)Block.REGISTRY.getNameForObject((Object)block)).toString().replace("minecraft:", ""));
            }

            private static /* synthetic */ void lambda$call$0(StringJoiner joiner, Item item) {
                joiner.add(((ResourceLocation)Objects.requireNonNull(Item.REGISTRY.getNameForObject((Object)item))).toString().replace("minecraft:", ""));
            }
        });
    }

    public void init() {
        this.register(new BindCommand(), new ConfigCommand(), new DrawnCommand(), new PrefixCommand(), new ToggleCommand(), new CoordsCommand(), new CrashCommand(), new CommandsCommand(), new SexCommand(), new TutorialCommand(), new FolderCommand(), new ShrugCommand(), new ResourceRefreshCommand(), new ModuleCommand(), new SoundRefreshCommand(), new DisconnectCommand(), new SearchCommand(), new SessionCommand(), new DeathCoordsCommand(), new CraftyCommand(), new LabyCommand(), new NameMCCommand(), new IgnoreCommand(), new UnIgnoreCommand(), new ChatFilterCommand(), new ConnectCommand(), new DrawnAllCommand(), new LogoutSpotsCommand(), new ResetPopsCommand(), new FakePlayerCommand(), new QuiverCommand(), new VelocityPercentageCommand(), new SpammerFileCommand(), new YawCommand(), new PitchCommand(), new HitboxDesyncCommand(), new HClipCommand(), new VClipCommand(), new EnchantCommand(), new StackInfoCommand(), new SubscriberCommand(), new ResetCommand(), new FriendCommand(), new FriendsCommand(), new AddCommand(), new RemoveCommand());
    }

    public void register(Command ... command) {
        Collections.addAll(this.commands, command);
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    private void currentMessage(Module module, Property<?> property, Object value) {
        Logger.getLogger().log("\u00a73" + module.getLabel() + "\u00a7d" + " property " + "\u00a7b" + property.getLabel() + "\u00a7d" + " current value is " + "\u00a7a" + value);
    }

    private void setColorMessage(Module module, Property<?> property, String color, Object value) {
        Logger.getLogger().log("\u00a73" + module.getLabel() + "\u00a7d" + " property " + "\u00a7b" + property.getLabel() + "\u00a7d" + " " + color + " was set to " + "\u00a7a" + value);
    }

    private void setMessage(Module module, Property<?> property, Object value) {
        Logger.getLogger().log("\u00a73" + module.getLabel() + "\u00a7d" + " property " + "\u00a7b" + property.getLabel() + "\u00a7d" + " was set to " + "\u00a7a" + value);
    }

    private String readClipboard() {
        try {
            return (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
        }
        catch (UnsupportedFlavorException | IOException exception) {
            return null;
        }
    }

    private String getFixedValue(Enum<?> enumValue) {
        return enumValue.name().charAt(0) + enumValue.name().toLowerCase().replace(Character.toString(enumValue.name().charAt(0)).toLowerCase(), "");
    }

    public Collection<Command> getCommands() {
        return this.commands;
    }

    static /* synthetic */ String access$000(CommandManager x0) {
        return x0.prefix;
    }

    static /* synthetic */ List access$100(CommandManager x0) {
        return x0.commands;
    }

    static /* synthetic */ void access$200(CommandManager x0, Module x1, Property x2, Object x3) {
        x0.setMessage(x1, x2, x3);
    }

    static /* synthetic */ void access$300(CommandManager x0, Module x1, Property x2, Object x3) {
        x0.currentMessage(x1, x2, x3);
    }

    static /* synthetic */ String access$400(CommandManager x0, Enum x1) {
        return x0.getFixedValue(x1);
    }

    static /* synthetic */ void access$500(CommandManager x0, Module x1, Property x2, String x3, Object x4) {
        x0.setColorMessage(x1, x2, x3, x4);
    }

    static /* synthetic */ String access$600(CommandManager x0) {
        return x0.readClipboard();
    }
}

