/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.GuiPlayerTabOverlay
 *  net.minecraft.client.network.NetworkPlayerInfo
 *  net.minecraft.scoreboard.ScoreObjective
 *  net.minecraft.scoreboard.Scoreboard
 */
package me.chachoox.lithium.asm.mixins.gui;

import java.util.List;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.misc.extratab.ExtraTab;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={GuiPlayerTabOverlay.class})
public abstract class MixinGuiPlayerTabOverlay {
    @Shadow
    private Minecraft mc;
    @Unique
    private int maxPingOffset;

    @Redirect(method={"renderPlayerlist"}, at=@At(value="INVOKE", target="Ljava/util/List;subList(II)Ljava/util/List;", remap=false))
    public List<NetworkPlayerInfo> renderPlayerList(List<NetworkPlayerInfo> list, int fromIndex, int toIndex) {
        ExtraTab EXTRA_TAB = Managers.MODULE.get(ExtraTab.class);
        return list.subList(fromIndex, EXTRA_TAB.isEnabled() ? Math.min((Integer)EXTRA_TAB.tabSize.getValue(), list.size()) : toIndex);
    }

    @Inject(method={"getPlayerName"}, at={@At(value="HEAD")}, cancellable=true)
    public void getPlayerNameHook(NetworkPlayerInfo playerInfo, CallbackInfoReturnable<String> info) {
        ExtraTab EXTRA_TAB = Managers.MODULE.get(ExtraTab.class);
        if (EXTRA_TAB.isEnabled()) {
            info.cancel();
            info.setReturnValue(EXTRA_TAB.getName(playerInfo));
        }
    }

    @Inject(method={"renderPlayerlist"}, at={@At(value="HEAD")})
    private void renderPlayerlistHeadHook(int width, Scoreboard scoreboardIn, ScoreObjective scoreObjectiveIn, CallbackInfo ci) {
        ExtraTab EXTRA_TAB = Managers.MODULE.get(ExtraTab.class);
        if (EXTRA_TAB.ping.getValue().booleanValue()) {
            this.maxPingOffset = this.mc.player.connection.getPlayerInfoMap().stream().map(NetworkPlayerInfo::getResponseTime).map(String::valueOf).map(arg_0 -> ((FontRenderer)this.mc.fontRenderer).getStringWidth(arg_0)).max(Integer::compare).orElse(0) + 1 + (EXTRA_TAB.bars.getValue() != false ? 12 : 0);
        }
    }

    @Redirect(method={"renderPlayerlist"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/gui/FontRenderer;getStringWidth(Ljava/lang/String;)I", ordinal=0))
    private int getStringWidthHook(FontRenderer instance, String k) {
        ExtraTab EXTRA_TAB = Managers.MODULE.get(ExtraTab.class);
        return EXTRA_TAB.ping.getValue() != false ? instance.getStringWidth(k) + this.maxPingOffset : instance.getStringWidth(k);
    }

    @Inject(method={"drawPing"}, at={@At(value="HEAD")}, cancellable=true)
    private void drawPingHook(int x, int xOff, int y, NetworkPlayerInfo networkPlayerInfoIn, CallbackInfo ci) {
        ExtraTab EXTRA_TAB = Managers.MODULE.get(ExtraTab.class);
        if (EXTRA_TAB.ping.getValue().booleanValue()) {
            int color = networkPlayerInfoIn.getResponseTime() < 100 ? -16711936 : (networkPlayerInfoIn.getResponseTime() < 200 ? -256 : -65536);
            String toDraw = String.valueOf(networkPlayerInfoIn.getResponseTime());
            this.mc.fontRenderer.drawStringWithShadow(toDraw, (float)(x + xOff - (EXTRA_TAB.bars.getValue() != false ? 12 : 0) - this.mc.fontRenderer.getStringWidth(toDraw)), (float)y, color);
        }
        if (!EXTRA_TAB.bars.getValue().booleanValue()) {
            ci.cancel();
        }
    }
}

