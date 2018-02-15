package frc.team5735;

import edu.wpi.first.wpilibj.TimedRobot;
import frc.team5735.constants.RobotConstants;
import frc.team5735.controllers.teleop.DrivetrainController;
import frc.team5735.controllers.teleop.SubsystemController;
import frc.team5735.subsystems.*;

public class Robot extends TimedRobot {

    private Wrist wrist;
    private Elevator elevator;
    private ElevatorIntake elevatorIntake;
    private DrivetrainIntake drivetrainIntake;
    private Drivetrain drivetrain;

    private SubsystemController subsystemController;

    private DrivetrainController drivetrainController;

    @Override
    public void robotInit() {
        wrist = Wrist.getInstance();
        elevator = Elevator.getInstance();
        elevatorIntake = ElevatorIntake.getInstance();
        drivetrainIntake = DrivetrainIntake.getInstance();
        subsystemController = new SubsystemController(RobotConstants.SUBSYSTEM_CONTROLLER_ID, elevator, wrist, elevatorIntake,drivetrainIntake);

        drivetrain = Drivetrain.getInstance();
        drivetrainController = new DrivetrainController(RobotConstants.DRIVETRAIN_CONTROLLER_ID, drivetrain);

    }

    /**
     * This function is called once when autonomous is enabled.
     */
    @Override
    public void autonomousInit() {

    }

    /**
     * This function is called periodically during autonomous.
     */
    @Override
    public void autonomousPeriodic() {

    }

    /**
     * This function is called once when teleop is enabled.
     */
    @Override
    public void teleopInit() {
        subsystemController.runInit();
        drivetrainController.runInit();
    }

    /**
     * This function is called periodically during operator control.
     */
    @Override
    public void teleopPeriodic() {
        subsystemController.runPeriodic();
        drivetrainController.runPeriodic();
    }

    /**
     * This function is called periodically during test mode.
     */
    @Override
    public void testPeriodic() {
        
    }

    @Override
    public void disabledInit() {
        subsystemController.disabledInit();
        drivetrainController.disabledInit();
    }

}
