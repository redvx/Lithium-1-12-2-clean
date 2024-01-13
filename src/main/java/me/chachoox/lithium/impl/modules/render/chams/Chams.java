/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.opengl.GL11
 */
package me.chachoox.lithium.impl.modules.render.chams;

import java.awt.Color;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.ColorProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.colors.ColorUtil;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.other.colours.Colours;
import me.chachoox.lithium.impl.modules.render.chams.ListenerDamageColour;
import me.chachoox.lithium.impl.modules.render.chams.ListenerPostModel;
import me.chachoox.lithium.impl.modules.render.chams.ListenerPreCrystalModel;
import me.chachoox.lithium.impl.modules.render.chams.ListenerPreModel;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class Chams
extends Module {
    public final Property<Boolean> self = new Property<Boolean>(false, new String[]{"SelfChams", "Self"}, "Renders chams on ourself.");
    public final Property<Boolean> playerWires = new Property<Boolean>(false, new String[]{"PlayerWireframe", "PlayerWire", "WirePlayer"}, "Renders wireframe on players.");
    public final Property<Boolean> playerChams = new Property<Boolean>(false, new String[]{"PlayerChams", "PlayerCham"}, "Renders chams on players.");
    public final Property<Boolean> crystalWires = new Property<Boolean>(false, new String[]{"CrystalWireframe", "CrystalWire", "WireCrystal"}, "Renders wireframe on crystals.");
    public final Property<Boolean> crystalChams = new Property<Boolean>(false, new String[]{"CrystalChams", "CrystalCham"}, "Renders chams on crystals.");
    public final Property<Boolean> normal = new Property<Boolean>(false, new String[]{"Normal", "og"}, "Orignal chams.");
    public final Property<Boolean> xqz = new Property<Boolean>(false, new String[]{"Xqz", "throughwall", "truwall"}, "Render entities through walls.");
    public final Property<Boolean> glint = new Property<Boolean>(false, new String[]{"Glint", "shyne", "enchant"}, "Renders a enchant effect in entities.");
    public final NumberProperty<Float> glintSpeed = new NumberProperty<Float>(Float.valueOf(5.0f), Float.valueOf(0.1f), Float.valueOf(20.0f), Float.valueOf(0.1f), new String[]{"GlintSpeed", "shynespeed", "enchantspeed"}, "Current speed of the glint.");
    public final NumberProperty<Float> glintScale = new NumberProperty<Float>(Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(10.0f), Float.valueOf(0.1f), new String[]{"GlintScale", "shynescale", "enchantscale"}, "Scale of the glint.");
    public final Property<Boolean> customGlint = new Property<Boolean>(false, new String[]{"CustomGlint", "cglint", "altglint"}, "Uses an alternative glint texture instead of minecraft's one");
    public final Property<Boolean> texture = new Property<Boolean>(false, new String[]{"Texture", "tex"}, "Render the model of the entity.");
    protected final Property<Boolean> damage = new Property<Boolean>(false, new String[]{"Damage", "dag"}, "Changes the damage colour.");
    public final NumberProperty<Float> scale = new NumberProperty<Float>(Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(2.0f), Float.valueOf(0.1f), new String[]{"CrystalScale", "scale"}, "Scale of crystals.");
    public final NumberProperty<Float> smallScale = new NumberProperty<Float>(Float.valueOf(0.0f), Float.valueOf(-1.0f), Float.valueOf(0.0f), Float.valueOf(0.1f), new String[]{"CrystalModelScale", "smallcrystalscale", "modelscale"}, "Scale of the inner model of crystals.");
    public final NumberProperty<Float> spinSpeed = new NumberProperty<Float>(Float.valueOf(1.0f), Float.valueOf(0.0f), Float.valueOf(5.0f), Float.valueOf(0.1f), new String[]{"SpinSpeed", "spinsped", "spinspeeed", "spin"}, "Spin speed of crystals.");
    public final NumberProperty<Float> bounceSpeed = new NumberProperty<Float>(Float.valueOf(1.0f), Float.valueOf(0.0f), Float.valueOf(5.0f), Float.valueOf(0.1f), new String[]{"CrystalBounce", "bounce"}, "Bounce speed of crystals.");
    public final NumberProperty<Float> lineWidth = new NumberProperty<Float>(Float.valueOf(1.0f), Float.valueOf(1.0f), Float.valueOf(4.0f), Float.valueOf(0.1f), new String[]{"LineWidth", "width"}, "Width of wireframes");
    private final ColorProperty visibleColor = new ColorProperty(new Color(0x34FFFFFF, true), true, new String[]{"VisibleColor", "viscolor"});
    private final ColorProperty invisibleColor = new ColorProperty(new Color(0x4DFFFFFF, true), true, new String[]{"XqzColor", "WallColor"});
    protected final ColorProperty friendColor = new ColorProperty(new Color(Colours.get().getFriendColour().getRGB(), true), false, new String[]{"FriendColor", "frdcolor"});
    private final ColorProperty wireframeColor = new ColorProperty(new Color(-1), true, new String[]{"WireframeColor", "outlinecolor"});
    protected final ColorProperty damageColor = new ColorProperty(new Color(255, 0, 0, 30), true, new String[]{"DamageColor", "dagamecol"});
    private final ResourceLocation ENCHANTED_ITEM_GLINT_RES = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    private final ResourceLocation CUSTOM_ENCHANT_GLINT_RES = new ResourceLocation("lithium/textures/misc/enchanted_item_glint.png");

    public Chams() {
        super("Chams", new String[]{"Chams", "cham", "chammies", "charms"}, "Renders entities through walls.", Category.RENDER);
        this.offerProperties(this.self, this.playerWires, this.playerChams, this.crystalChams, this.crystalWires, this.normal, this.xqz, this.glint, this.glintSpeed, this.glintScale, this.customGlint, this.texture, this.damage, this.scale, this.smallScale, this.spinSpeed, this.bounceSpeed, this.lineWidth, this.visibleColor, this.invisibleColor, this.friendColor, this.wireframeColor, this.damageColor);
        this.offerListeners(new ListenerPreModel(this), new ListenerPostModel(this), new ListenerPreCrystalModel(this), new ListenerDamageColour(this));
    }

    public void onWireframeModel(ModelBase base, Entity entity, float limbSwing, float limbSwingAmount, float age, float headYaw, float headPitch, float scale) {
        GL11.glPushMatrix();
        GL11.glPushAttrib((int)1048575);
        GL11.glPolygonMode((int)1032, (int)6913);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2896);
        GL11.glEnable((int)2848);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDepthMask((boolean)false);
        GL11.glDisable((int)2929);
        GlStateManager.color((float)((float)this.getWireColor(entity).getRed() / 255.0f), (float)((float)this.getWireColor(entity).getGreen() / 255.0f), (float)((float)this.getWireColor(entity).getBlue() / 255.0f), (float)((float)this.getWireColor(entity).getAlpha() / 255.0f));
        GlStateManager.glLineWidth((float)((Float)this.lineWidth.getValue()).floatValue());
        base.render(entity, limbSwing, limbSwingAmount, age, headYaw, headPitch, scale);
        GlStateManager.resetColor();
        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }

    public void onGlintModel(ModelBase base, Entity entity, float limbSwing, float limbSwingAmount, float age, float headYaw, float headPitch, float scale) {
        GL11.glPushMatrix();
        GL11.glPushAttrib((int)1048575);
        GL11.glPolygonMode((int)1032, (int)6914);
        GL11.glDisable((int)2896);
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        Chams.mc.getRenderManager().renderEngine.bindTexture(this.customGlint.getValue() != false ? this.CUSTOM_ENCHANT_GLINT_RES : this.ENCHANTED_ITEM_GLINT_RES);
        GL11.glPolygonMode((int)1032, (int)6914);
        GL11.glDisable((int)2896);
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glColor4f((float)((float)this.getVisibleColor(entity).getRed() / 255.0f), (float)((float)this.getVisibleColor(entity).getGreen() / 255.0f), (float)((float)this.getVisibleColor(entity).getBlue() / 255.0f), (float)((float)this.getVisibleColor(entity).getAlpha() / 255.0f));
        GlStateManager.blendFunc((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_COLOR, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE);
        for (int i = 0; i < 2; ++i) {
            GlStateManager.matrixMode((int)5890);
            GlStateManager.loadIdentity();
            float tScale = 0.33333334f * ((Float)this.glintScale.getValue()).floatValue();
            GlStateManager.scale((float)tScale, (float)tScale, (float)tScale);
            GlStateManager.rotate((float)(30.0f - (float)i * 60.0f), (float)0.0f, (float)0.0f, (float)1.0f);
            GlStateManager.translate((float)0.0f, (float)(((float)entity.ticksExisted + mc.getRenderPartialTicks()) * (0.001f + (float)i * 0.003f) * ((Float)this.glintSpeed.getValue()).floatValue()), (float)0.0f);
            GlStateManager.matrixMode((int)5888);
            GlStateManager.color((float)((float)this.getVisibleColor(entity).getRed() / 255.0f), (float)((float)this.getVisibleColor(entity).getGreen() / 255.0f), (float)((float)this.getVisibleColor(entity).getBlue() / 255.0f), (float)((float)this.getVisibleColor(entity).getAlpha() / 255.0f));
            if (!this.xqz.getValue().booleanValue()) {
                GL11.glDepthMask((boolean)true);
                GL11.glEnable((int)2929);
            }
            base.render(entity, limbSwing, limbSwingAmount, age, headYaw, headPitch, scale);
            if (this.xqz.getValue().booleanValue()) continue;
            GL11.glDisable((int)2929);
            GL11.glDepthMask((boolean)false);
        }
        GlStateManager.matrixMode((int)5890);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode((int)5888);
        GlStateManager.blendFunc((GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }

    public Color getFriendColor() {
        if (this.friendColor.isGlobal()) {
            return ColorUtil.changeAlpha(Colours.get().getFriendColour(), this.friendColor.getColor().getAlpha());
        }
        return this.friendColor.getColor();
    }

    public Color getVisibleColor(Entity entity) {
        if (entity instanceof EntityPlayer && Managers.FRIEND.isFriend((EntityPlayer)entity)) {
            return this.getFriendColor();
        }
        return this.visibleColor.getColor();
    }

    public Color getInvisibleColor(Entity entity) {
        if (entity instanceof EntityPlayer && Managers.FRIEND.isFriend((EntityPlayer)entity)) {
            return new Color(this.getFriendColor().getRed(), this.getFriendColor().getGreen(), this.getFriendColor().getBlue(), this.invisibleColor.getColor().getAlpha());
        }
        return this.invisibleColor.getColor();
    }

    public Color getWireColor(Entity entity) {
        if (entity instanceof EntityPlayer && Managers.FRIEND.isFriend((EntityPlayer)entity)) {
            return this.getFriendColor();
        }
        return this.wireframeColor.getColor();
    }

    public Color getDamageColor() {
        return this.damageColor.getColor();
    }

    public float getScale() {
        return ((Float)this.scale.getValue()).floatValue();
    }
}

