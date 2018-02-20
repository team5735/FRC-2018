package frc.team5735.controllers.teleop;

import edu.wpi.first.wpilibj.GenericHID;
import frc.team5735.controllers.Controller;
import frc.team5735.controllers.CustomXbox;
import frc.team5735.subsystems.Drivetrain;

public class DrivetrainController implements Controller {
    private CustomXbox xboxController;

    private Drivetrain drivetrain;

    private final double QUICK_TURN_MAX = 0.4;
    private final double Z_ROTATION_MAX = 0.5;
    private final double X_SPEED_MAX = 0.5;
    private final double TURBO_MAX = 0.75;


    public DrivetrainController(int joystickPort) {
        this.drivetrain = Drivetrain.getInstance();
        xboxController = new CustomXbox(joystickPort);
    }


    @Override
    public void runInit() {

    }

    @Override
    public void runPeriodic() {
//        if (xboxController.getAButton()) {
//            drivetrain.setPercentOutput(0.25,0.25);
//        } else {
//            drivetrain.curvatureDrive(0.5*xboxController.getY(GenericHID.Hand.kRight,0.1), 0.5*xboxController.getX(GenericHID.Hand.kLeft,0.1),xboxController.getTriggerAxis(GenericHID.Hand.kLeft) > 0);
//        }
        double leftJoystick = xboxController.getX(GenericHID.Hand.kLeft,0.1),
                rightJoystick = xboxController.getYSquared(GenericHID.Hand.kRight),
                quickTurnTrigger =  xboxController.getTriggerAxis(GenericHID.Hand.kLeft),
                turboTrigger = xboxController.getTriggerAxis(GenericHID.Hand.kRight);

//        System.out.println("leftJoystick: " + leftJoystick);
//        System.out.println("rightJoystick: " + rightJoystick);
//        System.out.println("quickTurnTrig: " + quickTurnTrigger);
//        System.out.println("turbo: " + turboTrigger);

        if(quickTurnTrigger > 0) {
            drivetrain.curvatureDrive(rightJoystick * (X_SPEED_MAX + turboTrigger * (TURBO_MAX - X_SPEED_MAX)),
                    leftJoystick * QUICK_TURN_MAX,true);
        } else {
            drivetrain.curvatureDrive(rightJoystick * (X_SPEED_MAX + turboTrigger * (TURBO_MAX - X_SPEED_MAX)),
                    leftJoystick * Z_ROTATION_MAX,false);
//            System.out.println("xSpeed: " + rightJoystick * (X_SPEED_MAX + turboTrigger * (TURBO_MAX - X_SPEED_MAX)));
//            System.out.println("zRot:" + leftJoystick * Z_ROTATION_MAX);
        }

        if (xboxController.getBackButton()) {
            drivetrain.resetGyro();
            drivetrain.getRightMotor().setSelectedSensorPosition(0,0,0);
            drivetrain.getLeftMotor().setSelectedSensorPosition(0,0,0);
        }
    }

    @Override
    public void disabledInit() {
    }
}
