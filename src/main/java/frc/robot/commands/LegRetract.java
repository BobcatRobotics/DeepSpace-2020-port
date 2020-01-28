package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

import frc.robot.OI;

public class LegRetract extends Command {

    public LegRetract() {
        super();
        requires(OI.lock);
    }

    @Override
    protected void execute() {
        OI.lock.disable();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}