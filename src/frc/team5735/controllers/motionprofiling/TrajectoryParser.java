package frc.team5735.controllers.motionprofiling;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;

import java.io.*;
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

    public enum CSV_FORMAT {
        PATHFINDER, NORMAL
    }

    public static double[][] getTrajectory(String filename) {
        return getTrajectory(filename, CSV_FORMAT.NORMAL);
    }

    public static double[][] getTrajectory(String filename, CSV_FORMAT csvFormat) {
        Reader in = null;
        double [][] points = new double [1][3];
        long timenow = System.currentTimeMillis();

        try {
            InputStream inputStream = TrajectoryParser.class.getResourceAsStream("trajectories/" + filename);
            in = new InputStreamReader(inputStream);


            List<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in).getRecords();
            points = new double[records.size()][3];

            for (int i = 0; i < records.size(); i++) {
                CSVRecord record = records.get(i);
                double [] point = new double[3];

                if (csvFormat == CSV_FORMAT.NORMAL) {
                    point[0] = Double.valueOf(record.get(0)).doubleValue();
                    point[1] = Double.valueOf(record.get(1)).doubleValue();
                    point[2] = Double.valueOf(record.get(2)).doubleValue();

//                    System.out.printf("{%f,%f,%f},\n", point[0], point[1], point[2]);
                } else if (csvFormat == CSV_FORMAT.PATHFINDER) {
                    point[0] = Double.valueOf(record.get(3)).doubleValue();
                    point[1] = Double.valueOf(record.get(4)).doubleValue();
                    point[2] = Double.valueOf(record.get(0)).doubleValue() * 1000;

//                    System.out.printf("{%f,%f,%f},\n", point[0], point[1], point[2]);
                }

                points[i] = point;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println((System.currentTimeMillis()-timenow) + " ms (" + filename + ")");

        return points;
    }

    public static void writeCSVTrajectory(String filename, Trajectory trajectory) throws IOException {
        writeCSVTrajectory(filename, trajectory, CSV_FORMAT.NORMAL);
    }

    public static void writeCSVTrajectory(String filename, Trajectory trajectory,
            CSV_FORMAT csvFormat) throws IOException {
        long timenow = System.currentTimeMillis();
        File outFile = new File(filename);

        if (csvFormat == CSV_FORMAT.PATHFINDER) {
            Pathfinder.writeToCSV(outFile, trajectory);
        } else if (csvFormat == CSV_FORMAT.NORMAL) {
            FileWriter outfw = new FileWriter(outFile);
            PrintWriter outpw = new PrintWriter(outfw);

            for (int i = 0; i < trajectory.length(); i++) {
                Trajectory.Segment seg = trajectory.get(i);

                //System.out.printf("%f,%f,%f,%f,%f,%f,%f,%f\n",
                //                    seg.dt, seg.x, seg.y, seg.position, seg.velocity,
                //                    seg.acceleration, seg.jerk, seg.heading);
                //System.out.println("{" + (float)(seg.position) + "," + seg.velocity*60 + "," + (int)(seg.dt*1000) + "},");
                System.out.printf("{%.6f,%.6f,%d},\n", seg.position, seg.velocity*60, (int)(seg.dt*1000));
                outpw.printf("%.6f, %.6f, %d\n", seg.position, seg.velocity, (int)(seg.dt * 1000));
            }
            outpw.close();
        }

        System.out.println(trajectory.length());
        System.out.println("writeCSVTrajectory: " + (System.currentTimeMillis() - timenow));
    }

}