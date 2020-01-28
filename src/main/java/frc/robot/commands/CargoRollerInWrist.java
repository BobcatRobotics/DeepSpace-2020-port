package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

import frc.robot.lib.RioLogger;
import frc.robot.OI;

public class CargoRollerInWrist extends Command {

    public CargoRollerInWrist() {
        super();
        requires(OI.cargo);
    }

    @Override
    protected void initialize() {
        super.initialize();
        setTimeout(2);
        OI.cargo.intake();
        RioLogger.errorLog("CargoRollerIn4sec.initialize() called");
    }

    @Override
    protected void execute() {
    }

    @Override
    protected boolean isFinished() {
        return isTimedOut();
    }

    @Override
    protected void interrupted() {
        OI.cargo.stop();
        RioLogger.errorLog("CargoRollerIn4sec.interrupted() called");
    }

    @Override
    protected void end() {
        OI.cargo.stop();
        RioLogger.errorLog("CargoRollerIn.stop() called");
    }
}