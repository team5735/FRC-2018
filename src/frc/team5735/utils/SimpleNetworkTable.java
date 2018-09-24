package frc.team5735.utils;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class SimpleNetworkTable {

    private static NetworkTableInstance instance = NetworkTableInstance.getDefault();
    private static NetworkTable networkTable = instance.getTable("subsystems");

    public SimpleNetworkTable(){
    }

    public static void setDouble (String key, double value) {
        networkTable.getEntry(key).setDouble(value);
    }
}
