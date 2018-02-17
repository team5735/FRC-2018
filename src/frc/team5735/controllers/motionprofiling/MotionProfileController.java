package frc.team5735.controllers.motionprofiling;

import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motorcontrol.ControlMode;
import frc.team5735.controllers.Controller;
import frc.team5735.subsystems.Drivetrain;

public class MotionProfileController implements Controller {

    private MotionProfileControllerState state;
    private MotionProfile leftMotionProfile, rightMotionProfile;

    private Drivetrain drivetrain;

    public MotionProfileController() {
        state = MotionProfileControllerState.EMPTY;
        this.drivetrain = Drivetrain.getInstance();

    }

    public void loadProfile(Trajectory trajectory) {
        leftMotionProfile = new MotionProfile(drivetrain.getLeftMotor(), trajectory.getLeftPoints());
        rightMotionProfile = new MotionProfile(drivetrain.getRightMotor(), trajectory.getRightPoints());
        state = MotionProfileControllerState.LOADED;
    }

    public void startProfile () {
        drivetrain.clearMotionProfileTrajectories();
        leftMotionProfile.reset();
        rightMotionProfile.reset();
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
        SetValueMotionProfile leftSetOutput = leftMotionProfile.getSetValue();
        SetValueMotionProfile rightSetOutput = rightMotionProfile.getSetValue();
        drivetrain.getLeftMotor().set(ControlMode.MotionProfile, leftSetOutput.value);
        drivetrain.getRightMotor().set(ControlMode.MotionProfile, rightSetOutput.value);

        leftMotionProfile.control();
        rightMotionProfile.control();

        //CHECKING
        if(leftMotionProfile.getSetValue() == SetValueMotionProfile.Hold && rightMotionProfile.getSetValue() == SetValueMotionProfile.Hold) {
            state = MotionProfileControllerState.FINISHED;
        }
    }

    public MotionProfileControllerState getState() {
        return state;
    }

    @Override
    public void disabledInit() {

    }

    public enum MotionProfileControllerState {
        EMPTY, LOADED, RUNNING, FINISHED
    }
}
