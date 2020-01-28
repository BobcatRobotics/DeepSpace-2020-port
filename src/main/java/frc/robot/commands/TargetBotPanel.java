package frc.robot.commands;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.OI;
import frc.robot.RobotMap;
import frc.robot.lib.RioLogger;
import frc.robot.lib.RioLoggerThread;

public class TargetBotPanel extends Command {
    private ShuffleboardTab tab = Shuffleboard.getTab("TargetTuning");
    private NetworkTableEntry tgt_area = tab.add("Target Area", 4.8).getEntry();
	private NetworkTableEntry drive_k = tab.add("Drive K", 0.15).getEntry();
	private NetworkTableEntry steer_k = tab.add("Steer K", 0.035).getEntry();
    private NetworkTableEntry x_offset = tab.add("X Offset", 0.0).getEntry();
    
    private static double DESIRED_TARGET_AREA; // Area of the target when the robot reaches the wall
	private static double DRIVE_K; // 0.15 how hard to drive fwd toward the target
	private static double STEER_K; // how hard to turn toward the target
	private static double X_OFFSET;  // 1.45 The number of degrees camera is off center

	// The following fields are updated by the LimeLight Camera
	private boolean hasValidTarget = false;
	private double driveCommand = 0.0;
	private double steerCommand = 0.0;

	// The following fields are updated by the state of the Command
	private boolean ledsON = false;
	private boolean isTargeting = false;
	private Log log = new Log();

	public TargetBotPanel() {
		super();
		requires(OI.driveTrain);
		requires(OI.limelight);
		initializeCommand();
        RioLogger.errorLog("TargetSkatebot Command Initialized");
	}

	@Override
	protected void execute() {
		// Turn on the LED's if they haven't been turned on before
		if (!ledsON) {
			OI.limelight.turnOnLED();
			ledsON = true;
            RioLogger.log("TargetSkateBot.execute() LED turned on");
            DESIRED_TARGET_AREA  = tgt_area.getDouble(0.0); 
            DRIVE_K = drive_k.getDouble(0.0); 
            STEER_K = steer_k.getDouble(0.0);
            X_OFFSET = x_offset.getDouble(0.0);  
            RioLogger.errorLog("TargetSkateBot.execute() tgt_area " + DESIRED_TARGET_AREA);
            RioLogger.errorLog("TargetSkateBot.execute() drive k " + DRIVE_K);
            RioLogger.errorLog("TargetSkateBot.execute() steer k" + STEER_K);
            RioLogger.errorLog("TargetSkateBot.execute() x offset " + X_OFFSET);
            RioLoggerThread.log(log.logHeader());
		}

		// Driving
		Update_Limelight_Tracking();
		double leftTarget = OI.limelight.leftTarget();
		double rightTarget = OI.limelight.rightTarget();
		
		//Determine left and right targets for more agressive steering
		double steerAdjustLeft = 0.0;
		double steerAdjustRight = -0.18;
		// if(leftTarget > rightTarget){
		// 	steerAdjustLeft = 0.15;
		// }
		// if (rightTarget > leftTarget){
		// 	steerAdjustRight = 0.15;
		// }

		double leftPwr = (driveCommand + steerCommand + steerAdjustLeft) * -1.0;
		double rightPwr = (driveCommand - steerCommand - steerAdjustRight) * -1.0;

		OI.driveTrain.setLeftPower(leftPwr);
		OI.driveTrain.setRightPower(rightPwr);
		OI.driveTrain.drive();
		SmartDashboard.putBoolean("Limelight.TargetIdentified", hasValidTarget);
		SmartDashboard.putNumber("LimeLight.RightPower", rightPwr);
		SmartDashboard.putNumber("LimeLight.LeftPower", leftPwr);

		log.leftPwr = leftPwr;
		log.rightPwr = rightPwr;
		RioLoggerThread.log(log.logLine());
	}

	@Override
	protected boolean isFinished() {
		boolean stop = false;
		// if (isTargeting) {
		// if (!hasValidTarget) {
		// stop = true;
		// }
		// if (speedToTarget < 0.01) {
		// stop = true;
		// }
		// }
		if (!OI.rightStick.getRawButton(RobotMap.targetBot)) {
			stop = true;
		}
		// if((DESIRED_TARGET_AREA - OI.limelight.targetArea()) <= 0){
		// 	stop = true;
		// }
		return stop;
	}

	@Override
	protected void end() {
		OI.driveTrain.stop();
		OI.limelight.turnOffLED();
		RioLogger.errorLog("TargetSkateBot command finished.");
		initializeCommand();
	}

	/**
	 * This function implements a simple method of generating driving and steering
	 * commands based on the tracking data from a limelight camera.
	 */
	public void Update_Limelight_Tracking() {
		//double drive_k = 0.13;
		//double steer_k = 0.012;
		//Tunning parameters

		hasValidTarget = OI.limelight.hasTargets();
		if (!hasValidTarget) {
			return;
		}
		isTargeting = true;
		// double ty = OI.limelight.y();
		double tx = OI.limelight.x();
		double ta = OI.limelight.targetArea();
	
		log.tx = tx;
		log.ta = ta;

		// Start with proportional steering

		steerCommand = (tx - X_OFFSET) * STEER_K;
		SmartDashboard.putNumber("Limelight.SteerCommand", steerCommand);
		if( DESIRED_TARGET_AREA - ta < 1){
			steerCommand = 0.0;
		}
		// try to drive forward until the target area reaches our desired area
		driveCommand = (DESIRED_TARGET_AREA - ta) * DRIVE_K;
		SmartDashboard.putNumber("Limelight.DriveCommand", driveCommand);

		log.drvCmd = driveCommand;
		log.strCmd = steerCommand;
	}

	private void initializeCommand() {
		ledsON = false;
		isTargeting = false;
	}

	class Log {
		double ta = 0.0;
		double tx = 0.0;
		double drvCmd = 0.0;
		double strCmd = 0.0;
		double leftPwr = 0.0;
		double rightPwr = 0.0;

		public String logLine() {
			return String.format("%6.4f %6.4f %6.4f %6.4f %6.4f %6.4f", ta, tx, drvCmd, strCmd, leftPwr, rightPwr);
        }
        public String logHeader() {
            return "ta tx ta0 ta1 ts0 ts1 drvCmd strCmd leftPwr rightPwr";
        }
        public String logTrailer() {
            return "===========================================================";
        }
	}
}