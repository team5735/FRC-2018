package frc.team5735;

import edu.wpi.first.wpilibj.TimedRobot;
import frc.team5735.constants.RobotConstants;
import frc.team5735.controllers.auto.*;
import frc.team5735.controllers.motionprofiling.Trajectory;
import frc.team5735.controllers.teleop.DrivetrainController;
import frc.team5735.controllers.teleop.SubsystemController;
import frc.team5735.subsystems.*;
import frc.team5735.utils.SimpleNetworkTable;

public class Robot extends TimedRobot {

    private Drivetrain drivetrain;
    private DrivetrainIntake drivetrainIntake;
    private Elevator elevator;
    private ElevatorIntake elevatorIntake;
    private Wrist wrist;

    private SubsystemController subsystemController;
    private DrivetrainController drivetrainController;

    private AutoController autoController;

    @Override
    public void robotInit() {
        new SimpleNetworkTable();

        drivetrain = Drivetrain.getInstance();
        drivetrainIntake = DrivetrainIntake.getInstance();
        elevator = Elevator.getInstance();
        elevatorIntake = ElevatorIntake.getInstance();
        wrist = Wrist.getInstance();

        subsystemController = new SubsystemController(RobotConstants.SUBSYSTEM_CONTROLLER_ID);
        drivetrainController = new DrivetrainController(RobotConstants.DRIVETRAIN_CONTROLLER_ID);
    }

    /**
     * This function is called once when autonomous is enabled.
     */
    @Override
    public void autonomousInit() {
        drivetrain.runInit();
        drivetrainIntake.runInit();
        elevator.runInit();
        elevatorIntake.runInit();
        wrist.runInit();

        autoController = new SuperAutoController(Autos.straightSwitch);
        autoController.runInit();
    }

    /**
     * This function is called periodically during autonomous.
     */
    @Override
    public void autonomousPeriodic() {
        drivetrain.runPeriodic();
        drivetrainIntake.runPeriodic();
        elevator.runPeriodic();
        elevatorIntake.runPeriodic();
        wrist.runPeriodic();

        autoController.runPeriodic();
    }

    /**
     * This function is called once when teleop is enabled.
     */
    @Override
    public void teleopInit() {
        drivetrain.runInit();
        drivetrainIntake.runInit();
        elevator.runInit();
        elevatorIntake.runInit();
        wrist.runInit();

        subsystemController.runInit();
        drivetrainController.runInit();
    }

    /**
     * This function is called periodically during operator control.
     */
    @Override
    public void teleopPeriodic() {
        drivetrain.runPeriodic();
        drivetrainIntake.runPeriodic();
        elevator.runPeriodic();
        elevatorIntake.runPeriodic();
        wrist.runPeriodic();

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
        drivetrain.disabledInit();
        drivetrainIntake.disabledInit();
        elevator.disabledInit();
        elevatorIntake.disabledInit();
        wrist.disabledInit();

        subsystemController.disabledInit();
        drivetrainController.disabledInit();

        if(autoController != null) {
            autoController.disabledInit();
        }
    }

}
