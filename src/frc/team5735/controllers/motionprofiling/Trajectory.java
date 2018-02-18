package frc.team5735.controllers.motionprofiling;

public class Trajectory {
    private double[][] leftPoints, rightPoints;  // Position (rotations), Velocity (RPM), Duration (ms)

    public Trajectory(String fileName) {
        try {
            leftPoints = TrajectoryParser.getTrajectory(fileName + "_left.csv");
            rightPoints = TrajectoryParser.getTrajectory(fileName + "_right.csv");
        } catch (Exception e) {
            System.out.println("Exception during trajectory point initialization. Do the CSV files exist?");
        }

    }

    public double[][] getLeftPoints() {
        return leftPoints;
    }

    public double[][] getRightPoints() {
        return rightPoints;
    }
}
