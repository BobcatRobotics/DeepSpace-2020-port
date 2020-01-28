package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;
// import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.shuffleboard.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.robot.lib.RioLogger;
import frc.robot.RobotMap;

public class Elevator extends Subsystem {
    private double elevBiasDefault = -0.06;  // Competition was -0.04 at Waterbury
                                             // -.05 ar hrtfrd,  -0.08 at ne champs with elastic
    private double elevScaleDefault = 1.0;   // Competition is 0.6 at Waterbury
    private boolean elevLimDisDef = false;
    private double elevUpperLimit = 42000;    // Competiton bot upper limit was 39500 is Wtbry
    private double elevUpperReading = 55000;  // Above this the disreguard the elevator encoder
    private double elevLowPosition = -600;  // Set the low position for the elevator, note
                                              // these must be NEGATIVE
    private double elevMidPosition = -19600;  // Set the mid position for the elevator, note
                                              // these must be NEGATIVE
    private double elevHighPosition = -41000; // Set the high position for the elevator, note
                                              // these must be NEGATIVE

    // Elevator talons
    private WPI_TalonSRX elevatorMotor1;
    private WPI_TalonSRX elevatorMotor2;
    private WPI_TalonSRX elevatorMotor3;

    private DigitalInput tLimit;
    private DigitalInput bLimit;

    private boolean limitsDisabled = false;
    private boolean limitsDisableButtonActivated = false;

    private double elevatorCmd = 0.0;
    private double elevatorVelocity = 0.0;
    private double elevatorDistance = 0.0;

    private double elevBias=elevBiasDefault;
    private double elevScale=elevScaleDefault;

    // Shuffle board stuff
    private ShuffleboardTab tab = Shuffleboard.getTab("Elevator");
    private NetworkTableEntry elevDist = tab.add("Elevator Distance", 0.0).getEntry();
    private NetworkTableEntry elevVel = tab.add("Elevator Velocity", 0.0).getEntry();
    private NetworkTableEntry elevCmd = tab.add("Elevator Command", 0.0).getEntry();
    private NetworkTableEntry elevLimState = tab.add("Feedback of ElevLim State (1=on,0=off)",false).getEntry();
    private NetworkTableEntry elevBiasNT = tab.add("Elevator Command Bias", elevBiasDefault).getEntry();
    private NetworkTableEntry elevScaleNT = tab.add("Elevator Command Scale", elevScaleDefault).getEntry();
    private NetworkTableEntry elevLimitDisable = tab.add("Input to Disable All Elev Limits", elevLimDisDef).getEntry();
    private NetworkTableEntry elevCtrlMode = tab.add("Elevator Control Mode", 0.0).getEntry();
   
    public Elevator() {
        elevatorMotor1 = new WPI_TalonSRX(RobotMap.elevMotor1);
        elevatorMotor2 = new WPI_TalonSRX(RobotMap.elevMotor2);
        elevatorMotor3 = new WPI_TalonSRX(RobotMap.elevMotor3);
        elevatorMotor2.follow(elevatorMotor1);  // Enable if using PID, and remove line in execute()
        elevatorMotor3.follow(elevatorMotor1);  // Enable if using PID, and remove line in execute()

        tab.add("elevMotor1", elevatorMotor1);
        tab.add("elevMotor2", elevatorMotor2);
        tab.add("elevMotor3", elevatorMotor3);

        // Flip the phase of the encoder for use with SRX motion magic, etc.
        // and set current position to 0.0;
        elevatorMotor1.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute,0,0);
        elevatorMotor1.setSensorPhase(true);
        elevatorMotor1.setSelectedSensorPosition(0,0,0);

        // Configure the Talons to be in brake mode
        elevatorMotor1.setNeutralMode(NeutralMode.Brake);
        elevatorMotor2.setNeutralMode(NeutralMode.Brake);
        elevatorMotor3.setNeutralMode(NeutralMode.Brake);
        
        tLimit = new DigitalInput(RobotMap.elevLSwitchT);  // Not wired yet, useing encoder instead
        bLimit = new DigitalInput(RobotMap.elevLSwitchB);

        // Config motion magic stuff, use slot zero
        elevatorMotor1.configMotionCruiseVelocity(8000); // 2000 native units per 100msec (about 2 seconds total)
        elevatorMotor1.configMotionAcceleration(40000); // 4000 nup100msec/sec (.5 secs to cruise velocity)
        elevatorMotor1.config_kF(0,0.0);
        elevatorMotor1.config_kP(0,0.3);     // Try 0.2 so need 10230 nu (~20 inches) of error to get full command
        elevatorMotor1.config_kI(0,0.0008);  // Maybe try 0.0004 after running some tests
        elevatorMotor1.config_kD(0,0.0);
        elevatorMotor1.configAllowableClosedloopError(0, 100); // Within +/-100 native units, is close enough (.3 inches)
        elevatorMotor1.config_IntegralZone(0, 1500);  // Only allow integral action with +/- 3 inches

        // Call reset, and log elevator creation
        reset();
        RioLogger.errorLog("Elevator() created.");
    }

    public void reset() {
        elevatorCmd = 0.0;
        elevatorMotor1.set(elevatorCmd);
        //elevatorMotor2.set(elevatorCmd);  // Don't set these
        //elevatorMotor3.set(elevatorCmd);  // if in follower mode.
    }

    public void stop() {
        elevatorMotor1.stopMotor();
        //elevatorMotor2.stopMotor();  // Don't set these if
        //elevatorMotor3.stopMotor();  // in follower mode.
        elevatorCmd = 0.0;
    }

    public void elevate(double cmd) {
        // Get Elevator sensor info
        getElevatorDistance();
        getElevatorVelocity();

        // Send command to shuffleboard
        elevCmd.setDouble(elevatorCmd);

        // Get Scale and Bias from shuffleboard
        elevScale=elevScaleNT.getDouble(elevScaleDefault);
        elevBias=elevBiasNT.getDouble(elevBiasDefault);

        // Process cmd and set motor commands
        // ToDo: Put in bprotection for belt here using sensor
        // ToDo: info or limit switch info.
        if (!lowerLimit() || isLimDisable()) {
            // elevBias = elevBiasDefault;  // Old line to not allow operator to change bias
        } else {
            elevBias = 0.0;
        }
        elevatorCmd = cmd * elevScale +  elevBias;
        elevatorMotor1.set(ControlMode.PercentOutput, elevatorCmd);
        // elevatorMotor2.set(elevatorCmd);  // This to be removed if using PID and follow mode
        // elevatorMotor3.set(elevatorCmd);  // This to be removed if using PID and follow mode
        elevCtrlMode.setDouble(1.0);
    }

    public void goToLow() {
        // Get Elevator sensor info
        getElevatorDistance();
        getElevatorVelocity();
        elevatorMotor1.set(ControlMode.MotionMagic, elevLowPosition);
        if(!limitsDisabled && lowerLimit()) {
            elevatorMotor1.set(ControlMode.PercentOutput, elevatorCmd);
        }
        // elevatorMotor1.set(ControlMode.MotionMagic, elevLowPosition, DemandType.ArbitraryFeedForward, elevBias);
        elevCtrlMode.setDouble(1.0);
    }

    public void goToMid() {
        // Get Elevator sensor info
        getElevatorDistance();
        getElevatorVelocity();
        elevatorMotor1.set(ControlMode.MotionMagic, elevMidPosition);
        // elevatorMotor1.set(ControlMode.MotionMagic, elevMidPosition, DemandType.ArbitraryFeedForward, elevBias);
        elevCtrlMode.setDouble(2.0);
    }

    public void goToHigh() {
        // Get Elevator sensor info
        getElevatorDistance();
        getElevatorVelocity();
        elevatorMotor1.set(ControlMode.MotionMagic, elevHighPosition);
        // elevatorMotor1.set(ControlMode.MotionMagic, elevHighPosition, DemandType.ArbitraryFeedForward, elevBias);
        elevCtrlMode.setDouble(3.0);
    }

    public double getElevatorDistance() {
        if (!isLimDisable() && lowerLimit()) {
            elevatorMotor1.setSelectedSensorPosition(0, 0, 0);
        }
        // Since the encoder sensor phase is flipped to support
        // using the SRX motion magic etc., flip the result here
        // so the rest of the code continues to thing of the
        // distance increasing as the elevator goes up
        elevatorDistance = -1.0 * elevatorMotor1.getSelectedSensorPosition(0);
        elevDist.setDouble(elevatorDistance);
        return elevatorDistance;
    }

    public double getElevatorVelocity() {
        elevatorVelocity = elevatorMotor1.getSelectedSensorVelocity(0);
        elevVel.setDouble(elevatorVelocity);
        return elevatorVelocity;
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

    public double getCurrentCmd() {
        return elevatorCmd;
    }

    public boolean upperLimit() {    
        // Is the encoder above the value to say we are as
        // high as we want the elevator to go, but not so
        // high that we think the encoder is lost
        if ((elevatorDistance > elevUpperLimit) && (elevatorDistance < elevUpperReading)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean lowerLimit() {
        return bLimit.get();
    }

    public boolean isLimDisable() {
        if (elevLimitDisable.getBoolean(elevLimDisDef) || limitsDisableButtonActivated) {
           limitsDisabled = true;
        } else {
           limitsDisabled = false;
        }

        elevLimState.setBoolean(!limitsDisabled);
        return limitsDisabled;
    }

    public void setLmitDisableStatusTrue() {
        limitsDisableButtonActivated = true;
    }

    public void setLmitDisableStatusFalse() {
        limitsDisableButtonActivated = false;
    }

    public void setLmitDisableStatusToggle() {
        limitsDisableButtonActivated = !limitsDisableButtonActivated;
    }
    
    public boolean getElevLimitStatus() {
        isLimDisable();
        elevLimState.setBoolean(!limitsDisabled);
        return !limitsDisabled;
    }

    public int checkSwitches() {
        if (upperLimit()) {
            return 1;
        } else if (lowerLimit()) {
            return -1;
        }
        return 0;
    }

    public void setElevBrakeMode() {
        elevatorMotor1.setNeutralMode(NeutralMode.Brake);
        elevatorMotor2.setNeutralMode(NeutralMode.Brake);
        elevatorMotor3.setNeutralMode(NeutralMode.Brake);
    }

    public void setElevCoastMode() {
        elevatorMotor1.setNeutralMode(NeutralMode.Coast);
        elevatorMotor2.setNeutralMode(NeutralMode.Coast);
        elevatorMotor3.setNeutralMode(NeutralMode.Coast);
    }
    public void displayDashboard() {
        SmartDashboard.putBoolean("Current Upper Value", tLimit.get());
        SmartDashboard.putBoolean("Current Lower Value", bLimit.get());
        SmartDashboard.putBoolean("Current Limit Status (1=on)", getElevLimitStatus());
    }

    @Override
	public void initDefaultCommand() {
	  // Set the default command for a subsystem here.
	  // setDefaultCommand(new MySpecialCommand());
	}
}
