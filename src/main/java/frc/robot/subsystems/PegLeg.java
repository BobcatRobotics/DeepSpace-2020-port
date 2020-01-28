package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.command.Subsystem;

import frc.robot.lib.RioLogger;
import frc.robot.RobotMap;

public class PegLeg extends Subsystem {
    // Declare motor for pegleg
    private WPI_VictorSPX pegLegMotor;

    public PegLeg() {
        pegLegMotor = new WPI_VictorSPX(RobotMap.pegLegMotor);
        RioLogger.errorLog("PegLegRoller() created.");
    }

    public void drivepegmotor(double pegPwr) {
		pegLegMotor.set(pegPwr);
    }

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }
}