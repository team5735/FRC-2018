package frc.team5735;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team5735.constants.RobotConstants;
import frc.team5735.constants.TrajectoryConstants;
import frc.team5735.controllers.auto.*;
import frc.team5735.controllers.teleop.DrivetrainController;
import frc.team5735.controllers.teleop.SubsystemController;
import frc.team5735.subsystems.*;
public class Robot extends TimedRobot {

    private Drivetrain drivetrain;
    private DrivetrainIntake drivetrainIntake;
    private Elevator elevator;
    private ElevatorIntake elevatorIntake;
    private Wrist wrist;

    private SubsystemController subsystemController;
    private DrivetrainController drivetrainController;

    private AutoController autoController;

    public static SendableChooser autoStartPositionChooser;
    public static SendableChooser autoPriorityChooser;


    @Override
    public void robotInit() {
        TrajectoryConstants.loadTrajectories();

        autoStartPositionChooser = new SendableChooser();
        autoStartPositionChooser.addDefault("Center", GameDataController.StartingPosition.CENTER);
        autoStartPositionChooser.addObject("Left", GameDataController.StartingPosition.LEFT);
        autoStartPositionChooser.addObject("Right", GameDataController.StartingPosition.RIGHT);
        SmartDashboard.putData("Auto Start Position", autoStartPositionChooser);

        autoPriorityChooser = new SendableChooser();
        autoPriorityChooser.addDefault("None", GameDataController.Priority.NONE);
        autoPriorityChooser.addObject("Switch", GameDataController.Priority.SWITCH);
        autoPriorityChooser.addObject("Scale", GameDataController.Priority.SCALE);
        SmartDashboard.putData("Auto Priority", autoPriorityChooser);

        SmartDashboard.putNumber("Delay", 0);

        drivetrain = Drivetrain.getInstance();
        drivetrainIntake = DrivetrainIntake.getInstance();
        elevator = Elevator.getInstance();
        elevatorIntake = ElevatorIntake.getInstance();
        wrist = Wrist.getInstance();

        subsystemController = new SubsystemController(RobotConstants.SUBSYSTEM_CONTROLLER_ID);
        drivetrainController = new DrivetrainController(RobotConstants.DRIVETRAIN_CONTROLLER_ID);

        CameraServer.getInstance().startAutomaticCapture();
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

        GameDataController.updateData();
        autoController = new SuperAutoController(GameDataController.findAppropriateTrajectory());
        autoController.runInit();

//        if(GameDataController.allFieldsPopulated) {
//
//        } else {
//            System.err.println("Not all necessary autonomous parameters populated yet!");
//        }
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

        if(autoController != null) {
            autoController.runPeriodic();
        }
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
