package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

import frc.robot.OI;

public class WristDeliever extends Command {
    boolean toggle = false;
    private CargoRollerInWrist cargoRollerInWrist;

    public WristDeliever() {
        super();
        requires(OI.wrist);
    }
    
    @Override
    protected void initialize() {
        // check the state, if 2, then run cargo in for 4 seconds
        if (OI.wrist.getWristState() == 2) {
            cargoRollerInWrist = new CargoRollerInWrist();
            cargoRollerInWrist.start();
        }
        OI.wrist.deliver();
    }

    @Override
    protected void execute() {
    }

    @Override
	protected boolean isFinished() {
		return true;
	}
}