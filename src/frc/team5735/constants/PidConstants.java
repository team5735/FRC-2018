package frc.team5735.constants;

public class PidConstants {
    // ========== PID Parameters ==========
    //Drivetrain Left - Closed Loop Velocity (Slot 0)
    public static final double
            DT_LEFT_VEL_KF = 0.25 * 1023 / 825,     // 0.212
            DT_LEFT_VEL_KP = 1.2, // 2.0, 0.7, 1.0
            DT_LEFT_VEL_KI = 0,
            DT_LEFT_VEL_KD = 0; //1.0
    public static final int DT_LEFT_VEL_SLOT_ID = 0;

    //Drivetrain Right - Closed Loop Velocity (Slot 0)
    public static final double
            DT_RIGHT_VEL_KF = 0.25 * 1023 / 685,
            DT_RIGHT_VEL_KP = 1.92, // 1.8 , 0.7, 1.92
            DT_RIGHT_VEL_KI = 0,
            DT_RIGHT_VEL_KD = 0; //1.23
    public static final int DT_RIGHT_VEL_SLOT_ID = 0;

    //Wrist - Closed Loop Position (Slot 0)
    public static final double
            WRIST_POS_KF = 0,
            WRIST_POS_KP = 0.3,    //0.19
            WRIST_POS_KI = 0,      //0.0001
            WRIST_POS_KD = 0;      //0.15
    public static final int WRIST_POS_SLOT_ID = 0;

    //Elevator - Closed Loop Position (Slot 0)
    public static final double
            ELEVATOR_POS_KF = (0.4 * 1023) / 415, //
            ELEVATOR_POS_KP = 0.28, //0.44
            ELEVATOR_POS_KI = 0,
            ELEVATOR_POS_KD = 0.05;
    public static final int ELEVATOR_POS_SLOT_ID = 0;

    //Gyro Turning
    public static final double TURN_P = 0.8;
}
