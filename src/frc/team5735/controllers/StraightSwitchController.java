package frc.team5735.controllers;

import frc.team5735.controllers.motionprofiling.MotionProfile;
import frc.team5735.controllers.motionprofiling.MotionProfileController;
import frc.team5735.subsystems.*;
import frc.team5735.utils.units.Degrees;
import frc.team5735.utils.units.Inches;

public class StraightSwitchController implements Controller {

    private int step;

    private Wrist wrist;
    private Elevator elevator;
    private ElevatorIntake elevatorIntake;
    private Drivetrain drivetrain;
    private MotionProfileController motionProfileController;

    public StraightSwitchController(Elevator elevator, Wrist wrist, ElevatorIntake elevatorIntake, Drivetrain drivetrain) {
        this.elevator = Elevator.getInstance();
        this.wrist = Wrist.getInstance();
        this.elevatorIntake = ElevatorIntake.getInstance();
        this.drivetrain = Drivetrain.getInstance();

        motionProfileController = new MotionProfileController(drivetrain);

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
//        if(step == 0 && elevator.setTargetHeight(new Inches(10)) == Elevator.ElevatorState.POSITION_HOLDING && wrist.setTargetAngle(new Degrees(30)) {
//            step ++;
//            startProfile();
//        }
//        if(step == 1 && elevator.setTargetHeight(new Inches(20)) == Elevator.ElevatorState.POSITION_HOLDING) {
//            step ++;
//        }
    }

    @Override
    public void disabledInit() {

    }
}
