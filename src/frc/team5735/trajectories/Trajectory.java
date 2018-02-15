package frc.team5735.trajectories;

public class Trajectory {
    private double[][] leftPoints, rightPoints;  // Position (rotations), Velocity (RPM), Duration (ms)

    public Trajectory(double[][] leftPoints, double[][] rightPoints) {  //TODO make a parser
        this.leftPoints = leftPoints;
        this.rightPoints = rightPoints;
    }

    public double[][] getLeftPoints() {
        return leftPoints;
    }

    public double[][] getRightPoints() {
        return rightPoints;
    }
}
