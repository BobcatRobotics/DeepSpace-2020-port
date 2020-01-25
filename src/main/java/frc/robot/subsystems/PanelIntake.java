package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
// import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.lib.RioLogger;

public class PanelIntake extends Subsystem {
    private Solenoid solenoid1;
    private Solenoid solenoid2;
    // private WPI_VictorSPX panelMotor; // Was Victor
    private WPI_TalonSRX panelMotor;     // Now talon if we want current limiting
    private boolean panelInOutState=false;
    private double panelSpeed = 0.0;

    public PanelIntake() {
        solenoid1 = new Solenoid(RobotMap.panelGripSolenoid);
        solenoid2 = new Solenoid(RobotMap.panelInOutSolenoid);
        // panelMotor = new WPI_VictorSPX(RobotMap.panelIntakeMotor); // Was Victor
        panelMotor = new WPI_TalonSRX(RobotMap.panelIntakeMotor);
        RioLogger.errorLog("PanelIntake() Created.");
    }

    public void holdPanel() {
        solenoid1.set(false);
        RioLogger.errorLog("PanelIntake.holdPanel() solenoid1=true");
    }

    public void releasePanel() {
        solenoid1.set(true);
        RioLogger.errorLog("PanelIntake.releasePanel() solenoid1=false");
    }

    public void panelInOutToggle() {
        panelInOutState = !panelInOutState;
        solenoid2.set(panelInOutState);
        RioLogger.errorLog("PanelIntake.panelInOutToggle() solenoid toggle");
    }

    public boolean panelIntakeIsOut() {
        return panelInOutState;
    }

    public void panelInOutSetToIn() {
        solenoid2.set(false);
        panelInOutState = false;
    }

    public void panelInOutSetToOut() {
        solenoid2.set(true);
        panelInOutState=true;
    }

    public void pushPanel() {
        panelMotor.set(0.7);
        panelSpeed = 0.7;
    }

    public void pullPanel() {
        panelMotor.set(-1.0);
        panelSpeed = -1.0;
    }

    public void pullPanelBias() {
        panelMotor.set(-0.25);
        panelSpeed = -0.25;
    }
    public void stop() {
        panelMotor.set(0.0);
        panelSpeed = 0.0;
    }

    public double getMotorSpeed() {
        return panelMotor.get();
    }

    public double getCurrentSpeed() {
        return panelSpeed;
    }
    public void reset() {
        holdPanel();
        panelInOutSetToIn();
        stop();
        RioLogger.errorLog("PanelIntake.reset()");
    }

    @Override
	public void initDefaultCommand() {
	  // Set the default command for a subsystem here.
	  // setDefaultCommand(new MySpecialCommand());
	}
}