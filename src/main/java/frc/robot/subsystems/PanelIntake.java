package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import frc.robot.RobotMap;

public class PanelIntake {

    public static Solenoid solenoid1 = new Solenoid(RobotMap.panelSolenoid);

    public void setIn() {

        solenoid1.set(true);
    }

    public void setOut() {

        solenoid1.set(false);
    }

    public void reset() {
        setOut();
    }
}