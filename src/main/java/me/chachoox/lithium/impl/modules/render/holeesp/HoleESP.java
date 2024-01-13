/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 */
package me.chachoox.lithium.impl.modules.render.holeesp;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.ColorProperty;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.blocks.BlockUtil;
import me.chachoox.lithium.api.util.blocks.HoleUtil;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.api.util.movement.PositionUtil;
import me.chachoox.lithium.api.util.render.RenderUtil;
import me.chachoox.lithium.api.util.render.TessellatorUtil;
import me.chachoox.lithium.impl.modules.render.holeesp.ListenerRender;
import me.chachoox.lithium.impl.modules.render.holeesp.mode.HolesMode;
import me.chachoox.lithium.impl.modules.render.holeesp.mode.Outline;
import me.chachoox.lithium.impl.modules.render.holeesp.util.TwoBlockPos;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class HoleESP
extends Module {
    protected final EnumProperty<HolesMode> holes = new EnumProperty<HolesMode>(HolesMode.HOLE, new String[]{"Holes", "holemode"}, "Hole: - Only draws 1x1 / 2x1 holes / Void: - Only draws void holes.");
    protected final NumberProperty<Integer> range = new NumberProperty<Integer>(6, 1, 50, new String[]{"Range", "Distance", "r"}, "The range of how far we want to see holes.");
    protected final NumberProperty<Integer> voidRange = new NumberProperty<Integer>(16, 1, 50, new String[]{"VoidRange", "voidrang", "voidr"}, "The range of how far we want to see holes.");
    protected final Property<Boolean> twoByOne = new Property<Boolean>(false, new String[]{"2x1", "doubles"}, "Draws 2x1 holes.");
    protected final NumberProperty<Float> height = new NumberProperty<Float>(Float.valueOf(0.0f), Float.valueOf(-1.0f), Float.valueOf(1.0f), Float.valueOf(0.05f), new String[]{"Height", "1x1H", "1x1height"}, "The height of 1x1 holes.");
    protected final NumberProperty<Float> doubleHeight = new NumberProperty<Float>(Float.valueOf(0.0f), Float.valueOf(-1.0f), Float.valueOf(1.0f), Float.valueOf(0.05f), new String[]{"2x1Height", "2x1H", "DoublesHeight", "Dh"}, "The height of the 2x1 holes.");
    protected final NumberProperty<Float> fadeHeight = new NumberProperty<Float>(Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.05f), new String[]{"FadeHeight", "fh"}, "The height of the fade.");
    protected final EnumProperty<Outline> outlineMode = new EnumProperty<Outline>(Outline.NORMAL, new String[]{"Outline", "Outlined", "out"}, "Normal: - Draws a normal outline surrounding the box / Cross: - Draws and X shape and an outline on the hole.");
    protected final NumberProperty<Integer> wireAlpha = new NumberProperty<Integer>(125, 0, 255, new String[]{"WireAlpha", "LineAlpha", "Alpha"}, "The alpha of the outline.");
    protected final NumberProperty<Float> lineWidth = new NumberProperty<Float>(Float.valueOf(1.0f), Float.valueOf(1.0f), Float.valueOf(4.0f), Float.valueOf(0.1f), new String[]{"WireWidth", "LineWidth", "Width", "w"}, "The thickness of the outline.");
    protected final Property<Boolean> fade = new Property<Boolean>(false, new String[]{"Fade", "Fadedthanahoe", "cpvpnnmode"}, "Draws an upwards fade on the hole.");
    protected final ColorProperty obbyColor = new ColorProperty(new Color(956235776, true), false, new String[]{"ObbyColor", "ObbyColour"});
    protected final ColorProperty bedrockColor = new ColorProperty(new Color(939589376, true), false, new String[]{"BedrockColor", "BedrockColour"});
    protected final ColorProperty voidColor = new ColorProperty(new Color(939524351, true), false, new String[]{"VoidColor", "VoidColour"});
    protected final NumberProperty<Integer> updates = new NumberProperty<Integer>(100, 0, 1000, new String[]{"Updates", "update"}, "Delay for updating holes to check if theyre safe.");
    protected List<BlockPos> obbyHoles = new ArrayList<BlockPos>();
    protected List<BlockPos> bedrockHoles = new ArrayList<BlockPos>();
    protected List<BlockPos> voidHoles = new ArrayList<BlockPos>();
    protected List<TwoBlockPos> obbyHolesTwoBlock = new ArrayList<TwoBlockPos>();
    protected List<TwoBlockPos> bedrockHolesTwoBlock = new ArrayList<TwoBlockPos>();
    protected StopWatch holeTimer = new StopWatch();
    protected StopWatch voidTimer = new StopWatch();

    public HoleESP() {
        super("HoleESP", new String[]{"HoleESP", "holesp", "safeesp", "safeholes"}, "Highlights safe holes.", Category.RENDER);
        this.offerProperties(this.holes, this.range, this.voidRange, this.twoByOne, this.height, this.doubleHeight, this.fadeHeight, this.outlineMode, this.wireAlpha, this.lineWidth, this.fade, this.obbyColor, this.bedrockColor, this.voidColor, this.updates);
        this.listeners.add(new ListenerRender(this));
    }

    @Override
    public void onEnable() {
        this.voidTimer.reset();
        this.holeTimer.reset();
    }

    @Override
    public String getSuffix() {
        return "" + this.getHoles();
    }

    protected Color getBedrockColor() {
        return this.bedrockColor.getColor();
    }

    protected Color getObbyColor() {
        return this.obbyColor.getColor();
    }

    protected Color getVoidColor() {
        return this.voidColor.getColor();
    }

    protected void calcHoles() {
        this.obbyHoles.clear();
        this.bedrockHoles.clear();
        this.obbyHolesTwoBlock.clear();
        this.bedrockHolesTwoBlock.clear();
        List<BlockPos> positions = BlockUtil.getSphere(RenderUtil.getEntity(), ((Integer)this.range.getValue()).intValue(), false);
        for (BlockPos pos : positions) {
            BlockPos validTwoBlock;
            if (HoleUtil.isObbyHole(pos)) {
                this.obbyHoles.add(pos);
            } else {
                validTwoBlock = HoleUtil.isDoubleObby(pos);
                if (validTwoBlock != null && this.twoByOne.getValue().booleanValue()) {
                    this.obbyHolesTwoBlock.add(new TwoBlockPos(pos, pos.add(validTwoBlock.getX(), validTwoBlock.getY(), validTwoBlock.getZ())));
                }
            }
            if (HoleUtil.isBedrockHole(pos)) {
                this.bedrockHoles.add(pos);
                continue;
            }
            validTwoBlock = HoleUtil.isDoubleBedrock(pos);
            if (validTwoBlock == null || !this.twoByOne.getValue().booleanValue()) continue;
            this.bedrockHolesTwoBlock.add(new TwoBlockPos(pos, pos.add(validTwoBlock.getX(), validTwoBlock.getY(), validTwoBlock.getZ())));
        }
    }

    protected List<BlockPos> findVoidHoles() {
        BlockPos playerPos = PositionUtil.getPosition(RenderUtil.getEntity());
        return this.getDisc(playerPos.add(0, -playerPos.getY(), 0), ((Integer)this.voidRange.getValue()).intValue()).stream().filter(this::isVoid).collect(Collectors.toList());
    }

    private boolean isVoid(BlockPos pos) {
        return (HoleESP.mc.world.getBlockState(pos).getBlock() == Blocks.AIR || HoleESP.mc.world.getBlockState(pos).getBlock() != Blocks.BEDROCK) && pos.getY() < 1 && pos.getY() >= 0;
    }

    private List<BlockPos> getDisc(BlockPos pos, float r) {
        ArrayList<BlockPos> circleblocks = new ArrayList<BlockPos>();
        int cx = pos.getX();
        int cy = pos.getY();
        int cz = pos.getZ();
        int x = cx - (int)r;
        while ((float)x <= (float)cx + r) {
            int z = cz - (int)r;
            while ((float)z <= (float)cz + r) {
                double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z);
                if (dist < (double)(r * r)) {
                    BlockPos position = new BlockPos(x, cy, z);
                    circleblocks.add(position);
                }
                ++z;
            }
            ++x;
        }
        return circleblocks;
    }

    protected void drawOutline(AxisAlignedBB bb, float with, Color color) {
        switch ((Outline)((Object)this.outlineMode.getValue())) {
            case NORMAL: {
                RenderUtil.drawOutline(bb, with, color);
                break;
            }
            case CROSS: {
                RenderUtil.drawCross(bb, with, color);
            }
        }
    }

    protected void drawFade(AxisAlignedBB bb, Color color) {
        TessellatorUtil.startRender();
        TessellatorUtil.drawGradientBox(bb, color);
        TessellatorUtil.drawGradientOutline(bb, color, ((Float)this.lineWidth.getValue()).floatValue(), (Integer)this.wireAlpha.getValue());
        TessellatorUtil.stopRender();
    }

    private int getHoles() {
        if (this.holes.getValue() != HolesMode.VOID) {
            return this.obbyHoles.size() + this.bedrockHoles.size() + this.obbyHolesTwoBlock.size() + this.bedrockHolesTwoBlock.size();
        }
        if (this.holes.getValue() != HolesMode.HOLE) {
            return this.voidHoles.size();
        }
        return this.obbyHoles.size() + this.bedrockHoles.size() + this.obbyHolesTwoBlock.size() + this.bedrockHolesTwoBlock.size() + this.voidHoles.size();
    }
}

