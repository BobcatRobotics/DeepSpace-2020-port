package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.lib.RioLogger;

public class Wrist  extends Subsystem {
    public static int wristState = 0; // 0=Stowed position, 1=Deliver position, 2=Deployed position
    public static Solenoid solenoid1 = new Solenoid(RobotMap.wristSolenoid1);
    public static Solenoid solenoid2 = new Solenoid(RobotMap.wristSolenoid2);

    public void deploy() {
        solenoid1.set(true);
        solenoid2.set(false);
        wristState = 2;
        RioLogger.errorLog("Wrist.deploy() called. solenoid1=true, solenoid2=false");
    }

    public void deliver() {
        solenoid1.set(false);
        solenoid2.set(true);
        wristState = 1;
    }

    public void stow() {
        solenoid1.set(false);
        solenoid2.set(false);
        wristState = 0;
    }

    public void reset() {
        solenoid1.set(false);
        solenoid2.set(false);
        wristState = 0;
    }

    public int getWristState() {
        return(wristState);
    }

    public void deployphase1() {
        solenoid1.set(true);
        wristState = 2;
    }

    public void deployphase2() {
        solenoid2.set(false);
        wristState = 2;
    }
    public void displayDashboard() {
        SmartDashboard.putNumber("Current Wrist State", wristState);
    }

    @Override
	public void initDefaultCommand() {
	  // Set the default command for a subsystem here.
	  // setDefaultCommand(new MySpecialCommand());
	}
}