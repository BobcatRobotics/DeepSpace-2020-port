package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.OI;
import frc.robot.lib.RioLogger;

public class PanelPullInOnToggle extends Command {
    PanelPullinBiasWhenOut panelPullinBiasWhenOut;

    public PanelPullInOnToggle() {
        super();
        requires(OI.panel);
    }

    @Override
    protected void initialize() {
        super.initialize();
        setTimeout(0.25);
        OI.panel.pullPanel();
        RioLogger.errorLog("PanelPullInOnToggle.initialize() called");
    }

    @Override
    protected void execute() {
    }

    @Override
    protected boolean isFinished() {
        if (isTimedOut()) {
            if (OI.panel.panelIntakeIsOut()) {
                panelPullinBiasWhenOut = new PanelPullinBiasWhenOut();
                panelPullinBiasWhenOut.start();
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void interrupted() {
        OI.panel.stop();
        RioLogger.errorLog("PanelPullInOnToggle.interrupted() called");
    }

    @Override
    protected void end() {
        OI.panel.stop();
        RioLogger.errorLog("PanelPullInOnToggle.stop() called");
    }
}