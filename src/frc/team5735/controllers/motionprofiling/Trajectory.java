package frc.team5735.controllers.motionprofiling;

public class Trajectory {
    private double[][] leftPoints, rightPoints;  // Position (rotations), Velocity (RPM), Duration (ms)

    public Trajectory(String fileName) {
        leftPoints = TrajectoryParser.getTrajectory(fileName + "_left.csv");
        rightPoints = TrajectoryParser.getTrajectory(fileName + "_right.csv");
    }

    public double[][] getLeftPoints() {
        return leftPoints;
    }

    public double[][] getRightPoints() {
        return rightPoints;
    }
}
