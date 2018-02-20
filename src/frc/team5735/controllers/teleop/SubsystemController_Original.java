package frc.team5735.controllers.teleop;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import frc.team5735.controllers.Controller;
import frc.team5735.controllers.CustomXbox;
import frc.team5735.subsystems.*;
import frc.team5735.utils.units.Degrees;
import frc.team5735.utils.units.Inches;

public class SubsystemController_Original implements Controller{

    private CustomXbox xboxController;

    private Wrist wrist;
    private Elevator elevator;
    private ElevatorIntake elevatorIntake;
    private DrivetrainIntake drivetrainIntake;

    public SubsystemController_Original(int joystickPort) {
        this.elevator = Elevator.getInstance();
        this.wrist = Wrist.getInstance();
        this.elevatorIntake = ElevatorIntake.getInstance();
        this.drivetrainIntake = DrivetrainIntake.getInstance();
        xboxController = new CustomXbox(joystickPort);
    }

    @Override
    public void runInit() {
    }

    @Override
    public void runPeriodic() {
        if (xboxController.getBackButtonPressed()) {
            elevator.setState(Elevator.ElevatorState.DEFAULT);
            wrist.setState(Wrist.WristState.DEFAULT);
        }

        //WRIST STUFF
        if (wrist.getState() == Wrist.WristState.DEFAULT) {
            wrist.setTargetSpeed(xboxController.getY(GenericHID.Hand.kLeft,0.1));
        }else {
            if (xboxController.getStartButtonPressed()) {
                wrist.zeroSensor();
            } else if (xboxController.getPOV() == 180) {
                wrist.setTargetAngle(new Degrees(-85));
            } else if (xboxController.getPOV() == 0) {
                wrist.setTargetAngle(new Degrees(-40));
            } else {
                wrist.setTargetAngle(new Degrees(wrist.getTargetAngle().getValue() + xboxController.getY(GenericHID.Hand.kLeft,0.1)));
            }
        }

        //ELEVATOR STUFF
        if (elevator.getState() == Elevator.ElevatorState.DEFAULT) {
            if (xboxController.getYButton()) {
                elevator.setTargetSpeed(0.8);
            }else{
                elevator.setTargetSpeed(xboxController.getY(GenericHID.Hand.kRight,0.1));
            }
        } else {
            if(xboxController.getStartButtonPressed()) {
                elevator.zeroSensor();
            } else if (xboxController.getPOV() == 180) {
                elevator.setTargetHeight(new Inches(6));
            } else if (xboxController.getPOV() == 0) {
                elevator.setTargetHeight(new Inches(20));
            } else {
                elevator.setTargetHeight(new Inches(elevator.getTargetHeight().getValue() + xboxController.getY(GenericHID.Hand.kRight,0.1)));
            }
        }

        //Elevator Stuff
//        elevator.setTargetSpeed(xboxController.getY(GenericHID.Hand.kLeft,0.1));

        //Intake
        if (xboxController.getTriggerAxis(GenericHID.Hand.kRight) > 0){
            drivetrainIntake.setTargetSpeed(xboxController.getTriggerAxis(GenericHID.Hand.kRight)*0.75);
        }else if(xboxController.getBButton()) {
            drivetrainIntake.setLeftTargetSpeed(-0.5);
            drivetrainIntake.setRightTargetSpeed(0.5);
        } else if (xboxController.getBumper(GenericHID.Hand.kLeft)) {
            drivetrainIntake.setTargetSpeed(-0.5);
        } else {
            drivetrainIntake.setTargetSpeed(0);
        }

        //Intake
        if (xboxController.getTriggerAxis(GenericHID.Hand.kLeft) > 0) {
            elevatorIntake.setTargetSpeed(-1);
        } else if(xboxController.getTriggerAxis(GenericHID.Hand.kRight) > 0) {
            elevatorIntake.setTargetSpeed(xboxController.getTriggerAxis(GenericHID.Hand.kRight)*0.85);
        } else if (xboxController.getAButton()) {
            elevatorIntake.setTargetSpeed(0.42);
        } else if (xboxController.getBumper(GenericHID.Hand.kLeft)) {
            elevatorIntake.setTargetSpeed(-0.5);
        } else {
            elevatorIntake.setTargetSpeed(0);
        }

//        else if (xboxController.getTriggerAxis(GenericHID.Hand.kLeft) > 0){
////            elevatorIntake.setTargetSpeed(-xboxController.getTriggerAxis(GenericHID.Hand.kLeft));
//            elevatorIntake.setTargetSpeed(-xboxController.getTriggerAxis(GenericHID.Hand.kLeft)*0.3);
//        }

        if(xboxController.getYButtonPressed()){
            wrist.printStatus();
            elevator.printStatus();
        }
    }

    @Override
    public void disabledInit() {
    }
}
