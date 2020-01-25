package frc.robot.commands;

import frc.robot.OI;
import frc.robot.RobotMap;
import edu.wpi.first.wpilibj.command.Command;

public class MoveElevator extends Command {
    private double cmdDeadBand=0.01;       // 1% of deadband about neutral stick
    private int elevPresetReading = -1;    // See if the DPAD is pressed
    private int moveCommandMode = 0;       // 0=operator, 1=low, 2=mid, 3=high


    public MoveElevator() {
        super();
        requires(OI.elev1);
    }

    protected int checkSwitches() {
        return OI.elev1.checkSwitches();
    }

    @Override
    protected void execute() {

        // Get the elevator up/down command from the gamepad thumbstick
        double elev = OI.gamePad.getRawAxis(RobotMap.gamePadLeftPwrStick);

        // Read the gamepad POV buttons
        int elevPreset = OI.gamePad.getPOV();

        // Put some deadband on the elevator command
        //   (if stick is between +/- cmdDeadBand make command 0.0)
        if (elev < cmdDeadBand) {
            if (elev > -1.0*cmdDeadBand) {
                elev = 0.0;
            }
        }

        double motorSpeed =  elev;  // Copy elevator command to motor speed

        // Adjust motorSpeed request based on state of robot
        if (!OI.elev1.isLimDisable()) {          // If Limits are not disabled
            if (OI.lock.isLocked()) {              // And the wrist Lock is engaged
                if (OI.elev1.lowerLimit()) {          // And the elevator is on lower limit switch
                    //if (motorSpeed > 0.3) {           // Then only let the operator push the elevator
                    //       motorSpeed = 0.3;          // down (lift the bot) at up to 30% 
                    //}
                    // Instead of capping the command at 30% down, scale the command
                    // by .3 to give the operator's thumb stick full travel to get to 30%
                    // if he wants to adjust how hard we push down after we've climbed.
                    if (motorSpeed > 0.0) {
                        motorSpeed = motorSpeed * 0.10;
                    }
                  }
            } else {                               // The wrist lock is not engaged
                if (motorSpeed > 0.7) {               // Limit the elevator drive down command
                    motorSpeed = 0.7;                 // to maxDownCmd when limits enabled & not trying
                }                                     // to climb (not locked).
            if (OI.elev1.lowerLimit()) {          // And the elevator is on the lower limit switch
                    if (motorSpeed > 0.0) {           // Then don't let the operator push the elevator
                           motorSpeed = 0.0;          // down anymore.
                       }
                  }
           }
            if (OI.elev1.upperLimit()) {          // Regardless of wrist lock positon if the limits
                if (motorSpeed < 0.0) {           //   are not disabled, and the upper limit is active
                    motorSpeed = 0.0;             //   don't let the operator command the elavator up.
                }
            }
        }  // End of is Limit Disabled block

        // If the finalMotor speed isn't 0.0, then call the elevator subsystem
        // to drive to the request, if it is zero, Check to see if any presets
        // are hit or had been hit, if so keep calling those presets
        // if not, then send zero command to Elevator subsystem
        if (motorSpeed != 0.0) {
           OI.elev1.elevate(motorSpeed);  // Call the elevator with the final motor speed
           moveCommandMode=0;
        } else {
            // Preset for low is pressed or was pressed last
            if ((elevPreset == 180) || (moveCommandMode == 1)) {
                OI.elev1.goToLow();
                moveCommandMode=1;
            }
            // Preset for low is pressed or was pressed last
            if ((elevPreset == 270) || (moveCommandMode == 2)) {
                OI.elev1.goToMid();
                moveCommandMode=2;
            }
            // Preset for low is pressed or was pressed last
            if ((elevPreset == 0) || (moveCommandMode == 3)) {
                OI.elev1.goToHigh();
                moveCommandMode=3;
            }
            if (moveCommandMode == 0) {
                OI.elev1.elevate(motorSpeed);
            }
        }
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
        OI.elev1.stop();
    }
}