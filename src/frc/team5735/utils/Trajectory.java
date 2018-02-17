package frc.team5735.utils;

import frc.team5735.controllers.motionprofiling.TrajectoryParser;

public class Trajectory {
    private double[][] leftPoints, rightPoints;  // Position (rotations), Velocity (RPM), Duration (ms)

    public Trajectory(double[][] leftPoints, double[][] rightPoints) {
        this.leftPoints = leftPoints;
        this.rightPoints = rightPoints;
    }

    public Trajectory(String fileName) {
        this.leftPoints = TrajectoryParser.getTrajectory(fileName + "_left.csv");
        this.rightPoints = TrajectoryParser.getTrajectory(fileName + "_right.csv");
    }

    public double[][] getLeftPoints() {
        return leftPoints;
    }

    public double[][] getRightPoints() {
        return rightPoints;
    }
}
