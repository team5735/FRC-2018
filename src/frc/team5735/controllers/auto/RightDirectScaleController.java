package frc.team5735.controllers.auto;

import frc.team5735.controllers.motionprofiling.MotionProfileController;
import frc.team5735.controllers.motionprofiling.Trajectory;
import frc.team5735.subsystems.Elevator;
import frc.team5735.subsystems.ElevatorIntake;
import frc.team5735.subsystems.Wrist;
import frc.team5735.utils.units.Degrees;
import frc.team5735.utils.units.Inches;

public class RightDirectScaleController extends AutoController{

    private int step;

    private Wrist wrist;
    private Elevator elevator;
    private ElevatorIntake elevatorIntake;

    private MotionProfileController motionProfileController;
    private Trajectory trajectory;

    public RightDirectScaleController() {
        this.elevator = Elevator.getInstance();
        this.wrist = Wrist.getInstance();
        this.elevatorIntake = ElevatorIntake.getInstance();

        motionProfileController = new MotionProfileController();
        trajectory = new Trajectory("right-direct-scale");
        motionProfileController.loadProfile(trajectory);
    }

    @Override
    public void runInit() {
        step = 0;
        motionProfileController.startProfile();
    }

    @Override
    public void runPeriodic() {
        motionProfileController.runPeriodic();

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
        } else if (step == 2 ) {
//            StraightSwitchController();
        }

    }

    @Override
    public void disabledInit() {

    }
}
