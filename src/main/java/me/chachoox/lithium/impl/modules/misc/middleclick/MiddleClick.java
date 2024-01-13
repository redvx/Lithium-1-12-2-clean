/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.RayTraceResult$Type
 */
package me.chachoox.lithium.impl.modules.misc.middleclick;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.misc.middleclick.ListenerUpdate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;

public class MiddleClick
extends Module {
    protected final Property<Boolean> friend = new Property<Boolean>(true, new String[]{"Friend", "MiddleClickFriend", "mcf", "f"}, "Adds the player you middle click as a friend.");
    protected final Property<Boolean> pearl = new Property<Boolean>(false, new String[]{"Pearl", "pearlington", "pear"}, "Throws a pearl whenever we middle click.");
    protected boolean clicked = false;

    public MiddleClick() {
        super("MiddleClick", new String[]{"MiddleClick", "mcf", "mcp"}, "Allows you to preform actions using the scroll wheel.", Category.MISC);
        this.offerProperties(this.friend, this.pearl);
        this.offerListeners(new ListenerUpdate(this));
    }

    protected boolean onEntity() {
        RayTraceResult result = MiddleClick.mc.objectMouseOver;
        return result != null && result.typeOfHit == RayTraceResult.Type.ENTITY && result.entityHit instanceof EntityPlayer;
    }

    protected void onClick() {
        Entity entity;
        RayTraceResult result = MiddleClick.mc.objectMouseOver;
        if (result != null && result.typeOfHit == RayTraceResult.Type.ENTITY && (entity = result.entityHit) instanceof EntityPlayer) {
            if (Managers.FRIEND.isFriend(entity.getName())) {
                Managers.FRIEND.removeFriend(entity.getName());
            } else {
                Managers.FRIEND.addFriend(entity.getName());
            }
        }
        this.clicked = true;
    }
}

