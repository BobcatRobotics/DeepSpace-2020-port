/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package frc.robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.TimedRobot;

import frc.robot.commands.DriveWithJoysticks;
import frc.robot.commands.MoveElevator;
import frc.robot.commands.PanelIntakeOut;
import frc.robot.commands.ShiftHigh;
import frc.robot.lib.RioLoggerThread;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private PanelIntakeOut panelIntakeOut;
  private ShiftHigh shiftHigh;
  private static boolean commandsStarted = false;
  private static boolean loggerInit = false;
  private static boolean wasInAutoPeriodic = false;
  // Thread m_visionThread;

  // Command m_autonomousCommand;
  // SendableChooser<Command> m_chooser = new SendableChooser<>();
  Command m_DriveWithJoysticks;
  Command m_MoveElevator;

  @Override
  public void robotInit() {
    m_DriveWithJoysticks = new DriveWithJoysticks();
    m_MoveElevator = new MoveElevator();
    panelIntakeOut = new PanelIntakeOut();
    shiftHigh = new ShiftHigh();
    wasInAutoPeriodic = false;
  }

  @Override
  public void robotPeriodic() {
  }

  @Override
  public void disabledInit() {
    // If we go to disabled, put most things to their
    // default state of command (since they will be disabled)
    // Set drive train to brake mode
    OI.driveTrain.setBrakeMode();
    // Set elevator motors to coast mode
    OI.elev1.setElevCoastMode();
    // Turn Limits back on
    OI.elev1.setLmitDisableStatusFalse();
    // Command panel intake to in position
    if (!wasInAutoPeriodic) {
      OI.panel.panelInOutSetToIn();
    }
    wasInAutoPeriodic = false;
    // Command wrist to be in stowed position
    OI.wrist.stow();
    // Turn off limelight LEDs
    OI.limelight.turnOffLED();

  }

  @Override
  public void disabledPeriodic() {
    OI.elev1.getElevatorDistance();
    OI.elev1.getElevatorVelocity();
    OI.elev1.getElevLimitStatus();
    OI.wrist.displayDashboard();
    OI.displayDashboard();
  }

  @Override
  public void autonomousInit() {
    // Set drive train to coast mode
    OI.driveTrain.setCoastMode();
    startCommands();
    OI.lock.disable();
    panelIntakeOut.start();
    shiftHigh.start();
   }

  @Override
  public void autonomousPeriodic() {
    Scheduler.getInstance().run();
    wasInAutoPeriodic = true;
 }

  @Override
  public void teleopInit() {
    // Set drive train to coast mode
    OI.driveTrain.setCoastMode();
    startCommands();
    OI.lock.disable();
    panelIntakeOut.start();
    shiftHigh.start();
    if (!loggerInit) {
      RioLoggerThread.getInstance();
      RioLoggerThread.setLoggingParameters(600, 60); // 10 mins, 1 min
      loggerInit = true;
    }
   }

  @Override
  public void teleopPeriodic() {
    Scheduler.getInstance().run();
    OI.wrist.displayDashboard();
 }

  // This function is called periodically during test mode.
  @Override
  public void testPeriodic() {
  }

  // Starts up all commands once 
  private void startCommands() {
    // Put elevator motors into brake mode
    OI.elev1.setElevBrakeMode();

    if (!commandsStarted) {
      m_DriveWithJoysticks.start();
      m_MoveElevator.start();

      //commandsStarted = true;
    }
  }
}
