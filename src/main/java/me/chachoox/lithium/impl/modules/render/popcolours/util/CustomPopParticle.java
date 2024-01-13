/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.particle.IParticleFactory
 *  net.minecraft.client.particle.Particle
 *  net.minecraft.client.particle.ParticleSimpleAnimated
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package me.chachoox.lithium.impl.modules.render.popcolours.util;

import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.render.popcolours.PopColours;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleSimpleAnimated;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class CustomPopParticle
extends ParticleSimpleAnimated {
    public CustomPopParticle(World p_i47220_1_, double p_i47220_2_, double p_i47220_4_, double p_i47220_6_, double p_i47220_8_, double p_i47220_10_, double p_i47220_12_) {
        super(p_i47220_1_, p_i47220_2_, p_i47220_4_, p_i47220_6_, 176, 8, -0.05f);
        PopColours POP_COLOURS = Managers.MODULE.get(PopColours.class);
        this.motionX = p_i47220_8_;
        this.motionY = p_i47220_10_;
        this.motionZ = p_i47220_12_;
        this.particleScale *= POP_COLOURS.isEnabled() ? POP_COLOURS.getScale().floatValue() : 0.75f;
        this.particleMaxAge = 60 + this.rand.nextInt(12);
        if (this.rand.nextInt(4) == 0) {
            this.setFirstColor();
        } else {
            this.setSecondColor();
        }
        this.setBaseAirFriction(0.6f);
    }

    public void setFirstColor() {
        PopColours POP_COLOURS = Managers.MODULE.get(PopColours.class);
        if (POP_COLOURS.isEnabled()) {
            this.setRBGColorF((float)POP_COLOURS.getColor().getRed() / 255.0f + this.rand.nextFloat() * POP_COLOURS.getRandomRed().floatValue(), (float)POP_COLOURS.getColor().getGreen() / 255.0f + this.rand.nextFloat() * POP_COLOURS.getRandomGreen().floatValue(), (float)POP_COLOURS.getColor().getBlue() / 255.0f + this.rand.nextFloat() * POP_COLOURS.getRandomBlue().floatValue());
        } else {
            this.setRBGColorF(0.6f + this.rand.nextFloat() * 0.2f, 0.6f + this.rand.nextFloat() * 0.3f, this.rand.nextFloat() * 0.2f);
        }
    }

    public void setSecondColor() {
        PopColours POP_COLOURS = Managers.MODULE.get(PopColours.class);
        if (POP_COLOURS.isEnabled()) {
            this.setRBGColorF((float)POP_COLOURS.getSecondColor().getRed() / 255.0f + this.rand.nextFloat() * POP_COLOURS.getRandomRed().floatValue(), (float)POP_COLOURS.getSecondColor().getGreen() / 255.0f + this.rand.nextFloat() * POP_COLOURS.getRandomGreen().floatValue(), (float)POP_COLOURS.getSecondColor().getBlue() / 255.0f + this.rand.nextFloat() * POP_COLOURS.getRandomBlue().floatValue());
        } else {
            this.setRBGColorF(0.1f + this.rand.nextFloat() * 0.2f, 0.4f + this.rand.nextFloat() * 0.3f, this.rand.nextFloat() * 0.2f);
        }
    }

    @SideOnly(value=Side.CLIENT)
    public static class Factory
    implements IParticleFactory {
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int ... p_178902_15_) {
            return new CustomPopParticle(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}

