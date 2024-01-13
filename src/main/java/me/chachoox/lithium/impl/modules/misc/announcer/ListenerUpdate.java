/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 */
package me.chachoox.lithium.impl.modules.misc.announcer;

import java.util.ArrayList;
import java.util.Map;
import me.chachoox.lithium.api.util.entity.EntityUtil;
import me.chachoox.lithium.impl.event.events.update.UpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.misc.announcer.Announcer;
import me.chachoox.lithium.impl.modules.misc.announcer.util.Type;
import net.minecraft.entity.Entity;

public class ListenerUpdate
extends ModuleListener<Announcer, UpdateEvent> {
    public ListenerUpdate(Announcer module) {
        super(module, UpdateEvent.class);
    }

    @Override
    public void call(UpdateEvent event) {
        float dist;
        if (ListenerUpdate.mc.player == null || ListenerUpdate.mc.world == null) {
            return;
        }
        ((Announcer)this.module).setMessages();
        if (((Announcer)this.module).move.getValue().booleanValue() && ((Announcer)this.module).moveTimer.passed((Integer)((Announcer)this.module).delay.getValue() * 1500) && (double)(dist = (float)((Announcer)this.module).speed) > 0.0) {
            ((Announcer)this.module).events.put(Type.WALK, Float.valueOf(dist));
            ((Announcer)this.module).speed = 0.0;
            ((Announcer)this.module).moveTimer.reset();
        }
        if (!((Announcer)this.module).events.isEmpty() && ((Announcer)this.module).timer.passed((Integer)((Announcer)this.module).delay.getValue() * 1000)) {
            try {
                int index = ((Announcer)this.module).random.nextInt(((Announcer)this.module).events.entrySet().size());
                for (int i = 0; i < ((Announcer)this.module).events.entrySet().size(); ++i) {
                    ArrayList<Map.Entry<Type, Float>> list;
                    Map.Entry entry;
                    if (i != index || (entry = (Map.Entry)(list = new ArrayList<Map.Entry<Type, Float>>(((Announcer)this.module).events.entrySet())).get(i)).getValue() == null || entry.getKey() == null || EntityUtil.isDead((Entity)ListenerUpdate.mc.player)) continue;
                    ListenerUpdate.mc.player.sendChatMessage((((Announcer)this.module).greenText.getValue() != false ? "> " : "") + ((Announcer)this.module).getMessage((Type)((Object)entry.getKey()), ((Float)entry.getValue()).floatValue()));
                    if (!((Announcer)this.module).cycle.getValue().booleanValue()) continue;
                    ((Announcer)this.module).language.increment();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            ((Announcer)this.module).timer.reset();
            ((Announcer)this.module).events.clear();
        }
    }
}

