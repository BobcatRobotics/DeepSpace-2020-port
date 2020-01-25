package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.OI;
import frc.robot.lib.RioLogger;

public class PanelPullIn extends Command {
    PanelPullinBiasWhenOut panelPullinBiasWhenOut;

    public PanelPullIn() {
        super();
        requires(OI.panel);
    }

    @Override
    protected void initialize() {
        super.initialize();
        OI.panel.pullPanel();
        RioLogger.errorLog("PanelPullIn.initialize() called");
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
        OI.panel.stop();
        if (OI.panel.panelIntakeIsOut()) {
            panelPullinBiasWhenOut = new PanelPullinBiasWhenOut();
            panelPullinBiasWhenOut.start();
        }
        RioLogger.errorLog("PanelPullIn.interrupted() called");
    }

    @Override
    protected void end() {
        OI.panel.stop();
        RioLogger.errorLog("PanelPullIn.stop() called");
    }
}