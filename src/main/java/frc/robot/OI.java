package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
// import edu.wpi.first.wpilibj.buttons.Button;
// import edu.wpi.first.wpilibj.buttons.JoystickButton;
// import edu.wpi.first.wpilibj.buttons.Trigger;
import frc.robot.lib.RioLogger;
import frc.robot.lib.RioLoggerThread;
import frc.robot.lib.SmartDashLog;
import frc.robot.subsystems.CargoRoller;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.PanelIntake;
import frc.robot.subsystems.Wrist;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
  // Loggers
  public static RioLoggerThread logFile;
  public static SmartDashLog smartLog = new SmartDashLog();

  // Drive Chain Subsystem
  public static DriveTrain driveTrain = new DriveTrain();
  
  //Wrist Subsystem
  public static Wrist wrist = new Wrist();

  // Joysticks
  public static Joystick rightStick = new Joystick(RobotMap.rightJoystick);
  public static Joystick leftStick = new Joystick(RobotMap.leftJoystick);
  public static Joystick gamePad = new Joystick(RobotMap.gamePad);

  //Elevator
  public static Elevator elev1 = new Elevator();
  public static boolean limitOn = true;

  //Panel Intake
  public static PanelIntake panel = new PanelIntake();

  //Cargo Roller
  public static CargoRoller cargo = new CargoRoller();

  // Buttons
  //public static Button btnCubePickup = new JoystickButton(gamePad, RobotMap.gamePadCubePickup);

  // Triggers
  //public static Trigger trigShifter = new JoystickButton(rightStick, RobotMap.rightJoystickShifter);

  static {
   
    // Start Logging Thread
    logFile = RioLoggerThread.getInstance();
    RioLogger.log("OI static block finished.");
  }
}
