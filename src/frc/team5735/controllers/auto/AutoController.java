package frc.team5735.controllers.auto;

import frc.team5735.controllers.Controller;
import frc.team5735.controllers.auto.AutoCommands.MotionProfileCommand;
import frc.team5735.controllers.motionprofiling.MotionProfileController;
import frc.team5735.subsystems.*;
import frc.team5735.utils.Trajectory;

public class AutoController implements Controller {

    private int step;

    private Wrist wrist;
    private Elevator elevator;
    private ElevatorIntake elevatorIntake;
    private Drivetrain drivetrain;
    private DrivetrainIntake drivetrainIntake;

    private MotionProfileController motionProfileController;
    private Trajectory trajectory;
    private int sequenceIndex;
    private int steps;
    private int sequenceStep = 0;
    private boolean startedCommand = false;
    private char commandType;
    private String commandInput;
    private AutoCommand command;

    public AutoController(int sequenceIndex) {
        this.elevator = Elevator.getInstance();
        this.wrist = Wrist.getInstance();
        this.elevatorIntake = ElevatorIntake.getInstance();
        this.drivetrain = Drivetrain.getInstance();
        this.drivetrainIntake = DrivetrainIntake.getInstance();

        motionProfileController = new MotionProfileController();
        trajectory = new Trajectory("Straight Switch");
        motionProfileController.loadProfile(trajectory);
        this.sequenceIndex = sequenceIndex;
    }

    @Override
    public void runInit() {
        sequenceStep = 0;
        elevator.runInit();
        wrist.runInit();
        elevatorIntake.runInit();
        drivetrain.runInit();
        steps = AutoSequences.Sequences[sequenceIndex].length;
        commandType = AutoSequences.Sequences[sequenceIndex][sequenceStep].charAt(0);
        commandInput = AutoSequences.Sequences[sequenceIndex][sequenceStep].substring(2);
    }

    @Override //TODO skechty
    public void runPeriodic(){
        runPeriodic(sequenceStep);
    }

    public void runPeriodic(int sequenceStep) {
        if (!startedCommand){
            if (commandType == 'M') { //Motion Profile or Move
                command = new MotionProfileCommand(new Trajectory(commandInput));
            }/*
            else if(commandType == 'E') { //Elevator
                command = new ElevatorCommand...
            } else if (commandType == 'W') { //Wrist
            
            }
            */
            startedCommand = true;
        } else if(command.runPeriodic()) {
            sequenceStep++;
            startedCommand = false;
        }
    }

    @Override
    public void disabledInit() {

    }
}
