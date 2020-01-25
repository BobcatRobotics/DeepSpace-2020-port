package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.RobotMap;


public class Elevator {

    private WPI_TalonSRX elevatorMotor1;
    private WPI_TalonSRX elevatorMotor2;
    private WPI_TalonSRX elevatorMotor3;

    private DigitalInput tLimit;
    private DigitalInput bLimit;

    private double elevatorSpeed = 0.0;


    public Elevator() {

        elevatorMotor1 = new WPI_TalonSRX(RobotMap.elevMotor1);
        elevatorMotor2 = new WPI_TalonSRX(RobotMap.elevMotor2);
        elevatorMotor3 = new WPI_TalonSRX(RobotMap.elevMotor3);

        tLimit = new DigitalInput(RobotMap.elevLSwitchT);
        bLimit = new DigitalInput(RobotMap.elevLSwitchB);

        reset();
    }

    public void reset() {

        elevatorMotor1.set(0.0);
        elevatorMotor2.set(0.0);
        elevatorMotor3.set(0.0);

        elevatorSpeed = 0.0;
    }

    public void stop() {

        elevatorMotor1.stopMotor();
        elevatorMotor2.stopMotor();
        elevatorMotor3.stopMotor();

        elevatorSpeed = 0.0;
    }

    public void elevate(double speed) {

        elevatorMotor1.set(speed);
        elevatorMotor2.set(speed);
        elevatorMotor3.set(speed);

        elevatorSpeed = speed;
    }

    public double getMotor1Speed() {

        return elevatorMotor1.get();
    }

    public double getMotor2Speed() {

        return elevatorMotor2.get();
    }

    public double getMotor3Speed() {

        return elevatorMotor3.get();
    }

    public double getCurrentSpeed() {

        return elevatorSpeed;
    }

    public boolean upperLimit() {

        return tLimit.get();
    }

    public boolean lowerLimit() {

        return bLimit.get();
    }

    public void displayDashboard() {

        SmartDashboard.putBoolean("Current Upper Value", tLimit.get());
        SmartDashboard.putBoolean("Current Lower Value", bLimit.get());
    }
}
