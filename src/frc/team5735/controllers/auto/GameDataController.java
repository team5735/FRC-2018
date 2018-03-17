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
                        //TODO Check for scale in the future
                        commands.addAll(Arrays.asList(Autos.moveForward));
                    }

                } else if(priority == Priority.SCALE) {
                    if(scale == 'L') {
                        commands.addAll(Arrays.asList(Autos.moveForward));	// SCALE
                    } else if(scale == 'R') {
                        if(homeSwitch == 'L') {
                            commands.addAll(Arrays.asList(Autos.leftToLeftSwitch));
                        } else if(homeSwitch == 'R') {
                            //TODO Change to Opposite Side
                            commands.addAll(Arrays.asList(Autos.moveForward));
                        }
                    }
                }

                break;

            case CENTER:
                if(homeSwitch == 'L') {
                    commands.addAll(Arrays.asList(Autos.centerToLeftSwitch));
//                    commands.addAll(Arrays.asList(Autos.leftSwitchTwoCube));
                } else if(homeSwitch == 'R'){
                    commands.addAll(Arrays.asList(Autos.centerToRightSwitch));
//                    commands.addAll(Arrays.asList(Autos.rightSwitchTwoCube));
                }

                // If we ever need to go to the scale from center... WHY????
//                if(priority == Priority.SWITCH) {
//
//                    if(homeSwitch == 'L') {
//                        commands.addAll(Arrays.asList(Autos.centerToLeftSwitch));
//                    } else if(homeSwitch == 'R'){
//                        commands.addAll(Arrays.asList(Autos.centerToRightSwitch));
//                    }
//
//                } else if(priority == Priority.SCALE) {
//
//                    if(scale == 'L') {
////                        commands.addAll(Arrays.asList(Autos.startCenterToScaleLeft));
//                    } else if(scale == 'R') {
////                        commands.addAll(Arrays.asList(Autos.startCenterToScaleRight));
//                    }
//
//                }

                break;

            case RIGHT:
                if(priority == Priority.SWITCH) {
                    if(homeSwitch == 'L') {
                        //TODO Check for scale in the future
                        commands.addAll(Arrays.asList(Autos.moveForward));
                    } else if(homeSwitch == 'R'){
                        commands.addAll(Arrays.asList(Autos.rightToRightSwitch));
                    }

                } else if(priority == Priority.SCALE) {
                    if(scale == 'L') {
                        if(homeSwitch == 'L') {
                            //TODO Change to Opposite Side
                            commands.addAll(Arrays.asList(Autos.moveForward));
                        } else if(homeSwitch == 'R') {
                            commands.addAll(Arrays.asList(Autos.rightToRightSwitch));
                        }

                    } else if(scale == 'R') {
                        if(homeSwitch == 'R'){
                            commands.addAll(Arrays.asList(Autos.moveForward));	//TWO CUBE SCALE?
                        }else {
                            commands.addAll(Arrays.asList(Autos.moveForward));	//SCALE
                        }
                    }

                }

                break;
        }

        AutoCommand[][] commandsArray = {new AutoCommand[commands.size()]};
        commandsArray = commands.toArray(commandsArray);
        return commandsArray;
    }
}