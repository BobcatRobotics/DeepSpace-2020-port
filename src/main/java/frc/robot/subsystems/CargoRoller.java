package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import frc.robot.RobotMap;

public class CargoRoller {

    private WPI_TalonSRX cargoMotor;

    private double cargoSpeed = 0.0;

    public CargoRoller() {

        cargoMotor = new WPI_TalonSRX(RobotMap.cargoMotor);
    }

    public void deposit() {

        cargoMotor.set(.5);

        cargoSpeed = .5;
    }

    public void intake() {

        cargoMotor.set(-1.0);

        cargoSpeed = -1.0;
    }

    public double getMotorSpeed() {

        return cargoMotor.get();
    }

    public double getCurrentSpeed() {

        return cargoSpeed;
    }
}