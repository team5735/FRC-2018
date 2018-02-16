package frc.team5735.constants;

public class PidConstants {
    // ========== PID Parameters ==========
    //Drivetrain Left - Closed Loop Velocity (Slot 0)
    public static final double
            DT_LEFT_VEL_KF = 0.212,
            DT_LEFT_VEL_KP = 0,
            DT_LEFT_VEL_KI = 0,
            DT_LEFT_VEL_KD = 0;
    public static final int DT_LEFT_VEL_SLOT_ID = 0;

    //Drivetrain Right - Closed Loop Velocity (Slot 0)
    public static final double
            DT_RIGHT_VEL_KF = 0.212,
            DT_RIGHT_VEL_KP = 0,
            DT_RIGHT_VEL_KI = 0,
            DT_RIGHT_VEL_KD = 0;
    public static final int DT_RIGHT_VEL_SLOT_ID = 0;

    //Wrist - Closed Loop Position (Slot 0)
    public static final double
            WRIST_POS_KF = 0,
            WRIST_POS_KP = 0.24,
            WRIST_POS_KI = 0,
            WRIST_POS_KD = 0;
    public static final int WRIST_POS_SLOT_ID = 0;

    //Elevator - Closed Loop Position (Slot 0)
    public static final double
            ELEVATOR_POS_KF = 0,
            ELEVATOR_POS_KP = 0.08,
            ELEVATOR_POS_KI = 0,
            ELEVATOR_POS_KD = 0.9;
    public static final int ELEVATOR_POS_SLOT_ID = 0;
}
