package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.OI;
import frc.robot.lib.RioLogger;

public class PanelPullinBiasWhenOut extends Command {

    public PanelPullinBiasWhenOut() {
        super();
        requires(OI.panel);
    }

    @Override
    protected void initialize() {
        super.initialize();
        // setTimeout(180);
        OI.panel.pullPanelBias();
        RioLogger.errorLog("PanelPullinBiasWhenOut.initialize() called");
    }

    @Override
    protected void execute() {
    }

    @Override
    protected boolean isFinished() {
        if (!OI.panel.panelIntakeIsOut()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void interrupted() {
        OI.panel.stop();
        RioLogger.errorLog("PanelPullinBiasWhenOut.interrupted() called");
    }

    @Override
    protected void end() {
        OI.panel.stop();
        RioLogger.errorLog("PanelPullinBiasWhenOut.stop() called");
    }
}