package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.OI;

public class RunWrist extends Command {
    boolean toggle = false;

    public RunWrist() {
		super();
    }
    
    
    @Override
    protected void execute () {
        //TODO update these once the wrist is more defined
        //Possibly extend to individual commands
        if(OI.gamePad.getRawButtonPressed(2)){
            OI.wrist.setBothSolenoids(true);
        }
        if(OI.gamePad.getRawButtonPressed(3)){
            OI.wrist.setDifferentSolenoids(true);
        }
        if(OI.gamePad.getRawButtonPressed(4)){
            OI.wrist.setBothSolenoids(false);
        }
    }

    @Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		OI.wrist.reset();
	}
    
}