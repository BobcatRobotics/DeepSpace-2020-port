package frc.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
  // Drive  Train
	public static int driveRightMotorFront = 2;
	public static int driveRightMotorMiddle = 19;
	public static int driveRightMotorRear = 5;
	public static int driveLeftMotorFront = 1;
	public static int driveLeftMotorMiddle = 9;
	public static int driveLeftMotorRear = 11;
	
	// Drive Train Encoders
	public static int leftEncoderChannel1 = 2;
	public static int leftEncoderChannel2 = 3;
	public static int rightEncoderChannel1 = 0;
	public static int rightEncoderChannel2 = 1;

 	// Joy Sticks
	public static int leftJoystick = 0;
	public static int rightJoystick = 1;
	public static int gamePad = 2;

	//Elev motors
	/*Update this to match wiring*/
	public static int elevMotor1 = 0;
	public static int elevMotor2 = 1;	
	public static int elevMotor3 = 2;
	
	//Elev limit Switches
	/*Update this to match wiring*/
	public static int elevLSwitchT = 0;
	public static int elevLSwitchB = 1;
	//Wrist
	/*Update this to match wiring*/
	public static int wristSolenoid1 = 1;
	public static int wristSolenoid2 = 2;

	//Panel Intake
	public static int panelSolenoid = 0;

	//Cargo Roller
	public static int cargoMotor = 0;

}
