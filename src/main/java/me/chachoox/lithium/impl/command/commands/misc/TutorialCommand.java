/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.tutorial.TutorialSteps
 */
package me.chachoox.lithium.impl.command.commands.misc;

import me.chachoox.lithium.impl.command.Argument;
import me.chachoox.lithium.impl.command.Command;
import net.minecraft.client.tutorial.TutorialSteps;

public class TutorialCommand
extends Command {
    public TutorialCommand() {
        super(new String[]{"Tutorial", "tut"}, new Argument[0]);
    }

    @Override
    public String execute() {
        TutorialCommand.mc.gameSettings.tutorialStep = TutorialSteps.NONE;
        mc.getTutorial().setStep(TutorialSteps.NONE);
        return "Set tutorial step to none";
    }
}

