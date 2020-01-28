package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.command.Subsystem;

import frc.robot.lib.RioLogger;
import frc.robot.RobotMap;

public class CargoRoller extends Subsystem {
    private WPI_VictorSPX cargoMotor;
    private double cargoSpeed = 0.0;

    public CargoRoller() {
        cargoMotor = new WPI_VictorSPX(RobotMap.cargoMotor);
        RioLogger.errorLog("CargoRoller() created.");
    }

    public void deposit() {
        cargoMotor.set(-.5);
        cargoSpeed = -.5;
    }

    public void intake() {
        cargoMotor.set(1.0);
        cargoSpeed = 1.0;
    }

    public void stop() {
        cargoMotor.set(0.0);
        cargoSpeed = 0.0;
    }

    public double getMotorSpeed() {
        return cargoMotor.get();
    }

    public double getCurrentSpeed() {
        return cargoSpeed;
    }

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }
}