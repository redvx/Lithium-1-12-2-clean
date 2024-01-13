/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  net.minecraft.client.particle.Barrier$Factory
 *  net.minecraft.client.particle.IParticleFactory
 *  net.minecraft.client.particle.ParticleBlockDust$Factory
 *  net.minecraft.client.particle.ParticleBreaking$Factory
 *  net.minecraft.client.particle.ParticleBreaking$SlimeFactory
 *  net.minecraft.client.particle.ParticleBreaking$SnowballFactory
 *  net.minecraft.client.particle.ParticleBubble$Factory
 *  net.minecraft.client.particle.ParticleCloud$Factory
 *  net.minecraft.client.particle.ParticleCrit$DamageIndicatorFactory
 *  net.minecraft.client.particle.ParticleCrit$Factory
 *  net.minecraft.client.particle.ParticleCrit$MagicFactory
 *  net.minecraft.client.particle.ParticleDigging$Factory
 *  net.minecraft.client.particle.ParticleDragonBreath$Factory
 *  net.minecraft.client.particle.ParticleDrip$LavaFactory
 *  net.minecraft.client.particle.ParticleDrip$WaterFactory
 *  net.minecraft.client.particle.ParticleEnchantmentTable$EnchantmentTable
 *  net.minecraft.client.particle.ParticleEndRod$Factory
 *  net.minecraft.client.particle.ParticleExplosion$Factory
 *  net.minecraft.client.particle.ParticleExplosionHuge$Factory
 *  net.minecraft.client.particle.ParticleExplosionLarge$Factory
 *  net.minecraft.client.particle.ParticleFallingDust$Factory
 *  net.minecraft.client.particle.ParticleFirework$Factory
 *  net.minecraft.client.particle.ParticleFlame$Factory
 *  net.minecraft.client.particle.ParticleFootStep$Factory
 *  net.minecraft.client.particle.ParticleHeart$AngryVillagerFactory
 *  net.minecraft.client.particle.ParticleHeart$Factory
 *  net.minecraft.client.particle.ParticleLava$Factory
 *  net.minecraft.client.particle.ParticleManager
 *  net.minecraft.client.particle.ParticleMobAppearance$Factory
 *  net.minecraft.client.particle.ParticleNote$Factory
 *  net.minecraft.client.particle.ParticlePortal$Factory
 *  net.minecraft.client.particle.ParticleRain$Factory
 *  net.minecraft.client.particle.ParticleRedstone$Factory
 *  net.minecraft.client.particle.ParticleSmokeLarge$Factory
 *  net.minecraft.client.particle.ParticleSmokeNormal$Factory
 *  net.minecraft.client.particle.ParticleSnowShovel$Factory
 *  net.minecraft.client.particle.ParticleSpell$AmbientMobFactory
 *  net.minecraft.client.particle.ParticleSpell$Factory
 *  net.minecraft.client.particle.ParticleSpell$InstantFactory
 *  net.minecraft.client.particle.ParticleSpell$MobFactory
 *  net.minecraft.client.particle.ParticleSpell$WitchFactory
 *  net.minecraft.client.particle.ParticleSpit$Factory
 *  net.minecraft.client.particle.ParticleSplash$Factory
 *  net.minecraft.client.particle.ParticleSuspend$Factory
 *  net.minecraft.client.particle.ParticleSuspendedTown$Factory
 *  net.minecraft.client.particle.ParticleSuspendedTown$HappyVillagerFactory
 *  net.minecraft.client.particle.ParticleSweepAttack$Factory
 *  net.minecraft.client.particle.ParticleWaterWake$Factory
 *  net.minecraft.util.EnumParticleTypes
 */
package me.chachoox.lithium.asm.mixins.render;

import com.google.common.collect.Maps;
import java.util.Map;
import me.chachoox.lithium.impl.modules.render.popcolours.util.CustomPopParticle;
import net.minecraft.client.particle.Barrier;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.ParticleBlockDust;
import net.minecraft.client.particle.ParticleBreaking;
import net.minecraft.client.particle.ParticleBubble;
import net.minecraft.client.particle.ParticleCloud;
import net.minecraft.client.particle.ParticleCrit;
import net.minecraft.client.particle.ParticleDigging;
import net.minecraft.client.particle.ParticleDragonBreath;
import net.minecraft.client.particle.ParticleDrip;
import net.minecraft.client.particle.ParticleEnchantmentTable;
import net.minecraft.client.particle.ParticleEndRod;
import net.minecraft.client.particle.ParticleExplosion;
import net.minecraft.client.particle.ParticleExplosionHuge;
import net.minecraft.client.particle.ParticleExplosionLarge;
import net.minecraft.client.particle.ParticleFallingDust;
import net.minecraft.client.particle.ParticleFirework;
import net.minecraft.client.particle.ParticleFlame;
import net.minecraft.client.particle.ParticleFootStep;
import net.minecraft.client.particle.ParticleHeart;
import net.minecraft.client.particle.ParticleLava;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.ParticleMobAppearance;
import net.minecraft.client.particle.ParticleNote;
import net.minecraft.client.particle.ParticlePortal;
import net.minecraft.client.particle.ParticleRain;
import net.minecraft.client.particle.ParticleRedstone;
import net.minecraft.client.particle.ParticleSmokeLarge;
import net.minecraft.client.particle.ParticleSmokeNormal;
import net.minecraft.client.particle.ParticleSnowShovel;
import net.minecraft.client.particle.ParticleSpell;
import net.minecraft.client.particle.ParticleSpit;
import net.minecraft.client.particle.ParticleSplash;
import net.minecraft.client.particle.ParticleSuspend;
import net.minecraft.client.particle.ParticleSuspendedTown;
import net.minecraft.client.particle.ParticleSweepAttack;
import net.minecraft.client.particle.ParticleWaterWake;
import net.minecraft.util.EnumParticleTypes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value={ParticleManager.class})
public class MixinParticleManager {
    @Final
    @Shadow
    private final Map<Integer, IParticleFactory> particleTypes = Maps.newHashMap();

    @Overwrite
    private void registerVanillaParticles() {
        this.registerParticle(EnumParticleTypes.EXPLOSION_NORMAL.getParticleID(), (IParticleFactory)new ParticleExplosion.Factory());
        this.registerParticle(EnumParticleTypes.SPIT.getParticleID(), (IParticleFactory)new ParticleSpit.Factory());
        this.registerParticle(EnumParticleTypes.WATER_BUBBLE.getParticleID(), (IParticleFactory)new ParticleBubble.Factory());
        this.registerParticle(EnumParticleTypes.WATER_SPLASH.getParticleID(), (IParticleFactory)new ParticleSplash.Factory());
        this.registerParticle(EnumParticleTypes.WATER_WAKE.getParticleID(), (IParticleFactory)new ParticleWaterWake.Factory());
        this.registerParticle(EnumParticleTypes.WATER_DROP.getParticleID(), (IParticleFactory)new ParticleRain.Factory());
        this.registerParticle(EnumParticleTypes.SUSPENDED.getParticleID(), (IParticleFactory)new ParticleSuspend.Factory());
        this.registerParticle(EnumParticleTypes.SUSPENDED_DEPTH.getParticleID(), (IParticleFactory)new ParticleSuspendedTown.Factory());
        this.registerParticle(EnumParticleTypes.CRIT.getParticleID(), (IParticleFactory)new ParticleCrit.Factory());
        this.registerParticle(EnumParticleTypes.CRIT_MAGIC.getParticleID(), (IParticleFactory)new ParticleCrit.MagicFactory());
        this.registerParticle(EnumParticleTypes.SMOKE_NORMAL.getParticleID(), (IParticleFactory)new ParticleSmokeNormal.Factory());
        this.registerParticle(EnumParticleTypes.SMOKE_LARGE.getParticleID(), (IParticleFactory)new ParticleSmokeLarge.Factory());
        this.registerParticle(EnumParticleTypes.SPELL.getParticleID(), (IParticleFactory)new ParticleSpell.Factory());
        this.registerParticle(EnumParticleTypes.SPELL_INSTANT.getParticleID(), (IParticleFactory)new ParticleSpell.InstantFactory());
        this.registerParticle(EnumParticleTypes.SPELL_MOB.getParticleID(), (IParticleFactory)new ParticleSpell.MobFactory());
        this.registerParticle(EnumParticleTypes.SPELL_MOB_AMBIENT.getParticleID(), (IParticleFactory)new ParticleSpell.AmbientMobFactory());
        this.registerParticle(EnumParticleTypes.SPELL_WITCH.getParticleID(), (IParticleFactory)new ParticleSpell.WitchFactory());
        this.registerParticle(EnumParticleTypes.DRIP_WATER.getParticleID(), (IParticleFactory)new ParticleDrip.WaterFactory());
        this.registerParticle(EnumParticleTypes.DRIP_LAVA.getParticleID(), (IParticleFactory)new ParticleDrip.LavaFactory());
        this.registerParticle(EnumParticleTypes.VILLAGER_ANGRY.getParticleID(), (IParticleFactory)new ParticleHeart.AngryVillagerFactory());
        this.registerParticle(EnumParticleTypes.VILLAGER_HAPPY.getParticleID(), (IParticleFactory)new ParticleSuspendedTown.HappyVillagerFactory());
        this.registerParticle(EnumParticleTypes.TOWN_AURA.getParticleID(), (IParticleFactory)new ParticleSuspendedTown.Factory());
        this.registerParticle(EnumParticleTypes.NOTE.getParticleID(), (IParticleFactory)new ParticleNote.Factory());
        this.registerParticle(EnumParticleTypes.PORTAL.getParticleID(), (IParticleFactory)new ParticlePortal.Factory());
        this.registerParticle(EnumParticleTypes.ENCHANTMENT_TABLE.getParticleID(), (IParticleFactory)new ParticleEnchantmentTable.EnchantmentTable());
        this.registerParticle(EnumParticleTypes.FLAME.getParticleID(), (IParticleFactory)new ParticleFlame.Factory());
        this.registerParticle(EnumParticleTypes.LAVA.getParticleID(), (IParticleFactory)new ParticleLava.Factory());
        this.registerParticle(EnumParticleTypes.FOOTSTEP.getParticleID(), (IParticleFactory)new ParticleFootStep.Factory());
        this.registerParticle(EnumParticleTypes.CLOUD.getParticleID(), (IParticleFactory)new ParticleCloud.Factory());
        this.registerParticle(EnumParticleTypes.REDSTONE.getParticleID(), (IParticleFactory)new ParticleRedstone.Factory());
        this.registerParticle(EnumParticleTypes.FALLING_DUST.getParticleID(), (IParticleFactory)new ParticleFallingDust.Factory());
        this.registerParticle(EnumParticleTypes.SNOWBALL.getParticleID(), (IParticleFactory)new ParticleBreaking.SnowballFactory());
        this.registerParticle(EnumParticleTypes.SNOW_SHOVEL.getParticleID(), (IParticleFactory)new ParticleSnowShovel.Factory());
        this.registerParticle(EnumParticleTypes.SLIME.getParticleID(), (IParticleFactory)new ParticleBreaking.SlimeFactory());
        this.registerParticle(EnumParticleTypes.HEART.getParticleID(), (IParticleFactory)new ParticleHeart.Factory());
        this.registerParticle(EnumParticleTypes.BARRIER.getParticleID(), (IParticleFactory)new Barrier.Factory());
        this.registerParticle(EnumParticleTypes.ITEM_CRACK.getParticleID(), (IParticleFactory)new ParticleBreaking.Factory());
        this.registerParticle(EnumParticleTypes.BLOCK_CRACK.getParticleID(), (IParticleFactory)new ParticleDigging.Factory());
        this.registerParticle(EnumParticleTypes.BLOCK_DUST.getParticleID(), (IParticleFactory)new ParticleBlockDust.Factory());
        this.registerParticle(EnumParticleTypes.EXPLOSION_HUGE.getParticleID(), (IParticleFactory)new ParticleExplosionHuge.Factory());
        this.registerParticle(EnumParticleTypes.EXPLOSION_LARGE.getParticleID(), (IParticleFactory)new ParticleExplosionLarge.Factory());
        this.registerParticle(EnumParticleTypes.FIREWORKS_SPARK.getParticleID(), (IParticleFactory)new ParticleFirework.Factory());
        this.registerParticle(EnumParticleTypes.MOB_APPEARANCE.getParticleID(), (IParticleFactory)new ParticleMobAppearance.Factory());
        this.registerParticle(EnumParticleTypes.DRAGON_BREATH.getParticleID(), (IParticleFactory)new ParticleDragonBreath.Factory());
        this.registerParticle(EnumParticleTypes.END_ROD.getParticleID(), (IParticleFactory)new ParticleEndRod.Factory());
        this.registerParticle(EnumParticleTypes.DAMAGE_INDICATOR.getParticleID(), (IParticleFactory)new ParticleCrit.DamageIndicatorFactory());
        this.registerParticle(EnumParticleTypes.SWEEP_ATTACK.getParticleID(), (IParticleFactory)new ParticleSweepAttack.Factory());
        this.registerParticle(EnumParticleTypes.TOTEM.getParticleID(), new CustomPopParticle.Factory());
    }

    public void registerParticle(int id, IParticleFactory particleFactory) {
        this.particleTypes.put(id, particleFactory);
    }
}

