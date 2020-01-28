package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

import frc.robot.lib.RioLogger;
import frc.robot.OI;

public class PanelIntakeOut extends Command {
    private PanelPullInOnToggle panelPullInOnToggle;

    public PanelIntakeOut() {
        super();
        requires(OI.panel);
        RioLogger.errorLog("PanelIntakeOut() Created.");
    }

    @Override
    protected void initialize() {
        panelPullInOnToggle = new PanelPullInOnToggle();
        panelPullInOnToggle.start();
    }

    @Override
    protected void execute() {
        OI.panel.panelInOutSetToOut();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}