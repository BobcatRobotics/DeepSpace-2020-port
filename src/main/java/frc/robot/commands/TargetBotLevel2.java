/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.OI;
import frc.robot.RobotMap;
import frc.robot.lib.RioLogger;
import frc.robot.lib.RioLoggerThread;

public class TargetBotLevel2 extends Command {
	private static double DESIRED_TARGET_AREA = 4.8; // Area of the target when the robot reaches the wall
	private static double DRIVE_K = 0.15; // 0.15 how hard to drive fwd toward the target
	private static double STEER_K = 0.035; // how hard to turn toward the target
	private static double X_OFFSET = 0.0;  // 1.45 The number of degrees camera is off center

	// The following fields are updated by the LimeLight Camera
	private boolean hasValidTarget = false;
	private double driveCommand = 0.0;
	private double steerCommand = 0.0;

	// The following fields are updated by the state of the Command
	private boolean ledsON = false;
	private TargetMode mode = TargetMode.TARGET;
	private int timeCounter = 0;
	private static int TIME_COUNT_LOOP = 50; // 50 times / second
	private static double TA_DISTANCE = 2.0;
	private Log log = new Log();

	public TargetBotLevel2() {
		super();
		requires(OI.driveTrain);
		requires(OI.limelight);
		requires(OI.elev1);
		initializeCommand();
		RioLogger.errorLog("TargetBotLevel2 Command Initialized");
	}

	@Override
	protected void execute() {
		// Turn on the LED's if they haven't been turned on before
		if (!ledsON) {
			OI.limelight.turnOnLED();
			ledsON = true;
			RioLogger.log("TargetBotLevel2.execute() LED turned on");
			mode = TargetMode.TARGET;
		}

		// Targeting 
		if (mode == TargetMode.TARGET) {
			SmartDashboard.putString("Limelight.Target Mode = ","Target");
			Update_Limelight_Tracking();
			//Determine left and right targets for more agressive steering
			double steerAdjustLeft = 0.0;
			double steerAdjustRight = -0.18;
			double leftPwr = (driveCommand + steerCommand + steerAdjustLeft) * -1.0;
			double rightPwr = (driveCommand - steerCommand - steerAdjustRight) * -1.0;
	
			OI.driveTrain.setLeftPower(leftPwr);
			OI.driveTrain.setRightPower(rightPwr);
			OI.driveTrain.drive();
			SmartDashboard.putBoolean("Limelight.TargetIdentified", hasValidTarget);
			SmartDashboard.putNumber("LimeLight.RightPower", rightPwr);
			SmartDashboard.putNumber("LimeLight.LeftPower", leftPwr); 

			if (OI.limelight.targetArea()  > TA_DISTANCE) {
				mode = TargetMode.ELEVATOR;
			}
		}
		if (mode ==  TargetMode.ELEVATOR) {
			SmartDashboard.putString("Limelight.Target Mode = ","Elevator");
			OI.elev1.goToMid();
			mode = TargetMode.DRIVE;
			timeCounter = 0;
		}
		if (mode == TargetMode.DRIVE) {
			OI.driveTrain.drive(0.2, 0.2); // Drive Straight
			timeCounter++;
			if (timeCounter > TIME_COUNT_LOOP)
				mode = TargetMode.END;
		}
		RioLoggerThread.log(log.logLine());
	}

	@Override
	protected boolean isFinished() {
		boolean stop = false;
		if (!OI.rightStick.getRawButton(RobotMap.targetBot)) {
			stop = true;
		}
		if (mode == TargetMode.END) {
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
		RioLogger.errorLog("TargetBotLevel2 command finished.");
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
	}

    enum TargetMode {
		TARGET,
		ELEVATOR,
		DRIVE,
		END
	} 
}
