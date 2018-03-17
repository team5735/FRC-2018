package frc.team5735.constants;

import frc.team5735.controllers.motionprofiling.Trajectory;

public class TrajectoryConstants {
    public static Trajectory
        centerToLeftSwitch = new Trajectory("centerToLeftSwitch", false),
        centerToRightSwitch = new Trajectory("centerToRightSwitch", false),
        switchBackupToCube = new Trajectory("rightBackupToCube", false),
        switchBackupToCubeReversed = new Trajectory("rightBackupToCube", true),
        switchBackupToSwitch = new Trajectory("rightBackupToSwitch", false),
        switchBackupToSwitchReversed = new Trajectory("rightBackupToSwitch", true),
        sideToSwitch = new Trajectory("rightToRightSwitch", false),
        sideToScale = new Trajectory("rightToRightScale", false),
        shortPath = new Trajectory("short", false),
        shortPathReversed = new Trajectory("short", true);

    public static void loadTrajectories() {
        // lol you thought
        // no code here
        // hehe get rekt m8
    }
}
