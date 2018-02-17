package frc.team5735.controllers.auto;

import frc.team5735.controllers.auto.AutoCommands.MotionProfileCommand;
import frc.team5735.utils.Trajectory;

import java.util.ArrayList;
import java.util.List;

public class AutoParallel implements AutoCommand{
    
    public String[] commands;
    private char commandType;
    private String commandInput;
    List<AutoCommand> autoCommands = new ArrayList<AutoCommand>();
    
    public AutoParallel(String[] commands){
        this.commands = commands;
    }
    
    @Override
    public void runInit() {
        for (String command : commands) {
            commandType = command.charAt(0);
            commandInput = command.substring(2);
            if (commandType == 'M') { //Motion Profile or Move
                 autoCommands.add(new MotionProfileCommand(new Trajectory(commandInput)));
            }/*
            else if(commandType == 'E') { //Elevator
                command = new ElevatorCommand...
            } else if (commandType == 'W') { //Wrist

            }
            */
        }
    }

    @Override
    public boolean runPeriodic() {
        for (AutoCommand autoCommand : autoCommands) {
            autoCommand.runPeriodic();
        }
        return false;
    }
}
