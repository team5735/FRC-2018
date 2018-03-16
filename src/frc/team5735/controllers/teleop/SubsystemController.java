package frc.team5735.controllers.teleop;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import frc.team5735.controllers.Controller;
import frc.team5735.controllers.CustomXbox;
import frc.team5735.subsystems.Elevator;
import frc.team5735.subsystems.ElevatorIntake;
import frc.team5735.subsystems.Wrist;
import frc.team5735.utils.units.Degrees;
import frc.team5735.utils.units.Inches;
import static frc.team5735.constants.RobotConstants.*;

import com.ctre.phoenix.motorcontrol.ControlMode;

import static frc.team5735.constants.PositionConstants.*;

public class SubsystemController implements Controller{

    private CustomXbox xboxController;

    private Wrist wrist;
    private Elevator elevator;
    private ElevatorIntake elevatorIntake;
    
    public SubsystemController(int joystickPort) {    	
        this.elevator = Elevator.getInstance();
        this.wrist = Wrist.getInstance();
        this.elevatorIntake = ElevatorIntake.getInstance();
        xboxController = new CustomXbox(joystickPort);
    }

    @Override
    public void runInit() {
    }

    @Override
    public void runPeriodic() {
        if (xboxController.getBackButtonPressed() && xboxController.getBumper(Hand.kLeft)) {
            if(elevator.getState() == Elevator.ElevatorState.DEFAULT || wrist.getState() == Wrist.WristState.DEFAULT) {
                elevator.setState(Elevator.ElevatorState.POSITION_HOLDING);
                wrist.setState(Wrist.WristState.POSITION_HOLDING);
            } else {
                elevator.setState(Elevator.ElevatorState.DEFAULT);
                wrist.setState(Wrist.WristState.DEFAULT);
            }
        }

        //WRIST STUFF
        double wristJoystick = xboxController.getY(GenericHID.Hand.kRight,0.2),
                WRIST_INCREMENT = 0.5;
        if (wrist.getState() == Wrist.WristState.DEFAULT) {
            wrist.setTargetSpeed(wristJoystick); // limited in Wrist.java
        }else {
            if (xboxController.getStartButton()) {
                wrist.zeroSensor();
            } else {
                if(Math.abs(wristJoystick) > 0 && xboxController.getBumper(GenericHID.Hand.kLeft)) {
                    wrist.setTargetAngle(new Degrees(wrist.getTargetAngle().getValue() + Math.copySign(WRIST_INCREMENT, wristJoystick)));
                } else if (xboxController.getXButton()) {
                    // Cancel
                    wrist.setTargetAngle(wrist.getCurrentAngle());
                } else if (xboxController.getAButton()) {
                    // Intake position
                    wrist.setTargetAngle(new Degrees(WRIST_INTAKE));
                } else if (xboxController.getBButton()) {
                    // Switch position
                    wrist.setTargetAngle(new Degrees(WRIST_FLAT));
                } else if (xboxController.getYButton()) {
                    // Scale position
                    wrist.setTargetAngle(new Degrees(WRIST_SCALE));
                }
            }
        }

        //ELEVATOR STUFF
        double elevatorJoystick = xboxController.getY(GenericHID.Hand.kLeft, 0.2),
                ELEVATOR_INCREMENT = 0.5;
        if (elevator.getState() == Elevator.ElevatorState.DEFAULT) {
            elevator.setTargetSpeed(elevatorJoystick);
        } else {
            if(xboxController.getStartButton()) {
                elevator.zeroSensor();
            } else {
                if(Math.abs(elevatorJoystick) > 0 && xboxController.getBumper(GenericHID.Hand.kLeft)) {
                    elevator.setTargetHeight(new Inches(elevator.getTargetHeight().getValue() + (ELEVATOR_INCREMENT * elevatorJoystick) ));
                } else if (xboxController.getXButton()) {
                    // Cancel
                    elevator.setTargetHeight(elevator.getCurrentHeight());
                } else if (xboxController.getAButton()) {
                    // Intake position
                    elevator.setTargetHeight(new Inches(ELEVATOR_INTAKE));
                } else if (xboxController.getBButton()) {
                    // Switch position
                    elevator.setTargetHeight(new Inches(ELEVATOR_SWITCH));
                } else if (xboxController.getYButton()) {
                    // Scale position
                    elevator.setTargetHeight(new Inches(ELEVATOR_SCALE));
                }
            }
        }

        double ejectTrigger = xboxController.getTriggerAxis(GenericHID.Hand.kLeft),
                intakeTrigger = xboxController.getTriggerAxis(GenericHID.Hand.kRight),
                EJECT_SPEED_MAX = 0.55,
                INTAKE_MAX = 0.85;
        if (ejectTrigger > 0) {
            //Eject
            elevatorIntake.setTargetSpeed(-ejectTrigger * EJECT_SPEED_MAX);
//            elevatorIntake.set(ControlMode.PercentOutput, -EJECT_SPEED);
        } else if (intakeTrigger > 0) {
            elevatorIntake.setTargetSpeed(intakeTrigger * INTAKE_MAX);
//            elevatorIntake.set(ControlMode.PercentOutput, intakeTrigger * INTAKE_MAX);
        } else {
            elevatorIntake.setTargetSpeed(0);
//            elevatorIntake.set(ControlMode.PercentOutput, 0);
        }
        
        if (xboxController.getBumperPressed(GenericHID.Hand.kRight)) {
            elevatorIntake.toggleIntakeClaw();
        }
        
        //Intake (Eject override Hold override Intake)
//        double ejectTrigger = xboxController.getTriggerAxis(GenericHID.Hand.kLeft),
//                intakeTrigger = xboxController.getTriggerAxis(GenericHID.Hand.kRight),
//                INTAKE_MAX = 1;
//        if (ejectTrigger > 0) {
//            //Eject
//            if(xboxController.getBumper(GenericHID.Hand.kRight)) {
//                // Constant
//                elevatorIntake.setTargetSpeed(-1);
//            } else {
//                // Variable
//                elevatorIntake.setTargetSpeed(-ejectTrigger);
//            }
//        } else if (xboxController.getBumper(GenericHID.Hand.kRight)) {
//            // Hold
//            elevatorIntake.setTargetSpeed(0.42);
//        } else if (intakeTrigger > 0) {
//            elevatorIntake.setTargetSpeed(intakeTrigger * INTAKE_MAX);
//        } else {
//            elevatorIntake.setTargetSpeed(0);
//        }

        if(xboxController.getPOV() == 180){
            wrist.printStatus();
            elevator.printStatus();
        }
        
        
    }

    @Override
    public void disabledInit() {
    }
}
