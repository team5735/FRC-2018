package frc.team5735.utils;

public class Trajectory {
    private double[][] leftPoints, rightPoints;  // Position (rotations), Velocity (RPM), Duration (ms)

    public Trajectory(double[][] leftPoints, double[][] rightPoints) {
        this.leftPoints = leftPoints;
        this.rightPoints = rightPoints;
    }

    public Trajectory(String fileName) {
        
    }

    public double[][] getLeftPoints() {
        return leftPoints;
    }

    public double[][] getRightPoints() {
        return rightPoints;
    }
}
