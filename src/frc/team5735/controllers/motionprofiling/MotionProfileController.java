package frc.team5735.controllers.motionprofiling;

import com.ctre.phoenix.motion.SetValueMotionProfile;
import frc.team5735.controllers.Controller;
import frc.team5735.subsystems.Drivetrain;
import frc.team5735.trajectories.Trajectory;

public class MotionProfileController implements Controller {

    private MotionProfileControllerState state;
    private MotionProfile leftMotionProfile, rightMotionProfile;

    private Drivetrain drivetrain;

    public MotionProfileController(Drivetrain drivetrain) {
        state = MotionProfileControllerState.EMPTY;
        this.drivetrain = drivetrain;

    }

    public void loadProfile(Trajectory trajectory) {
        leftMotionProfile = new MotionProfile(drivetrain.getLeftMotor(), trajectory.getLeftPoints());
        rightMotionProfile = new MotionProfile(drivetrain.getRightMotor(), trajectory.getRightPoints());
        state = MotionProfileControllerState.LOADED;
    }

    public void startProfile () {
        leftMotionProfile.startMotionProfile();
        rightMotionProfile.startMotionProfile();
        state = MotionProfileControllerState.RUNNING;
    }

    @Override
    public void runInit() {
        drivetrain.runInit();
    }

    @Override
    public void runPeriodic() {
        leftMotionProfile.control();
        rightMotionProfile.control();

        //CHECKING
        if(leftMotionProfile.getSetValue() == SetValueMotionProfile.Hold && rightMotionProfile.getSetValue() == SetValueMotionProfile.Hold) {
            state = MotionProfileControllerState.FINISHED;
        }
    }

    @Override
    public void disabledInit() {

    }

    public enum MotionProfileControllerState {
        EMPTY, LOADED, RUNNING, FINISHED
    }
}
