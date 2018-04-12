package frc.team5735.controllers.motionprofiling;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;

import java.io.*;
import java.net.URL;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

/**
 * Pathfinder CSV format
 * dt (time delta between points in seconds), x, y, position, velocity, acceleration, jerk, heading
 *
 * Normal CSV Format
 * position, velocity, dt
 */
public class TrajectoryParser {
    public static Trajectory getTrajectory(String filename) {
        Trajectory trajectory = null;
        long timenow = System.currentTimeMillis();
        System.out.println("filename: " + filename);
        try {
//            URL url = TrajectoryParser.class.getResource("/trajectories/" + filename);
//            File myFile = new File(url.getPath());
            File myFile = new File("/trajectories/" + filename);
            System.out.println(myFile.exists());
            trajectory = Pathfinder.readFromCSV(myFile);
        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println((System.currentTimeMillis()-timenow) + " ms (" + filename + ")");

        return trajectory;
    }
}