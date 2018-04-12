package frc.team5735.controllers.auto;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team5735.Robot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameDataController {
    private static char homeSwitch, scale, enemySwitch;

    public enum StartingPosition {
        LEFT, CENTER, RIGHT
    }
    public enum Priority {
        SWITCH, SCALE, NONE
    }

    public static StartingPosition startingPosition;    // Center, Left, or Right
    public static Priority priority;                    // Switch or Scale
    public static int delay;                            // Delay in ms before running auto

    public static void updateData() {
        String gameData = DriverStation.getInstance().getGameSpecificMessage();

        if(gameData.length() == 0) {
            System.err.println("No game data available!");
        } else {
            homeSwitch = gameData.charAt(0);
            scale = gameData.charAt(1);
            enemySwitch = gameData.charAt(2);
        }

        // Grab delay data and correct it to never be negative
        delay = (int) SmartDashboard.getNumber("Delay", 0);
        if (delay < 0) {
            delay = 0;
        }

        // Check for null object
        startingPosition = (Robot.autoStartPositionChooser.getSelected() != null) ? StartingPosition.valueOf(Robot.autoStartPositionChooser.getSelected().toString()) : StartingPosition.CENTER;
        priority = (Robot.autoPriorityChooser.getSelected() != null) ? Priority.valueOf(Robot.autoPriorityChooser.getSelected().toString()) : Priority.NONE;

    }

    /**
     * Logics out what set of AutoCommands are appropriate based on the game data
     * @return 2D Array of commands
     */
    public static AutoCommand[][] findAppropriateTrajectory() {
        List<AutoCommand[]> commands = new ArrayList<>();

        if(delay > 0) {
            AutoCommand[] delayCommand = {new AutoCommand(null, delay)};
            commands.add(delayCommand);
        }

        switch (startingPosition) {
            case LEFT:

                if(priority == Priority.SWITCH) {
                    if(homeSwitch == 'L') {
                        commands.addAll(Arrays.asList(Autos.leftToLeftSwitch));
                    } else if(homeSwitch == 'R') {
                        commands.addAll(Arrays.asList(Autos.moveForward));
                    }

                } else if(priority == Priority.SCALE) {
                    if(scale == 'L') {
                        commands.addAll(Arrays.asList(Autos.leftToLeftScale));
                    } else if(scale == 'R') {
                        commands.addAll(Arrays.asList(Autos.leftToRightScale));
                    }
                } else if(priority == Priority.NONE) {
                    commands.addAll(Arrays.asList(Autos.moveForward));
                }

                break;

            case CENTER:
                if(homeSwitch == 'L') {
                    commands.addAll(Arrays.asList(Autos.centerToLeftSwitch));
                } else if(homeSwitch == 'R'){
                    commands.addAll(Arrays.asList(Autos.centerToRightSwitch));
                }
//                commands.addAll(Arrays.asList(Autos.gyroTes   t));
                break;

            case RIGHT:
                if(priority == Priority.SWITCH) {
                    if(homeSwitch == 'L') {
                        commands.addAll(Arrays.asList(Autos.moveForward));
                    } else if(homeSwitch == 'R'){
                        commands.addAll(Arrays.asList(Autos.rightToRightSwitch));
                    }

                } else if(priority == Priority.SCALE) {
                    if(scale == 'L') {
                        commands.addAll(Arrays.asList(Autos.rightToLeftScale));
                    } else if(scale == 'R') {
                        commands.addAll(Arrays.asList(Autos.rightToRightScale));
                    }
                } else if(priority == Priority.NONE) {
                    commands.addAll(Arrays.asList(Autos.moveForward));
                }

                break;
        }

        AutoCommand[][] commandsArray = {new AutoCommand[commands.size()]};
        commandsArray = commands.toArray(commandsArray);
        return commandsArray;
    }
}