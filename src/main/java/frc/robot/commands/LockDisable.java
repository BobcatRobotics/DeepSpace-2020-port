package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.OI;
import frc.robot.lib.RioLogger;

public class LockDisable extends Command {

    public LockDisable() {
        super();
        requires(OI.lock);
    }

    @Override
    protected void execute() {
        RioLogger.log("Lock Disable button pressed");
        OI.lock.disable();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}