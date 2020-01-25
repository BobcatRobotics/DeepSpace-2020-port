package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.OI;
import frc.robot.lib.RioLogger;

public class PanelIntakeIn extends Command {

    public PanelIntakeIn() {
         super();
         requires(OI.panel);
         RioLogger.errorLog("PanelIntakeIn() Created.");
    }

    @Override
    protected void execute() {
        RioLogger.errorLog("PanelIntakeIn.execute() called.");
        OI.panel.panelInOutSetToIn();
    }

    @Override
    protected boolean isFinished() {
        RioLogger.errorLog("PanelIntakeIn.isFinished() called. return true");
        return true;
    }
}