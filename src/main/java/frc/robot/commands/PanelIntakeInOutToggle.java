package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.OI;
import frc.robot.lib.RioLogger;

public class PanelIntakeInOutToggle extends Command {
    private PanelPullInOnToggle panelPullInOnToggle;
    private int currentWristState;

    public PanelIntakeInOutToggle() {
        super();
        requires(OI.panel);
        RioLogger.errorLog("PanelIntakeInOutToggle() Created.");
    }

    @Override
    protected void initialize() {
        // Get current wrist state
        currentWristState = OI.wrist.getWristState();
        // If current wrist state is deployed (2)
        // don't bother spinning wheels, any other state, do it.
        if (currentWristState != 2) {
            panelPullInOnToggle = new PanelPullInOnToggle();
            panelPullInOnToggle.start();
        }
    }

    @Override
    protected void execute() {
        // If the current wrist state is deployed (2)
        // then don't toggle the panel intake,
        // let it stay in (driven in WristDeployed()
        // by PanelInOutSetToIn()).  Any other state, do it.
        if (currentWristState != 2) {
            OI.panel.panelInOutToggle();
        }
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}