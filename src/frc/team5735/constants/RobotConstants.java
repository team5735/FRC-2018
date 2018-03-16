package frc.team5735.constants;

public class RobotConstants {

    /*
         |  --FRONT--  |
         | |         | |
       L   |  |   |  |   R
       E | |  |___|  | | I
       F | |    |    | | G
       T   |   LIFT  |   H
         | |         | | T
         |   --REAR--  |
    */

    // ========== Motor Controllers ==========
    //Drivetrain
    public static final int
            TALON_DT_LEFT_FRONT_ID = 1,     //3 FOR OLD ROBOT
            TALON_DT_LEFT_REAR_ID = 2,      //4
            TALON_DT_RIGHT_FRONT_ID = 3,    //1
            VICTOR_DT_RIGHT_REAR_ID = 4;    //2

    //Elevator
    public static final int
            TALON_ELEV_ID = 5;

    //Wrist
    public static final int
            TALON_WRIST_ID = 7;

    //Intake Drivetrain
    public static final int
            VICTOR_INTAKE_DT_LEFT_ID = 8,
            VICTOR_INTAKE_DT_RIGHT_ID = 9;

    //Intake Elevator
    public static final int
            VICTOR_INTAKE_ELEV_ID = 10,
            PCM_ID = 12,
            INTAKE_SOLENOID_FORWARD_ID = 0, 
            INTAKE_SOLENOID_BACKWARD_ID = 1;


    // ========== Controllers ==========
    public static final int DRIVETRAIN_CONTROLLER_ID = 0;
    public static final int SUBSYSTEM_CONTROLLER_ID = 1;

    // ========== Physical Robot Properties ==========
    public static final double WHEEL_DIAMETER = 5.9;  //Inches 5.8
    public static final double TRACKWIDTH = 24;       //Inches
}
