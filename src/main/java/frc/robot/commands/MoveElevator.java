package frc.robot.commands;

import frc.robot.OI;
import frc.robot.subsystems.ElevatorState;
import edu.wpi.first.wpilibj.command.Command;

public abstract class MoveElevator extends Command {

    protected double elev = 0.0;

    public MoveElevator() {
        
        super();
    }

    protected void intitalize() {

        OI.elev1.reset();
    }

    protected ElevatorState checkSwitches() {

        if (OI.elev1.upperLimit()) {
            
            return ElevatorState.LIMIT_UP;
        } else if (OI.elev1.lowerLimit()) {
            
            return ElevatorState.LIMIT_DOWN;
        } else {

            return ElevatorState.NONE;
        }
    }

    protected abstract void execute();
    
    protected boolean isFinished() {

        return false;
    }

    protected void end() {

        OI.elev1.stop();
    }

    protected void interruppted() {

        OI.elev1.stop();
    }
}