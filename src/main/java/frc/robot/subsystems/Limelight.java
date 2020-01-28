/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.command.Subsystem;
// import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
// import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

import frc.robot.lib.RioLogger;

/**
 * Add your docs here.
 */
public class Limelight extends Subsystem {
    private boolean initialized = false;
    private NetworkTableEntry tTarget = null;
    private NetworkTableEntry tx = null;
    private NetworkTableEntry ty = null;
    private NetworkTableEntry ta = null;
    private NetworkTableEntry ta0 = null;
    private NetworkTableEntry ta1 = null;

    public Limelight() {
    }

    public void initializeLimeLight() {
        NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
        try {
            tTarget = table.getEntry("tv");

            tx = table.getEntry("tx");
            ty = table.getEntry("ty");
            ta = table.getEntry("ta");
            ta0 = table.getEntry("ta0");
            ta1 = table.getEntry("ta1");
        } catch (Exception e) {
            RioLogger.errorLog("Unable to initialize LimeLight. Error is " + e);
            return;
        }
        initialized = true;
    }

    public boolean isInitialized() {
        return this.initialized;
    }

    public boolean hasTargets() {
       boolean hits = false;
       if (isInitialized()) {
           hits = (tTarget.getDouble(0.0) == 1.0);
       }
       return hits;
    }

    public double x() {
        double dx = 0.0;
        if (isInitialized()) {
            dx = tx.getDouble(0.0);
        }
        return dx;
     }

     public double y() {
        double dy = 0.0;
        if (isInitialized()) {
            dy = ty.getDouble(0.0);
        }
        return dy;
     }

     public double targetArea() {
        double dArea = 0.0;
        if (isInitialized()) {
            dArea = ta.getDouble(0.0);
        }
        return dArea;
     }

     public double rightTarget() {
        double dArea = 0.0;
        if (isInitialized()) {
            dArea = ta1.getDouble(0.0);
        }
        return dArea;
     }

     public double leftTarget() {
        double dArea = 0.0;
        if (isInitialized()) {
            dArea = ta0.getDouble(0.0);
        }
        return dArea;
     }

     public void turnOnLED() {
        lightLED(LimelightLED.ON);
     }

     public void turnOffLED() {
        lightLED(LimelightLED.OFF); 
     }
     
     private void lightLED( LimelightLED value) {
        NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
        table.getEntry("ledMode").setNumber(value.ordinal());
        RioLogger.errorLog("Setting Limelight LEDs to "+ value.ordinal());
     }
     
    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }
}
