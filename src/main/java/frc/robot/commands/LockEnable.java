package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.OI;
import frc.robot.lib.RioLogger;

public class LockEnable extends Command {

    public LockEnable() {
        super();
        requires(OI.lock);
    }

    @Override
    protected void initialize(){
    }

    @Override
    protected void execute() {
        RioLogger.log("Lock Enable button pressed");
        OI.lock.enable();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}