package frc.team5735.constants;

import frc.team5735.controllers.motionprofiling.Trajectory;

public class TrajectoryConstants {
    public static Trajectory
        centerToLeftSwitch = new Trajectory("centerToLeftSwitch"),
        centerToRightSwitch = new Trajectory("centerToRightSwitch"),
        switchBackupToCube = new Trajectory("rightBackupToCube"), //mirrorable
        switchBackupToSwitch = new Trajectory("rightBackupToSwitch"),
        sideToSwitch = new Trajectory("rightToRightSwitch"),
        sideToScale = new Trajectory("rightToRightScale"),
        shortPath = new Trajectory("short");

    public static void loadTrajectories() {
        // lol you thought
        // no code here
        // hehe get rekt m8
    }
}
