package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

import frc.robot.lib.RioLogger;
import frc.robot.OI;

// Note, this was called PanelIntakeOut before becoming PanelIntakeReleasePanel

public class PanelIntakeReleasePanel extends Command {

    public PanelIntakeReleasePanel() {
        super();
        requires(OI.panel);
        RioLogger.errorLog("PanelIntakeReleasePanel() Created.");
    }

    @Override
    protected void execute() {
        OI.panel.releasePanel();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}