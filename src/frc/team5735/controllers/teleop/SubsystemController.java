package frc.team5735.controllers.teleop;

import edu.wpi.first.wpilibj.GenericHID;
import frc.team5735.controllers.Controller;
import frc.team5735.controllers.CustomXbox;
import frc.team5735.subsystems.*;
import frc.team5735.utils.units.Degrees;
import frc.team5735.utils.units.Inches;

public class SubsystemController implements Controller{
    private CustomXbox xboxController;

    private Wrist wrist;
    private Elevator elevator;
    private ElevatorIntake elevatorIntake;
    private DrivetrainIntake drivetrainIntake;

    public SubsystemController(int joystickPort, Elevator elevator, Wrist wrist, ElevatorIntake elevatorIntake, DrivetrainIntake drivetrainIntake) {
        this.elevator = elevator;
        this.wrist = wrist;
        this.elevatorIntake = elevatorIntake;
        this.drivetrainIntake = drivetrainIntake;
        xboxController = new CustomXbox(joystickPort);
    }


    @Override
    public void runInit() {
        elevator.runInit();
        wrist.runInit();
        elevatorIntake.runInit();
        drivetrainIntake.runInit();
    }

    @Override
    public void runPeriodic() {
        //WRIST STUFF
        if (xboxController.getStartButtonPressed()) {
            wrist.zeroSensor();
        } else {
            wrist.setTargetAngle(new Degrees(wrist.getTargetAngle().getValue() + xboxController.getY(GenericHID.Hand.kLeft,0.1)));
        }

        elevator.setTargetHeight(new Inches(elevator.getTargetHeight().getValue() + xboxController.getY(GenericHID.Hand.kRight,0.1)));

        //Elevator Stuff
//        elevator.setTargetSpeed(xboxController.getY(GenericHID.Hand.kLeft,0.1));

        //Intake
        if (xboxController.getTriggerAxis(GenericHID.Hand.kRight) > 0){
            drivetrainIntake.setTargetSpeed(xboxController.getTriggerAxis(GenericHID.Hand.kRight));
        }else if(xboxController.getBButton()) {
            drivetrainIntake.setLeftTargetSpeed(-0.5);
            drivetrainIntake.setRightTargetSpeed(0.5);
        } else if(xboxController.getTriggerAxis(GenericHID.Hand.kLeft) > 0) {
            drivetrainIntake.setTargetSpeed(-xboxController.getTriggerAxis(GenericHID.Hand.kLeft)*0.8);
        } else {
            drivetrainIntake.setTargetSpeed(0);
        }

        //Intake
        if (xboxController.getBumper(GenericHID.Hand.kLeft)) {
            elevatorIntake.setTargetSpeed(-1);
        }else if (xboxController.getTriggerAxis(GenericHID.Hand.kLeft) > 0){
//            elevatorIntake.setTargetSpeed(-xboxController.getTriggerAxis(GenericHID.Hand.kLeft));
            elevatorIntake.setTargetSpeed(-xboxController.getTriggerAxis(GenericHID.Hand.kLeft)*0.3);
        } else if(xboxController.getTriggerAxis(GenericHID.Hand.kRight) > 0) {
            elevatorIntake.setTargetSpeed(xboxController.getTriggerAxis(GenericHID.Hand.kRight)*0.55);
        }  else if (xboxController.getAButton()) {
            elevatorIntake.setTargetSpeed(0.3);
        } else {
            elevatorIntake.setTargetSpeed(0);
        }

        elevator.runPeriodic();
        wrist.runPeriodic();
        elevatorIntake.runPeriodic();
        drivetrainIntake.runPeriodic();
    }

    @Override
    public void disabledInit() {
        elevator.disabledInit();
        wrist.disabledInit();
        elevatorIntake.disabledInit();
        drivetrainIntake.disabledInit();
    }
}
