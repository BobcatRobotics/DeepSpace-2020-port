package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.OI;
import frc.robot.lib.RioLogger;

// Note, this was called PanelIntakeIn before becoming PanelIntakeHoldPanel

public class PanelIntakeHoldPanel extends Command {

    public PanelIntakeHoldPanel() {
         super();
         requires(OI.panel);
         RioLogger.errorLog("PanelIntakeHoldPanel() Created.");
    }

    @Override
    protected void execute() {
        RioLogger.errorLog("PanelIntakeHoldPanel.execute() called.");
        OI.panel.holdPanel();
    }

    @Override
    protected boolean isFinished() {
        RioLogger.errorLog("PanelIntakeHoldPanel.isFinished() called. return true");
        return true;
    }
}