package frc.team5735.controllers.motionprofiling;

public class Trajectory {
    private double[][] leftPoints, rightPoints;  // Position (rotations), Velocity (RPM), Duration (ms)

    public Trajectory(String fileName, TrajectoryParser.CSV_FORMAT format) {
        try {
            if(format == TrajectoryParser.CSV_FORMAT.NORMAL) {
                leftPoints = TrajectoryParser.getTrajectory(fileName + "_left.csv");
                rightPoints = TrajectoryParser.getTrajectory(fileName + "_right.csv");
            } else if(format == TrajectoryParser.CSV_FORMAT.PATHFINDER) {
                leftPoints = TrajectoryParser.getTrajectory(fileName + "_left_detailed.csv");
                rightPoints = TrajectoryParser.getTrajectory(fileName + "_right_detailed.csv");
            }
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
