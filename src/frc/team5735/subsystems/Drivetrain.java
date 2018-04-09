package frc.team5735.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.sensors.PigeonIMU;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team5735.constants.PidConstants;
import frc.team5735.constants.RobotConstants;
import frc.team5735.controllers.motionprofiling.MotionProfile;
import frc.team5735.utils.SimpleNetworkTable;
import frc.team5735.utils.units.Degrees;

public class Drivetrain implements Subsystem {

    // ===== Singleton =====
    private static Drivetrain instance = new Drivetrain();

    public static Drivetrain getInstance() {
        if (instance == null) {
            instance = new Drivetrain();
        }
        return instance;
    }

    // ===== Instance Fields =====
    // Curvature drive
    private double m_quickStopThreshold = 0.2;
    private double m_quickStopAlpha = 0.1;
    private double m_quickStopAccumulator = 0.0;
    private double m_deadband = 0.02;
    private double m_maxOutput = 1;

    // Motor Controllers
    private TalonSRX leftFrontMotor, rightFrontMotor, leftRearMotor;
    private VictorSPX rightRearMotor;
    private PigeonIMU gyro;

    // Gyro Values
    private double gyroSpeedLimit = 0.50;
    private Degrees gyroMargin = new Degrees(3);
    private double turnSpeedMin = 0.33;

    // Output Values
    private double leftSideTargetPercent = 0, rightSideTargetPercent = 0;
    private double leftSideMPOutput = 0, rightSideMPOutput = 0;
    private Degrees targetAngle = new Degrees(0);

    // Info
    private DrivetrainState state;
    private PigeonIMU.GeneralStatus gyroStatus;

    // ===== Methods =====
    private Drivetrain() {
        initMotors();
        //putStatus();
        state = DrivetrainState.DEFAULT;
        gyro = new PigeonIMU(leftRearMotor);
        gyroStatus = new PigeonIMU.GeneralStatus();
    }

    private void initMotors() {
        // ===== LEFT FRONT MOTOR SETUP =====
        leftFrontMotor = new TalonSRX(RobotConstants.TALON_DT_LEFT_FRONT_ID);
        leftFrontMotor.set(ControlMode.PercentOutput,0);     //Default control mode TODO ask if current was a typo
        leftFrontMotor.setInverted(false);

        // Configure Sensors
        leftFrontMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,0,10); //TODO Check encoder error codes
        leftFrontMotor.setSensorPhase(true);

        // Configure voltage limits TODO add ramp rate
        leftFrontMotor.configNominalOutputForward(+0.0f, 0);
        leftFrontMotor.configNominalOutputReverse(-0.0f, 0);
        leftFrontMotor.configPeakOutputForward(+12.0f, 0);
        leftFrontMotor.configPeakOutputReverse(-12.0f, 0);

        // Configure PID constants
        leftFrontMotor.config_kF(PidConstants.DT_LEFT_VEL_SLOT_ID, PidConstants.DT_LEFT_VEL_KF, 100);
        leftFrontMotor.config_kP(PidConstants.DT_LEFT_VEL_SLOT_ID, PidConstants.DT_LEFT_VEL_KP, 100);
        leftFrontMotor.config_kI(PidConstants.DT_LEFT_VEL_SLOT_ID, PidConstants.DT_LEFT_VEL_KI, 100);
        leftFrontMotor.config_kD(PidConstants.DT_LEFT_VEL_SLOT_ID, PidConstants.DT_LEFT_VEL_KD, 100);

        // ===== LEFT REAR MOTOR SETUP =====
        leftRearMotor = new TalonSRX(RobotConstants.TALON_DT_LEFT_REAR_ID);
        leftRearMotor.set(ControlMode.Follower, 0);
        leftRearMotor.follow(leftFrontMotor);
        leftRearMotor.setInverted(false);

        // ===== RIGHT FRONT MOTOR SETUP =====
        rightFrontMotor = new TalonSRX(RobotConstants.TALON_DT_RIGHT_FRONT_ID);
        rightFrontMotor.set(ControlMode.PercentOutput,0);     //Default control mode TODO ask if current was a typo
        rightFrontMotor.setInverted(true);

        // Configure Sensors
        rightFrontMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,0,10); //TODO Check encoder error codes
        rightFrontMotor.setSensorPhase(true);

        // Configure voltage limits TODO add ramp rate
        rightFrontMotor.configNominalOutputForward(+0.0f, 0);
        rightFrontMotor.configNominalOutputReverse(-0.0f, 0);
        rightFrontMotor.configPeakOutputForward(+12.0f, 0);
        rightFrontMotor.configPeakOutputReverse(-12.0f, 0);

        // Configure PID constants
        rightFrontMotor.config_kF(PidConstants.DT_RIGHT_VEL_SLOT_ID, PidConstants.DT_RIGHT_VEL_KF, 100);
        rightFrontMotor.config_kP(PidConstants.DT_RIGHT_VEL_SLOT_ID, PidConstants.DT_RIGHT_VEL_KP, 100);
        rightFrontMotor.config_kI(PidConstants.DT_RIGHT_VEL_SLOT_ID, PidConstants.DT_RIGHT_VEL_KI, 100);
        rightFrontMotor.config_kD(PidConstants.DT_RIGHT_VEL_SLOT_ID, PidConstants.DT_RIGHT_VEL_KD, 100);

        // ===== RIGHT REAR MOTOR SETUP =====
        rightRearMotor = new VictorSPX(RobotConstants.VICTOR_DT_RIGHT_REAR_ID);
        rightRearMotor.set(ControlMode.Follower, 0);
        rightRearMotor.follow(rightFrontMotor);
        rightRearMotor.setInverted(true);
    }

    @Override
    public void runInit() {

    }

    @Override
    public void runPeriodic() {
//        System.out.println(gyro.getFusedHeading());
        if (state == DrivetrainState.MP) {
            leftFrontMotor.set(ControlMode.PercentOutput, leftSideMPOutput);
            rightFrontMotor.set(ControlMode.PercentOutput, rightSideMPOutput);
        } else if (state == DrivetrainState.GYRO_STARTED || state == DrivetrainState.GYRO_BUSY) {
            gyro.getGeneralStatus(gyroStatus);
            if ( gyroStatus.state == PigeonIMU.PigeonState.Ready) {
                if(state == DrivetrainState.GYRO_STARTED) {
                    state = DrivetrainState.GYRO_BUSY;
                } else {
                    double turnSpeed = (targetAngle.getValue() - gyro.getFusedHeading()) / 180.;
                    turnSpeed = limit(turnSpeed);

                    turnSpeed *= gyroSpeedLimit;

                    if (Math.abs(turnSpeed) < turnSpeedMin) {
                        if (turnSpeed < 0) {
                            turnSpeed = -turnSpeedMin;
                        } else {
                            turnSpeed = turnSpeedMin;
                        }
                    }

                    leftFrontMotor.set(ControlMode.PercentOutput, -turnSpeed);
                    rightFrontMotor.set(ControlMode.PercentOutput, turnSpeed);

                    if (new Degrees(gyro.getFusedHeading()).withinMargin(targetAngle, gyroMargin)) {
                        state = DrivetrainState.GYRO_FINISHED;
                    }
                }
            } else {
                leftFrontMotor.set(ControlMode.PercentOutput, 0);
                rightFrontMotor.set(ControlMode.PercentOutput, 0);
            }
        } else {
            leftFrontMotor.set(ControlMode.PercentOutput, leftSideTargetPercent);
            rightFrontMotor.set(ControlMode.PercentOutput, rightSideTargetPercent);
        }
        //putStatus();
    }

    @Override
    public void disabledInit() {

    }

    public DrivetrainState getState() {
        return state;
    }

    public void setMotionProfileOutput (double leftOutput, double rightOutput) {
        leftSideMPOutput = leftOutput;
        rightSideMPOutput = rightOutput;
        state = DrivetrainState.MP;
    }

    public void setPercentOutput (double leftSideTargetPercent, double rightSideTargetPercent) {
        this.leftSideTargetPercent = leftSideTargetPercent;
        this.rightSideTargetPercent = rightSideTargetPercent;
        this.state = DrivetrainState.DEFAULT;
    }

    public void setTargetAngle (Degrees angle) {
        targetAngle = angle;
        if (state != DrivetrainState.GYRO_BUSY && state != DrivetrainState.GYRO_FINISHED){
            this.state = DrivetrainState.GYRO_STARTED;
        }
    }

    public void curvatureDrive(double xSpeed, double zRotation, boolean isQuickTurn) {
        xSpeed = limit(xSpeed);
        xSpeed = applyDeadband(xSpeed, m_deadband);

        zRotation = limit(zRotation);
        zRotation = applyDeadband(zRotation, m_deadband);

        double angularPower;
        boolean overPower;

        if (isQuickTurn) {
            if (Math.abs(xSpeed) < m_quickStopThreshold) {
                m_quickStopAccumulator = (1 - m_quickStopAlpha) * m_quickStopAccumulator
                        + m_quickStopAlpha * limit(zRotation) * 2;
            }
            overPower = true;
            angularPower = zRotation;
        } else {
            overPower = false;
            angularPower = Math.abs(xSpeed) * zRotation - m_quickStopAccumulator;

            if (m_quickStopAccumulator > 1) {
                m_quickStopAccumulator -= 1;
            } else if (m_quickStopAccumulator < -1) {
                m_quickStopAccumulator += 1;
            } else {
                m_quickStopAccumulator = 0.0;
            }
        }

        double leftMotorOutput = xSpeed + angularPower;
        double rightMotorOutput = xSpeed - angularPower;

        // If rotation is overpowered, reduce both outputs to within acceptable range
        if (overPower) {
            if (leftMotorOutput > 1.0) {
                rightMotorOutput -= leftMotorOutput - 1.0;
                leftMotorOutput = 1.0;
            } else if (rightMotorOutput > 1.0) {
                leftMotorOutput -= rightMotorOutput - 1.0;
                rightMotorOutput = 1.0;
            } else if (leftMotorOutput < -1.0) {
                rightMotorOutput -= leftMotorOutput + 1.0;
                leftMotorOutput = -1.0;
            } else if (rightMotorOutput < -1.0) {
                leftMotorOutput -= rightMotorOutput + 1.0;
                rightMotorOutput = -1.0;
            }
        }

        setPercentOutput(leftMotorOutput * m_maxOutput,rightMotorOutput * m_maxOutput);
    }

    public void clearMotionProfileTrajectories() {
//        leftFrontMotor.clearMotionProfileTrajectories();
//        leftRearMotor.clearMotionProfileTrajectories();
//        rightFrontMotor.clearMotionProfileTrajectories();
//        rightRearMotor.clearMotionProfileTrajectories();
    }

    public void resetGyro(){
        gyro.setFusedHeading(0,0);
    }

    public void setState(DrivetrainState state) {
        this.state = state;
    }

    public TalonSRX getLeftMotor() {
        return leftFrontMotor;
    }

    public TalonSRX getRightMotor() {
        return rightFrontMotor;
    }

    public double getGyroHeading() {
        return gyro.getFusedHeading();
    }

    // ===== RobotDriveBase Methods =====

    /**
     * Limit motor values to the -1.0 to +1.0 range.
     */
    protected double limit(double value) {
        if (value > 1.0) {
            return 1.0;
        }
        if (value < -1.0) {
            return -1.0;
        }
        return value;
    }

    /**
     * Returns 0.0 if the given value is within the specified range around zero. The remaining range
     * between the deadband and 1.0 is scaled from 0.0 to 1.0.
     *
     * @param value    value to clip
     * @param deadband range around zero
     */
    protected double applyDeadband(double value, double deadband) {
        if (Math.abs(value) > deadband) {
            if (value > 0.0) {
                return (value - deadband) / (1.0 - deadband);
            } else {
                return (value + deadband) / (1.0 - deadband);
            }
        } else {
            return 0.0;
        }
    }

    public void printStatus() {
        System.out.println("===== Drivetrain =====");
        System.out.println("Right Velocity: " + rightFrontMotor.getSelectedSensorVelocity(0));
        System.out.println("Left Velocity: " + leftFrontMotor.getSelectedSensorVelocity(0));
        System.out.println();
    }

    public void putStatus() {
        SmartDashboard.getNumber("test",-1);
        SmartDashboard.putNumber("dtLSensorPosition", leftFrontMotor.getSelectedSensorPosition(0));
        SmartDashboard.putNumber("dtLSensorVelocity", leftFrontMotor.getSelectedSensorVelocity(0));

        SmartDashboard.putNumber("dtLPercent", leftFrontMotor.getMotorOutputPercent());
        SmartDashboard.putNumber("dtLVoltage", leftFrontMotor.getMotorOutputVoltage());
        SmartDashboard.putNumber("dtLCurrent", leftFrontMotor.getOutputCurrent());

        SmartDashboard.putNumber("dtRSensorPosition", rightFrontMotor.getSelectedSensorPosition(0));
        SmartDashboard.putNumber("dtRSensorVelocity", rightFrontMotor.getSelectedSensorVelocity(0));

        SmartDashboard.putNumber("dtRPercent", rightFrontMotor.getMotorOutputPercent());
        SmartDashboard.putNumber("dtRVoltage", rightFrontMotor.getMotorOutputVoltage());
        SmartDashboard.putNumber("dtRCurrent", rightFrontMotor.getOutputCurrent());
    }

    public enum DrivetrainState {
        DEFAULT, MP, GYRO_STARTED, GYRO_BUSY, GYRO_FINISHED
    }
}
