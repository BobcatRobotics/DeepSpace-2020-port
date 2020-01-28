package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

import frc.robot.OI;

public class MoveCargoRoller extends Command {

    public MoveCargoRoller() {
        super();
    }

    @Override
    protected void execute() {
        if (OI.gamePad.getRawButton(6)) {
            OI.cargo.intake();
        }
        if (OI.gamePad.getRawButton(8)) {
            OI.cargo.deposit();
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}