package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

import frc.robot.OI;

public class ShiftHigh extends Command {
    private boolean shifterState = true;

    public ShiftHigh() {
        super();
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        shifterState = true;
        // DriverStation.reportError(" In ShiftHigh, shifterState = " + shifterState,true);
        OI.shifter.set(shifterState);
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
    }
}
