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
import frc.robot.subsystems.DriveTrain;
import edu.wpi.first.wpilibj.Joystick;


public class TargetBot extends Command {
	private static double DESIRED_TARGET_AREA = 4.6; // Area of the target when the robot reaches the wall
	private static double DRIVE_K = 0.15; // 0.15 how hard to drive fwd toward the target
	private static double STEER_K = 0.035; // 0.35 how hard to turn toward the target
	private static double STEER_I = 0.01;
	private static double X_OFFSET = 0.0; // 1.45 The number of degrees camera is off center

	// The following fields are updated by the LimeLight Camera
	private boolean hasValidTarget = false;
	private double driveCommand = 0.0;
	private double steerCommand = 0.0;

	// The following fields are updated by the state of the Command
	private boolean ledsON = false;
	private boolean isTargeting = false;
	private Log log = new Log();

	public TargetBot() {
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
		}

		// Driving
		Update_Limelight_Tracking();
		double leftTarget = OI.limelight.leftTarget();
		double rightTarget = OI.limelight.rightTarget();

		// Determine left and right targets for more agressive steering
		double minLeftPwr = 0.06;
		double minRightPwr = 0.06; // -0.18

		driveCommand = OI.leftStick.getRawAxis(Joystick.AxisType.kY.value) * -1.0;

		double steerCommandSign = Math.signum(steerCommand);
		double minSteerCommand = 0.15;
		if(Math.abs(steerCommand) < minSteerCommand){
			steerCommand = minSteerCommand * steerCommandSign;
		}

		double driveSign = Math.signum(driveCommand);

		double leftBias = driveSign * 0.00;
		double rightBias = driveSign * 0.05;

		double leftPwr = (driveCommand + steerCommand  + leftBias) * -1.0;
		double rightPwr = (driveCommand - steerCommand + rightBias ) * -1.0;

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
		// stop = true;
		// }
		return stop;
	}

	@Override
	protected void end() {
		OI.driveTrain.stop();
		OI.limelight.turnOffLED();
		RioLogger.errorLog("TargetSkateBot command finished.");
		//OI.driveTrain.setCoastMode();
		OI.driveTrain.setVoltageComp(12);
		initializeCommand();
	}

	/**
	 * This function implements a simple method of generating driving and steering
	 * commands based on the tracking data from a limelight camera.
	 */
	public void Update_Limelight_Tracking() {
		// double drive_k = 0.13;
		// double steer_k = 0.012;
		// Tunning parameters

		hasValidTarget = OI.limelight.hasTargets();
		if (!hasValidTarget) {
			driveCommand = 0.0;
			steerCommand = 0.0;
			return;
		}
		isTargeting = true;
		// double ty = OI.limelight.y();
		double tx = OI.limelight.x();
		double ta = OI.limelight.targetArea();

		log.tx = tx;
		log.ta = ta;

		// Start with proportional steering
		double targetAreaError = DESIRED_TARGET_AREA - ta;

		steerCommand = (tx - X_OFFSET) * STEER_K;
		// try to drive forward until the target area reaches our desired area
		driveCommand = (targetAreaError) * DRIVE_K;
		SmartDashboard.putNumber("Limelight.SteerCommand", steerCommand);
		if (targetAreaError < 1) {
			steerCommand = 0.0;
		}
		if (targetAreaError < 0) {
			driveCommand = 0.0;
			steerCommand = 0.0;
		}

		SmartDashboard.putNumber("Limelight.DriveCommand", driveCommand);

		log.drvCmd = driveCommand;
		log.strCmd = steerCommand;
	}

	private void initializeCommand() {
		ledsON = false;
		isTargeting = false;
		//OI.driveTrain.setBrakeMode();
		double voltageComp = 9;
		OI.driveTrain.setVoltageComp(voltageComp);
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
}
