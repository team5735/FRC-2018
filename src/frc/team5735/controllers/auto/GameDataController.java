package frc.team5735.controllers.auto;

import edu.wpi.first.wpilibj.DriverStation;
import frc.team5735.controllers.motionprofiling.Trajectory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameDataController {
    private static char homeSwitch, scale, enemySwitch;
    private static void parseGameData() {
        String gameData = DriverStation.getInstance().getGameSpecificMessage();
        if(gameData.length() == 0) {
            System.err.println("No game data available!");
        } else {
            homeSwitch = gameData.charAt(0);
            scale = gameData.charAt(1);
//            enemySwitch = gameData.charAt(2);
        }
    }

    /**
     * Logics out what set of AutoCommands are appropriate based on the game data
     * @return 2D Array of commands
     */
    public static AutoCommand[][] findAppropriateTrajectory() {
        parseGameData();
        List<AutoCommand> commands = new ArrayList<>();
        if(homeSwitch == 'L') {
            commands.addAll(Arrays.asList(Autos.startToHomeLeft[0]));
            if(scale == 'L') {
                commands.addAll(Arrays.asList(Autos.homeLeftToScaleLeft[0]));
            } else {
                commands.addAll(Arrays.asList(Autos.homeLeftToScaleRight[0]));
            }
        } else {
            commands.addAll(Arrays.asList(Autos.startToHomeRight[0]));
            if(scale == 'L') {
                commands.addAll(Arrays.asList(Autos.homeRightToScaleLeft[0]));
            } else {
                commands.addAll(Arrays.asList(Autos.homeRightToScaleRight[0]));
            }
        }

        AutoCommand[][] commandsArray = {(AutoCommand[]) commands.toArray()};
        return commandsArray;
    }
}
