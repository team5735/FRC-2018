package frc.team5735.controllers.teleop;

import static frc.team5735.constants.RobotConstants.PCM_ID;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team5735.controllers.Controller;
import frc.team5735.controllers.CustomXbox;
import frc.team5735.subsystems.Drivetrain;
import frc.team5735.subsystems.Elevator;

public class DrivetrainController implements Controller {
    private CustomXbox xboxController;

    private Drivetrain drivetrain;

    private final double QUICK_TURN_MAX = 0.3; //0.4
    private final double Z_ROTATION_MAX = 0.75; //0.5
    private final double X_SPEED_MAX = 0.3;//0.5
    private final double TURBO_MAX = 0.3;
    private final double UNBALANCED_HEIGHT = 40;
    private final double UNBALANCED_SPEED_LIMIT = 0.7;
    private final double UNBALANCED_SPIN_LIMIT = 0.85; //0.8
    private final Compressor compressor;

    public DrivetrainController(int joystickPort) {
    	compressor = new Compressor(PCM_ID);
    	compressor.setClosedLoopControl(false);
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
                rightJoystick = xboxController.getY(GenericHID.Hand.kRight,0.1),
                quickTurnTrigger =  xboxController.getTriggerAxis(GenericHID.Hand.kLeft),
                turboTrigger = 0,
                xSpeedWithTurbo = rightJoystick * (X_SPEED_MAX + turboTrigger * (TURBO_MAX - X_SPEED_MAX));

        //OLD ROBOT Compensation
//        if (xSpeedWithTurbo > 0.2) {
//            leftJoystick -= 0.17;
//        }else if (xSpeedWithTurbo < -0.2) {
//            leftJoystick += 0.20;
//        }

//        System.out.println("leftJoystick: " + leftJoystick);
//        System.out.println("rightJoystick: " + rightJoystick);
//        System.out.println("quickTurnTrig: " + quickTurnTrigger);
//        System.out.println("turbo: " + turboTrigger);

        if(Elevator.getInstance().getCurrentHeight().getValue() > UNBALANCED_HEIGHT) {
            if(quickTurnTrigger > 0) {
                drivetrain.curvatureDrive(xSpeedWithTurbo * UNBALANCED_SPEED_LIMIT,
                        leftJoystick * QUICK_TURN_MAX * UNBALANCED_SPIN_LIMIT,true);
            } else {
                drivetrain.curvatureDrive(xSpeedWithTurbo * UNBALANCED_SPEED_LIMIT,
                        leftJoystick * Z_ROTATION_MAX * UNBALANCED_SPIN_LIMIT,false);
//            System.out.println("xSpeed: " + rightJoystick * (X_SPEED_MAX + turboTrigger * (TURBO_MAX - X_SPEED_MAX)));
//            System.out.println("zRot:" + leftJoystick * Z_ROTATION_MAX);
            }
        } else {
            if(quickTurnTrigger > 0) {
                drivetrain.curvatureDrive(xSpeedWithTurbo,
                        leftJoystick * QUICK_TURN_MAX,true);
            } else {
                drivetrain.curvatureDrive(xSpeedWithTurbo,
                        leftJoystick * Z_ROTATION_MAX,false);
//            System.out.println("xSpeed: " + rightJoystick * (X_SPEED_MAX + turboTrigger * (TURBO_MAX - X_SPEED_MAX)));
//            System.out.println("zRot:" + leftJoystick * Z_ROTATION_MAX);
            }
        }

        if(xboxController.getBackButtonPressed()) {
        	if(compressor.enabled()) {
        		compressor.stop();
        	} else {
        		compressor.start();
        	}
        }

//        if (xboxController.getBackButton()) {
//            drivetrain.resetGyro();
//            drivetrain.getRightMotor().setSelectedSensorPosition(0,0,0);
//            drivetrain.getLeftMotor().setSelectedSensorPosition(0,0,0);
//        }
    }

    @Override
    public void disabledInit() {
    }
}
