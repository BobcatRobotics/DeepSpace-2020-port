package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

import frc.robot.OI;

public class LimitToggle extends Command {

    public LimitToggle() {
        super();
    }

    @Override
    protected void execute() {
        OI.elev1.setLmitDisableStatusToggle();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}