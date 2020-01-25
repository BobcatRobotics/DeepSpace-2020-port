package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.lib.RioLogger;

public class DriveTrain extends Subsystem {
	/** Inverts drive direction **/
	private static final double INVERT_MOTOR = -1.0;
	
	private WPI_VictorSPX leftFront;
	private WPI_VictorSPX leftMiddle;
	private WPI_VictorSPX leftRear;
	private WPI_VictorSPX rightFront;
	private WPI_VictorSPX rightMiddle;
	private WPI_VictorSPX rightRear;
	private boolean invertLeft = true;
	private double leftPower = 0.0;
	private double rightPower = 0.0;

	public DriveTrain() {
		super();
		// Initialize Drive Train
		leftFront = new WPI_VictorSPX(RobotMap.driveLeftMotorFront);
		leftMiddle = new WPI_VictorSPX(RobotMap.driveLeftMotorMiddle);
		leftRear = new WPI_VictorSPX(RobotMap.driveLeftMotorRear);
		rightFront = new WPI_VictorSPX(RobotMap.driveRightMotorFront);
		rightMiddle = new WPI_VictorSPX(RobotMap.driveRightMotorMiddle);
		rightRear = new WPI_VictorSPX(RobotMap.driveRightMotorRear);
		setLeftMotorsReverse(false);
		RioLogger.log("DriveTrain() created.");
		leftFront.configVoltageCompSaturation(12);
		leftFront.enableVoltageCompensation(true);
		leftMiddle.configVoltageCompSaturation(12);
		leftMiddle.enableVoltageCompensation(true);
		leftRear.configVoltageCompSaturation(12);
		leftRear.enableVoltageCompensation(true);
		rightFront.configVoltageCompSaturation(12);
		rightFront.enableVoltageCompensation(true);
		rightMiddle.configVoltageCompSaturation(12);
		rightMiddle.enableVoltageCompensation(true);
		rightRear.configVoltageCompSaturation(12);
		rightRear.enableVoltageCompensation(true);
		leftFront.setNeutralMode(NeutralMode.Coast);
		leftMiddle.setNeutralMode(NeutralMode.Coast);
		leftRear.setNeutralMode(NeutralMode.Coast);
		rightFront.setNeutralMode(NeutralMode.Coast);
		rightMiddle.setNeutralMode(NeutralMode.Coast);
		rightRear.setNeutralMode(NeutralMode.Coast);
	}
	
	// Put methods for controlling this subsystem here. Call these from Commands.
	public void setLeftMotorsReverse(boolean invert) {
		invertLeft = invert;
	}
	
	public double getLeftPower() {
		return leftPower;
	}

	public void setLeftPower(double leftPwr) {
		if (leftPwr > 1.0)
			leftPwr = 1.0;
		else
		if (leftPwr < -1.0)
			leftPwr = -1.0;
			//was leftPwr = 1.0, we thought it should be -1.0
		this.leftPower = leftPwr;
	}

	public double getRightPower() {
		return rightPower;
	}

	public void setRightPower(double rightPwr) {
		if (rightPwr > 1.0)
			rightPwr = 1.0;
		else
		if (rightPwr < -1.0)
			rightPwr = -1.0;
			//was rightPwr = 1.0, we thought it should be -1.0
		this.rightPower = rightPwr;
	}

	public void drive() {
		drive(leftPower,rightPower);
	}
	
	public void drive(double leftPwr, double rightPwr) {
		if (invertLeft)
			leftPwr *= INVERT_MOTOR;
		else
			rightPwr *= INVERT_MOTOR;
		
		leftFront.set(leftPwr);
		leftMiddle.set(leftPwr);
		leftRear.set(leftPwr);
		rightFront.set(rightPwr);
		rightMiddle.set(rightPwr);
		rightRear.set(rightPwr);
	}

	public void stop() {
		leftPower = 0.0;
		rightPower = 0.0;
		leftFront.stopMotor();
		leftMiddle.stopMotor();
		leftRear.stopMotor();
		rightFront.stopMotor();
		rightMiddle.stopMotor();
		rightRear.stopMotor();;
	}

	public void reset() {
		leftPower = 0.0;
		rightPower = 0.0;
	}
	
	public void setBrakeMode(){
		leftFront.setNeutralMode(NeutralMode.Brake);
		leftMiddle.setNeutralMode(NeutralMode.Brake);
		leftRear.setNeutralMode(NeutralMode.Brake);
		rightFront.setNeutralMode(NeutralMode.Brake);
		rightMiddle.setNeutralMode(NeutralMode.Brake);
		rightRear.setNeutralMode(NeutralMode.Brake);
	}

	public void setCoastMode(){
		leftFront.setNeutralMode(NeutralMode.Coast);
		leftMiddle.setNeutralMode(NeutralMode.Coast);
		leftRear.setNeutralMode(NeutralMode.Coast);
		rightFront.setNeutralMode(NeutralMode.Coast);
		rightMiddle.setNeutralMode(NeutralMode.Coast);
		rightRear.setNeutralMode(NeutralMode.Coast);
	}

	public void setVoltageComp(double voltageComp){
		leftFront.configVoltageCompSaturation(voltageComp);
		leftMiddle.configVoltageCompSaturation(voltageComp);
		leftRear.configVoltageCompSaturation(voltageComp);
		rightFront.configVoltageCompSaturation(voltageComp);
		rightMiddle.configVoltageCompSaturation(voltageComp);
		rightRear.configVoltageCompSaturation(voltageComp);


	}

	@Override
	public void initDefaultCommand() {
	  // Set the default command for a subsystem here.
	  // setDefaultCommand(new MySpecialCommand());
	}
}