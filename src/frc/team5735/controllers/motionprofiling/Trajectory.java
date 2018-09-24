package frc.team5735.controllers.motionprofiling;

public class Trajectory {
    private String filename;
    private double[][] leftPoints, rightPoints;  // Position (rotations), Velocity (RPM), Duration (ms)

    public Trajectory(String fileName) {
        this(fileName,false);
    }

    public Trajectory(String fileName, boolean isReversed) {
        this.filename = fileName;
        leftPoints = TrajectoryParser.getTrajectory(fileName + "/" + fileName + "_left.csv");
        rightPoints = TrajectoryParser.getTrajectory(fileName + "/" + fileName + "_right.csv");

        if (isReversed) {    // Make velocity negative, flip profile (first point is the last point), correct position
            leftPoints = reversePath(leftPoints);
            rightPoints = reversePath(rightPoints);
        }
    }

    public Trajectory(double[][] leftPoints, double[][] rightPoints) {
        this.leftPoints = leftPoints;
        this.rightPoints = rightPoints;
    }

    public static Trajectory reverse(Trajectory trajectory) {
        double[][] newLeftPoints = reversePath(trajectory.getLeftPoints());
        double[][] newRightPoints = reversePath(trajectory.getRightPoints());
        return new Trajectory(newLeftPoints, newRightPoints);
    }

    public static Trajectory swapSides(Trajectory trajectory) {
        return new Trajectory(trajectory.getRightPoints(), trajectory.getLeftPoints());
    }

    public double[][] getLeftPoints() {
        return leftPoints;
    }

    public double[][] getRightPoints() {
        return rightPoints;
    }

    public String getFilename() {
        return filename;
    }

    private static double[][] reversePath(double[][] points) {
        double finalPosition = points[points.length-1][0];
        double[][] reversedPoints = new double[points.length][3];
        for (int i = 0; i < points.length; i++) {
            int currentRowIndex = points.length-1-i;
            reversedPoints[i][0] = points[currentRowIndex][0] - finalPosition;
            reversedPoints[i][1] = points[currentRowIndex][1] * -1;
            reversedPoints[i][2] = points[currentRowIndex][2];
        }

        return reversedPoints;
    }

    private static double round(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }
}
