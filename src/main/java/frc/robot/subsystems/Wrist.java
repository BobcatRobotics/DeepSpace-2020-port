package frc.robot.subsystems;


import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.Trigger;
import frc.robot.RobotMap;

public class Wrist {
    public static Solenoid solenoid1 = new Solenoid(RobotMap.wristSolenoid1); /* Controls the wrist */
    public static Solenoid solenoid2 = new Solenoid(RobotMap.wristSolenoid2); /* Controls the wrist */

    public void setBothSolenoids(boolean on) {
        solenoid1.set(on);
        solenoid2.set(on);
    }
    public void setDifferentSolenoids(boolean on) {
        solenoid1.set(on);
        solenoid2.set(!on);
    }
    public void reset() {
        solenoid1.set(false);
        solenoid2.set(false);
    }
}