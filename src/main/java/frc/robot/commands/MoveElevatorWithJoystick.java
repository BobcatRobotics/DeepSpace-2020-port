package frc.robot.commands;

import frc.robot.OI;
import frc.robot.RobotMap;

public class MoveElevatorWithJoystick extends MoveElevator {

    public MoveElevatorWithJoystick() {

        super();
    }
    
    @Override
    protected void execute() {

        elev = OI.gamePad.getRawAxis(RobotMap.leftJoystick);
        double motorSpeed = -1*elev;
        if (OI.limitOn) {

            if ((motorSpeed > 0.0) || (motorSpeed < 0.0)) {
                motorSpeed = 0.0;
            }
        }

    }
    
}