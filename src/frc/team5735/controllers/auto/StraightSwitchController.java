package frc.team5735.controllers.auto;

import frc.team5735.controllers.Controller;
import frc.team5735.controllers.motionprofiling.MotionProfile;
import frc.team5735.controllers.motionprofiling.MotionProfileController;
import frc.team5735.subsystems.*;
import frc.team5735.utils.Trajectory;
import frc.team5735.utils.units.Degrees;
import frc.team5735.utils.units.Inches;

public class StraightSwitchController extends AutoController {

    private int step;

    private Wrist wrist;
    private Elevator elevator;
    private ElevatorIntake elevatorIntake;
    private Drivetrain drivetrain;

    private MotionProfileController motionProfileController;
    private Trajectory trajectory;

    public StraightSwitchController() {
        this.elevator = Elevator.getInstance();
        this.wrist = Wrist.getInstance();
        this.elevatorIntake = ElevatorIntake.getInstance();
        this.drivetrain = Drivetrain.getInstance();

        motionProfileController = new MotionProfileController();
        trajectory = new Trajectory("Straight Switch");
        motionProfileController.loadProfile(trajectory);
    }

    @Override
    public void runInit() {
        step = 0;
        elevator.runInit();
        wrist.runInit();
        elevatorIntake.runInit();
        drivetrain.runInit();
    }

    @Override
    public void runPeriodic() {
        motionProfileController.startProfile();
        if (step == 0 &&
                motionProfileController.getState() == MotionProfileController.MotionProfileControllerState.FINISHED &&
                elevator.setTargetHeight(new Inches(10)) == Elevator.ElevatorState.POSITION_HOLDING &&
                wrist.setTargetAngle(new Degrees(-20)) == Wrist.WristState.POSITION_HOLDING) {
            //Turn on intake motor to spit out cube
            elevatorIntake.setTargetSpeed( -1);

            step ++;
        }else if (step == 1 && delay(1000)){
            //Turn off intake motor
            elevatorIntake.setTargetSpeed(0);
            isFinished = true;

            step ++;
        }

    }

    @Override
    public void disabledInit() {

    }
}
