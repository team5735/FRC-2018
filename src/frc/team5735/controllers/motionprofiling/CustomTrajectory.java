package frc.team5735.controllers.motionprofiling;

import edu.wpi.first.wpilibj.Encoder;
import frc.team5735.constants.RobotConstants;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;

public class CustomTrajectory {
    private String filename;
    private Trajectory trajectory;
    private TankModifier modifier;
    private EncoderFollower leftEF, rightEF;

    public CustomTrajectory() {

    }

    public CustomTrajectory(String fileName) {
        this(fileName,false);
    }

    public CustomTrajectory(String fileName, boolean isReversed) {
        System.out.println("loading trajs");
        this.filename = fileName;
        trajectory = TrajectoryParser.getTrajectory(fileName + "/" + fileName + ".csv");
        System.out.println("loaded trajs");
        if (isReversed) {    // Make velocity negative, flip profile (first point is the last point), correct position
            trajectory = reverse(trajectory);
            System.out.println("reversing");
        }

        modifier = new TankModifier(trajectory).modify(RobotConstants.WHEEL_BASE);
        leftEF = new EncoderFollower(modifier.getLeftTrajectory());
        rightEF = new EncoderFollower(modifier.getRightTrajectory());
        System.out.println("finished trajs");
    }

    public CustomTrajectory(Trajectory trajectory) {
        this.trajectory = trajectory;
        modifier = new TankModifier(trajectory).modify(RobotConstants.WHEEL_BASE);
        leftEF = new EncoderFollower(modifier.getLeftTrajectory());
        rightEF = new EncoderFollower(modifier.getRightTrajectory());
    }

    public static CustomTrajectory reverse(CustomTrajectory trajectory) {
        Trajectory.Segment[] segments = trajectory.trajectory.segments.clone();
        Trajectory.Segment[] newSegs = new Trajectory.Segment[segments.length];
        double finalPosition = segments[segments.length-1].position;

        for(int i = 0; i < segments.length; i++) {
            int currentRowIndex = segments.length - 1 - i;
            newSegs[i] = segments[currentRowIndex];
            newSegs[i].position = segments[currentRowIndex].position - finalPosition;
            newSegs[i].velocity = segments[currentRowIndex].velocity * -1;
            newSegs[i].acceleration = segments[currentRowIndex].acceleration;
            newSegs[i].dt = segments[currentRowIndex].dt;
            newSegs[i].heading = segments[currentRowIndex].heading;
            newSegs[i].jerk = segments[currentRowIndex].jerk;
            newSegs[i].x = segments[currentRowIndex].x;
            newSegs[i].y = segments[currentRowIndex].y;
        }

        return new CustomTrajectory(new Trajectory(newSegs));
    }

    public static Trajectory reverse(Trajectory trajectory) {
        Trajectory.Segment[] segments = trajectory.segments.clone();
        Trajectory.Segment[] newSegs = new Trajectory.Segment[segments.length];

        double finalPosition = segments[segments.length-1].position;

        for(int i = 0; i < segments.length; i++) {
            int currentRowIndex = segments.length - 1 - i;
            newSegs[i].position = segments[currentRowIndex].position - finalPosition;
            newSegs[i].velocity = segments[currentRowIndex].velocity * -1;
            newSegs[i].acceleration = segments[currentRowIndex].acceleration;
            newSegs[i].dt = segments[currentRowIndex].dt;
            newSegs[i].heading = segments[currentRowIndex].heading;
            newSegs[i].jerk = segments[currentRowIndex].jerk;
            newSegs[i].x = segments[currentRowIndex].x;
            newSegs[i].y = segments[currentRowIndex].y;
        }

        return new Trajectory(newSegs);
    }

    public static CustomTrajectory swapSides(CustomTrajectory trajectory) {
        CustomTrajectory newCustomTrajectory = new CustomTrajectory();

        Trajectory.Segment[] segments = trajectory.getTrajectory().segments.clone();
        Trajectory.Segment[] newSegs = new Trajectory.Segment[segments.length];

        for(int i = 0; i < segments.length; i++) {
            newSegs[i] = segments[i];
            newSegs[i].position = segments[i].position;
            newSegs[i].velocity = segments[i].velocity;
            newSegs[i].acceleration = segments[i].acceleration;
            newSegs[i].dt = segments[i].dt;
            newSegs[i].heading = segments[i].heading * -1;
            newSegs[i].jerk = segments[i].jerk;
            newSegs[i].x = segments[i].x; // unused I think
            newSegs[i].y = 13.5 - (segments[i].y - 13.5); // unused I think //flips over center line at 13ft
        }

        Trajectory newTrajectory = new Trajectory(newSegs);

        newCustomTrajectory = new CustomTrajectory(newTrajectory);

        EncoderFollower temp = newCustomTrajectory.leftEF;
        newCustomTrajectory.leftEF = newCustomTrajectory.rightEF;
        newCustomTrajectory.rightEF = temp;

        return trajectory;
    }

    public String getFilename() {
        return filename;
    }

    public Trajectory getTrajectory() {
        return trajectory;
    }

    public TankModifier getModifier() {
        return modifier;
    }

    public EncoderFollower getLeftEF() {
        return leftEF;
    }

    public EncoderFollower getRightEF() {
        return rightEF;
    }
}
