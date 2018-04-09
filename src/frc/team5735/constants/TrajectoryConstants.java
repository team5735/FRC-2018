package frc.team5735.constants;


import frc.team5735.controllers.motionprofiling.CustomTrajectory;

public class TrajectoryConstants {
    public static CustomTrajectory
        centerToRightSwitch = new CustomTrajectory("centerToRightSwitch"),
            rightSwitchToSwitchBackup = new CustomTrajectory("rightSwitchToSwitchBackup"),
                switchBackupToCube = new CustomTrajectory("switchBackupToCube"),
        rightToRightSwitch = new CustomTrajectory("rightToRightSwitch"),
        rightToRightScale = new CustomTrajectory("rightToRightScale"),
            rightScaleToCube = new CustomTrajectory("rightScaleToCube"),
        rightToLeftScalePrep = new CustomTrajectory("rightToLeftScalePrep"),
            leftScalePrepToLeftScale = new CustomTrajectory("leftScalePrepToLeftScale"),
        straight = new CustomTrajectory("goStraight"),
        shortPath = new CustomTrajectory("short");



    public static void loadTrajectories() {
        // lol you thought
        // no code here
        // hehe get rekt m8
    }
}
