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

    private MotionProfileController motionProfileController;

    private AutoCommand[][] commands;

    public SuperAutoController(AutoCommand[][] commands) {
        this.wrist = Wrist.getInstance();
        this.elevator = Elevator.getInstance();
        this.elevatorIntake = ElevatorIntake.getInstance();
        this.drivetrainIntake = DrivetrainIntake.getInstance();

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

            boolean isStepFinished = true;
            for (AutoCommand command : stepCommands) {
                if(command.getSubsystem() == null) {
                    isStepFinished = isStepFinished && delay((int)command.getValue()); //assuming command purely used for delay
                } else {
                    switch (command.getSubsystem().getClass().getSimpleName()) {
                        case "Wrist":
                            wrist.setTargetAngle((Degrees) command.getValue());
                            isStepFinished = isStepFinished && wrist.getState() == Wrist.WristState.POSITION_HOLDING;
                            break;
                        case "Elevator":
                            elevator.setTargetHeight((Inches) command.getValue());
                            isStepFinished = isStepFinished && elevator.getState() == Elevator.ElevatorState.POSITION_HOLDING;
                            break;
                        case "ElevatorIntake":
                            elevatorIntake.setTargetSpeed((Double) command.getValue());
                            break;
                        case "DrivetrainIntake":
                            drivetrainIntake.setTargetSpeed((Double) command.getValue());
                            break;
                        case "Drivetrain":
                            if (motionProfileController.getState() == MotionProfileController.MotionProfileControllerState.EMPTY) {
                                motionProfileController.loadProfile((Trajectory) command.getValue());
                                motionProfileController.startProfile();
                            }
                            motionProfileController.runPeriodic();
                            isStepFinished = isStepFinished && motionProfileController.getState() == MotionProfileController.MotionProfileControllerState.FINISHED;
                            break;
                    }
                }
            }

            if (isStepFinished) {
                step++;
                motionProfileController.empty();
            }
        }
    }

    @Override
    public void disabledInit() {

    }
}
