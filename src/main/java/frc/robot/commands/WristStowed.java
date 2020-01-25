package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.OI;

public class WristStowed extends Command {
    boolean toggle = false;
    CargoRollerInWrist cargoRollerInWrist;

    public WristStowed() {
        super();
        requires(OI.wrist);
    }

    @Override
    protected void initialize() {
        // check the state, if 2, then run cargo in for 4 seconds
        if (OI.wrist.getWristState() == 2) {
            cargoRollerInWrist = new CargoRollerInWrist();
            cargoRollerInWrist.start();
        }
        OI.wrist.stow();
    }

    @Override
    protected void execute() {
        // TODO update these once the wrist is more defined
        // Possibly extend to individual commands
        OI.wrist.stow();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }

    @Override
    protected void end() {
        OI.wrist.stow();
    }

    @Override
    protected void interrupted() {
        OI.wrist.stow();
    }
}