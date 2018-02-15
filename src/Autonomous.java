//import com.ctre.phoenix.motorcontrol.ControlMode;
//import com.ctre.phoenix.motorcontrol.FeedbackDevice;
//import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
//import org.usfirst.frc.team5735.robot.constants.PidConstants;
//import org.usfirst.frc.team5735.robot.constants.RobotConstants;
//import org.usfirst.frc.team5735.robot.constants.autonomous.trajectories.LeftTrajectory;
//import org.usfirst.frc.team5735.robot.constants.autonomous.trajectories.RightTrajectory;
//import org.usfirst.frc.team5735.robot.constants.enums.State;
//import org.usfirst.frc.team5735.robot.controllers.teleop.GameDataController;
//import org.usfirst.frc.team5735.robot.interfaces.Subsystem;
//import org.usfirst.frc.team5735.robot.motionprofiling.MotionProfile;
//
//public class Autonomous implements Subsystem {
//
//    private static Autonomous instance = null;
//
//    private boolean homeSwitchRight;
//    private boolean scaleRight;
//    private boolean enemySwitchRight;
//
//    public static Autonomous getInstance() {
//        if (instance == null) {
//            instance = new Autonomous();
//        }
//        return instance;
//    }
//
//    private WPI_TalonSRX leftFrontMotor, leftRearMotor, rightFrontMotor, rightRearMotor;
//    private MotionProfile leftMotionProfile;
//    private MotionProfile rightMotionProfile;
//    private boolean startedProfiles = false;
//    private State state = State.IDLE;
//    public int profileId = 0;
////    public
//
//    private Autonomous() {
//        initMotors();
//    }
//
//    private void initMotors() {
//        // ===== LEFT FRONT MOTOR SETUP =====
//        leftFrontMotor = new WPI_TalonSRX(RobotConstants.TALON_DT_LEFT_FRONT_ID);
//        leftFrontMotor.set(ControlMode.MotionProfile, 0);
//        leftFrontMotor.clearMotionProfileTrajectories();
//        leftFrontMotor.setInverted(false);
//
//        // Configure Sensors
//        leftFrontMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
//        leftFrontMotor.setSensorPhase(true);
//
//        // Configure voltage limits TODO add ramp rate
//        leftFrontMotor.configNominalOutputForward(+0.0f, 0);
//        leftFrontMotor.configNominalOutputReverse(-0.0f, 0);
//        leftFrontMotor.configPeakOutputForward(+12.0f, 0);
//        leftFrontMotor.configPeakOutputReverse(-12.0f, 0);
//
//        // Configure PID constants
//        leftFrontMotor.config_kF(PidConstants.DT_LEFT_VEL_SLOT_ID, PidConstants.DT_LEFT_VEL_KF, 100);
//        leftFrontMotor.config_kP(PidConstants.DT_LEFT_VEL_SLOT_ID, PidConstants.DT_LEFT_VEL_KP, 100);
//        leftFrontMotor.config_kI(PidConstants.DT_LEFT_VEL_SLOT_ID, PidConstants.DT_LEFT_VEL_KI, 100);
//        leftFrontMotor.config_kD(PidConstants.DT_LEFT_VEL_SLOT_ID, PidConstants.DT_LEFT_VEL_KD, 100);
//
//        // ===== LEFT REAR MOTOR SETUP =====
//        leftRearMotor = new WPI_TalonSRX(RobotConstants.TALON_DT_LEFT_REAR_ID);
//        leftRearMotor.set(ControlMode.Follower, 0);
//        leftRearMotor.follow(leftFrontMotor);
//        leftRearMotor.setInverted(false);
//
//        // ===== RIGHT FRONT MOTOR SETUP =====
//        rightFrontMotor = new WPI_TalonSRX(RobotConstants.TALON_DT_RIGHT_FRONT_ID);
//        rightFrontMotor.set(ControlMode.MotionProfile, 0);
//        rightFrontMotor.clearMotionProfileTrajectories();
//        rightFrontMotor.setInverted(true);
//
//        // Configure Sensors
//        rightFrontMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
//        rightFrontMotor.setSensorPhase(true);
//
//        // Configure voltage limits TODO add ramp rate
//        rightFrontMotor.configNominalOutputForward(+0.0f, 0);
//        rightFrontMotor.configNominalOutputReverse(-0.0f, 0);
//        rightFrontMotor.configPeakOutputForward(+12.0f, 0);
//        rightFrontMotor.configPeakOutputReverse(-12.0f, 0);
//
//        // Configure PID constants
//        rightFrontMotor.config_kF(PidConstants.DT_RIGHT_VEL_SLOT_ID, PidConstants.DT_RIGHT_VEL_KF, 100);
//        rightFrontMotor.config_kP(PidConstants.DT_RIGHT_VEL_SLOT_ID, PidConstants.DT_RIGHT_VEL_KP, 100);
//        rightFrontMotor.config_kI(PidConstants.DT_RIGHT_VEL_SLOT_ID, PidConstants.DT_RIGHT_VEL_KI, 100);
//        rightFrontMotor.config_kD(PidConstants.DT_RIGHT_VEL_SLOT_ID, PidConstants.DT_RIGHT_VEL_KD, 100);
//
//        // ===== RIGHT REAR MOTOR SETUP =====
//        rightRearMotor = new WPI_TalonSRX(RobotConstants.TALON_DT_RIGHT_REAR_ID);
//        rightRearMotor.set(ControlMode.Follower, 0);
//        rightRearMotor.follow(rightFrontMotor);
//        rightRearMotor.setInverted(false);
//    }
//
//    @Override
//    public void runInit() {
//        leftFrontMotor.setSensorPhase(true);
//        leftFrontMotor.setInverted(false);
//        leftRearMotor.setInverted(false);
//
//        rightFrontMotor.setSensorPhase(true);
//        rightFrontMotor.setInverted(true);
//        rightRearMotor.setInverted(true);
//
//        leftFrontMotor.clearMotionProfileTrajectories();
//        leftRearMotor.clearMotionProfileTrajectories();
//        rightFrontMotor.clearMotionProfileTrajectories();
//        rightRearMotor.clearMotionProfileTrajectories();
//        startProfile(0);
//        leftMotionProfile.control();
//        rightMotionProfile.control();
//        this.homeSwitchRight = GameDataController.homeSwitchRight;
//        this.scaleRight = GameDataController.scaleRight;
//        this.enemySwitchRight = GameDataController.enemySwitchRight;
//
//    }
//
//    @Override
//    public void runPeriodic() {
////        System.out.println("Completed Right profile: " + rightMotionProfile.completedProfile);
//////        if (startedProfiles && !leftMotionProfile.completedProfile && !rightMotionProfile.completedProfile) {
////        if (startedProfiles && !rightMotionProfile.completedProfile) {
////            if (leftFrontMotor.getMotionProfileTopLevelBufferCount() == 0 && rightFrontMotor.getMotionProfileTopLevelBufferCount() == 0) {
//////            leftMotionProfile.refill();
////                rightMotionProfile.refill();
////            }
//////            SetValueMotionProfile leftSetOutput = leftMotionProfile.getSetValue(); // xX usually 1 Xx
////            SetValueMotionProfile rightSetOutput = rightMotionProfile.getSetValue();
//////            leftFrontMotor.set(ControlMode.MotionProfile, leftSetOutput.value);
////            rightFrontMotor.set(ControlMode.MotionProfile, rightSetOutput.value);
////
//////            leftMotionProfile.control();
////            rightMotionProfile.control();
////        } else if (rightMotionProfile.completedProfile) {
////            startedProfiles = false;
////        } else {
////            startProfile(0);
////        }
//
//    }
//
//    public boolean run() {
//
//        return false;
//    }
//
//    public void startProfile(int profileNumber) {
//        leftMotionProfile = new MotionProfile(leftFrontMotor, LeftTrajectory.Points);
//        rightMotionProfile = new MotionProfile(rightFrontMotor, RightTrajectory.Points);
//        leftMotionProfile.startMotionProfile();
//        rightMotionProfile.startMotionProfile();
//        startedProfiles = true;
//        state = State.BUSY;
//    }
//
//    @Override
//    public void disablePeriodic() {
//        if(leftMotionProfile != null) {
//            leftMotionProfile.reset();
//        }
//
//        if(rightMotionProfile != null) {
//            rightMotionProfile.reset();
//        }
//        leftFrontMotor.set(ControlMode.PercentOutput, 0);
//        rightFrontMotor.set(ControlMode.PercentOutput, 0);
//    }
//
//
//}
