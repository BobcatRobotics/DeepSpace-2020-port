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

public class DriveWithJoysticks extends Command {
	private double left = 0.0;
	private double right = 0.0;
	private double pegPwr = 0.0;
	
	public DriveWithJoysticks() {
		super();
	}

	@Override
	protected void execute() {
		// Driving
		left = OI.leftStick.getRawAxis(Joystick.AxisType.kY.value);
		right = OI.rightStick.getRawAxis(Joystick.AxisType.kY.value);
		if (Math.abs(right) < 0.08) {
			right = 0.0;
			//done to prevent motor wear, in case of joystick doesn't center
		}

		if (Math.abs(left) < 0.08) {
			left = 0.0;
			//done to prevent motor wear, in case of joystick doesn't center
		}

		//DriverStation.reportError("left stick value: " + left + " right stick value " + right, false);
		OI.driveTrain.setLeftPower(left);
		OI.driveTrain.setRightPower(right);
		OI.driveTrain.drive();

		// Check if the lock is in the locked state, and if so, then drive
		// the peg leg motor too
		if (OI.lock.isLocked()) {
			if (left >= right) {
				pegPwr = left;
			} else {
				pegPwr = right;
			}

			if (pegPwr > 0.0) {
				pegPwr=0.0;
			}

			OI.pegleg.drivepegmotor(pegPwr);
		} else {
			OI.pegleg.drivepegmotor(0.0);
		}
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
