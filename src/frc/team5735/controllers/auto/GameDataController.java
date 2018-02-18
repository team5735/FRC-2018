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
    public static StartingPosition startingPosition;
    public static Priority priority;
    public static int delay; // in ms

    public static boolean allFieldsPopulated;

    public static void updateData() {
        String gameData = DriverStation.getInstance().getGameSpecificMessage();

        if(gameData.length() == 0) {
            System.err.println("No game data available!");
        } else {
            homeSwitch = gameData.charAt(0);
            scale = gameData.charAt(1);
//            enemySwitch = gameData.charAt(2);
        }

        startingPosition = (Robot.autoStartPositionChooser.getSelected() != null) ? StartingPosition.valueOf(Robot.autoStartPositionChooser.getSelected().toString()) : null;
        priority = (Robot.autoPriorityChooser.getSelected() != null) ? Priority.valueOf(Robot.autoPriorityChooser.getSelected().toString()) : null;
        delay = (int) SmartDashboard.getNumber("Delay", -1);

        System.out.println();

        allFieldsPopulated = (homeSwitch != 0 && scale != 0 && startingPosition != null && priority != null && delay >= 0);
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
                        commands.addAll(Arrays.asList(Autos.startLeftToHomeLeft));
                    } else if(homeSwitch == 'R') {
                        commands.addAll(Arrays.asList(Autos.startLeftToForward));
                    }

                } else if(priority == Priority.SCALE) {

                    if(scale == 'L') {
                        commands.addAll(Arrays.asList(Autos.startLeftToScaleLeft));
                    } else if(scale == 'R') {

                        if(homeSwitch == 'L') {
                            commands.addAll(Arrays.asList(Autos.startLeftToHomeLeft));
                        } else if(homeSwitch == 'R') {
                            commands.addAll(Arrays.asList(Autos.startLeftToForward));
                        }

                    }

                }

                break;

            case CENTER:

                if(priority == Priority.SWITCH) {

                    if(homeSwitch == 'L') {
                        commands.addAll(Arrays.asList(Autos.startCenterToHomeLeft));
                    } else if(homeSwitch == 'R'){
                        commands.addAll(Arrays.asList(Autos.startCenterToHomeRight));
                    }

                } else if(priority == Priority.SCALE) {

                    if(scale == 'L') {
                        commands.addAll(Arrays.asList(Autos.startCenterToScaleLeft));
                    } else if(scale == 'R') {
                        commands.addAll(Arrays.asList(Autos.startCenterToScaleRight));
                    }

                }

                break;

            case RIGHT:

                if(priority == Priority.SWITCH) {

                    if(homeSwitch == 'L') {
                        commands.addAll(Arrays.asList(Autos.startRightToForward));
                    } else if(homeSwitch == 'R'){
                        commands.addAll(Arrays.asList(Autos.startRightToHomeRight));
                    }

                } else if(priority == Priority.SCALE) {

                    if(scale == 'L') {

                        if(homeSwitch == 'L') {
                            commands.addAll(Arrays.asList(Autos.startRightToForward));
                        } else if(homeSwitch == 'R') {
                            commands.addAll(Arrays.asList(Autos.startRightToHomeRight));
                        }

                    } else if(scale == 'R') {
                        commands.addAll(Arrays.asList(Autos.startRightToScaleRight));
                    }

                }

                break;
        }

        AutoCommand[][] commandsArray = {(AutoCommand[]) commands.toArray()};
        return commandsArray;
    }
}
