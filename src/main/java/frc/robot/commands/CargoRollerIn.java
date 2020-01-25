package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.OI;
import frc.robot.lib.RioLogger;

public class CargoRollerIn extends Command {

    public CargoRollerIn() {
        super();
        requires(OI.cargo);
    }

    @Override
    protected void initialize() {
        super.initialize();
        OI.cargo.intake();
        RioLogger.errorLog("CargoRollerIn.initialize() called");
    }

    @Override
    protected void execute() {
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void interrupted() {
        OI.cargo.stop();
        RioLogger.errorLog("CargoRollerIn.interrupted() called");
    }

    @Override
    protected void end() {
        OI.cargo.stop();
        RioLogger.errorLog("CargoRollerIn.stop() called");
    }
}