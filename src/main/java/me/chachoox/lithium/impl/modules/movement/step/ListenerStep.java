/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 */
package me.chachoox.lithium.impl.modules.movement.step;

import me.chachoox.lithium.api.util.network.PacketUtil;
import me.chachoox.lithium.impl.event.events.movement.StepEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.movement.step.Step;
import me.chachoox.lithium.impl.modules.movement.step.mode.StepMode;
import net.minecraft.network.play.client.CPacketPlayer;

public class ListenerStep
extends ModuleListener<Step, StepEvent> {
    public ListenerStep(Step module) {
        super(module, StepEvent.class);
    }

    @Override
    public void call(StepEvent event) {
        if (((Step)this.module).mode.getValue() == StepMode.NORMAL) {
            double stepHeight = event.getBB().minY - ListenerStep.mc.player.posY;
            if (stepHeight <= 0.0 || stepHeight > (double)((Float)((Step)this.module).height.getValue()).floatValue()) {
                return;
            }
            double[] offsets = ((Step)this.module).getOffset(stepHeight);
            if (offsets != null && offsets.length > 1) {
                for (double offset : offsets) {
                    PacketUtil.send(new CPacketPlayer.Position(ListenerStep.mc.player.posX, ListenerStep.mc.player.posY + offset, ListenerStep.mc.player.posZ, false));
                }
            }
            ((Step)this.module).timer.reset();
        }
    }
}

