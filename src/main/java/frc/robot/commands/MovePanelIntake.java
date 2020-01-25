package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.OI;

public class MovePanelIntake extends Command {

    public MovePanelIntake() {
        
        super();
    }

    @Override
    protected void execute() {

        if (OI.gamePad.getRawButton(5)) {

            OI.panel.setIn();
        }

        if (OI.gamePad.getRawButton(8)) {

            OI.panel.setOut();
        }
    }

    @Override
    protected boolean isFinished() {

        return false;
    }

}