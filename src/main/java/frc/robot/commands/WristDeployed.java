package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

import frc.robot.lib.RioLogger;
import frc.robot.OI;

public class WristDeployed extends Command {

    public WristDeployed() {
        super();
        requires(OI.wrist);
        requires(OI.panel);
        setTimeout(1.0);
        RioLogger.errorLog("WristDeployed() created.");
    }

    @Override
    protected void initialize() {
        RioLogger.errorLog("WristDeployed.initialize() called.");
    }

    @Override
    protected void execute() {
        RioLogger.errorLog("WristDeployed.execute() called.");
        // When the wrist it deployed, the panel intake needs to be retracted
        // so that it does not hit the ground and get damaged
        OI.panel.holdPanel();
        OI.panel.panelInOutSetToIn();
        OI.wrist.deployphase1();
    }

    @Override
    protected boolean isFinished() {
        RioLogger.errorLog("WristDeployed.isFinished() called. Returning true if TimedOut.");
        return isTimedOut();
    }

    @Override
    protected void end() {
        OI.wrist.deployphase2();
        RioLogger.errorLog("WristDeployed.end() called.");
    }

    @Override
    protected void interrupted() {
        RioLogger.errorLog("WristDeployed.interrupted() called.");
    }
}