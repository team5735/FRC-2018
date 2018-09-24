package frc.team5735.controllers.auto;

import frc.team5735.controllers.motionprofiling.MotionProfileController;
import frc.team5735.subsystems.*;
import frc.team5735.controllers.motionprofiling.Trajectory;
import frc.team5735.utils.units.Degrees;
import frc.team5735.utils.units.Inches;

public class StraightSwitchController extends AutoController {

    private int step;

    private Wrist wrist;
    private Elevator elevator;
    private ElevatorIntake elevatorIntake;

    private MotionProfileController motionProfileController;
    private Trajectory trajectory;

    public StraightSwitchController() {
        this.elevator = Elevator.getInstance();
        this.wrist = Wrist.getInstance();
        this.elevatorIntake = ElevatorIntake.getInstance();

        motionProfileController = new MotionProfileController();
        trajectory = new Trajectory("straight-switch");
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

        boolean moveOn = true;

        if (step == 0) {
            motionProfileController.runPeriodic();
            elevator.setTargetHeight(new Inches(24));
            wrist.setTargetAngle(new Degrees(-40));

            if (motionProfileController.getState() == MotionProfileController.MotionProfileControllerState.FINISHED &&
                    elevator.getState() == Elevator.ElevatorState.POSITION_HOLDING &&
                    wrist.getState() == Wrist.WristState.POSITION_HOLDING) {
                elevatorIntake.setTargetSpeed( -1);

                step ++;
            }
        }else if (step == 1 && delay(1000)) {
            //Turn off intake motor
            elevatorIntake.setTargetSpeed(0);
            isFinished = true;

            step ++;
        }

//        if (step == 0 &&
//                motionProfileController.getState() == MotionProfileController.MotionProfileControllerState.FINISHED &&
//                elevator.setTargetHeight(new Inches(24)) == Elevator.ElevatorState.POSITION_HOLDING &&
//                wrist.setTargetAngle(new Degrees(-40)) == Wrist.WristState.POSITION_HOLDING) {
//            //Turn on intake motor to spit out cube
//            elevatorIntake.setTargetSpeed( -1);
//
//            step ++;
//        }else if (step == 1 && delay(1000)){
//            //Turn off intake motor
//            elevatorIntake.setTargetSpeed(0);
//            isFinished = true;
//
//            step ++;
//        }

//        if(moveOn)

    }

    @Override
    public void disabledInit() {

    }
}
