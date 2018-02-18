package frc.team5735.controllers.teleop;

import edu.wpi.first.wpilibj.GenericHID;
import frc.team5735.controllers.Controller;
import frc.team5735.controllers.CustomXbox;
import frc.team5735.subsystems.Drivetrain;

public class DrivetrainController implements Controller {
    private CustomXbox xboxController;

    private Drivetrain drivetrain;

    public DrivetrainController(int joystickPort) {
        this.drivetrain = Drivetrain.getInstance();
        xboxController = new CustomXbox(joystickPort);
    }


    @Override
    public void runInit() {

    }

    @Override
    public void runPeriodic() {
        drivetrain.curvatureDrive(0.5*xboxController.getY(GenericHID.Hand.kRight,0.1), 0.5*xboxController.getX(GenericHID.Hand.kLeft,0.1),xboxController.getTriggerAxis(GenericHID.Hand.kLeft) > 0);
    }

    @Override
    public void disabledInit() {
    }
}
