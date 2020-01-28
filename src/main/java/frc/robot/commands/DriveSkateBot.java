/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

import frc.robot.OI;

public class DriveSkateBot extends Command {
	
	public DriveSkateBot() {
		super();
	}

	@Override
	protected void execute() {
		// Driving
		double left = OI.gamePad.getRawAxis(Joystick.AxisType.kY.value);
		double right = OI.gamePad.getRawAxis(Joystick.AxisType.kTwist.value);
		if (Math.abs(right) < 0.02) {
			right = 0.0;
			//done to prevent motor wear, in case of joystick doesn't center
		}

		if (Math.abs(left) < 0.02) {
			left = 0.0;
			//done to prevent motor wear, in case of joystick doesn't center
		}

		//DriverStation.reportError("left stick value: " + left + " right stick value " + right, false);
		OI.driveTrain.setLeftPower(left);
		OI.driveTrain.setRightPower(right);
		OI.driveTrain.drive();
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		OI.driveTrain.stop();
	}
}
