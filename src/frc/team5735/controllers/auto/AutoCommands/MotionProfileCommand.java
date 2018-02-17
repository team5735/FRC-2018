package frc.team5735.controllers.auto.AutoCommands;

import frc.team5735.controllers.auto.AutoCommand;
import frc.team5735.controllers.auto.AutoController;
import frc.team5735.controllers.motionprofiling.MotionProfile;
import frc.team5735.controllers.motionprofiling.MotionProfileController;
import frc.team5735.subsystems.Drivetrain;
import frc.team5735.subsystems.Elevator;
import frc.team5735.utils.Trajectory;

public class MotionProfileCommand implements AutoCommand{

    Drivetrain drivetrain;
    MotionProfile leftMotionProfile, rightMotionProfile;
    Trajectory trajectory;

    public MotionProfileCommand(Trajectory trajectory) {
        this.drivetrain = Drivetrain.getInstance();
        this.trajectory = trajectory;
    }

    public void startProfile () {
        leftMotionProfile.startMotionProfile();
        rightMotionProfile.startMotionProfile();
    }

    @Override
    public void runInit() {
        drivetrain.runInit();
        leftMotionProfile = new MotionProfile(drivetrain.getLeftMotor(), this.trajectory.getLeftPoints());
        rightMotionProfile = new MotionProfile(drivetrain.getRightMotor(), this.trajectory.getRightPoints());
    }

    @Override
    public boolean runPeriodic() {
        leftMotionProfile.control();
        rightMotionProfile.control();

        if (leftMotionProfile.done && rightMotionProfile.done) {
            return true;
        } else {
            return false;
        }
    }
}
