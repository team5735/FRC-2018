package frc.team5735.controllers.auto;

import frc.team5735.controllers.Controller;
import frc.team5735.controllers.auto.AutoCommands.ElevatorCommand;
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
    private int sequenceStep = 1;
    private boolean startedCommand = false;
    private char commandType;
    private String commandInput;
    private AutoCommand command;
    private String commandString;

    private String[] commands;

    public AutoController(int sequenceIndex) {
        this.elevator = Elevator.getInstance();
        this.wrist = Wrist.getInstance();
        this.elevatorIntake = ElevatorIntake.getInstance();
        this.drivetrain = Drivetrain.getInstance();
        this.drivetrainIntake = DrivetrainIntake.getInstance();

        motionProfileController = new MotionProfileController();
        trajectory = new Trajectory("Straight Switch");
//        motionProfileController.loadProfile(trajectory);
        this.sequenceIndex = sequenceIndex;
    }

    @Override
    public void runInit() {
        sequenceStep = 1; // starts at index 1 because 0 is name
        elevator.runInit();
        wrist.runInit();
        elevatorIntake.runInit();
        drivetrain.runInit();
        drivetrainIntake.runInit();
        steps = AutoSequences.Sequences[sequenceIndex].length;
    }

    @Override //TODO skechty
    public void runPeriodic(){
        runPeriodic(sequenceStep);
    }

    public void runPeriodic(int sequenceStep) {
        if (!startedCommand) {
            commandString = AutoSequences.Sequences[sequenceIndex][sequenceStep];
            if (commandString.contains(",")) {
                commands = commandString.split("\\,");
                command = new AutoParallel(commands);
            } else {
                commandType = commandString.charAt(0);
                commandInput = commandString.substring(2);
                startedCommand = true;
            }

            if (commandType == 'M') { //Motion Profile or Move
                command = new MotionProfileCommand(new Trajectory(commandInput));
            } else if(commandType == 'E') { //Elevator
                command = new ElevatorCommand(Integer.parseInt(commandInput));
            } /*else if (commandType == 'W') { //Wrist
            
            }
            */
        } else if(command.runPeriodic()) {
            sequenceStep++;
            startedCommand = false;
        }
    }

    @Override
    public void disabledInit() {

    }
}
