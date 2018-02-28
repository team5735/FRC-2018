package frc.team5735.constants;

public class PidConstants {
    // ========== PID Parameters ==========
    //Drivetrain Left - Closed Loop Velocity (Slot 0)
    public static final double
            DT_LEFT_VEL_KF = 0.25 * 1023 / 745,     // 0.212
            DT_LEFT_VEL_KP = 0.7, // 2.0
            DT_LEFT_VEL_KI = 0,
            DT_LEFT_VEL_KD = 0.5;
    public static final int DT_LEFT_VEL_SLOT_ID = 0;

    //Drivetrain Right - Closed Loop Velocity (Slot 0)
    public static final double
            DT_RIGHT_VEL_KF = 0.25 * 1023 / 745,
            DT_RIGHT_VEL_KP = .7, // 1.8
            DT_RIGHT_VEL_KI = 0,
            DT_RIGHT_VEL_KD = 0.4;
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
            ELEVATOR_POS_KF = (0.4 * 1023) / 350, //
            ELEVATOR_POS_KP = 0.44,
            ELEVATOR_POS_KI = 0,
            ELEVATOR_POS_KD = 0;
    public static final int ELEVATOR_POS_SLOT_ID = 0;
}
