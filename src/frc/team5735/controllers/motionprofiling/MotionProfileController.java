package frc.team5735.controllers.motionprofiling;

import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.SensorCollection;
import frc.team5735.Robot;
import frc.team5735.constants.PidConstants;
import frc.team5735.constants.RobotConstants;
import frc.team5735.controllers.Controller;
import frc.team5735.subsystems.Drivetrain;
import jaci.pathfinder.Pathfinder;

import static frc.team5735.constants.PidConstants.*;

public class MotionProfileController implements Controller {

    private MotionProfileControllerState state;
    private CustomTrajectory trajectory;
    private SensorCollection leftSensors, rightSensors;

    private Drivetrain drivetrain;

    public MotionProfileController() {
        state = MotionProfileControllerState.EMPTY;
        this.drivetrain = Drivetrain.getInstance();
    }

    public void loadProfile(CustomTrajectory trajectory) {
        this.trajectory = trajectory;
        state = MotionProfileControllerState.LOADED;
    }

    public void startProfile () {
        leftSensors = Drivetrain.getInstance().getLeftMotor().getSensorCollection();
        rightSensors = Drivetrain.getInstance().getRightMotor().getSensorCollection();

        trajectory.getLeftEF().configureEncoder(
                leftSensors.getQuadraturePosition(),
                4096,
                RobotConstants.WHEEL_DIAMETER_METERS);

        trajectory.getLeftEF().configurePIDVA(DT_LEFT_VEL_KP, DT_LEFT_VEL_KI, DT_LEFT_VEL_KD, 1 / RobotConstants.MAX_VELOCITY, DT_LEFT_VEL_KF);

        trajectory.getRightEF().configureEncoder(
                rightSensors.getQuadraturePosition(),
                4096,
                RobotConstants.WHEEL_DIAMETER_METERS);

        trajectory.getRightEF().configurePIDVA(DT_RIGHT_VEL_KP, DT_RIGHT_VEL_KI, DT_RIGHT_VEL_KD, 1 / RobotConstants.MAX_VELOCITY, DT_RIGHT_VEL_KF);

        state = MotionProfileControllerState.RUNNING;
    }

    @Override
    public void runInit() {
        drivetrain.runInit();
    }

    @Override
    public void runPeriodic() {
        double l = trajectory.getLeftEF().calculate(leftSensors.getQuadraturePosition());
        double r = trajectory.getRightEF().calculate(rightSensors.getQuadraturePosition());

        double gyro_heading = Drivetrain.getInstance().getGyroHeading();    // Assuming the gyro is giving a value in degrees
        double desired_heading = Pathfinder.r2d(trajectory.getLeftEF().getHeading());  // Should also be in degrees

        double angleDifference = Pathfinder.boundHalfDegrees(desired_heading - gyro_heading);
        double turn = PidConstants.TURN_LIMIT * -PidConstants.TURN_P * angleDifference;

        drivetrain.setMotionProfileOutput(l + turn, r - turn);

        //CHECKING
        if(trajectory.getLeftEF().isFinished() && trajectory.getRightEF().isFinished()) {
            state = MotionProfileControllerState.FINISHED;
        }
    }

    public MotionProfileControllerState getState() {
        return state;
    }

    @Override
    public void disabledInit() {

    }

    public void empty(){
        drivetrain.clearMotionProfileTrajectories();
        if(trajectory != null) {
            trajectory.getLeftEF().reset();
            trajectory.getRightEF().reset();
        }
        state = MotionProfileControllerState.EMPTY;
    }

    public enum MotionProfileControllerState {
        EMPTY, LOADED, RUNNING, FINISHED
    }
}