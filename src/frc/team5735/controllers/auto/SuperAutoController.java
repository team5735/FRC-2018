package frc.team5735.controllers.auto;

import frc.team5735.controllers.motionprofiling.MotionProfileController;
import frc.team5735.controllers.motionprofiling.Trajectory;
import frc.team5735.subsystems.*;
import frc.team5735.utils.units.Degrees;
import frc.team5735.utils.units.Inches;

public class SuperAutoController extends AutoController {

    private int step;

    private Wrist wrist;
    private Elevator elevator;
    private ElevatorIntake elevatorIntake;
    private DrivetrainIntake drivetrainIntake;
    private Drivetrain drivetrain;

    private MotionProfileController motionProfileController;

    private AutoCommand[][] commands;

    public SuperAutoController(AutoCommand[][] commands) {
        this.wrist = Wrist.getInstance();
        this.elevator = Elevator.getInstance();
        this.elevatorIntake = ElevatorIntake.getInstance();
        this.drivetrainIntake = DrivetrainIntake.getInstance();
        this.drivetrain = Drivetrain.getInstance();

        motionProfileController = new MotionProfileController();

        this.commands = commands;
    }

    @Override
    public void runInit() {
        step = 0;
    }

    @Override
    public void runPeriodic() {
        if (step < commands.length) {
            AutoCommand[] stepCommands = commands[step];

            if (stepCommands != null) {
                boolean isStepFinished = true;
                for (AutoCommand command : stepCommands) {
                    if (command.getSubsystem() instanceof Wrist) {
                        wrist.setTargetAngle((Degrees) command.getValue());
                        isStepFinished = isStepFinished && wrist.getState() == Wrist.WristState.POSITION_HOLDING;
                    } else if (command.getSubsystem() instanceof Elevator) {
                        elevator.setTargetHeight((Inches) command.getValue());
                        isStepFinished = isStepFinished && elevator.getState() == Elevator.ElevatorState.POSITION_HOLDING;
                    } else if (command.getSubsystem() instanceof ElevatorIntake) {
                        elevatorIntake.setTargetSpeed((Double) command.getValue());
                    } else if (command.getSubsystem() instanceof DrivetrainIntake) {
                        drivetrainIntake.setTargetSpeed((Double) command.getValue());
                    } else if (command.getSubsystem() instanceof Drivetrain) {
                        if (command.getValue() instanceof Degrees) {
                            drivetrain.setTargetAngle((Degrees) command.getValue());
                            isStepFinished = isStepFinished && drivetrain.getState() == Drivetrain.DrivetrainState.GYRO_FINISHED;
                        } else {
                            if (motionProfileController.getState() == MotionProfileController.MotionProfileControllerState.EMPTY) {
                                motionProfileController.loadProfile((Trajectory) command.getValue());
                                motionProfileController.startProfile();
                            }
                            motionProfileController.runPeriodic();
                            isStepFinished = isStepFinished && motionProfileController.getState() == MotionProfileController.MotionProfileControllerState.FINISHED;
                        }
                    } else {
                        isStepFinished = isStepFinished && delay((int) command.getValue());
                    }
                }

                if (isStepFinished) {
                    drivetrain.setState(Drivetrain.DrivetrainState.DEFAULT);
                    step++;
                    motionProfileController.empty();
                }
            }
        }
    }

    @Override
    public void disabledInit() {
        elevator.setTargetHeight(elevator.getCurrentHeight());
        elevatorIntake.setTargetSpeed(0);
        wrist.setTargetAngle(wrist.getCurrentAngle());
    }
}
